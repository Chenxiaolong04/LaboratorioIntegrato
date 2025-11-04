@echo off
echo ========================================
echo  AVVIO BACKEND SPRING BOOT
echo ========================================
echo.
echo Verifica che MySQL sia avviato su localhost:3306
echo Database: AgenziaImmobiliare
echo User: ITS_2025
echo.
echo Avvio in corso...
echo.

cd /d "%~dp0"

REM Prova prima con mvnw.cmd (wrapper Maven)
if exist mvnw.cmd (
    echo Uso mvnw.cmd wrapper...
    call mvnw.cmd spring-boot:run
    if %ERRORLEVEL% EQU 0 goto :success
    echo.
    echo [ERRORE] mvnw.cmd fallito. Provo con Maven globale...
    echo.
)

REM Se mvnw fallisce, prova con Maven globale
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Uso Maven globale...
    call mvn spring-boot:run
    if %ERRORLEVEL% EQU 0 goto :success
)

REM Se anche Maven fallisce
echo.
echo [ERRORE] Impossibile avviare il backend.
echo.
echo Possibili cause:
echo 1. Maven non installato o mvnw.cmd bloccato
echo 2. JDK non installato (richiesto JDK 17)
echo 3. MySQL non avviato o credenziali errate
echo 4. Porta 8080 gia' in uso
echo.
echo Prova a eseguire questo script come Amministratore.
echo.
pause
exit /b 1

:success
echo.
echo ========================================
echo  Backend Spring Boot avviato!
echo  URL: http://localhost:8080
echo ========================================
pause
