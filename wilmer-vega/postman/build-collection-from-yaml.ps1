param(
    [string]$YamlRoot = "..\..\postman\collections\Wilmer Vega Bank - E2E",
    [string]$OutputFile = ".\WilmerVegaBank.postman_collection.json"
)

$ErrorActionPreference = "Stop"

function Translate-Label {
    param([string]$Text)

    if ([string]::IsNullOrWhiteSpace($Text)) {
        return $Text
    }

    $translated = $Text
    $map = [ordered]@{
        "Solicitar Restablecimiento de Contraseña" = "Request Password Reset"
        "Restablecer Contraseña con Token" = "Reset Password with Token"
        "Cambiar Contraseña" = "Change Password"
        "Ver Perfil de Usuario Autenticado" = "View Authenticated User Profile"
        "Cerrar Sesión" = "Sign Out"
        "Ver Cuenta por Número" = "View Account by Number"
        "Ver Cuenta Bancaria por Número" = "View Bank Account by Number"
        "Préstamo" = "Loan"
        "Prestamo" = "Loan"
        "Analista" = "Analyst"
        "Bancaria" = "Bank"
        "Autenticacion" = "Authentication"
        "Clientes Naturales" = "Natural Clients"
        "Clientes Empresa" = "Company Clients"
        "Cuentas Bancarias" = "Bank Accounts"
        "Transferencias" = "Transfers"
        "Prestamos" = "Loans"
        "Préstamos" = "Loans"
        "Usuarios y Roles" = "Users and Roles"
        "Contraseña" = "Password"
        "Sesión" = "Session"
        "Crear" = "Create"
        "Listar" = "List"
        "Ver" = "View"
        "Consultar" = "Check"
        "Realizar" = "Make"
        "Aprobar" = "Approve"
        "Rechazar" = "Reject"
        "Desembolsar" = "Disburse"
        "Solicitar" = "Request"
        "Restablecimiento" = "Reset"
        "Cuenta" = "Account"
        "Cuentas" = "Accounts"
        "Cliente" = "Client"
        "Clientes" = "Clients"
        "Empresa" = "Company"
        "Saldo" = "Balance"
        "Auditoria" = "Audit"
        "Prueba" = "Test"
        "Transferencia" = "Transfer"
        "Depósito" = "Deposit"
        "Deposito" = "Deposit"
        "Retiro" = "Withdrawal"
        "Pendientes" = "Pending"
        "Pendiente" = "Pending"
        "Número" = "Number"
        "Numero" = "Number"
        "Usuario" = "User"
        "Usuarios" = "Users"
        "Operación" = "Operation"
        "Operacion" = "Operation"
        "Tipo Operación" = "Operation Type"
        "Tipo Operacion" = "Operation Type"
        "Mis Operaciones" = "My Operations"
        "Por" = "By"
        " por " = " by "
        " de " = " of "
        " y " = " and "
    }

    foreach ($key in $map.Keys) {
        $translated = $translated.Replace($key, $map[$key])
    }

    return $translated
}

function Get-QuotedValue {
    param([string]$Line)

    if ($Line -match ":\s*'(.*)'\s*$") {
        return $Matches[1]
    }
    if ($Line -match ":\s*(.*)\s*$") {
        return $Matches[1].Trim()
    }
    return ""
}

