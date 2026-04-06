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
Ejemplo enviando un `POST /api/client/transfers`:

1. El cliente HTTP hace su llamada `POST`.
2. El **controlador guiado por rol** (`ClientController.executeTransfer(...)`) la recibe.
3. Le pasa los datos a la **capa de aplicación por rol** (`ClientUseCase.executeTransfer(...)`).
4. El caso de uso invoca a la **capa de dominio orientada a comandos operativos** pura (`CreateTransfer.save(...)`).
5. El servicio de comando, sin saber si existe MySQL o no, le ordena grabar la transferencia al puerto (`TransferPort.save(...)`).
6. El puerto mágicamente es interceptado por un **adaptador de infraestructura** de Spring (`TransferPersistenceAdapter`).
7. El adaptador finalmente usa el repositorio de JPA (`TransferRepository`) insertando el registro en la base de datos real.

Este proceso de "adentro hacia afuera" hace que el dominio (el negocio) sea indiferente a cambios futuros (como migrar la BD de MySQL a Mongo).

---

## 4) Endpoints disponibles
Todos protegidos y organizados por Roles:

- **Rutas Administrativas (`/api/admin/**`) -> `AdminController`**
  - `/users` y `/audit-logs` (gestión del backoffice)
- **Rutas para Empleados o Cajeros (`/api/employee/**`) -> `EmployeeController`**
  - `/bank-accounts`, `/natural-clients`, `/company-clients`, `/loans` (aperturas de cuentas, originación de créditos, matriculación de clientes)
- **Rutas para Uso del Cliente (`/api/client/**`) -> `ClientController`**
  - `/bank-accounts` (consultar detalles de su propia cuenta)
  - `/transfers` (ejecución y lectura de sus transferencias)

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
3. Verás cómo interactúan los nuevos **Servicios de Operaciones Comando** (ej: `CreateBankAccount.java`, `FindBankAccount.java`) con ese puerto.
4. Luego asómate al **Caso de Uso Por Rol** (ej: `EmployeeUseCase.java`) que agrupa todos los servicios comandos usados por empleados.
5. Pasa al **Controlador de Rol** (`EmployeeController.java`) para ver cómo exponen los endpoints específicos a `/api/employee/bank-accounts`.
6. Finalmente nota cómo el **Adaptador de Persistencia** (`BankAccountPersistenceAdapter.java`) es la "magia" que cierra el circuito conectando el dominio con la BD en MySQL.
