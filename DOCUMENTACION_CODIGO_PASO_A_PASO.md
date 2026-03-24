# Documentación del Código — Paso a Paso

## 1) ¿Qué es este proyecto?
Este proyecto es una API bancaria construida con **Spring Boot**. Su objetivo es administrar:
- Clientes de empresa (`CompanyClient`)
- Bitácora de auditoría (`AuditLog`)
- Entidades de dominio bancario (cuentas, préstamos, transferencias, usuarios, etc.)

La aplicación sigue una estructura por capas:
- **Controller**: recibe peticiones HTTP
- **Service**: contiene la lógica de negocio
- **Repository**: accede a la base de datos
- **Domain/Models**: representa las entidades y reglas del negocio

---

## 2) Estructura general del proyecto
Dentro de `wilmer-vega/src/main/java/gestiondeunbanco/wilmervega`:

- `WilmerVegaApplication.java`: punto de entrada de Spring Boot
- `config/SecurityConfig.java`: configuración de seguridad HTTP
- `controller/`: endpoints REST
- `service/`: reglas de negocio
- `repository/`: acceso a MongoDB para auditoría y clientes de empresa
- `domain/models/`: modelo de dominio bancario completo
- `dto/`: objetos de transferencia de datos
- `exception/`: excepciones de negocio

---

## 3) Paso a paso del arranque
### Paso 1: Inicio de la aplicación
Archivo: `WilmerVegaApplication.java`

- Se ejecuta `SpringApplication.run(...)`
- Spring Boot carga automáticamente configuración, beans, controladores y repositorios

### Paso 2: Configuración de seguridad
Archivo: `config/SecurityConfig.java`

Reglas activas:
1. Se desactiva CSRF (`csrf().disable()`)
2. Todas las rutas `/api/**` requieren autenticación
3. Otras rutas quedan permitidas
4. Se usa **HTTP Basic Auth**

> Resultado: para consumir endpoints de `/api/...` se necesita usuario/contraseña válidos en el contexto de Spring Security.

---

## 4) Paso a paso del flujo de una petición REST
Ejemplo con `CompanyClient`:

1. El cliente HTTP llama `POST /api/company-clients`
2. `CompanyClientController.create(...)` recibe el JSON
3. El controlador llama `companyClientService.save(...)`
4. `CompanyClientService` delega en `companyClientRepository.save(...)`
5. `CompanyClientRepository` persiste en MongoDB
6. El controlador responde con `200 OK` y el objeto guardado

Mismo patrón general en el resto de endpoints:
- **Controller** valida/coordina entrada y salida HTTP
- **Service** aplica la lógica
- **Repository** persiste/consulta datos

---

## 5) Endpoints disponibles actualmente

## 5.1 CompanyClientController (`/api/company-clients`)
- `GET /api/company-clients` → lista todos los clientes empresa
- `GET /api/company-clients/{id}` → busca por ID
- `POST /api/company-clients` → crea cliente empresa
- `DELETE /api/company-clients/{id}` → elimina cliente empresa

Comportamiento importante:
- Si `POST` recibe datos inválidos y se lanza `IllegalArgumentException`, responde `400 Bad Request`
- Si un `GET /{id}` no existe, responde `404 Not Found`

## 5.2 AuditLogController (`/api/audit-logs`)
- `GET /api/audit-logs` → lista logs
- `GET /api/audit-logs/user/{userId}` → filtra por usuario
- `GET /api/audit-logs/product/{productId}` → filtra por producto afectado
- `POST /api/audit-logs` → crea un log

---

## 6) Capa Service (lógica de negocio)

## 6.1 CompanyClientService
Métodos principales:
- `getAllCompanyClients()`
- `findById(Long id)`
- `save(CompanyClient companyClient)`
- `deleteById(Long id)`

También existen métodos equivalentes (`createCompanyClient`, `updateCompanyClient`, etc.), lo que indica duplicidad funcional.

## 6.2 AuditLogService
Métodos principales:
- `findAll()`
- `save(AuditLog auditLog)`
- `findByUserId(Long userId)`
- `findByAffectedProductId(String productId)`

---

## 7) Capa Repository (persistencia)

## 7.1 Repositorios activos en `repository/`
- `CompanyClientRepository extends MongoRepository<CompanyClient, Long>`
- `AuditLogRepository extends MongoRepository<AuditLog, Long>`

`AuditLogRepository` incluye búsquedas derivadas por nombre de método:
- `findByUserId(Long userId)`
- `findByAffectedProductId(String productId)`

## 7.2 Repositorios en `domain/repository/`
En esa carpeta existen interfaces adicionales (`BankAccountRepository`, `NaturalClientRepository`, etc.), pero la API activa actual usa los repositorios del paquete `repository`.

---

## 8) Modelo de dominio explicado fácil

