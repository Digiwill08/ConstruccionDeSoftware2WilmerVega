param(
    [Parameter(Mandatory = $true)]
    [string]$MongoUser,

    [Parameter(Mandatory = $true)]
    [string]$MongoClusterHost,

    [string]$MongoDatabase = "bank_audit_db",
    [int]$Port = 8083,
    [bool]$MongoRequired = $true
)

$securePassword = Read-Host "Mongo password" -AsSecureString
$bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
try {
    $plainPassword = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
} finally {
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
}

$encodedPassword = [System.Uri]::EscapeDataString($plainPassword)
$uri = "mongodb+srv://$MongoUser:$encodedPassword@$MongoClusterHost/$MongoDatabase?retryWrites=true&w=majority"

$env:MONGODB_URI = $uri
$env:MONGODB_DATABASE = $MongoDatabase
$env:MONGODB_REQUIRED = if ($MongoRequired) { "true" } else { "false" }

Write-Host "MONGODB_URI configurado para cluster: $MongoClusterHost"
Write-Host "MONGODB_DATABASE: $MongoDatabase"
Write-Host "MONGODB_REQUIRED: $env:MONGODB_REQUIRED"
Write-Host "Iniciando app en puerto $Port..."

.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=$Port"
