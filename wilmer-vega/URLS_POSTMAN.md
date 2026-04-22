# API de Wilmer Vega Bank

Base URL: http://localhost:8083

## Públicos

- GET /
- GET /ping

## Auth

El controlador responde en dos prefijos equivalentes:

- POST /auth/login
- POST /auth/register
- GET /auth/profile
- POST /auth/logout
- POST /api/auth/login
- POST /api/auth/register
- GET /api/auth/profile
- POST /api/auth/logout

## Cliente

- GET /api/client/bank-accounts/{accountNumber}
- GET /api/client/transfers
- GET /api/client/transfers/{id}
- POST /api/client/transfers

## Admin

- GET /api/admin/users
- GET /api/admin/users/{id}
- GET /api/admin/users/username/{username}
- POST /api/admin/users
- DELETE /api/admin/users/{id}
- GET /api/admin/audit-logs
- GET /api/admin/audit-logs/user/{userId}

## Audit logs

- GET /api/audit-logs
- GET /api/audit-logs/my-operations?username={username}
- GET /api/audit-logs/product/{productId}
- GET /api/audit-logs/operation-type/{operationType}
- GET /api/audit-logs/user/{userId}
- GET /api/audit-logs/date-range?startDate=2026-01-01&endDate=2026-12-31

## Analyst

- GET /api/analyst/loans
- GET /api/analyst/loans/{id}
- POST /api/analyst/loans/{id}/approve?analystUserId={analystUserId}&role=INTERNAL_ANALYST
- POST /api/analyst/loans/{id}/reject?analystUserId={analystUserId}&role=INTERNAL_ANALYST
- POST /api/analyst/loans/{id}/disburse?disbursementAccountId={accountId}&analystUserId={analystUserId}&role=INTERNAL_ANALYST
- GET /api/analyst/audit-logs
- GET /api/analyst/audit-logs/product/{productId}
- GET /api/analyst/audit-logs/user/{userId}

## Supervisor

- GET /api/supervisor/transfers/pending
- GET /api/supervisor/transfers/{id}
- POST /api/supervisor/transfers/{id}/approve?supervisorUserId={supervisorUserId}&role=COMPANY_SUPERVISOR
- POST /api/supervisor/transfers/{id}/reject?supervisorUserId={supervisorUserId}&role=COMPANY_SUPERVISOR

## Customers

- GET /api/customers/natural
- GET /api/customers/natural/{id}
- GET /api/customers/natural/document/{documentNumber}
- POST /api/customers/natural
- PUT /api/customers/natural/{id}
- DELETE /api/customers/natural/{id}
- GET /api/customers/company
- GET /api/customers/company/{id}
- GET /api/customers/company/document/{documentNumber}
- POST /api/customers/company
- PUT /api/customers/company/{id}
- DELETE /api/customers/company/{id}

## Employee

- GET /api/employee/bank-accounts
- POST /api/employee/bank-accounts
- GET /api/employee/natural-clients
- POST /api/employee/natural-clients
- GET /api/employee/company-clients
- POST /api/employee/company-clients
- GET /api/employee/loans
- POST /api/employee/loans

## Accounts

- POST /api/accounts
- GET /api/accounts
- GET /api/accounts/{accountNumber}
- GET /api/accounts/{accountId}/balance
- PUT /api/accounts/{accountId}/status?newStatus={status}

## Notas

- Los endpoints de auth aceptan tanto /auth como /api/auth porque el controlador está expuesto con ambos prefijos.
- Las rutas con llaves como {id} o {username} deben reemplazarse con valores reales al probar en Postman.
- La fecha de ejemplo en /api/audit-logs/date-range puede ajustarse al rango que se quiera consultar.
