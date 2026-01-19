# Research: UI Test Suite

## Research Items

### 1. Page Object Model (POM) in Java Playwright
- **Context**: Need a maintainable structure for UI tests.
- **Task**: Identify the standard pattern for implementing POM with the Java Playwright bindings.

### 2. Mocking AI Responses vs Full Extraction
- **Context**: AI extraction can be slow and non-deterministic.
- **Task**: Research how to inject mock data into the Kafka queue or API during E2E tests to verify UI reactivity without waiting for actual Azure AI processing.

### 3. Multi-tenant Session Isolation
- **Context**: Must verify that User A cannot see User B's data.
- **Task**: Research Playwright's `BrowserContext` for switching between different tenant credentials in a single test run.

## Findings

### 1. POM Pattern
- **Decision**: Use a **Functional Page Object** pattern.
- **Rationale**: Pages will be represented by classes in `com.contract.master.e2e.pages`. Actions (like `login()`) will return the next page object (like `DashboardPage`) to enable fluent test chains.
- **Alternatives considered**: Static selector constants (harder to maintain), Cucumber (added complexity not needed for small team).

### 2. Data Injection Strategy
- **Decision**: **API-driven state setup**.
- **Rationale**: Tests will use the backend REST APIs to inject specific contract states (including those with "unverified AI data") before the browser opens. This ensures tests are deterministic and fast.
- **Alternatives considered**: DB direct injection (skips business logic), Full journey (too slow).

### 3. Multi-tenant Isolation
- **Decision**: **Context-per-Tenant**.
- **Rationale**: Use `browser.newContext()` for each tenant role. This ensures localStorage, cookies, and indexedDB are completely isolated, accurately mimicking separate browser instances.
