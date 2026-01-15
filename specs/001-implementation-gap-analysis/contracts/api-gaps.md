# API Contract Gaps

## Identified Deviations from OpenAPI Spec

1. **Webhook Security**:
   - Spec requires signature verification for all sources.
   - **Current Code**: `WebHookSignatureFilter` has a hardcoded HubSpot check and returns `true` for all others.

2. **Response Consistency**:
   - `openapi.yaml` defines a standard `ApiResponse` wrapper.
   - **Current Code**: `FieldConfigController` and some others return raw lists/entities instead of the wrapper.

3. **Status Codes**:
   - Extraction trigger should return `202 Accepted`.
   - **Current Code**: Returns `200 OK` (synchronous simulation).
