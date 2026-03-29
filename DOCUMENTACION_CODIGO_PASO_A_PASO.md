# Documentación del Código — Paso a Paso

## 1) ¿Qué es este proyecto?
Este proyecto es una API bancaria construida con **Spring Boot**. Su objetivo es administrar:
- Clientes Naturales y Empresas
- Cuentas Bancarias y Préstamos
- Transferencias y Usuarios
- Bitácora de Auditoría

La aplicación sigue estrictamente la **Arquitectura Hexagonal (Puertos y Adaptadores)** mediante 4 capas principales:
- **Capa Web (Controladores)**: expone los endpoints REST HTTP
- **Capa de Aplicación (Casos de Uso)**: orquesta y dirige peticiones
- **Capa de Dominio (Servicios y Puertos)**: núcleo puro en Java sin dependencias de frameworks
- **Capa de Infraestructura (Adaptadores y Repositorios)**: gestiona la conexión a bases de datos

---

## 2) Estructura general del proyecto
Dentro de `wilmer-vega/src/main/java/gestiondeunbanco/wilmervega`:

- `WilmerVegaApplication.java`: punto de arranque del microservicio
- `controller/`: REST endpoints HTTP expuestos hacia el exterior (Capa 1)
- `application/usecases/`: lógica de orquestación de la aplicación (Capa 2)
- `domain/`: núcleo del negocio (Capa 3)
  - `models/`: clases ricas, datos y reglas (`Client`, `Account`, etc)
  - `ports/`: contratos obligatorios o interfaces a implementar
  - `services/`: reglas estrictas de dominio sin `@Service` de Spring
- `infrastructure/adapters/`: unión del dominio a la base de datos (Capa 4)
  - `repository/`: los `JpaRepository` propios de Spring Data

---

## 3) Paso a paso del flujo de una petición REST
Ejemplo enviando un `POST /api/transfers`:

1. El cliente HTTP hace su llamada `POST`.
2. El **controlador** (`TransferController.create(...)`) la recibe.
3. Le pasa los datos a la **capa de aplicación** (`TransferUseCase.save(...)`).
4. El caso de uso invoca a la **capa de dominio** pura (`TransferDomainService.save(...)`). Aquí el domino valida que el monto sea, por ejemplo, mayor a cero.
5. El servicio de dominio, sin saber si existe MySQL o no, le ordena grabar la transferencia al puerto (`TransferPort.save(...)`).
6. El puerto mágicamente es interceptado por un **adaptador de infraestructura** de Spring (`TransferPersistenceAdapter`).
7. El adaptador finalmente usa el repositorio de JPA (`TransferRepository`) insertando el registro en la base de datos real.

Este proceso de "adentro hacia afuera" hace que el dominio (el negocio) sea indiferente a cambios futuros (como migrar la BD de MySQL a Mongo).

---

## 4) Endpoints disponibles
Todos protegidos bajo `/api/**`:

- **Usuarios (`UserController`)**
  - `/api/users` (GET, GET /{id}, GET /username/{username}, POST, DELETE)
- **Cuentas Bancarias (`BankAccountController`)**
  - `/api/bank-accounts` (GET, GET /{id}, GET /number/{accountNumber}, POST, DELETE)
- **Transferencias (`TransferController`)**
  - `/api/transfers` (GET, GET /{id}, POST, DELETE)
- **Préstamos (`LoanController`)**
  - `/api/loans` (GET, GET /{id}, POST, DELETE)
- **Clientes Empresa (`CompanyClientController`)**
  - `/api/company-clients` (GET, GET /{id}, POST, DELETE)
- **Clientes Naturales (`NaturalClientController`)**
  - `/api/natural-clients` (GET, GET /{id}, GET /document/{documentNumber}, POST, DELETE)
- **Auditoría (`AuditLogController`)**
  - `/api/audit-logs` (GET, GET /{id}, POST, DELETE)

---

## 5) Diseño de Clases (Modelos de Dominio)

### Jerarquía
- `Person` -> `Client` -> `NaturalClient` / `CompanyClient`
- `Person` -> `UserManager` -> `User` / `SystemUser`

### Centralización a través de Interfaces
Ningún requerimiento de datos llama directo al repositorio `Jpa`. Todo pasa obligatoriamente por interfaces dentro de `domain/ports` (`AuditLogPort`, `TransferPort`, etc.).

---

## 6) Configuración de Seguridad
Archivo base: `config/SecurityConfig.java`
1. Se desactiva CSRF
2. Todas las rutas se autentican (`anyRequest().authenticated()`)
3. La estrategia de login es de tipo Basic (`httpBasic()`)

---

## 7) Guía de Lectura Rápida
Si vas a presentar o leer el código, hazlo en este orden:

1. Lee un **Modelo** de dominio (`BankAccount.java`).
2. Mira el **Puerto** respectivo (`BankAccountPort.java`).
3. Verás cómo interactúa el **Servicio de Dominio** puro (`BankAccountDomainService.java`) con ese puerto.
4. Luego asómate al **Caso de Uso** (`BankAccountUseCase.java`) que une Spring Boot con el dominio.
5. Pasa al **Controlador** (`BankAccountController.java`) para ver cómo exponen los endpoints.
6. Finalmente nota cómo el **Adaptador de Persistencia** (`BankAccountPersistenceAdapter.java`) es la "magia" que cierra el circuito conectando el dominio con la BD en MySQL.
