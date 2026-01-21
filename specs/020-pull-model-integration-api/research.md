# Research: Pull-Model Integration API

## 1. Authentication & System Context
- **Decision**: Implement a custom `IntegrationApiKeyInterceptor` that maps `X-API-KEY` to a `DownstreamSystem` entity.
- **Rationale**: Using a dedicated interceptor ensures that every request to the pull API is pre-validated and that the correct `tenant_id` and `targetSystemId` are available in the `TenantContext`.
- **Alternatives Considered**: 
    - Spring Security Filter: Rejected as too heavyweight for a simple API Key check.
    - Manual check in Controller: Rejected as violating DRY (Don't Repeat Yourself) principles.

## 2. Dynamic Schema Transformation
- **Decision**: Reuse the `IntegrationPushService.transformData` logic and the sandboxed `ScriptSandbox`.
- **Rationale**: Since the Pull-Model is simply an alternative delivery method for the same data, reusing the same transformation engine ensures consistency between push and pull integrations.
- **Note**: The API will only return fields defined in the **OUTBOUND** mappings for the requesting system.

## 3. Data Masking & Security
- **Decision**: Any internal field not mapped in the `FieldMapping` for the requesting system will be automatically nullified/removed from the response JSON.
- **Rationale**: This provides built-in data masking, ensuring downstream systems only pull the data they are explicitly configured to receive.

## 4. Efficiency: Delta Sync
- **Decision**: Support the `updatedSince` query parameter using JPA's `updateTime`.
- **Rationale**: Standardizing on `updateTime` allows for simple, performant delta queries without complex state tracking on the backend.
