# Documentación de Arquitectura — Gestión de un Banco API

Modelo C4 completo del sistema bancario. Los diagramas siguen el estándar **C4 Model** (Context → Container → Component → Code) usando notación **Mermaid**.

---

## Tabla de Contenidos

- [Nivel 1 — Diagrama de Contexto del Sistema](#nivel-1--diagrama-de-contexto-del-sistema)
- [Nivel 2 — Diagrama de Contenedores](#nivel-2--diagrama-de-contenedores)
- [Nivel 3 — Diagrama de Componentes (API)](#nivel-3--diagrama-de-componentes-api)
- [Nivel 3 — Diagrama de Componentes (Dominio)](#nivel-3--diagrama-de-componentes-dominio)
- [Flujos de Secuencia](#flujos-de-secuencia)
  - [Autenticación JWT](#flujo-1-autenticación-jwt)
  - [Creación de Transferencia](#flujo-2-creación-de-transferencia)
  - [Aprobación de Transferencia](#flujo-3-aprobación-de-transferencia)
  - [Aprobación de Préstamo](#flujo-4-aprobación-de-préstamo)
  - [Desembolso de Préstamo](#flujo-5-desembolso-de-préstamo)
- [Diagrama de Clases del Dominio](#diagrama-de-clases-del-dominio)
- [Flujo de Seguridad JWT](#flujo-de-seguridad-jwt)
- [Diagrama Entidad-Relación](#diagrama-entidad-relación)

---

## Nivel 1 — Diagrama de Contexto del Sistema

```mermaid
C4Context
    title Sistema de Gestión Bancaria — Diagrama de Contexto

    Person(clienteNatural, "Cliente Natural", "Persona natural registrada en el banco. Puede crear transferencias y consultar cuentas.")
    Person(clienteEmpresa, "Cliente Empresa", "Empresa registrada. Puede hacer transferencias de alto valor que requieren aprobación.")
    Person(empleado, "Empleado Bancario", "Teller o empleado comercial. Gestiona cuentas, clientes y préstamos.")
    Person(supervisor, "Supervisor de Empresa", "Aprueba o rechaza transferencias de alto valor (> 10,000,000 COP).")
    Person(analista, "Analista Interno", "Aprueba, rechaza y desembolsa préstamos. Consulta logs de auditoría.")
    Person(admin, "Administrador", "Gestiona usuarios del sistema y consulta logs de auditoría.")

    System(bancoSystem, "Wilmer Vega — Banco API", "Sistema REST de gestión bancaria. Expone endpoints para manejo de cuentas, transferencias, préstamos y clientes con autenticación JWT.")

    Rel(clienteNatural, bancoSystem, "Consulta cuentas y transfiere dinero", "HTTPS/JSON")
    Rel(clienteEmpresa, bancoSystem, "Realiza transferencias empresariales", "HTTPS/JSON")
    Rel(empleado, bancoSystem, "Gestiona clientes, cuentas y préstamos", "HTTPS/JSON")
    Rel(supervisor, bancoSystem, "Aprueba o rechaza transferencias pendientes", "HTTPS/JSON")
    Rel(analista, bancoSystem, "Gestiona ciclo de vida de préstamos", "HTTPS/JSON")
    Rel(admin, bancoSystem, "Administra usuarios y consulta auditoría", "HTTPS/JSON")
```

---

## Nivel 2 — Diagrama de Contenedores

```mermaid
C4Container
    title Banco API — Diagrama de Contenedores

    Person(clienteNatural, "Cliente Natural", "")
    Person(empleado, "Empleado", "")
    Person(supervisor, "Supervisor", "")
    Person(analista, "Analista", "")
    Person(admin, "Administrador", "")

    System_Boundary(bancoSystem, "Banco API") {
        Container(apiApp, "Wilmer Vega Spring Boot App", "Java 17 / Spring Boot 4", "Aplicación principal. Expone la API REST, aplica seguridad JWT y orquesta la lógica de negocio.")
        ContainerDb(mysqlDb, "MySQL 8", "Base de datos relacional", "Almacena clientes, cuentas bancarias, transferencias, préstamos y usuarios.")
        ContainerDb(mongoDb, "MongoDB", "Base de datos documental", "Almacena logs de auditoría de operaciones críticas (transferencias, préstamos).")
    }

    Rel(clienteNatural, apiApp, "GET/POST /api/accounts, /api/customers", "HTTPS Bearer JWT")
    Rel(empleado, apiApp, "CRUD /api/employee/**", "HTTPS Bearer JWT")
    Rel(supervisor, apiApp, "POST /api/supervisor/transfers/{id}/approve|reject", "HTTPS Bearer JWT")
    Rel(analista, apiApp, "POST /api/analyst/loans/{id}/approve|reject|disburse", "HTTPS Bearer JWT")
    Rel(admin, apiApp, "GET/POST/DELETE /api/admin/**", "HTTPS Bearer JWT")

    Rel(apiApp, mysqlDb, "Lee y escribe entidades del dominio", "JPA/Hibernate TCP:3306")
    Rel(apiApp, mongoDb, "Escribe y consulta logs de auditoría", "Spring Data MongoDB TCP:27017")
```

---

## Nivel 3 — Diagrama de Componentes (API)

```mermaid
C4Component
    title Banco Spring Boot App — Diagrama de Componentes

    Container_Boundary(apiApp, "Banco Spring Boot App") {

        Component(jwtFilter, "JwtAuthFilter", "Spring Security Filter", "Intercepta cada request, valida el JWT y establece el contexto de autenticación.")
        Component(securityConfig, "SecurityConfig", "Spring Security Config", "Define reglas de autorización por rol y endpoints públicos.")

        Component(authCtrl, "AuthController", "REST Controller /auth", "Login y registro de usuarios. Retorna JWT token.")
        Component(adminCtrl, "AdminController", "REST Controller /api/admin", "CRUD de usuarios del sistema y consulta de audit logs.")
        Component(employeeCtrl, "EmployeeController", "REST Controller /api/employee", "CRUD de cuentas bancarias, clientes y préstamos.")
        Component(customerCtrl, "CustomerController", "REST Controller /api/customers", "CRUD de clientes naturales y empresas.")
        Component(accountCtrl, "BankAccountController", "REST Controller /api/accounts", "Creación, consulta y actualización de cuentas bancarias.")
        Component(analystCtrl, "AnalystController", "REST Controller /api/analyst", "Aprobación, rechazo y desembolso de préstamos.")
        Component(supervisorCtrl, "CompanySupervisorController", "REST Controller /api/supervisor", "Aprobación y rechazo de transferencias de alto valor.")
        Component(auditCtrl, "AuditLogController", "REST Controller /api/audit", "Consulta de logs de auditoría desde MongoDB.")
        Component(exHandler, "GlobalExceptionHandler", "RestControllerAdvice", "Centraliza el manejo de errores HTTP con ErrorResponse tipado.")

        Component(usecases, "Use Cases", "Application Layer", "AdminUseCase, EmployeeUseCase, CustomerUseCase, BankAccountUseCase, AnalystUseCase, CompanySupervisorUseCase, AuthUseCase.")
        Component(domainServices, "Domain Services", "Domain Layer (POJOs puros)", "CreateNaturalClient, CreateBankAccount, CreateTransfer, ApproveLoanService, DisburseLoanService, etc. (31 servicios)")
        Component(ports, "Ports (Interfaces)", "Domain Layer", "UserPort, NaturalClientPort, CompanyClientPort, BankAccountPort, LoanPort, TransferPort, AuditLogPort, AuditLogMongoPort.")
        Component(persistence, "Persistence Adapters", "Application Layer", "Implementaciones JPA (MySQL) y MongoDB de los puertos del dominio.")
        Component(scheduler, "TransferExpirationScheduler", "Scheduled Task", "Expira automáticamente transferencias AWAITING_APPROVAL después de 60 minutos.")
    }

    Rel(jwtFilter, securityConfig, "Aplica reglas de autorización")
    Rel(authCtrl, usecases, "AuthUseCase")
    Rel(adminCtrl, usecases, "AdminUseCase")
    Rel(employeeCtrl, usecases, "EmployeeUseCase")
    Rel(customerCtrl, usecases, "CustomerUseCase")
    Rel(accountCtrl, usecases, "BankAccountUseCase")
    Rel(analystCtrl, usecases, "AnalystUseCase")
    Rel(supervisorCtrl, usecases, "CompanySupervisorUseCase")
    Rel(usecases, domainServices, "Invoca servicios de dominio")
    Rel(domainServices, ports, "Usa interfaces de puertos")
    Rel(ports, persistence, "Implementado por adaptadores de persistencia")
    Rel(scheduler, domainServices, "ExpireTransferService")
```

---

## Nivel 3 — Diagrama de Componentes (Dominio)

```mermaid
C4Component
    title Banco API — Capa de Dominio

    Container_Boundary(domain, "Domain Layer") {
        Component(person, "Person (abstract)", "Domain Model", "Clase base con id.")
        Component(client, "Client (abstract)", "Domain Model", "Extiende Person. Tiene documentNumber, bankAccounts, loans.")
        Component(naturalClient, "NaturalClient", "Domain Model", "Cliente persona natural. Tiene fullName, birthDate, role.")
        Component(companyClient, "CompanyClient", "Domain Model", "Cliente empresa. Tiene businessName y legalRepresentative.")
        Component(bankAccount, "BankAccount", "Domain Model", "Cuenta bancaria con accountNumber, balance, status, type, currency.")
        Component(transfer, "Transfer", "Domain Model", "Transferencia entre cuentas. Tiene monto, estado y fechas de aprobación.")
        Component(loan, "Loan", "Domain Model", "Préstamo. Ciclo de vida: PENDING -> UNDER_REVIEW -> APPROVED -> DISBURSED.")
        Component(user, "User", "Domain Model", "Usuario del sistema con username, password y SystemRole.")
        Component(auditLog, "AuditLog", "Domain Model", "Log de auditoría. Almacenado en MongoDB.")
        Component(systemRole, "SystemRole", "Enum", "NATURAL_CLIENT, COMPANY_CLIENT, TELLER_EMPLOYEE, COMMERCIAL_EMPLOYEE, COMPANY_EMPLOYEE, COMPANY_SUPERVISOR, INTERNAL_ANALYST")
        Component(transferStatus, "TransferStatus", "Enum", "AWAITING_APPROVAL, EXECUTED, REJECTED, EXPIRED")
        Component(loanStatus, "LoanStatus", "Enum", "PENDING, UNDER_REVIEW, APPROVED, REJECTED, DISBURSED")
    }

    Rel(naturalClient, client, "extiende")
    Rel(companyClient, client, "extiende")
    Rel(client, person, "extiende")
    Rel(companyClient, naturalClient, "tiene legalRepresentative N-a-1")
    Rel(bankAccount, client, "pertenece a holder N-a-1")
    Rel(transfer, bankAccount, "origen y destino N-a-1")
    Rel(loan, client, "solicitado por N-a-1")
    Rel(loan, bankAccount, "desembolsado en N-a-1")
    Rel(user, naturalClient, "vinculado a N-a-1")
    Rel(user, systemRole, "tiene rol")
    Rel(transfer, transferStatus, "tiene estado")
    Rel(loan, loanStatus, "tiene estado")
```

---

## Flujos de Secuencia

### Flujo 1: Autenticación JWT

```mermaid
sequenceDiagram
    actor Cliente
    participant AuthController
    participant AuthUseCase
    participant UserPort
    participant JwtService

    Cliente->>AuthController: POST /auth/login {username, password}
    AuthController->>AuthUseCase: login(username, password)
    AuthUseCase->>UserPort: findByUsername(username)
    UserPort-->>AuthUseCase: User (con password BCrypt)
    AuthUseCase->>AuthUseCase: BCrypt.matches(password, hash)
    AuthUseCase->>JwtService: generateToken(username, role)
    JwtService-->>AuthUseCase: JWT String
    AuthUseCase-->>AuthController: LoginResult(token, username, role)
    AuthController-->>Cliente: 200 OK {token, username, role}

    Note over Cliente,AuthController: Peticiones posteriores:<br/>Authorization: Bearer <token>
```

### Flujo 2: Creación de Transferencia

```mermaid
sequenceDiagram
    actor Usuario
    participant JwtAuthFilter
    participant CustomerController
    participant CustomerUseCase
    participant CreateTransfer
    participant BankAccountPort
    participant AuditLogMongoPort

    Usuario->>JwtAuthFilter: POST /api/accounts/transfers + Bearer Token + {amount, sourceAccount, destinationAccount}
    JwtAuthFilter->>JwtAuthFilter: Valida JWT + verifica rol
    JwtAuthFilter-->>CustomerController: Request autorizado
    CustomerController->>CustomerUseCase: createTransfer(transfer)
    CustomerUseCase->>CreateTransfer: save(transfer)
    CreateTransfer->>BankAccountPort: findByAccountNumber(sourceAccount)
    BankAccountPort-->>CreateTransfer: BankAccount (ACTIVE)
    alt amount <= 10,000,000 COP
        CreateTransfer->>BankAccountPort: save(sourceAccount con balance reducido)
        CreateTransfer->>BankAccountPort: save(destinationAccount con balance aumentado)
        CreateTransfer-->>Usuario: 201 Created - status: EXECUTED
    else amount > 10,000,000 COP
        CreateTransfer-->>Usuario: 201 Created - status: AWAITING_APPROVAL
    end
    CreateTransfer->>AuditLogMongoPort: save(auditLog)
```

### Flujo 3: Aprobación de Transferencia

```mermaid
sequenceDiagram
    actor Supervisor
    participant CompanySupervisorController
    participant ApproveTransferService
    participant TransferPort
    participant BankAccountPort
    participant AuditLogMongoPort

    Supervisor->>CompanySupervisorController: POST /api/supervisor/transfers/{id}/approve
    CompanySupervisorController->>ApproveTransferService: approve(transferId, supervisorId, role)
    ApproveTransferService->>TransferPort: findById(transferId)
    TransferPort-->>ApproveTransferService: Transfer (AWAITING_APPROVAL)
    ApproveTransferService->>ApproveTransferService: Verifica que no hayan pasado 60 minutos
    alt Expirada
        ApproveTransferService->>TransferPort: save(transfer -> EXPIRED)
        ApproveTransferService-->>Supervisor: 400 Bad Request - Transfer has expired
    else Vigente
        ApproveTransferService->>BankAccountPort: Reduce balance origen
        ApproveTransferService->>BankAccountPort: Aumenta balance destino
        ApproveTransferService->>TransferPort: save(transfer -> EXECUTED)
        ApproveTransferService->>AuditLogMongoPort: save(auditLog)
        ApproveTransferService-->>Supervisor: 200 OK - status: EXECUTED
    end
```

### Flujo 4: Aprobación de Préstamo

```mermaid
sequenceDiagram
    actor Analista
    participant AnalystController
    participant ApproveLoanService
    participant LoanPort
    participant AuditLogMongoPort

    Analista->>AnalystController: POST /api/analyst/loans/{id}/approve?analystUserId=1
    AnalystController->>ApproveLoanService: approve(loanId, analystId, role)
    ApproveLoanService->>LoanPort: findById(loanId)
    LoanPort-->>ApproveLoanService: Loan (UNDER_REVIEW)
    ApproveLoanService->>ApproveLoanService: Verifica approvedAmount > 0
    ApproveLoanService->>LoanPort: save(loan -> APPROVED)
    ApproveLoanService->>AuditLogMongoPort: save(auditLog LOAN_APPROVAL)
    ApproveLoanService-->>Analista: 200 OK - status: APPROVED
```

### Flujo 5: Desembolso de Préstamo

```mermaid
sequenceDiagram
    actor Analista
    participant AnalystController
    participant DisburseLoanService
    participant LoanPort
    participant BankAccountPort
    participant AuditLogMongoPort

    Analista->>AnalystController: POST /api/analyst/loans/{id}/disburse?disbursementAccountId=5
    AnalystController->>DisburseLoanService: disburse(loanId, accountId, analystId, role)
    DisburseLoanService->>LoanPort: findById(loanId)
    LoanPort-->>DisburseLoanService: Loan (APPROVED)
    DisburseLoanService->>BankAccountPort: findById(accountId)
    BankAccountPort-->>DisburseLoanService: BankAccount (ACTIVE)
    DisburseLoanService->>BankAccountPort: save(account con balance + approvedAmount)
    DisburseLoanService->>LoanPort: save(loan -> DISBURSED)
    DisburseLoanService->>AuditLogMongoPort: save(auditLog LOAN_DISBURSEMENT)
    DisburseLoanService-->>Analista: 200 OK - status: DISBURSED
```

---

## Diagrama de Clases del Dominio

```mermaid
classDiagram
    class Person {
        <<abstract>>
        +Long id
    }

    class Client {
        <<abstract>>
        +String documentNumber
        +List~BankAccount~ bankAccounts
        +List~Loan~ loans
    }

    class NaturalClient {
        +String fullName
        +LocalDate birthDate
        +SystemRole role
    }

    class CompanyClient {
        +String businessName
        +NaturalClient legalRepresentative
    }

    class BankAccount {
        +Long id
        +String accountNumber
        +AccountType accountType
        +Currency currency
        +BigDecimal currentBalance
        +AccountStatus accountStatus
        +LocalDate openingDate
        +Client holder
    }

    class Transfer {
        +Long transferId
        +BankAccount sourceAccount
        +BankAccount destinationAccount
        +BigDecimal amount
        +LocalDateTime creationDateTime
        +LocalDateTime approvalDateTime
        +TransferStatus transferStatus
        +Long creatorUserId
        +Long approverUserId
    }

    class Loan {
        +Long loanId
        +LoanType loanType
        +Client clientApplicant
        +BigDecimal requestedAmount
        +BigDecimal approvedAmount
        +BigDecimal interestRate
        +Integer termInMonths
        +LoanStatus loanStatus
        +LocalDate approvalDate
        +LocalDate disbursementDate
        +BankAccount disbursementAccount
        +Long approvedByUserId
    }

    class User {
        +Long userId
        +String username
        +String password
        +SystemRole systemRole
        +NaturalClient relatedClient
    }

    class AuditLog {
        +String id
        +OperationType operationType
        +LocalDateTime operationDateTime
        +Long userId
        +String userRole
        +String affectedProductId
        +Map details
    }

    class SystemRole {
        <<enumeration>>
        NATURAL_CLIENT
        COMPANY_CLIENT
        TELLER_EMPLOYEE
        COMMERCIAL_EMPLOYEE
        COMPANY_EMPLOYEE
        COMPANY_SUPERVISOR
        INTERNAL_ANALYST
    }

    class TransferStatus {
        <<enumeration>>
        AWAITING_APPROVAL
        EXECUTED
        REJECTED
        EXPIRED
    }

    class LoanStatus {
        <<enumeration>>
        PENDING
        UNDER_REVIEW
        APPROVED
        REJECTED
        DISBURSED
    }

    class AccountStatus {
        <<enumeration>>
        ACTIVE
        INACTIVE
        BLOCKED
        CLOSED
    }

    class AccountType {
        <<enumeration>>
        SAVINGS
        CHECKING
        PERSONAL
        BUSINESS
    }

    class Currency {
        <<enumeration>>
        USD
        COP
        EUR
    }

    Person <|-- Client
    Client <|-- NaturalClient
    Client <|-- CompanyClient
    CompanyClient "N" --> "1" NaturalClient : legalRepresentative
    BankAccount "N" --> "1" Client : holder
    Transfer "N" --> "1" BankAccount : sourceAccount
    Transfer "N" --> "1" BankAccount : destinationAccount
    Loan "N" --> "1" Client : clientApplicant
    Loan "N" --> "1" BankAccount : disbursementAccount
    User "N" --> "1" NaturalClient : relatedClient
    User --> SystemRole
    Transfer --> TransferStatus
    Loan --> LoanStatus
    BankAccount --> AccountStatus
    BankAccount --> AccountType
    BankAccount --> Currency
```

---

## Flujo de Seguridad JWT

```mermaid
flowchart TD
    A([Request HTTP]) --> B{¿Es /auth/**\no /ping?}
    B -- Sí --> C[Procesado sin autenticación]
    B -- No --> D[JwtAuthFilter]

    D --> E{¿Header\nAuthorization\npresente?}
    E -- No --> F[401 Unauthorized\nNo autenticado]
    E -- Sí --> G[Extrae Bearer Token]

    G --> H{¿Token\nválido?}
    H -- Expirado --> I[401 Unauthorized\nToken expirado]
    H -- Inválido --> J[401 Unauthorized\nToken inválido]
    H -- Válido --> K[Extrae claims:\nusername, role]

    K --> L[SecurityContext\nAuthentication establecida]
    L --> M{¿Rol autorizado\npara el endpoint?}

    M -- No --> N[403 Forbidden\nAcceso denegado]
    M -- Sí --> O[Controller procesa request]
    O --> P([Response HTTP])

    style F fill:#ff6b6b,color:#fff
    style I fill:#ff6b6b,color:#fff
    style J fill:#ff6b6b,color:#fff
    style N fill:#ff9f43,color:#fff
    style C fill:#54a0ff,color:#fff
    style P fill:#1dd1a1,color:#fff
```

---

## Diagrama Entidad-Relación

```mermaid
erDiagram
    CLIENTS {
        bigint id PK
        varchar document_number UK
        varchar email
        varchar phone
        varchar address
    }

    NATURAL_CLIENTS {
        bigint id PK FK
        varchar full_name
        date birth_date
        varchar role
    }

    COMPANY_CLIENTS {
        bigint id PK FK
        varchar business_name
        bigint legal_rep_id FK
    }

    USERS {
        bigint id PK
        varchar username UK
        varchar password
        varchar role
        bigint client_id FK
    }

    BANK_ACCOUNTS {
        bigint id PK
        varchar account_number UK
        varchar account_type
        decimal current_balance
        varchar currency
        varchar account_status
        date opening_date
        bigint client_id FK
    }

    TRANSFERS {
        bigint transfer_id PK
        bigint source_account_id FK
        bigint destination_account_id FK
        decimal amount
        datetime creation_date_time
        datetime approval_date_time
        varchar transfer_status
        bigint creator_user_id
        bigint approver_user_id
    }

    LOANS {
        bigint loan_id PK
        varchar loan_type
        decimal requested_amount
        decimal approved_amount
        decimal interest_rate
        int term_in_months
        varchar loan_status
        date approval_date
        date disbursement_date
        bigint approved_by_user_id
        bigint client_id FK
        bigint disbursement_account_id FK
    }

    CLIENTS ||--o{ BANK_ACCOUNTS : "titular de"
    CLIENTS ||--o{ LOANS : "solicita"
    NATURAL_CLIENTS ||--|| CLIENTS : "extiende"
    COMPANY_CLIENTS ||--|| CLIENTS : "extiende"
    COMPANY_CLIENTS }o--|| NATURAL_CLIENTS : "representada por"
    USERS }o--|| CLIENTS : "vinculado a"
    TRANSFERS }o--|| BANK_ACCOUNTS : "cuenta origen"
    TRANSFERS }o--o| BANK_ACCOUNTS : "cuenta destino"
    LOANS }o--o| BANK_ACCOUNTS : "cuenta desembolso"
```
