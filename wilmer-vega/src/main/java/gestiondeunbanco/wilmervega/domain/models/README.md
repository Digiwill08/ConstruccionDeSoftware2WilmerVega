# Bank Domain Model - DDD Architecture

## Project Description
This is the domain model for a banking system using Domain-Driven Design (DDD).

## Folder Organization

```
application/
├── adapters/
│   ├── api/                 → Controladores REST (@RestController)
│   └── persistence/sql/     → Persistencia
│       ├── entities/        → Entidades JPA (@Entity)
│       └── repositories/    → Repositorios (Spring Data JPA)
├── usecases/                → Casos de uso (Orquestación)
domain/
├── models/         → Clases de negocio puras (Sin JPA)
├── ports/          → Interfaces para guardar y consultar (Abstractas)
├── services/       → Lógica central del negocio (@Service)
```

## Main Entities

### Users and Clients
- **User**: Base class for system users
- **NaturalClient**: Individual bank clients
- **CompanyClient**: Company clients
- **Employee**: Bank employees

### Banking Operations
- **BankAccount**: Savings and checking accounts
- **Loan**: Credits and loans
- **Transfer**: Money transfers between accounts
- **BankProduct**: Product catalog

### Auditing
- **OperationLog**: Record of all operations (for MongoDB)

## Value Objects (Immutable Values)

- **Email**: Validates email format
- **IdentificationNumber**: Validates ID length
- **Phone**: Validates phone number
- **Money**: Amount with currency (USD, COP, EUR)

## Enumerations (Lists of Options)

- **SystemRole**: User types (Client, Employee, Analyst, etc.)
- **UserStatus**: ACTIVE, INACTIVE, BLOCKED
- **AccountStatus**: ACTIVE, BLOCKED, CANCELLED
- **LoanStatus**: UNDER_REVIEW, APPROVED, REJECTED, DISBURSED
- **TransferStatus**: PENDING, APPROVED, EXECUTED, REJECTED
- **AccountType**: SAVINGS, CHECKING, BUSINESS
- **LoanType**: CONSUMER, VEHICLE, MORTGAGE
- **Currency**: USD, COP, EUR
- **ProductCategory**: ACCOUNTS, LOANS, SERVICES
- **OperationType**: Types of operations for audit

## Repositories (Save and Search Data)

- UserRepository
- NaturalClientRepository
- CompanyClientRepository
- EmployeeRepository
- BankAccountRepository
- LoanRepository
- TransferRepository

## What This Model Does

✅ Validates age (must be 18+)  
✅ Validates emails and phones  
✅ Controls loan approvals  
✅ Controls transfer approvals  
✅ Checks account balances  
✅ Logs all operations  

## DDD Concepts Applied

- **Entities**: Classes with ID
- **Value Objects**: Immutable values
- **Repositories**: Save data
- **Business Rules**: Validations in classes

---

**Date**: March 1, 2026  
**Status**: ✅ Ready for delivery
