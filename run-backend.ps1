# Start Spring Boot with variables from .env in this directory (do not hardcode secrets here).

$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

$envFile = Join-Path $PSScriptRoot ".env"
if (-not (Test-Path $envFile)) {
    Write-Host "Missing .env - copy env.example to .env and fill values." -ForegroundColor Red
    exit 1
}

Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()
    if ($line -eq "" -or $line.StartsWith("#")) { return }
    $idx = $line.IndexOf("=")
    if ($idx -lt 1) { return }
    $key = $line.Substring(0, $idx).Trim()
    $val = $line.Substring($idx + 1).Trim()
    Set-Item -Path "Env:$key" -Value $val
}

Write-Host "Loaded environment from .env" -ForegroundColor Green
Write-Host "Starting Spring Boot (Maven)..." -ForegroundColor Yellow
& .\mvnw.cmd spring-boot:run
