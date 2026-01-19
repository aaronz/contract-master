# Quickstart: Running UI Tests

## Prerequisites

1. **Backend Running**: The Spring Boot application must be running on `http://localhost:8080`.
2. **Frontend Running**: The Vue application must be running on `http://localhost:5173`.
3. **Database**: PostgreSQL and Redis must be accessible.

## Running Tests

Execute the specific E2E test suite via Maven:

```bash
# Run all UI tests
mvn test -Dtest=com.contract.master.e2e.**

# Run specific scenario
mvn test -Dtest=ContractLifecycleTest
```

## Test Reports

The test suite generates two types of reports:

### 1. HTML Summary Report
A high-level overview of all test results (pass/fail rates, execution time).
- **Location**: `backend/target/site/surefire-report.html`
- **Generation**: Automatically generated when running `./run-e2e-tests.sh`.

### 2. Visual Playwright Traces
Detailed visual recordings of every test step.
- **Location**: `backend/target/playwright-traces/`
- **Viewing**:
  ```bash
  cd backend
  mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="show-trace target/playwright-traces/<filename>.zip"
  ```

## Debugging

To run in **headed** mode (visible browser) for debugging:

1. Set environment variable `PLAYWRIGHT_HEADLESS=false`
2. Run the test:
   ```bash
   PLAYWRIGHT_HEADLESS=false mvn test -Dtest=ContractLifecycleTest
   ```

## Test Data

Tests automatically seed their own data. Do not rely on pre-existing database state.
