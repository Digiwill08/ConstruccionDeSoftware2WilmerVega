param(
    [string]$BaseUrl = 'http://127.0.0.1:8083'
)

function Pretty($obj) {
    $obj | ConvertTo-Json -Depth 10
}

Write-Host "Base URL: $BaseUrl"

try {
    Write-Host "1) Login..."
    $loginBody = @{ username = 'analista'; password = 'pass123' } | ConvertTo-Json
    $login = Invoke-RestMethod -Method Post -Uri "$BaseUrl/auth/login" -ContentType 'application/json' -Body $loginBody -ErrorAction Stop
    $token = $login.token
    $tokenType = $login.tokenType
    $headers = @{ Authorization = "$tokenType $token"; 'Content-Type' = 'application/json' }
    Write-Host " -> OK, token received"

    Write-Host "2) Crear company client..."
    $companyBody = @{ businessName = 'Empresa Demo S.A.S.'; documentNumber = '900123456'; email = 'contabilidad@empresa-demo.com'; phone = '3001234567'; address = 'Calle 123 #45-67'; legalRepresentativeId = 1 } | ConvertTo-Json
    $companyResp = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/customers/company" -Headers $headers -Body $companyBody -ErrorAction Stop
    $companyId = $companyResp.id
    Write-Host " -> OK, companyId = $companyId"

    Write-Host "3) Crear cuenta para la empresa..."
    $accountBody = @{ accountNumber = 'ACCEMP001'; accountType = 'BUSINESS'; currency = 'USD'; holderId = $companyId; initialBalance = 25000000 } | ConvertTo-Json
    $accountResp = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/accounts" -Headers $headers -Body $accountBody -ErrorAction Stop
    $accountId = $accountResp.id
    Write-Host " -> OK, accountId = $accountId"

    Write-Host "4) Solicitar préstamo..."
    $loanBody = @{ loanType = 'BUSINESS'; clientApplicant = @{ id = $companyId }; requestedAmount = 50000000; approvedAmount = 45000000; interestRate = 1.8; termInMonths = 36; loanStatus = 'UNDER_REVIEW' } | ConvertTo-Json
    $loanResp = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/employee/loans" -Headers $headers -Body $loanBody -ErrorAction Stop
    $loanId = $loanResp.id
    Write-Host " -> OK, loanId = $loanId, status = $($loanResp.status)"

    Write-Host "5) Aprobar préstamo (analyst)..."
    $approveUri = "$BaseUrl/api/analyst/loans/$loanId/approve?analystUserId=1&role=INTERNAL_ANALYST"
    $approveResp = Invoke-RestMethod -Method Post -Uri $approveUri -Headers $headers -ErrorAction Stop
    Write-Host " -> OK, approved id=$($approveResp.id) status=$($approveResp.status)"

    Write-Host "\nResumen:"
    Write-Host ("CompanyId: " + $companyId)
    Write-Host ("AccountId: " + $accountId)
    Write-Host ("LoanId: " + $loanId)

} catch {
    Write-Host "ERROR: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        try { $body = (New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())).ReadToEnd(); Write-Host "Response body:"; Write-Host $body } catch {}
    }
    exit 1
}
