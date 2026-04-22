# Lista de URLs

http://localhost:8083/
http://localhost:8083/ping

# Auth
http://localhost:8083/auth/login
http://localhost:8083/auth/register
http://localhost:8083/auth/profile
http://localhost:8083/auth/logout
http://localhost:8083/api/auth/login
http://localhost:8083/api/auth/register
http://localhost:8083/api/auth/profile
http://localhost:8083/api/auth/logout

# Cliente
http://localhost:8083/api/client/bank-accounts/{accountNumber}
http://localhost:8083/api/client/transfers
http://localhost:8083/api/client/transfers/{id}

# Admin
http://localhost:8083/api/admin/users
http://localhost:8083/api/admin/users/{id}
http://localhost:8083/api/admin/users/username/{username}
http://localhost:8083/api/admin/audit-logs
http://localhost:8083/api/admin/audit-logs/user/{userId}

# Audit logs
http://localhost:8083/api/audit-logs
http://localhost:8083/api/audit-logs/my-operations?username={username}
http://localhost:8083/api/audit-logs/product/{productId}
http://localhost:8083/api/audit-logs/operation-type/{operationType}
http://localhost:8083/api/audit-logs/user/{userId}
http://localhost:8083/api/audit-logs/date-range?startDate=2026-01-01&endDate=2026-12-31

# Analyst
http://localhost:8083/api/analyst/loans
http://localhost:8083/api/analyst/loans/{id}
http://localhost:8083/api/analyst/loans/{id}/approve?analystUserId={analystUserId}&role=INTERNAL_ANALYST
http://localhost:8083/api/analyst/loans/{id}/reject?analystUserId={analystUserId}&role=INTERNAL_ANALYST
http://localhost:8083/api/analyst/loans/{id}/disburse?disbursementAccountId={accountId}&analystUserId={analystUserId}&role=INTERNAL_ANALYST
http://localhost:8083/api/analyst/audit-logs
http://localhost:8083/api/analyst/audit-logs/product/{productId}
http://localhost:8083/api/analyst/audit-logs/user/{userId}

# Supervisor
http://localhost:8083/api/supervisor/transfers/pending
http://localhost:8083/api/supervisor/transfers/{id}
http://localhost:8083/api/supervisor/transfers/{id}/approve?supervisorUserId={supervisorUserId}&role=COMPANY_SUPERVISOR
http://localhost:8083/api/supervisor/transfers/{id}/reject?supervisorUserId={supervisorUserId}&role=COMPANY_SUPERVISOR

# Customers
http://localhost:8083/api/customers/natural
http://localhost:8083/api/customers/natural/{id}
http://localhost:8083/api/customers/natural/document/{documentNumber}
http://localhost:8083/api/customers/company
http://localhost:8083/api/customers/company/{id}
http://localhost:8083/api/customers/company/document/{documentNumber}

# Employee
http://localhost:8083/api/employee/bank-accounts
http://localhost:8083/api/employee/natural-clients
http://localhost:8083/api/employee/company-clients
http://localhost:8083/api/employee/loans

# Accounts
http://localhost:8083/api/accounts
http://localhost:8083/api/accounts/{accountNumber}
http://localhost:8083/api/accounts/{accountId}/balance
http://localhost:8083/api/accounts/{accountId}/status?newStatus={status}
