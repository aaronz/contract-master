# Tech Stack & Standards

## Core Stack
- **Backend**: Java 17, Spring Boot 3.2.x, Spring Data JPA (Hibernate 6.x).
- **Frontend**: Vue 3 (Composition API), Element Plus, Vite.
- **Messaging**: Spring Kafka 3.x for asynchronous event-driven updates.
- **Scripting**: Groovy 4.x (via ScriptSandbox) for rule engine.
- **Storage**: PostgreSQL (Relational), Redis (Cache), MinIO (Object).

## Performance Standards
- **Latency**: P95 response time for core APIs < 500ms.
- **Sync**: Data sync latency < 3s.

## Quality Gates
- **Type Safety**: No `as any` or `@ts-ignore` in frontend.
- **Isolation**: Hibernate Filters mandatory for multi-tenancy.
- **Logging**: Simplified backend logs according to standard patterns.
