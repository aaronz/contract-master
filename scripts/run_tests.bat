@echo off
set SCRIPT_DIR=%~dp0
set ROOT_DIR=%SCRIPT_DIR%..

echo Starting all tests for Contract Master...

if not exist "%ROOT_DIR%\backend" (
    echo Error: 'backend' directory not found at %ROOT_DIR%\backend.
    exit /b 1
)

echo.
echo Step 1: Running Backend Unit and Integration Tests...
cd /d "%ROOT_DIR%\backend"
call mvn test
if %ERRORLEVEL% neq 0 (
    echo Backend tests failed!
    exit /b %ERRORLEVEL%
)

echo.
echo All tests passed successfully!