## 8.1 Jerarquía de personas y usuarios
- `Person`: base con `id`, `email`, `phone`, `address`
- `UserManager extends Person`: agrega `fullName`, `birthDate`, `userStatus`
- `SystemUser extends UserManager`: usuario operativo del sistema
- `User extends UserManager`: otro tipo de usuario con `username/password`
- `Client extends Person`: cliente base del banco (tiene documentNumber, cuentas y préstamos)
- `NaturalClient extends Client`: cliente persona natural
- `CompanyClient extends Client`: cliente empresarial

## 8.2 Productos bancarios
- `BankingProduct`: catálogo base (`productCode`, `productName`, `category`, etc.)
- `BankAccount`: cuenta bancaria con saldo, tipo, estado y titular
- `Loan`: préstamo con estado, montos, tasa e información de desembolso
- `Transfer`: transferencia entre cuentas con creador, aprobador y estado

## 8.3 Regla de negocio explícita en código
`BankAccount` tiene validaciones internas:
- `validateState()` exige saldo no nulo y no negativo
- `hasSingleHolder()` obliga a tener el campo de `holder` (titular único) asignado.

---

## 9) Enumeraciones (catálogos de estados/tipos)
El modelo incluye enums para normalizar reglas:
- `AccountStatus`: ACTIVE, BLOCKED, CANCELLED
- `AccountType`: SAVINGS, CHECKING, PERSONAL, BUSINESS
- `Currency`: USD, COP, EUR
- `LoanStatus`: UNDER_REVIEW, APPROVED, REJECTED, DISBURSED, OVERDUE, CANCELLED
- `LoanType`: CONSUMER, VEHICLE, MORTGAGE, BUSINESS
- `TransferStatus`: PENDING, AWAITING_APPROVAL, APPROVED, EXECUTED, REJECTED, EXPIRED
- `ProductCategory`: ACCOUNTS, LOANS, SERVICES
- `SystemRole`: roles de cliente/empleado/supervisor/analista
- `UserStatus`: ACTIVE, INACTIVE, BLOCKED
- `OperationType`: tipos de operaciones auditables

Algunos enums documentan flujos válidos de estado directamente en comentarios (ejemplo: `LoanStatus`, `TransferStatus`).

---

## 10) Configuración técnica actual
Archivo: `src/main/resources/application.properties`

- Base de datos SQL configurada: **MySQL** (`spring.datasource.*` + JPA)
- Base de datos NoSQL configurada: **MongoDB** (`spring.data.mongodb.uri`)

### Interpretación práctica
- Los repositorios que se están usando para `CompanyClient` y `AuditLog` son de **MongoDB**.
- La configuración de MySQL/JPA está presente, pero en el código mostrado no se observan entidades JPA (`@Entity`) en uso directo para esos endpoints.

---

## 11) Dependencias principales (pom.xml)
- `spring-boot-starter-web`
- `spring-boot-starter-security`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-data-mongodb`
- `mysql-connector-j`
- `lombok`

Esto confirma una arquitectura híbrida preparada para usar SQL + Mongo.

---

## 12) Guía rápida para entender el código al leerlo
Si vas a estudiar el proyecto por primera vez, sigue este orden:

1. `WilmerVegaApplication.java` (entrada)
2. `config/SecurityConfig.java` (reglas de acceso)
3. Controladores (`controller/`) para ver endpoints
4. Servicios (`service/`) para ver lógica
5. Repositorios (`repository/`) para ver persistencia
6. Modelos (`domain/models/`) para reglas y relaciones
7. `application.properties` y `pom.xml` para infraestructura

---

## 13) Ejemplo mental completo (de principio a fin)
Caso: consultar logs por usuario

1. Cliente hace `GET /api/audit-logs/user/10`
2. Security verifica autenticación HTTP Basic
3. `AuditLogController.getByUserId(10)` recibe la solicitud
4. Controlador llama `AuditLogService.findByUserId(10)`
5. Servicio llama `AuditLogRepository.findByUserId(10)`
6. Mongo devuelve lista de logs
7. API responde `200 OK` con JSON

---

## 14) Puntos de mejora detectados (sin cambiar el código)
1. **Consistencia de modelo**: revisar herencia `CompanyClient extends NaturalClient`
2. **Duplicidad en service**: consolidar métodos repetidos en `CompanyClientService`
3. **Estrategia de persistencia**: documentar oficialmente qué va en MySQL y qué va en Mongo
4. **Manejo de errores**: centralizar excepciones con `@ControllerAdvice`
5. **DTOs/mappers**: actualmente existe `CompanyClientDTO`, pero los controladores usan entidades directas

---

## 15) Resumen
El proyecto ya tiene una base sólida por capas, seguridad básica y un dominio bancario amplio. La API funcional actual se concentra en clientes empresa y auditoría usando MongoDB, mientras el resto del dominio está modelado y listo para evolucionar con más casos de uso.
