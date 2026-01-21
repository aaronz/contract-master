# Data Model: Robust Integration Hub

## Updated Entities

### FieldMapping
- **transformationScript**: TEXT (Groovy code snippet)
- **description**: String (User notes)

### IntegrationLog
- **requestPayload**: TEXT/JSONB (The actual data sent to downstream)
- **retryCount**: Integer
- **traceId**: String (Link to Kafka message)

### DownstreamSystem
- **healthStatus**: Enum (HEALTHY, UNSTABLE, DOWN)
- **lastHeartbeat**: LocalDateTime
- **authConfig**: TEXT/JSONB (Stores client_id, client_secret, token_url for OAuth2)

## New Entities/Schemas

### IntegrationEvent (Kafka Message)
- **eventId**: UUID
- **contractId**: String
- **systemId**: String
- **tenantId**: String
- **attempt**: Integer
- **timestamp**: Long

## Relationships
- `DownstreamSystem` owns many `FieldMapping` records (already existing).
- `IntegrationLog` records every attempt for a `DownstreamSystem`.
