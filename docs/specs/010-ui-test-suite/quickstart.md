# Quickstart: Running UI Tests

## Setup
1. Ensure the backend and frontend are running:
   ```bash
   ./dev.sh
   ```
2. Navigate to the backend directory:
   ```bash
   cd backend
   ```

## Running Tests
Run all UI tests in headless mode:
```bash
mvn test -Dtest=com.contract.master.e2e.**
```

Run a specific user story test:
```bash
mvn test -Dtest=US1SyncTest
```

## Debugging
To run tests in headed mode (visible browser), modify `E2ETestBase.java`:
```java
browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
```
