# Data Model: Pull-Model API

## Source Entities (Existing)

### DownstreamSystem
- Provides: `accessKey`, `systemId`, `tenantId`.

### FieldMapping
- Provides: `internalField`, `externalField`, `transformationScript` (OUTBOUND).

### Contract
- Provides: Core contract data and `updateTime`.

## Context Entity (New/Transient)

### IntegrationContext (Request-scoped)
- **targetSystem**: DownstreamSystem
- **direction**: "OUTBOUND" (since data is leaving the system)
- **activeMappings**: List<FieldMapping>

## Logic Flow
1. Interceptor finds `DownstreamSystem` by `X-API-KEY`.
2. Interceptor sets `TenantContext`.
3. Controller retrieves `Contract` entities (filtered by `tenantId` and `updatedSince`).
4. Service applies `activeMappings` and `ScriptSandbox` to each `Contract`.
5. API returns the transformed JSON array.
