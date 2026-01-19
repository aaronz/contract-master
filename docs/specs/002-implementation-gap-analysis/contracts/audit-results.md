# API Contract Audit

## Controller Alignment Status

| Controller | ApiResponse Wrapper | Tenant Validated | Source Attribution |
|------------|---------------------|------------------|---------------------|
| ContractController | ✅ Yes | ✅ Yes | ⚠ No (missing verify toggle) |
| FieldConfigController | ✅ Yes | ✅ Yes | N/A |
| WebHookController | ⚠ No (returns void) | ✅ Yes | ✅ Yes |
| ExportController | ⚠ No (returns file) | ✅ Yes | N/A |

## Required API Updates
1.  **`POST /api/v1/contracts/{id}/verify`**: New endpoint to transition AI fields to `VERIFIED` state.
2.  **`GET /api/v1/contracts/{id}/history`**: Enhanced endpoint to return field-level diffs from `AuditLog`.
