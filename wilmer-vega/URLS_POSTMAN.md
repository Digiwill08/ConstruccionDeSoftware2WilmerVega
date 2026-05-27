# Lista de URLs Organizada

## System
- GET http://localhost:8083/
- GET http://localhost:8083/ping

## Auth
- GET http://localhost:8083/auth/profile
- POST http://localhost:8083/auth/login
- POST http://localhost:8083/auth/register
- POST http://localhost:8083/auth/logout
- GET http://localhost:8083/api/auth/profile
- POST http://localhost:8083/api/auth/login
- POST http://localhost:8083/api/auth/register
- POST http://localhost:8083/api/auth/logout

## Cliente
- GET http://localhost:8083/api/client/bank-accounts/{accountNumber}
- GET http://localhost:8083/api/client/transfers
- GET http://localhost:8083/api/client/transfers/{id}
- POST http://localhost:8083/api/client/transfers

## Admin
- GET http://localhost:8083/api/admin/users
- GET http://localhost:8083/api/admin/users/{id}
- GET http://localhost:8083/api/admin/users/username/{username}
- GET http://localhost:8083/api/admin/audit-logs
- GET http://localhost:8083/api/admin/audit-logs/user/{userId}
- POST http://localhost:8083/api/admin/users

## Audit Logs
- GET http://localhost:8083/api/audit-logs
- GET http://localhost:8083/api/audit-logs/my-operations?username={username}
- GET http://localhost:8083/api/audit-logs/product/{productId}
- GET http://localhost:8083/api/audit-logs/operation-type/{operationType}
- GET http://localhost:8083/api/audit-logs/user/{userId}
- GET http://localhost:8083/api/audit-logs/date-range?startDate=2026-01-01&endDate=2026-12-31

## Analyst
- GET http://localhost:8083/api/analyst/loans
- GET http://localhost:8083/api/analyst/loans/{id}
- GET http://localhost:8083/api/analyst/audit-logs
- GET http://localhost:8083/api/analyst/audit-logs/product/{productId}
- GET http://localhost:8083/api/analyst/audit-logs/user/{userId}
- POST http://localhost:8083/api/analyst/loans/{id}/approve?analystUserId={analystUserId}&role=INTERNAL_ANALYST
- POST http://localhost:8083/api/analyst/loans/{id}/reject?analystUserId={analystUserId}&role=INTERNAL_ANALYST
- POST http://localhost:8083/api/analyst/loans/{id}/disburse?disbursementAccountId={accountId}&analystUserId={analystUserId}&role=INTERNAL_ANALYST

## Supervisor
- GET http://localhost:8083/api/supervisor/transfers/pending
- GET http://localhost:8083/api/supervisor/transfers/{id}
- POST http://localhost:8083/api/supervisor/transfers/{id}/approve?supervisorUserId={supervisorUserId}&role=COMPANY_SUPERVISOR
- POST http://localhost:8083/api/supervisor/transfers/{id}/reject?supervisorUserId={supervisorUserId}&role=COMPANY_SUPERVISOR

## Customers
- GET http://localhost:8083/api/customers/natural
- GET http://localhost:8083/api/customers/natural/{id}
- GET http://localhost:8083/api/customers/natural/document/{documentNumber}
- GET http://localhost:8083/api/customers/company
- GET http://localhost:8083/api/customers/company/{id}
- GET http://localhost:8083/api/customers/company/document/{documentNumber}

## Employee
- GET http://localhost:8083/api/employee/bank-accounts
- GET http://localhost:8083/api/employee/natural-clients
- GET http://localhost:8083/api/employee/company-clients
- GET http://localhost:8083/api/employee/loans

## Accounts
- GET http://localhost:8083/api/accounts
- GET http://localhost:8083/api/accounts/{accountNumber}
- GET http://localhost:8083/api/accounts/{accountId}/balance
- PUT http://localhost:8083/api/accounts/{accountId}/status?newStatus={status}
