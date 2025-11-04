# Script PowerShell per avviare il backend Spring Boot

Write-Host "========================================"
Write-Host " AVVIO BACKEND SPRING BOOT" -ForegroundColor Cyan
Write-Host "========================================"
Write-Host ""
Write-Host "Verifica che MySQL sia avviato su localhost:3306" -ForegroundColor Yellow
Write-Host "Database: AgenziaImmobiliare"
Write-Host "User: ITS_2025"
Write-Host ""
Write-Host "Avvio in corso..." -ForegroundColor Green
Write-Host ""

# Vai alla directory dello script
Set-Location $PSScriptRoot

# Prova prima con mvnw.cmd (wrapper Maven)
if (Test-Path ".\mvnw.cmd") {
    Write-Host "Uso mvnw.cmd wrapper..." -ForegroundColor Cyan
    try {
        & cmd /c "mvnw.cmd spring-boot:run"
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "========================================" -ForegroundColor Green
            Write-Host " Backend Spring Boot avviato!" -ForegroundColor Green
            Write-Host " URL: http://localhost:8080" -ForegroundColor Green
            Write-Host "========================================" -ForegroundColor Green
            exit 0
        }
    } catch {
        Write-Host ""
        Write-Host "[ERRORE] mvnw.cmd fallito. Provo con Maven globale..." -ForegroundColor Yellow
        Write-Host ""
    }
}

# Se mvnw fallisce, prova con Maven globale
$mvnCommand = Get-Command mvn -ErrorAction SilentlyContinue
if ($mvnCommand) {
    Write-Host "Uso Maven globale..." -ForegroundColor Cyan
    try {
        & cmd /c "mvn spring-boot:run"
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "========================================" -ForegroundColor Green
            Write-Host " Backend Spring Boot avviato!" -ForegroundColor Green
            Write-Host " URL: http://localhost:8080" -ForegroundColor Green
            Write-Host "========================================" -ForegroundColor Green
            exit 0
        }
    } catch {
        Write-Host "Maven fallito." -ForegroundColor Red
    }
}

# Se tutto fallisce
Write-Host ""
Write-Host "[ERRORE] Impossibile avviare il backend." -ForegroundColor Red
Write-Host ""
Write-Host "Possibili cause:" -ForegroundColor Yellow
Write-Host "1. Maven non installato o mvnw.cmd bloccato"
Write-Host "2. JDK non installato (richiesto JDK 17)"
Write-Host "3. MySQL non avviato o credenziali errate"
Write-Host "4. Porta 8080 gia' in uso"
Write-Host ""
Write-Host "Prova a eseguire questo script come Amministratore:" -ForegroundColor Cyan
Write-Host "  Start-Process powershell -ArgumentList '-File start-backend.ps1' -Verb RunAs"
Write-Host ""
Read-Host "Premi INVIO per chiudere"
exit 1
