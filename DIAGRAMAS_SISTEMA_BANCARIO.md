# 📊 DIAGRAMAS DEL SISTEMA BANCARIO - GESTIÓN DE UN BANCO

> **Proyecto:** Gestión de un Banco - Wilmer Vega  
> **Fecha:** 12 de marzo de 2026  
> **Paquete:** gestiondeunbanco.wilmervega.domain.models

---

## 📋 ÍNDICE

1. [Diagrama de Clases del Modelo](#1-diagrama-de-clases-del-modelo)
2. [Diagrama de Estados - Transferencias](#2-diagrama-de-estados---transferencias)
3. [Diagrama de Estados - Préstamos](#3-diagrama-de-estados---préstamos)
4. [Diagrama de Estados - Cuentas Bancarias](#4-diagrama-de-estados---cuentas-bancarias)
5. [Diagrama de Estados - Usuarios](#5-diagrama-de-estados---usuarios)
6. [Diagrama de Roles del Sistema](#6-diagrama-de-roles-del-sistema)
7. [Diagrama de Tipos de Productos Bancarios](#7-diagrama-de-tipos-de-productos-bancarios)
8. [Diagrama de Flujo - Tipos de Operaciones](#8-diagrama-de-flujo---tipos-de-operaciones)
9. [Resumen del Modelo](#9-resumen-del-modelo)

---

## 1. DIAGRAMA DE CLASES DEL MODELO

Este diagrama muestra todas las clases y enumeraciones actuales del dominio del sistema bancario, con la jerarquía de usuarios refactorizada.

```mermaid
classDiagram
    %% ─── Jerarquía de Personas ─────────────────────────────────
    class Person {
        +Long id
        +String email
        +String phone
        +String address
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
        +List~SystemUser~ systemUsers
        +List~CompanyClient~ representedCompanies
    }

    class CompanyClient {
        +String businessName
        +NaturalClient legalRepresentative
    }

    %% ─── Jerarquía de Usuarios ────────────────────────────────
    class UserManager {
        +String fullName
        +Date birthDate
        +UserStatus userStatus
    }

    class SystemUser {
        +String relatedId
        +NaturalClient naturalClient
        +CompanyClient companyClient
        +String identificationId
        +SystemRole role
        +List~Transfer~ createdTransfers
        +List~Transfer~ approvedTransfers
        +List~AuditLog~ auditLogs
    }

    class User {
        +Long userId
        +Client relatedClient
        +String documentNumber
        +SystemRole systemRole
        +String username
        +String password
    }

    %% ─── Entidades del Negocio ────────────────────────────────
    class BankAccount {
        +Long id
        +String accountNumber
        +AccountType accountType
        +BigDecimal currentBalance
        +Currency currency
        +AccountStatus accountStatus
        +LocalDate openingDate
        +List~Transfer~ outgoingTransfers
        +List~Transfer~ incomingTransfers
        +validateState()
        +hasSingleHolder() boolean
    }

    class BankingProduct {
        +Long id
        +String productCode
        +String productName
        +ProductCategory category
        +Boolean requiresApproval
    }

    class Loan {
        +Long loanId
        +LoanType loanType
        +BigDecimal requestedAmount
        +BigDecimal approvedAmount
        +BigDecimal interestRate
        +Integer termInMonths
        +LoanStatus loanStatus
        +Date approvalDate
        +Date disbursementDate
    }

    class Transfer {
        +Long transferId
        +BigDecimal amount
        +Date creationDateTime
        +Date approvalDateTime
        +TransferStatus transferStatus
    }

    class AuditLog {
        +Long auditLogId
        +OperationType operationType
        +LocalDateTime operationDateTime
        +Long userId
        +String userRole
        +String affectedProductId
        +Map~String, Object~ details
    }

    %% ─── Enumeraciones ────────────────────────────────────────
    class SystemRole {
        <<enumeration>>
        NATURAL_CLIENT
        COMPANY_CLIENT
        TELLER_EMPLOYEE
        COMMERCIAL_EMPLOYEE
        COMPANY_EMPLOYEE
        COMPANY_SUPERVISOR
        INTERNAL_ANALYST
        +String getDescription()
    }

    class UserStatus {
        <<enumeration>>
        ACTIVE
        INACTIVE
        BLOCKED
        +String getDescription()
    }

    class AccountStatus {
        <<enumeration>>
        ACTIVE
        BLOCKED
        CANCELLED
        +String getDescription()
    }

    class AccountType {
        <<enumeration>>
        SAVINGS
        CHECKING
        PERSONAL
        BUSINESS
        +String getDescription()
    }

    class Currency {
        <<enumeration>>
        USD
        COP
        EUR
        +String getDescription()
    }

    class LoanStatus {
        <<enumeration>>
        UNDER_REVIEW
        APPROVED
        REJECTED
        DISBURSED
        OVERDUE
        CANCELLED
        +String getDescription()
    }

    class LoanType {
        <<enumeration>>
        CONSUMER
        VEHICLE
        MORTGAGE
        BUSINESS
        +String getDescription()
    }

    class TransferStatus {
        <<enumeration>>
        PENDING
        AWAITING_APPROVAL
        APPROVED
        EXECUTED
        REJECTED
        EXPIRED
        +String getDescription()
    }

    class ProductCategory {
        <<enumeration>>
        ACCOUNTS
        LOANS
        SERVICES
        +String getDescription()
    }

    class OperationType {
        <<enumeration>>
        ACCOUNT_OPENING
        ACCOUNT_CLOSURE
        DEPOSIT
        WITHDRAWAL
        TRANSFER_EXECUTED
        TRANSFER_REJECTED
        TRANSFER_EXPIRED
        LOAN_REQUEST
        LOAN_APPROVAL
        LOAN_REJECTION
        LOAN_DISBURSEMENT
        USER_CREATION
        USER_BLOCKING
        LOGIN
        LOGOUT
        +String getDescription()
    }

    %% ─── Herencia ─────────────────────────────────────────────
    Person          <|-- Client
    Person          <|-- UserManager
    Client          <|-- NaturalClient
    Client          <|-- CompanyClient
    UserManager     <|-- SystemUser
    UserManager     <|-- User

    %% ─── Asociaciones ─────────────────────────────────────────
    Client          "1" --> "0..*" BankAccount       : bankAccounts
    Client          "1" --> "0..*" Loan              : loans
    NaturalClient   "1" --> "0..*" SystemUser        : systemUsers
    NaturalClient   "1" --> "0..*" CompanyClient     : representedCompanies

    SystemUser      "1" --> "0..*" Transfer          : createdTransfers
    SystemUser      "1" --> "0..*" Transfer          : approvedTransfers
    SystemUser      "1" --> "0..*" AuditLog          : auditLogs
    SystemUser      --> NaturalClient                : naturalClient
    SystemUser      --> CompanyClient                : companyClient

    User            --> Client                       : relatedClient

    BankAccount     --> BankingProduct               : bankingProduct
    BankAccount     --> Client                       : holder
    BankAccount     --> AccountType
    BankAccount     --> AccountStatus
    BankAccount     --> Currency

    Loan            --> BankingProduct               : bankingProduct
    Loan            --> Client                       : applicant
    Loan            --> BankAccount                  : disbursementAccount
    Loan            --> LoanType
    Loan            --> LoanStatus

    Transfer        --> BankAccount                  : sourceAccount
    Transfer        --> BankAccount                  : destinationAccount
    Transfer        --> SystemUser                   : creatorUser
    Transfer        --> SystemUser                   : approverUser
    Transfer        --> TransferStatus

    AuditLog        --> SystemUser                   : user
    AuditLog        --> OperationType

    NaturalClient   --> SystemRole
    SystemUser      --> SystemRole
    User            --> SystemRole
    UserManager     --> UserStatus
    BankingProduct  --> ProductCategory
```

**Descripción de la jerarquía actualizada:**
- **Person**: clase base con datos de contacto comunes
- **UserManager**: nueva clase madre de usuarios — concentra `fullName`, `birthDate` y `userStatus`
- **SystemUser** `extends UserManager`: usuario del sistema bancario con rol, transferencias y logs
- **User** `extends UserManager`: usuario de acceso con credenciales (`username`, `password`)
- **Client** `extends Person`: clase base para clientes del banco
- **NaturalClient** `extends Client`: cliente persona natural con empresas representadas
- **CompanyClient** `extends Client`: cliente empresa con representante legal

---

## 2. DIAGRAMA DE ESTADOS - TRANSFERENCIAS

Flujo de estados para las transferencias bancarias según el monto.

```mermaid
stateDiagram-v2
    [*] --> PENDIENTE : Transferencia de bajo monto
    [*] --> EN_ESPERA_APROBACION : Transferencia de alto monto

    PENDIENTE --> EJECUTADA : Procesar automáticamente

    EN_ESPERA_APROBACION --> APROBADA : Aprobada por supervisor
    EN_ESPERA_APROBACION --> RECHAZADA : Rechazada por supervisor
    EN_ESPERA_APROBACION --> VENCIDA : Más de 60 minutos sin aprobación

    APROBADA --> EJECUTADA : Ejecutar transferencia

    EJECUTADA --> [*]
    RECHAZADA --> [*]
    VENCIDA --> [*]

    note right of PENDIENTE
        Para montos bajos
        Se ejecuta automáticamente
    end note

    note right of EN_ESPERA_APROBACION
        Para montos altos
        Requiere aprobación
        Vence en 60 minutos
    end note

    note right of EJECUTADA
        Transferencia completada
        Fondos transferidos
    end note
```

**Flujos permitidos:**
- **Flujo bajo monto:** `PENDIENTE → EJECUTADA`
- **Flujo alto monto:** `EN_ESPERA_APROBACION → APROBADA → EJECUTADA`
- **Flujo de rechazo:** `EN_ESPERA_APROBACION → RECHAZADA`
- **Flujo de vencimiento:** `EN_ESPERA_APROBACION → VENCIDA` (60 minutos)

---

## 3. DIAGRAMA DE ESTADOS - PRÉSTAMOS

Ciclo de vida completo de un préstamo bancario.

```mermaid
stateDiagram-v2
    [*] --> EN_ESTUDIO : Cliente solicita préstamo

    EN_ESTUDIO --> APROBADO : Analista aprueba
    EN_ESTUDIO --> RECHAZADO : Analista rechaza

    APROBADO --> DESEMBOLSADO : Realizar desembolso

    DESEMBOLSADO --> EN_MORA : Cliente no paga a tiempo
    DESEMBOLSADO --> CANCELADO : Cliente termina de pagar

    EN_MORA --> CANCELADO : Cliente regulariza y termina de pagar

    RECHAZADO --> [*]
    CANCELADO --> [*]

    note right of EN_ESTUDIO
        Analista interno evalúa
        la solicitud del préstamo
    end note

    note right of APROBADO
        Préstamo aprobado
        Listo para desembolsar
    end note

    note right of DESEMBOLSADO
        Fondos entregados al cliente
        Comienza el plan de pagos
    end note

    note right of EN_MORA
        Cliente atrasado en pagos
        Puede regularizar
    end note
```

**Flujos permitidos:**
- **Flujo de evaluación:** `EN_ESTUDIO → APROBADO | RECHAZADO`
- **Flujo de desembolso:** `APROBADO → DESEMBOLSADO`
- **Flujo de pago:** `DESEMBOLSADO → CANCELADO`
- **Flujo de mora:** `DESEMBOLSADO → EN_MORA → CANCELADO`

---

## 4. DIAGRAMA DE ESTADOS - CUENTAS BANCARIAS

Estados posibles de una cuenta bancaria.

```mermaid
stateDiagram-v2
    [*] --> ACTIVA : Apertura de cuenta

    ACTIVA --> BLOQUEADA : Actividad sospechosa / Solicitud cliente
    BLOQUEADA --> ACTIVA : Desbloquear cuenta
    BLOQUEADA --> CANCELADA : Cancelar definitivamente
    ACTIVA --> CANCELADA : Cierre de cuenta

    CANCELADA --> [*]

    note right of ACTIVA
        Cuenta operativa
        Permite transacciones
    end note

    note right of BLOQUEADA
        Cuenta suspendida
        temporalmente
    end note

    note right of CANCELADA
        Cuenta cerrada
        definitivamente
    end note
```

**Estados:**
- **ACTIVA**: Cuenta operativa que permite todas las transacciones
- **BLOQUEADA**: Cuenta temporalmente suspendida (reversible)
- **CANCELADA**: Cuenta cerrada permanentemente

---

## 5. DIAGRAMA DE ESTADOS - USUARIOS

Gestión del ciclo de vida de usuarios del sistema.

```mermaid
stateDiagram-v2
    [*] --> ACTIVO : Crear usuario

    ACTIVO --> INACTIVO : Usuario inactivo por tiempo
    ACTIVO --> BLOQUEADO : Bloqueo por seguridad

    INACTIVO --> ACTIVO : Usuario reactiva cuenta
    BLOQUEADO --> ACTIVO : Desbloqueo administrativo

    note right of ACTIVO
        Usuario con permisos
        completos del sistema
    end note

    note right of INACTIVO
        Usuario sin actividad
        reciente
    end note

    note right of BLOQUEADO
        Usuario bloqueado
        por motivos de seguridad
    end note
```

**Estados:**
- **ACTIVO**: Usuario con acceso completo al sistema
- **INACTIVO**: Usuario sin actividad reciente (reversible)
- **BLOQUEADO**: Usuario bloqueado por seguridad (requiere desbloqueo administrativo)

---

## 6. DIAGRAMA DE ROLES DEL SISTEMA

Jerarquía y clasificación de roles en el sistema bancario.

```mermaid
graph TD
    A[Usuarios del Sistema] --> B[Clientes]
    A --> C[Empleados del Banco]

    B --> B1[Cliente Persona Natural]
    B --> B2[Cliente Empresa]

    C --> C1[Empleado de Ventanilla]
    C --> C2[Empleado Comercial]
    C --> C3[Empleado de Empresa]
    C --> C4[Supervisor de Empresa]
    C --> C5[Analista Interno]

    B1 -.->|realiza| D[Operaciones de Cliente]
    B2 -.->|realiza| D

    C1 -.->|gestiona| E[Atención al Cliente]
    C2 -.->|gestiona| F[Ventas y Productos]
    C3 -.->|gestiona| G[Cuentas Empresariales]
    C4 -.->|aprueba| H[Transferencias Altas]
    C5 -.->|analiza| I[Solicitudes de Préstamo]

    style B1 fill:#90EE90
    style B2 fill:#90EE90
    style C5 fill:#FFB6C1
    style C4 fill:#FFD700
```

**7 Roles del Sistema:**

### Clientes:
1. **CLIENTE_PERSONA_NATURAL** - Personas físicas
2. **CLIENTE_EMPRESA** - Personas jurídicas

### Empleados:
3. **EMPLEADO_VENTANILLA** - Atención al cliente
4. **EMPLEADO_COMERCIAL** - Ventas y productos
5. **EMPLEADO_EMPRESA** - Gestión de cuentas empresariales
6. **SUPERVISOR_EMPRESA** - Aprobación de transferencias de alto monto
7. **ANALISTA_INTERNO** - Evaluación de solicitudes de préstamo

---

## 7. DIAGRAMA DE TIPOS DE PRODUCTOS BANCARIOS

Catálogo de productos y servicios del banco.

```mermaid
graph LR
    A[Productos Bancarios] --> B[CUENTAS]
    A --> C[PRÉSTAMOS]
    A --> D[SERVICIOS]

    B --> B1[Ahorros]
    B --> B2[Corriente]
    B --> B3[Personal]
    B --> B4[Empresarial]

    C --> C1[Consumo]
    C --> C2[Vehículo]
    C --> C3[Hipotecario]
    C --> C4[Empresarial]

    B1 -.->|soporta| M[Monedas]
    B2 -.->|soporta| M
    B3 -.->|soporta| M
    B4 -.->|soporta| M

    M --> M1[USD - Dólar]
    M --> M2[COP - Peso]
    M --> M3[EUR - Euro]

    style B fill:#87CEEB
    style C fill:#FFA07A
    style D fill:#DDA0DD
```

**Categorías de Productos:**

### CUENTAS (4 tipos):
- **Ahorros** - Cuenta de ahorro personal
- **Corriente** - Cuenta corriente con cheques
- **Personal** - Cuenta personal estándar
- **Empresarial** - Cuenta para empresas

### PRÉSTAMOS (4 tipos):
- **Consumo** - Préstamo personal
- **Vehículo** - Financiamiento de vehículos
- **Hipotecario** - Préstamo para vivienda
- **Empresarial** - Préstamo comercial

### SERVICIOS:
- Otros servicios bancarios

**Monedas Soportadas:**
- **USD** - Dólar Estadounidense
- **COP** - Peso Colombiano
- **EUR** - Euro

---

## 8. DIAGRAMA DE FLUJO - TIPOS DE OPERACIONES

Clasificación de las 15 operaciones del sistema.

```mermaid
graph TD
    A[Tipos de Operaciones] --> B[Operaciones de Cuenta]
    A --> C[Operaciones de Transferencia]
    A --> D[Operaciones de Préstamo]
    A --> E[Operaciones de Usuario]

    B --> B1[Apertura de Cuenta]
    B --> B2[Cierre de Cuenta]
    B --> B3[Depósito]
    B --> B4[Retiro]

    C --> C1[Transferencia Ejecutada]
    C --> C2[Transferencia Rechazada]
    C --> C3[Transferencia Vencida]

    D --> D1[Solicitud de Préstamo]
    D --> D2[Aprobación de Préstamo]
    D --> D3[Rechazo de Préstamo]
    D --> D4[Desembolso de Préstamo]

    E --> E1[Creación de Usuario]
    E --> E2[Bloqueo de Usuario]
    E --> E3[Inicio de Sesión]
    E --> E4[Cierre de Sesión]

    style B fill:#90EE90
    style C fill:#FFD700
    style D fill:#FFA07A
    style E fill:#87CEEB
```

**15 Tipos de Operaciones:**

### Operaciones de Cuenta (4):
1. APERTURA_CUENTA
2. CIERRE_CUENTA
3. DEPOSITO
4. RETIRO

### Operaciones de Transferencia (3):
5. TRANSFERENCIA_EJECUTADA
6. TRANSFERENCIA_RECHAZADA
7. TRANSFERENCIA_VENCIDA

### Operaciones de Préstamo (4):
8. SOLICITUD_PRESTAMO
9. APROBACION_PRESTAMO
10. RECHAZO_PRESTAMO
11. DESEMBOLSO_PRESTAMO

### Operaciones de Usuario (4):
12. CREACION_USUARIO
13. BLOQUEO_USUARIO
14. INICIO_SESION
15. CIERRE_SESION

---

## 9. RESUMEN DEL MODELO

### 📦 Estructura del Paquete `models/`

```
gestiondeunbanco.wilmervega.domain.models/
├── Person.java                  (clase base)
├── UserManager.java             (clase madre de usuarios)
├── SystemUser.java              (extiende UserManager)
├── User.java                    (extiende UserManager)
├── NaturalClient.java           (extiende Person)
├── CompanyClient.java           (extiende NaturalClient)
├── BankAccount.java
├── BankingProduct.java
├── Loan.java
├── Transfer.java
├── AuditLog.java
└── Enumeraciones (10):
    ├── AccountStatus.java
    ├── AccountType.java
    ├── Currency.java
    ├── LoanStatus.java
    ├── LoanType.java
    ├── OperationType.java
    ├── ProductCategory.java
    ├── SystemRole.java
    ├── TransferStatus.java
    └── UserStatus.java
```

### 🎯 Jerarquía de Clases

```
Person
├── NaturalClient
│   └── CompanyClient
└── UserManager                  ← clase madre de gestión de usuarios
    ├── SystemUser               ← usuario del sistema bancario
    └── User                     ← usuario de acceso con credenciales
```

### 📊 Enumeraciones del Dominio

| Enumeración | Valores | Descripción |
|-------------|---------|-------------|
| **SystemRole** | 7 | Roles de usuarios y empleados |
| **UserStatus** | 3 | ACTIVE · INACTIVE · BLOCKED |
| **AccountStatus** | 3 | ACTIVE · BLOCKED · CANCELLED |
| **AccountType** | 4 | SAVINGS · CHECKING · PERSONAL · BUSINESS |
| **Currency** | 3 | USD · COP · EUR |
| **LoanStatus** | 6 | UNDER_REVIEW → APPROVED/REJECTED → DISBURSED → OVERDUE/CANCELLED |
| **LoanType** | 4 | CONSUMER · VEHICLE · MORTGAGE · BUSINESS |
| **TransferStatus** | 6 | PENDING · AWAITING_APPROVAL · APPROVED · EXECUTED · REJECTED · EXPIRED |
| **ProductCategory** | 3 | ACCOUNTS · LOANS · SERVICES |
| **OperationType** | 15 | Todas las operaciones auditables |

### 🔄 Reglas de Negocio Principales

#### Transferencias
- **Bajo monto:** `PENDING → EXECUTED` (automático)
- **Alto monto:** `AWAITING_APPROVAL → APPROVED → EXECUTED`
- **Rechazo:** `AWAITING_APPROVAL → REJECTED`
- **Vencimiento:** `AWAITING_APPROVAL → EXPIRED` (60 minutos sin aprobación)

#### Préstamos
- **Evaluación:** `UNDER_REVIEW → APPROVED | REJECTED`
- **Desembolso:** `APPROVED → DISBURSED`
- **Pago:** `DISBURSED → CANCELLED`
- **Mora:** `DISBURSED → OVERDUE → CANCELLED`

#### Cuentas Bancarias
- Debe tener **exactamente un titular** (natural o empresa, nunca ambos)
- `validateState()` impide saldo negativo y titular múltiple

#### Usuarios
- `UserManager` centraliza `fullName`, `birthDate` y `userStatus`
- `SystemUser` agrega rol bancario y registro de operaciones
- `User` agrega credenciales de acceso (`username`, `password`)

---

## 📝 Notas de Implementación

### Tecnologías Utilizadas
- **Spring Boot 3** · **Java 17**
- **Lombok:** `@Getter`, `@Setter`, `@NoArgsConstructor`
- **Spring Security:** configuración de seguridad base
- **Maven Wrapper:** gestión del ciclo de build

### Patrones Aplicados
- **Arquitectura Hexagonal:** Estricta separación multicapa (Web, Aplicación, Dominio puro e Infraestructura).
- **Herencia limpia:** jerarquía `Person → UserManager → SystemUser/User`
- **Value Objects:** enumeraciones para tipos y estados
- **Validación de negocio:** en métodos de entidad (`BankAccount.validateState()`)

---

**Documento actualizado automáticamente — 12 de marzo de 2026**  
**Sistema de Gestión Bancaria — Wilmer Vega**
Wilmer Vega - Construcción de Software 2
