@echo off
set SCRIPT_DIR=%~dp0
set ROOT_DIR=%SCRIPT_DIR%..
set BACKEND_DIR=%ROOT_DIR%\backend
set FRONTEND_DIR=%ROOT_DIR%\frontend

echo Starting Contract Master Development Environment...

call "%SCRIPT_DIR%check_env.bat"
if %ERRORLEVEL% NEQ 0 (
    echo Environment check failed. Aborting.
    pause
    exit /b %ERRORLEVEL%
)

echo Resetting Docker environment...
cd /d "%ROOT_DIR%"
docker-compose down -v --remove-orphans
docker-compose up -d

echo Starting Backend (Spring Boot)...
cd /d "%BACKEND_DIR%"
start "Backend" mvn spring-boot:run

echo Starting Frontend (Vue 3/Vite)...
cd /d "%FRONTEND_DIR%"
if not exist node_modules (
    echo node_modules not found, installing...
    call npm install
)
start "Frontend" npm run dev -- --host

echo Setup initiated!
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr "IPv4"') do (
    set ip=%%a
    goto :show
)
:show
set ip=%ip: =%
echo Backend: http://%ip%:8080
echo Frontend: http://%ip%:5173
echo Close the separate windows to stop.
