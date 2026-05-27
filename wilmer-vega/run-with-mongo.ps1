param(
    [ValidateSet("local", "atlas")]
    [string]$Mode = "local",

    [string]$MongoUser,

    [string]$MongoClusterHost,

    [string]$MongoDatabase = "bank_audit_db",
    [int]$MongoLocalPort = 27017,
    [int]$Port = 8083,
    [bool]$MongoRequired = $true
)

if ($Mode -eq "atlas") {
    if ([string]::IsNullOrWhiteSpace($MongoUser) -or [string]::IsNullOrWhiteSpace($MongoClusterHost)) {
        throw "In 'atlas' mode you must provide -MongoUser and -MongoClusterHost."
    }

    $securePassword = Read-Host "Mongo password" -AsSecureString
    $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    try {
        $plainPassword = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
    } finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
    }

    $encodedPassword = [System.Uri]::EscapeDataString($plainPassword)
    $uri = "mongodb+srv://${MongoUser}:$encodedPassword@$MongoClusterHost/$MongoDatabase?retryWrites=true&w=majority"
} else {
    # Local mode (free): requires MongoDB running on your machine or Docker on the specified port.
    $uri = "mongodb://127.0.0.1:$MongoLocalPort/${MongoDatabase}?connectTimeoutMS=5000&socketTimeoutMS=5000&serverSelectionTimeoutMS=5000"
}

$env:MONGODB_URI = $uri
$env:MONGODB_DATABASE = $MongoDatabase
$env:MONGODB_REQUIRED = if ($MongoRequired) { "true" } else { "false" }

Write-Host "Mongo mode: $Mode"
Write-Host "Configured MONGODB_URI: $uri"
Write-Host "MONGODB_DATABASE: $MongoDatabase"
Write-Host "MONGODB_REQUIRED: $env:MONGODB_REQUIRED"
Write-Host "Starting app on port $Port..."

.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=$Port"

