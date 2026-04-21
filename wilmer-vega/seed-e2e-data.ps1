param(
    [string]$BaseUrl = "http://127.0.0.1:8083",
    [string]$AdminUsername = "pancracio",
    [string]$AdminPassword = "123456"
)

$ErrorActionPreference = "Stop"

function To-JsonBody($obj) {
    return ($obj | ConvertTo-Json -Depth 10)
}

Write-Host "Starting E2E seed on $BaseUrl ..."

$loginBody = To-JsonBody @{
    username = $AdminUsername
    password = $AdminPassword
}

$login = Invoke-RestMethod -Method Post -Uri "$BaseUrl/auth/login" -ContentType "application/json" -Body $loginBody
$token = $login.token
if ([string]::IsNullOrWhiteSpace($token)) {
    throw "No token returned from login."
}

$headers = @{
    Authorization = "Bearer $token"
    "Content-Type" = "application/json"
}

$ts = [DateTimeOffset]::UtcNow.ToUnixTimeSeconds()
$newUsername = "nuevo_user_$ts"
$newPassword = "123456"

$registerBody = To-JsonBody @{
    username = $newUsername
    password = $newPassword
    systemRole = "NATURAL_CLIENT"
}

Invoke-RestMethod -Method Post -Uri "$BaseUrl/auth/register" -ContentType "application/json" -Body $registerBody | Out-Null

$naturalBody = To-JsonBody @{
    fullName = "Demo Client $ts"
    documentNumber = "10$ts"
    email = "client$ts@demo.com"
    phone = "3001234567"
    birthDate = "1995-05-10"
    address = "Demo Street 123"
    role = "NATURAL_CLIENT"
}

$natural = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/customers/natural" -Headers $headers -Body $naturalBody

$companyBody = To-JsonBody @{
    businessName = "Demo Company $ts"
    documentNumber = "90$ts"
    email = "company$ts@demo.com"
    phone = "3019876543"
    address = "Company Ave 456"
    legalRepresentativeId = $natural.id
}

$company = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/customers/company" -Headers $headers -Body $companyBody

$accountBody = To-JsonBody @{
    accountNumber = "ACC$ts"
    accountType = "SAVINGS"
    currency = "USD"
    holderId = $natural.id
    initialBalance = 25000000
}

$account = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/accounts" -Headers $headers -Body $accountBody

$destinationAccountBody = To-JsonBody @{
    accountNumber = "DST$ts"
    accountType = "SAVINGS"
    currency = "USD"
    holderId = $natural.id
    initialBalance = 500
}

$destinationAccount = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/accounts" -Headers $headers -Body $destinationAccountBody

$companyEmployeeUsername = "companyemp$ts"
$supervisorUsername = "supervisor$ts"
$rolePassword = "123456"

$companyEmployeeBody = To-JsonBody @{
    username = $companyEmployeeUsername
    password = $rolePassword
    systemRole = "COMPANY_EMPLOYEE"
}

Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/admin/users" -Headers $headers -Body $companyEmployeeBody | Out-Null

$supervisorBody = To-JsonBody @{
    username = $supervisorUsername
    password = $rolePassword
    systemRole = "COMPANY_SUPERVISOR"
}

Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/admin/users" -Headers $headers -Body $supervisorBody | Out-Null

$companyLoginBody = To-JsonBody @{
    username = $companyEmployeeUsername
    password = $rolePassword
}

$companyLogin = Invoke-RestMethod -Method Post -Uri "$BaseUrl/auth/login" -ContentType "application/json" -Body $companyLoginBody
$companyToken = $companyLogin.token
$companyUserId = $companyLogin.userId

$supervisorLoginBody = To-JsonBody @{
    username = $supervisorUsername
    password = $rolePassword
}

$supervisorLogin = Invoke-RestMethod -Method Post -Uri "$BaseUrl/auth/login" -ContentType "application/json" -Body $supervisorLoginBody
$supervisorToken = $supervisorLogin.token
$supervisorUserId = $supervisorLogin.userId

$companyHeaders = @{
    Authorization = "Bearer $companyToken"
    "Content-Type" = "application/json"
}

$supervisorHeaders = @{
    Authorization = "Bearer $supervisorToken"
}

$transferBody = To-JsonBody @{
    sourceAccount = @{
        id = $account.id
        accountNumber = $account.accountNumber
    }
    destinationAccount = @{
        id = $destinationAccount.id
        accountNumber = $destinationAccount.accountNumber
    }
    amount = 12000000
    creatorUserId = $companyUserId
}

$transfer = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/client/transfers" -Headers $companyHeaders -Body $transferBody

$pendingTransfers = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/supervisor/transfers/pending" -Headers $supervisorHeaders
$pendingTransferId = if ($transfer.transferId) { $transfer.transferId } else { $pendingTransfers[0].transferId }

$approveTransferUri = "$BaseUrl/api/supervisor/transfers/$pendingTransferId/approve?supervisorUserId=$supervisorUserId&role=COMPANY_SUPERVISOR"
$approvedTransfer = Invoke-RestMethod -Method Post -Uri $approveTransferUri -Headers $supervisorHeaders

$loanBody = To-JsonBody @{
    loanType = "CONSUMER"
    requestedAmount = 3000000
    approvedAmount = 2000000
    interestRate = 1.8
    termInMonths = 12
    loanStatus = "UNDER_REVIEW"
}

$loan = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/employee/loans" -Headers $headers -Body $loanBody
$loanId = $loan.loanId

$analystUserId = $login.userId
$approveLoanUri = "$BaseUrl/api/analyst/loans/$loanId/approve?analystUserId=$analystUserId&role=INTERNAL_ANALYST"
$approvedLoan = Invoke-RestMethod -Method Post -Uri $approveLoanUri -Headers @{ Authorization = "Bearer $token" }

$disburseLoanUri = "$BaseUrl/api/analyst/loans/$loanId/disburse?disbursementAccountId=$($account.id)&analystUserId=$analystUserId&role=INTERNAL_ANALYST"
$disbursedLoan = Invoke-RestMethod -Method Post -Uri $disburseLoanUri -Headers @{ Authorization = "Bearer $token" }

Write-Host "Seed completed:"
Write-Host "- New user: $newUsername"
Write-Host "- New user password: $newPassword"
Write-Host "- Natural client id: $($natural.id)"
Write-Host "- Company client id: $($company.id)"
Write-Host "- Account id: $($account.id)"
Write-Host "- Account number: $($account.accountNumber)"
Write-Host "- Destination account id: $($destinationAccount.id)"
Write-Host "- Destination account number: $($destinationAccount.accountNumber)"
Write-Host "- Company employee username: $companyEmployeeUsername"
Write-Host "- Supervisor username: $supervisorUsername"
Write-Host "- Transfer id: $pendingTransferId"
Write-Host "- Transfer status: $($approvedTransfer.transferStatus)"
Write-Host "- Loan id: $loanId"
Write-Host "- Loan status: $($disbursedLoan.loanStatus)"