function Parse-RequestYaml {
    param([string]$Path)

    $lines = Get-Content -Path $Path -Encoding UTF8

    $name = ""
    $method = "GET"
    $url = ""
    $order = 999999
    $headers = @()
    $query = @()
    $bodyRaw = $null

    for ($i = 0; $i -lt $lines.Count; $i++) {
        $line = $lines[$i]
        if ($line -match '^name:\s*') {
            $name = Translate-Label (Get-QuotedValue -Line $line)
            continue
        }
        if ($line -match '^method:\s*') {
            $method = (Get-QuotedValue -Line $line).ToUpperInvariant()
            continue
        }
        if ($line -match '^url:\s*') {
            $url = Get-QuotedValue -Line $line
            continue
        }
        if ($line -match '^order:\s*(\d+)') {
            $order = [int]$Matches[1]
            continue
        }

        if ($line -match '^headers:\s*$') {
            $j = $i + 1
            while ($j -lt $lines.Count -and $lines[$j] -match '^  - key:\s*') {
                $key = Get-QuotedValue -Line $lines[$j]
                $value = ""
                if ($j + 1 -lt $lines.Count -and $lines[$j + 1] -match '^    value:\s*') {
                    $value = Get-QuotedValue -Line $lines[$j + 1]
                    $j += 2
                } else {
                    $j += 1
                }
                $headers += [ordered]@{ key = $key; value = $value }
            }
            $i = $j - 1
            continue
        }

        if ($line -match '^queryParams:\s*$') {
            $j = $i + 1
            while ($j -lt $lines.Count -and $lines[$j] -match '^  - key:\s*') {
                $key = Get-QuotedValue -Line $lines[$j]
                $value = ""
                if ($j + 1 -lt $lines.Count -and $lines[$j + 1] -match '^    value:\s*') {
                    $value = Get-QuotedValue -Line $lines[$j + 1]
                    $j += 2
                } else {
                    $j += 1
                }
                $query += [ordered]@{ key = $key; value = $value }
            }
            $i = $j - 1
            continue
        }

        if ($line -match '^  content:\s*\|-\s*$') {
            $j = $i + 1
            $bodyLines = @()
            while ($j -lt $lines.Count) {
                $bline = $lines[$j]
                if ($bline -match '^    ') {
                    $bodyLines += $bline.Substring(4)
                    $j++
                    continue
                }
                break
            }
            $bodyRaw = ($bodyLines -join "`n")
            $i = $j - 1
            continue
        }
    }

    $request = [ordered]@{
        method = $method
        header = $headers
        url = $url
    }

    if ($null -ne $bodyRaw -and $bodyRaw.Trim().Length -gt 0) {
        $request.body = [ordered]@{
            mode = "raw"
            raw = $bodyRaw
        }
    }

    if ($query.Count -gt 0) {
        if ($url -match '\?') {
            $request.url = $url
        } else {
            $qs = ($query | ForEach-Object { "{0}={1}" -f $_.key, $_.value }) -join "&"
            $request.url = "$($url)?$qs"
        }
    }

    return [ordered]@{
        name = $name
        order = $order
        item = [ordered]@{
            name = $name
            request = $request
        }
    }
}

$yamlRootAbs = Resolve-Path $YamlRoot
$files = Get-ChildItem -Path $yamlRootAbs -Recurse -Filter '*.request.yaml' | Sort-Object FullName

if ($files.Count -eq 0) {
    throw "No .request.yaml files were found in $YamlRoot"
}

$grouped = @{}
foreach ($f in $files) {
    $relative = $f.FullName.Substring($yamlRootAbs.Path.Length).TrimStart('\\')
    $parts = $relative.Split('\\')
    $folder = if ($parts.Length -gt 1) { Translate-Label $parts[0] } else { "General" }

    $parsed = Parse-RequestYaml -Path $f.FullName

    if (-not $grouped.ContainsKey($folder)) {
        $grouped[$folder] = @()
    }
    $grouped[$folder] += $parsed
}

$folderItems = @()
$folderNames = $grouped.Keys | Sort-Object
foreach ($folderName in $folderNames) {
    $requests = $grouped[$folderName] | Sort-Object order, name | ForEach-Object { $_.item }
    $folderItems += [ordered]@{
        name = $folderName
        item = $requests
    }
}

$collection = [ordered]@{
    info = [ordered]@{
        name = "Wilmer Vega Bank - E2E"
        _postman_id = "8b95bd7f-5d86-4c03-9de2-95f278b2f8c1"
        description = "Full collection generated from request.yaml files"
        schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
    }
    item = $folderItems
}

$json = $collection | ConvertTo-Json -Depth 30
Set-Content -Path $OutputFile -Value $json -Encoding UTF8

Write-Host "Collection generated: $OutputFile"
Write-Host "Total folders:" $folderItems.Count
Write-Host "Total requests:" $files.Count
