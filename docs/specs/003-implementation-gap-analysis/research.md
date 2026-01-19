# Research: UI & User Journey Gap Analysis

## Research Items

### 1. Administrative UI Coverage Audit
- **Context**: Every backend entity must have a corresponding CRUD management interface.
- **Findings**:
  - **Fields**: `FieldConfigController` exists, and basic UI is present.
  - **Rules**: `RuleConfigRepository` exists, but the UI for defining complex DRL logic visually is missing.
  - **Downstream Systems**: `DownstreamSystemRepository` is a skeleton; NO frontend view exists for managing these.
  - **Gap**: Critical. Admins cannot configure integrations or complex rules via UI.

### 2. End-to-End Journey Continuity
- **Context**: Document lifecycle from CRM Sync to Publish.
- **Findings**:
  - **Sync**: Automated via WebHooks.
  - **Verification**: `ContractService` has logic, but UI button for "Confirm AI Data" is missing from `detail.vue`.
  - **Publishing**: `PublishService` exists, but there is no "Publish" button or status tracker in the frontend.
  - **Gap**: High. The journey is fragmented; users can see data but cannot "advance" the contract state.

### 3. Visual Feedback & UX consistency
- **Context**: Consistent error handling and state feedback.
- **Findings**:
  - Rule violations (Drools) return a `remark` string.
  - **Gap**: Frontend lacks a dedicated "Compliance Dashboard" or sidebar to display these violations interactively.
  - **UX**: AI data is displayed with badges, but there's no way to filter the list by "Needs Verification".

### 4. Audit History Readability
- **Context**: Presenting field diffs to non-technical users.
- **Findings**:
  - `AuditLog` now includes a `details` JSONB column.
  - **Gap**: `audit.vue` is a simple list; it does not yet parse the JSON to show side-by-side "Before vs After" comparisons.
