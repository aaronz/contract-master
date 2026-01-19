@echo off
set SCRIPT_DIR=%~dp0
set ROOT_DIR=%SCRIPT_DIR%..
set BACKEND_DIR=%ROOT_DIR%\backend
set FRONTEND_DIR=%ROOT_DIR%\frontend

echo Starting Contract Master Development Environment...

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
start "Frontend" npm run dev

echo Setup initiated!
echo Backend: http://localhost:8080
echo Frontend: http://localhost:5173
echo Close the separate windows to stop.
