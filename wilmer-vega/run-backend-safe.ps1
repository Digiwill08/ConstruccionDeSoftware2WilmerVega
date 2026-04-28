param(
    [ValidateSet("start", "status", "stop")]
    [string]$Action = "status",

    [int]$Port = 8083,
    [int]$StartupTimeoutSec = 60
)

$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$runDir = Join-Path $scriptDir ".run"
$logFile = Join-Path $runDir "spring-boot.log"
$errFile = Join-Path $runDir "spring-boot.err.log"
$pidFile = Join-Path $runDir "backend.pid"

function Get-ListeningProcessId {
    param([int]$TargetPort)

    $conn = Get-NetTCPConnection -LocalPort $TargetPort -State Listen -ErrorAction SilentlyContinue |
        Select-Object -First 1

    if ($null -ne $conn) {
        return [int]$conn.OwningProcess
    }

    return $null
}

function Test-HttpUp {
    param([int]$TargetPort)

    try {
        $resp = Invoke-WebRequest -Uri "http://127.0.0.1:$TargetPort/" -UseBasicParsing -TimeoutSec 3
        return $resp.StatusCode -ge 200 -and $resp.StatusCode -lt 500
    } catch {
        return $false
    }
}

if (-not (Test-Path $runDir)) {
    New-Item -ItemType Directory -Path $runDir | Out-Null
}

switch ($Action) {
    "start" {
        $existingPid = Get-ListeningProcessId -TargetPort $Port
        if ($null -ne $existingPid) {
            Write-Host "Backend is already running on port $Port con PID $existingPid."
            Set-Content -Path $pidFile -Value $existingPid -Encoding ascii
            exit 0
        }

        if (Test-Path $logFile) {
            Remove-Item $logFile -Force
        }
        if (Test-Path $errFile) {
            Remove-Item $errFile -Force
        }

        $args = @(
            "-NoProfile",
            "-ExecutionPolicy", "Bypass",
            "-Command",
            "& '$scriptDir\\mvnw.cmd' spring-boot:run '-Dspring-boot.run.arguments=--server.port=$Port'"
        )

        $proc = Start-Process -FilePath "powershell.exe" -ArgumentList $args -WorkingDirectory $scriptDir -RedirectStandardOutput $logFile -RedirectStandardError $errFile -PassThru
        Write-Host "Process started (wrapper PID: $($proc.Id)). Waiting for backend response..."

        $deadline = (Get-Date).AddSeconds($StartupTimeoutSec)
        $backendPid = $null

        while ((Get-Date) -lt $deadline) {
            Start-Sleep -Seconds 1
            $backendPid = Get-ListeningProcessId -TargetPort $Port
            if ($null -ne $backendPid -and (Test-HttpUp -TargetPort $Port)) {
                Set-Content -Path $pidFile -Value $backendPid -Encoding ascii
                Write-Host "Backend is up at http://127.0.0.1:$Port (PID $backendPid)."
                Write-Host "Log: $logFile"
                if (Test-Path $errFile) {
                    $errLength = (Get-Item $errFile).Length
                    if ($errLength -gt 0) {
                        Write-Host "Errors: $errFile"
                    }
                }
                exit 0
            }
        }

        Write-Host "No backend available detected within $StartupTimeoutSec seconds."
        Write-Host "Check log: $logFile"
        if (Test-Path $errFile) {
            Write-Host "Check errors: $errFile"
        }
        exit 1
    }

    "status" {
        $runningPid = Get-ListeningProcessId -TargetPort $Port
        if ($null -eq $runningPid) {
            Write-Host "Backend stopped on port $Port."
            exit 1
        }

        if (Test-HttpUp -TargetPort $Port) {
            Write-Host "Backend activo en http://127.0.0.1:$Port (PID $runningPid)."
            exit 0
        }

        Write-Host "A process is listening on port $Port (PID $runningPid), but it does not respond to HTTP correctly."
        exit 1
    }

    "stop" {
        $runningPid = Get-ListeningProcessId -TargetPort $Port
        if ($null -eq $runningPid) {
            Write-Host "No backend is listening on port $Port."
            if (Test-Path $pidFile) {
                Remove-Item $pidFile -Force
            }
            exit 0
        }

        Stop-Process -Id $runningPid -Force
        Start-Sleep -Seconds 1

        $afterStopPid = Get-ListeningProcessId -TargetPort $Port
        if ($null -eq $afterStopPid) {
            Write-Host "Backend stopped successfully (PID $runningPid)."
            if (Test-Path $pidFile) {
                Remove-Item $pidFile -Force
            }
            exit 0
        }

        Write-Host "Could not stop process on port $Port (PID actual: $afterStopPid)."
        exit 1
    }
}

