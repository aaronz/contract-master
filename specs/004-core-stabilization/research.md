# Research: Core Middleware Stabilization

## Research Items

### 1. Spring Kafka Global RecordInterceptor
- **Decision**: Implement `RecordInterceptor<String, String>`.
- **Rationale**: A `RecordInterceptor` allows for pre-processing every message before it reaches the `@KafkaListener`. By extracting the `tenantId` from the Kafka Header and setting it in `TenantContext`, we eliminate the need for manual parsing in every service.
- **Alternatives considered**: Manual parsing in every listener (error-prone), Aspect-oriented approach (harder to manage for Kafka consumers).

### 2. Hibernate PostCommitUpdateEventListener for Field Diffs
- **Decision**: Use `PostCommitUpdateEventListener`.
- **Rationale**: Unlike standard `EntityListeners`, Hibernate `PostUpdateEventListener` provides access to `Object[] oldState` and `Object[] state`. Comparing these arrays allows us to identify exactly which fields changed and store the diff as JSONB. Using the `PostCommit` variant ensures logs are only written on successful transaction commits.
- **Alternatives considered**: Manual service-layer logging (cluttered logic), Spring Data JPA Auditing (only tracks timestamps/users, no diffs).

### 3. PostgreSQL JSONB Diffs in AuditLog
- **Decision**: Store diffs in a `details` column using Hibernate 6's native `@JdbcTypeCode(SqlTypes.JSON)`.
- **Rationale**: Storing diffs as JSONB allows for flexible querying (e.g., "find all changes to contract amounts") using PostgreSQL's GIN indexes while maintaining a structured schema.
- **Alternatives considered**: Flat string storage (impossible to query), Separate `AuditLogItem` table (too many rows).

### 4. UI Journey Closure (Verification Loop)
- **Decision**: Add a `VERIFIED` status and a dedicated `Confirm` action in `detail.vue`.
- **Rationale**: The core gap is that AI data remains "Suggested" indefinitely. A human-in-the-loop "Confirm" action validates the AI output and transitions the contract to a `READY_TO_PUBLISH` state.
