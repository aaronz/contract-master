# Research: Integration Hub

## Research Items

### 1. HMAC-SHA256 Payload Signing
- **Decision**: Implement a `HmacSigner` utility using standard Java `javax.crypto.Mac`.
- **Rationale**: Provides a standard, framework-agnostic way to sign JSON payloads. The signature will be sent in the `X-Hub-Signature` header.
- **Alternatives considered**: JWT (too complex for simple webhook auth), OAuth2 (required for some systems but overkill for the initial hub).

### 2. Async Push with Retry
- **Decision**: Use Spring `@Async` combined with a simple `for` loop retry logic for the initial MVP.
- **Rationale**: Avoids blocking the main UI thread during publication. Exponential backoff will be handled via `Thread.sleep` in the async worker.
- **Alternatives considered**: RabbitMQ/Kafka for retries (better for scale but adds infrastructure complexity).

### 3. Outbound Payload Schema
- **Decision**: Send a flattened JSON object derived from `ContractDTO`.
- **Rationale**: Downstream systems usually prefer a stable, flat structure rather than nested JPA hierarchies.
- **Alternatives considered**: Raw Entity JSON (leaks internal schema), XML (obsolete for modern webhooks).

### 4. Integration Dashboard UI
- **Decision**: Use Element Plus `el-table` with status tags and a "Retry" button.
- **Rationale**: Consistent with the existing Settings UI. Provides immediate visual feedback on delivery status.
