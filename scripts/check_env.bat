@echo off
setlocal enabledelayedexpansion

echo ------------------------------------------
echo   Contract Master Environment Check
echo ------------------------------------------

set EXIT_CODE=0

:: Check Java
echo Checking Java version...
java -version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
        set JAVA_VER=%%g
        set JAVA_VER=!JAVA_VER:"=!
        for /f "delims=. tokens=1" %%v in ("!JAVA_VER!") do set JAVA_MAJOR=%%v
    )
    if !JAVA_MAJOR! GEQ 17 (
        echo  [PASS] Java version !JAVA_VER! detected.
    ) else (
        echo  [FAIL] Java version 17 or higher is required. Found version !JAVA_VER!.
        set EXIT_CODE=1
    )
) else (
    echo  [FAIL] Java is not installed.
    set EXIT_CODE=1
)

:: Check Node
echo Checking Node.js version...
node -v >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    for /f "tokens=1 delims=v." %%v in ('node -v') do set NODE_MAJOR=%%v
    if !NODE_MAJOR! GEQ 18 (
        echo  [PASS] Node.js version !NODE_MAJOR! detected.
    ) else (
        echo  [FAIL] Node.js version 18 or higher is required. Found version !NODE_MAJOR!.
        set EXIT_CODE=1
    )
) else (
    echo  [FAIL] Node.js is not installed.
    set EXIT_CODE=1
)

:: Check Docker
echo Checking Docker...
docker --version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo  [PASS] Docker installed.
    docker compose version >nul 2>&1
    if %ERRORLEVEL% EQU 0 (
        echo  [PASS] Docker Compose detected.
    ) else (
        echo  [WARN] Docker Compose not found.
    )
) else (
    echo  [FAIL] Docker is not installed.
    set EXIT_CODE=1
)

echo ------------------------------------------
if %EXIT_CODE% EQU 0 (
    echo  [PASS] Environment check passed!
) else (
    echo  [FAIL] Environment check failed.
)

exit /b %EXIT_CODE%
