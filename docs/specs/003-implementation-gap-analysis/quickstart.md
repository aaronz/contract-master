# Gap Analysis Quickstart (UI & User Journey)

## UI Walkthrough Audit

### 1. Verification Journey Probe
1. Sync a contract via CRM Mock.
2. Observe if "AI Suggestion" badges appear in `detail.vue`.
3. Check for a "Verify All" or field-level "Confirm" button.
4. **Target**: Button MUST exist and transition status to `VERIFIED`.

### 2. Management Coverage Check
1. Go to `/settings`.
2. Attempt to add a new "Downstream WebHook".
3. **Target**: UI MUST provide fields for target URL and secret key.

### 3. Audit Transparency Test
1. Edit a contract field.
2. Go to "Audit History".
3. **Target**: UI MUST show a table with "Field", "Old Value", and "New Value" parsed from JSON.
