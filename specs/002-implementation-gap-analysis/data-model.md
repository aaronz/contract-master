# Data Model: Audit Scenarios & State Gaps

## Test Data Scenarios

### 1. Isolation Breach Scenario
- **Test Case**: Tenant A attempts to fetch Tenant B's contract by ID.
- **Goal**: Verify `ContractBaseRepository` returns 404 or empty Optional even when bypass headers are attempted.

### 2. AI Synergy Lifecycle
- **Test Case**: Upload PDF -> AI Suggests "2024-12-31" -> Human corrects to "2025-01-01".
- **Goal**: Verify `fill_type` transition from `AI` to `MANUAL` and presence of audit log JSON with both values.

## Schema Gaps Identified
- **ContractBase**: Add `validation_results` (JSONB) to store structured Drools outcomes.
- **AuditLog**: Add `details` (JSONB) to store field diffs (e.g., `{ "amount": {"old": 500, "new": 600} }`).
- **ContractBase**: Add `group_id` (String) for corporate hierarchy filtering logic.
