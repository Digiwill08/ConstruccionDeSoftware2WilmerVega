# Domain Model

This package contains the pure banking domain used by the application. The code here is intentionally free of Spring annotations where possible so the business rules remain testable and isolated.

## Folder Organization

```
domain/
├── models/   -> Business entities and enums
├── ports/    -> Output contracts for persistence and integrations
├── services/ -> Domain rules and operations
└── exceptions/ -> Domain-specific errors
```

Application code lives outside the domain in `application/` and only orchestrates use cases or exposes REST endpoints.

## Main Entities

### Users and clients

- `User`: system credentials and role.
- `SystemUser`: operational user that creates or approves financial operations.
- `NaturalClient`: individual client.
- `CompanyClient`: corporate client.
- `Person` and `UserManager`: shared base abstractions.

### Banking operations

- `BankAccount`: account state, balance and type.
- `Loan`: requested and approved financing.
- `Transfer`: movement of money between accounts.
- `BankingProduct`: product catalog.

### Auditing

- `AuditLog`: immutable trace of business actions stored in MongoDB.

## Enumerations

- `SystemRole`: includes `ADMINISTRATOR`, clients, employees, supervisor and analyst roles.
- `UserStatus`: `ACTIVE`, `INACTIVE`, `BLOCKED`.
- `AccountStatus`: `ACTIVE`, `BLOCKED`, `CANCELLED`.
- `AccountType`: `SAVINGS`, `CHECKING`, `PERSONAL`, `BUSINESS`.
- `Currency`: `USD`, `COP`, `EUR`.
- `LoanStatus`: `UNDER_REVIEW`, `APPROVED`, `REJECTED`, `DISBURSED`, `OVERDUE`, `CANCELLED`.
- `LoanType`: `CONSUMER`, `VEHICLE`, `MORTGAGE`, `BUSINESS`.
- `TransferStatus`: `PENDING`, `AWAITING_APPROVAL`, `APPROVED`, `EXECUTED`, `REJECTED`, `EXPIRED`.
- `ProductCategory`: product families for the bank.
- `OperationType`: audit events such as login, loan approval and transfer execution.

## What this model supports

- Business validation in pure Java.
- Role-based banking workflows.
- Approval and rejection flows for loans and transfers.
- Traceability through audit events.
- Reusable contracts through ports.

## Notes

- The domain is designed to be testable without starting the web layer.
- Persistence details are handled by adapters in the application layer.
