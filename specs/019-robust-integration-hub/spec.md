# Feature Specification: Robust Integration Hub (ESB Foundation)

**Feature Branch**: `019-robust-integration-hub`  
**Created**: 2026-01-21  
**Status**: Draft  
**Input**: User description: "1. Shift from @Async to Persistent Retry Queues (Reliability)... 2. Advanced Script-Based Transformations (Flexibility)... 3. Integration 'Replay' Capability... 4. Proactive Connection Health Monitoring... 5. Standardized OAuth2 Credential Manager..."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Reliable Eventual Consistency (Kafka Outbox) (Priority: P1)

As a system, when a contract is finalized, I want to ensure that its data is reliably pushed to all enabled downstream systems even if the target systems are temporarily unavailable or the application restarts.

**Why this priority**: Core reliability requirement. Prevents data loss during network partitions or system downtime.

**Independent Test**: Trigger a contract push while a mock downstream system is offline. Verify that an integration event is persisted in Kafka. Bring the mock system online and verify the data is eventually delivered after automatic retries.

**Acceptance Scenarios**:

1. **Given** a contract is saved, **When** the system publishes an integration event to Kafka, **Then** the event is stored persistently.
2. **Given** a downstream push fails due to a 503 error, **When** the Kafka consumer retries with exponential backoff, **Then** the message is not lost and successful delivery is eventually achieved.

---

### User Story 2 - Complex Data Transformation (Groovy Scripts) (Priority: P2)

As a tenant administrator, I want to define complex transformation logic for field mappings (e.g., conditional values, string manipulation) so that I can satisfy the unique API requirements of different ERP systems without backend code changes.

**Why this priority**: Flexibility. Allows the system to support any downstream schema via configuration.

**Independent Test**: Create a field mapping with a Groovy script: `if (value > 1000) { "PREMIUM" } else { "STANDARD" }`. Push a contract and verify the transformed value in the outbound payload.

**Acceptance Scenarios**:

1. **Given** a transformation script is configured for a field, **When** data is pushed, **Then** the script is executed against the internal value and the result is used in the payload.

---

### User Story 3 - Integration Observability & Manual Recovery (Priority: P2)

As an integration manager, I want to view the full payload of failed integration attempts and manually trigger a "Replay" for specific events so that I can recover from configuration errors or long-term system outages.

**Why this priority**: Critical for operational maintenance. Allows human intervention when automatic retries fail.

**Independent Test**: Identify a failed integration log. Click "Replay" in the dashboard. Verify that a new push attempt is triggered using the original payload.

**Acceptance Scenarios**:

1. **Given** an integration attempt occurs, **When** the log is created, **Then** the full JSON payload is stored in the `IntegrationLog`.
2. **Given** a failed log entry, **When** a user triggers a replay, **Then** the system re-processes the event using current mapping/system settings.

---

### User Story 4 - Secure OAuth2 Integration (Priority: P3)

As a security administrator, I want to connect to modern SaaS platforms using OAuth2 Client Credentials flow so that I don't have to manage static, long-lived API keys.

**Why this priority**: Security compliance. Mandatory for integrating with Salesforce, Microsoft Dynamics, etc.

**Independent Test**: Configure a downstream system with `OAUTH2` auth type. Verify that the system successfully exchanges credentials for a Bearer token before performing the push.

**Acceptance Scenarios**:

1. **Given** a system requires OAuth2, **When** a push is triggered, **Then** the system retrieves a valid token from the token manager and attaches it to the request header.

---

### User Story 5 - Proactive Health Monitoring (Priority: P3)

As a developer, I want to see the real-time connectivity status of all downstream systems on the dashboard so that I can identify and fix integration issues before users report data missing.

**Why this priority**: Reduced time-to-detection for outages.

**Independent Test**: Disable a mock downstream system. Verify that the Integration Hub dashboard shows a "Disconnected" or "Error" status within the configured heartbeat interval.

**Acceptance Scenarios**:

1. **Given** a registered downstream system, **When** the heartbeat task runs, **Then** it updates the `healthStatus` and `lastHeartbeat` fields.

---

### Edge Cases

- **Circular Replays**: Ensure replaying an event doesn't trigger infinite loops if the replay itself produces new events.
- **Script Sandbox**: Ensure Groovy scripts cannot execute malicious system commands or access unauthorized data. (Requirement: Sandbox environment with restricted imports).
- **Kafka Unavailability**: How does the system handle the initial produce to Kafka if Kafka is down? (Requirement: Synchronous fallback or transaction management).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST publish `IntegrationEvent` to Kafka topic `contract-integration-events` upon contract events.
- **FR-002**: System MUST implement a Kafka listener with `DefaultErrorHandler` and `ExponentialBackOff` for retries.
- **FR-003**: `FieldMapping` entity MUST include a `transformationScript` field (TEXT).
- **FR-004**: System MUST use a sandboxed Groovy Shell to execute transformation scripts with a 5-second timeout.
- **FR-005**: `IntegrationLog` entity MUST include a `requestPayload` field (TEXT/JSONB).
- **FR-006**: System MUST provide a "Replay" API that takes an `IntegrationLog` ID and re-publishes the event.
- **FR-007**: `DownstreamSystem` entity MUST support `authType = 'OAUTH2'` and store `clientId`, `clientSecret`, and `tokenUrl` in `authConfig`.
- **FR-008**: System MUST implement a `TokenManager` to cache and refresh OAuth2 Bearer tokens.
- **FR-009**: System MUST implement a `@Scheduled` heartbeat task that pings enabled downstream systems every 5 minutes.

### Key Entities *(include if feature involves data)*

- **IntegrationEvent**: Represents the intent to push data (contractId, systemId, attemptCount).
- **FieldMapping**: Updated to include `transformationScript`.
- **IntegrationLog**: Updated to include `requestPayload`.
- **DownstreamSystem**: Updated to include `healthStatus`, `lastHeartbeat`, and OAuth2 config.

## Constitution Check *(mandatory)*

- [x] **I. Transparent Tenant Isolation**: All Kafka events and logs carry `tenant_id`.
- [x] **II. Full-Link Context Propagation**: `TenantContext` is propagated to Kafka headers and restored in the consumer.
- [x] **III. Unified Metadata-Driven Design**: Transformation logic is moved from Java code to metadata (scripts).
- [x] **IV. AI-Manual Synergy**: Replay actions identify the actor (manual) in logs.
- [x] **V. Rule-Driven Data Governance**: Outbound data can be validated against rules before being enqueued.
- [x] **VI. Middleware Std**: Adopts ESB patterns and standardized OAuth2/HMAC protocols.
- [x] **VII. Absolute Auditability**: Replays and status changes are logged.
- [x] **VIII. UX/Completeness**: Provides a dashboard for monitoring and recovery.
- [x] **IX. Manifest Sync**: Will update `api.md` and `table.md`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 99.9% of integration events are eventually delivered despite temporary network failures.
- **SC-002**: Administrators can resolve 80% of sync failures via the UI (script updates + replay) without redeploying code.
- **SC-003**: System handles at least 5 different auth protocols (None, API-Key, HMAC, OAuth2, Basic).
- **SC-004**: Mean Time to Detection (MTTD) for downstream outages reduced from "Reactive" to < 5 minutes.
