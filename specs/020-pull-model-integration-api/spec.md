# Feature Specification: Pull-Model Integration API

**Feature Branch**: `020-pull-model-integration-api`  
**Created**: 2026-01-21  
**Status**: Draft  
**Input**: Add Pull-Model API for downstream systems using X-API-KEY and FieldMapping.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Secure Data Retrieval with API Key (Priority: P1)

As a downstream system developer, I want to pull contract data using an API Key so that I can synchronize data on my own schedule without implementing a Webhook receiver.

**Why this priority**: Core requirement for systems that don't support push notifications.

**Independent Test**: Request `GET /api/v1/integration/contracts` with an invalid `X-API-KEY`. Verify a 401 Unauthorized response. Then, request with a valid `X-API-KEY` and verify a 200 OK response with contract data.

**Acceptance Scenarios**:

1. **Given** a downstream system is registered with a valid Access Key, **When** it calls the Pull API with the key in the header, **Then** it receives a list of contracts.
2. **Given** no API Key or an expired key, **When** the API is called, **Then** access is denied.

---

### User Story 2 - Dynamic Schema Adaptation (Priority: P1)

As a system, I want to automatically transform the contract data based on the downstream system's specific `FieldMapping` and Groovy scripts during a pull request so that each system receives data in its own required format.

**Why this priority**: Essential for maintaining the "Transparent Design" where systems receive only relevant and correctly formatted data.

**Independent Test**: Define an OUTBOUND mapping for "System A" that renames `contractName` to `legal_title` and uses a Groovy script to uppercase it. Pull data as "System A" and verify the JSON keys and values.

**Acceptance Scenarios**:

1. **Given** "System A" has a mapping rule for `amount`, **When** it pulls data, **Then** the JSON contains the mapped key and the value processed by the associated Groovy script.
2. **Given** a field is not mapped for "System B", **When** it pulls data, **Then** that field is excluded from the response (automatic data masking).

---

### User Story 3 - Delta Sync Support (Priority: P2)

As a downstream system, I want to pull only the contracts that have changed since my last sync so that I can minimize bandwidth usage and processing time.

**Why this priority**: Operational efficiency for high-volume tenants.

**Independent Test**: Call the API with a `since` timestamp. Verify that only contracts updated after that timestamp are returned.

**Acceptance Scenarios**:

1. **Given** multiple contracts exist, **When** the pull API is called with `?updatedSince=2026-01-21T00:00:00`, **Then** only contracts with `updateTime` greater than that value are returned.

---

### Edge Cases

- **Tenant Leakage**: Ensure a system using Tenant A's API Key cannot pull Tenant B's contracts. (Requirement: API Key must be uniquely linked to a tenant-specific `DownstreamSystem`).
- **Script Failures**: What if a Groovy transformation script crashes during a pull? (Requirement: Return a partial success with errors logged, or fallback to the raw value depending on configuration).
- **Pagination**: Large datasets must be paginated to prevent memory exhaustion. (Requirement: Support `page` and `size` parameters).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST implement `IntegrationApiKeyInterceptor` to validate `X-API-KEY` against the `DownstreamSystem` repository.
- **FR-002**: System MUST expose `GET /api/v1/integration/contracts` endpoint.
- **FR-003**: The Pull API MUST automatically identify the `targetSystemId` and `tenantId` from the authenticated API Key context.
- **FR-004**: The Pull API MUST apply **OUTBOUND** `FieldMapping` rules (including Groovy scripts) to each contract in the result set.
- **FR-005**: System MUST support filtering by `updatedSince` timestamp.
- **FR-006**: System MUST support standard pagination (`page`, `size`) for pull requests.
- **FR-007**: Every successful pull request MUST be logged in the `IntegrationLog` as an `INBOUND_PULL` event.

### Key Entities *(include if feature involves data)*

- **DownstreamSystem**: Used for authentication and system identification.
- **FieldMapping**: Used to determine the response schema and transformations.
- **Contract**: The source data being pulled.

## Constitution Check *(mandatory)*

- [x] **I. Transparent Tenant Isolation**: Pull results are strictly scoped to the tenant associated with the API Key.
- [x] **II. Full-Link Context Propagation**: `TenantContext` is populated from the API Key before data retrieval.
- [x] **III. Unified Metadata-Driven Design**: The Pull API response schema is defined by `FieldMapping` metadata.
- [x] **IV. AI-Manual Synergy**: Data source (AI/Manual) is preserved in the response if mapped.
- [x] **V. Rule-Driven Data Governance**: Outbound data can be validated against rules before being enqueued.
- [x] **VI. Middleware Std**: Uses standard `X-API-KEY` header and REST patterns.
- [x] **VII. Absolute Auditability**: Every pull attempt is recorded in `IntegrationLog`.
- [x] **VIII. UX/Completeness**: Provides a complete integration alternative for polling systems.
- [x] **IX. Manifest Sync**: Will update `api.md`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Downstream systems can successfully pull data in their required format with zero manual data cleaning.
- **SC-002**: 100% of unmapped fields are automatically excluded from the Pull API response.
- **SC-003**: Pull request latency for 100 records is < 500ms (including script execution).
- **SC-004**: Zero cross-tenant data leakage confirmed by E2E tests using multiple API Keys.
