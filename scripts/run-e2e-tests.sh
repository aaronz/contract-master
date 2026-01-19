#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

export PLAYWRIGHT_HEADLESS=true
cd "$ROOT_DIR/backend"
mvn test -Dtest=com.contract.master.e2e.** surefire-report:report-only

echo ""
echo "===================================================="
echo "TEST EXECUTION COMPLETE"
echo "===================================================="
echo "1. HTML Report:  backend/target/site/surefire-report.html"
echo "2. Visual Traces: backend/target/playwright-traces/"
echo ""
echo "To view traces: mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args=\"show-trace target/playwright-traces/<filename>.zip\""
echo "===================================================="
