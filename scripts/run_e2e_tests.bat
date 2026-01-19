@echo off
set SCRIPT_DIR=%~dp0
set ROOT_DIR=%SCRIPT_DIR%..

set PLAYWRIGHT_HEADLESS=true
cd /d "%ROOT_DIR%\backend"
call mvn test -Dtest=com.contract.master.e2e.** surefire-report:report-only

echo.
echo ====================================================
echo TEST EXECUTION COMPLETE
echo ====================================================
echo 1. HTML Report:  backend\target\site\surefire-report.html
echo 2. Visual Traces: backend\target\playwright-traces\
echo.
echo To view traces: mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="show-trace target\playwright-traces\<filename>.zip"
echo ====================================================
