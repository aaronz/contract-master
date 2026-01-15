# Data Model: UI Test Suite (Test Data Entities)

## Test Entities

### 1. TestUser
- **Description**: Mock credentials used for automated login.
- **Fields**:
  - `username` (String)
  - `password` (String)
  - `tenantId` (String)
  - `role` (Enum: ADMIN, MANAGER, USER)

### 2. TestContractScenario
- **Description**: Pre-defined contract states for verification.
- **Scenarios**:
  - `SCENARIO_NEW`: Empty contract with no AI data.
  - `SCENARIO_AI_PENDING`: Contract with unverified AI-extracted fields.
  - `SCENARIO_VIOLATION`: Contract that triggers a Drools rule error.
  - `SCENARIO_AUDITED`: Contract with a rich modification history.

## Relationships
- One **TestUser** belongs to one **Tenant**.
- One **TestContractScenario** is injected per user journey test.
