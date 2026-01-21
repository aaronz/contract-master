# Research: Robust Integration Hub (ESB Foundation)

## 1. Reliability via Kafka Outbox Pattern
- **Decision**: Implement a Kafka-based reliable delivery system.
- **Rationale**: The current `@Async` implementation is volatile and cannot survive application restarts. Kafka provides persistent storage for integration intents and built-in retry mechanisms with exponential backoff.
- **Alternatives Considered**: 
    - Database Outbox: Rejected because the project already has Kafka established for other events (AI extraction, rules), and Kafka handles message consumption state better than polling a DB table.

## 2. Dynamic Transformation with Groovy
- **Decision**: Use `GroovyShell` within a sandboxed environment for field mappings.
- **Rationale**: Allows for complex logic (ternary operators, string manipulation, arithmetic) without changing compiled code. 
- **Security Note**: The sandbox will restrict imports to `java.util.*` and `java.lang.*` (excluding `ProcessBuilder`, etc.) to prevent RCE (Remote Code Execution) vulnerabilities.

## 3. OAuth2 Client Credentials Management
- **Decision**: Centralized `OAuth2TokenManager` using Caffeine cache.
- **Rationale**: Standardizing token retrieval avoids redundant calls to auth servers across different push attempts. Caffeine provides high-performance expiration-based caching.

## 4. Payload Observability and Replay
- **Decision**: Store the `final_json_payload` in `integration_log` upon each attempt.
- **Rationale**: Critical for debugging. The "Replay" feature will re-enqueue the event into Kafka, allowing the system to leverage the existing ESB pipeline for manual recovery.

## 5. Proactive Health Checks
- **Decision**: Implement a `@Scheduled` task using Spring's `Scheduler`.
- **Rationale**: Simple authenticated GET/HEAD requests to target endpoints provide immediate dashboard feedback, transitioning the IT team from "reactive" to "proactive" monitoring.
