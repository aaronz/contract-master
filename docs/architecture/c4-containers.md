# Container Diagram - Contract Master

The Container diagram shows the high-level technical building blocks of the Contract Master system.

```mermaid
C4Container
  title Container Diagram - Contract Master

  Person(user, "Enterprise User", "Enterprise employees, contract managers, and admins.")

  System_Boundary(contractMasterBoundary, "Contract Master") {
    Container(spa, "Single-Page App", "Vue 3, TypeScript, Element Plus", "Provides the user interface for contract management, compliance cockpit, and system configuration.")
    Container(apiApp, "API Application", "Java 17, Spring Boot", "The core backend service providing RESTful APIs for contract management, rule evaluation, and integration.")
    
    ContainerDb(db, "Main Database", "PostgreSQL", "Stores contract data, metadata, rules, audit logs, and system configuration.")
    ContainerQueue(kafka, "Message Broker", "Kafka", "Handles asynchronous events for contract evaluation and integration tasks.")
    Container(fileStorage, "File Storage", "MinIO / S3 / Local", "Stores contract documents and attachments.")
    ContainerDb(redis, "Cache", "Redis", "Caches frequently accessed data and handles rate limiting.")
  }

  System_Ext(crm, "CRM Systems", "External SaaS CRM platforms.")
  System_Ext(enterpriseSystems, "Enterprise Systems", "Downstream internal business systems.")
  System_Ext(llmProviders, "AI Providers", "External AI parsing services.")

  Rel(user, spa, "Uses", "HTTPS")
  Rel(spa, apiApp, "API calls", "JSON/HTTPS (JWT)")
  
  Rel(apiApp, db, "Reads from and writes to", "JDBC/JPA")
  Rel(apiApp, kafka, "Publishes and consumes events", "Kafka Protocol")
  Rel(apiApp, redis, "Caches and rate limits", "Redis Protocol")
  Rel(apiApp, fileStorage, "Stores and retrieves files", "S3 API / File System")

  Rel(crm, apiApp, "Sends contract data to", "Webhooks/HTTPS")
  Rel(apiApp, crm, "Pulls contract data from", "REST API")
  Rel(apiApp, llmProviders, "Sends documents to", "OpenAI API")
  Rel(apiApp, enterpriseSystems, "Pushes verified data to", "Webhooks/HTTPS")
```

## Containers

| Container | Technology | Description |
|-----------|------------|-------------|
| **Single-Page App** | Vue 3, Vite, Element Plus | Modern web interface for all user interactions. |
| **API Application** | Java 17, Spring Boot 3.2.x | Core backend service following DDD principles. |
| **Main Database** | PostgreSQL | Relational database for persistent storage. |
| **Message Broker** | Kafka | Asynchronous messaging for decoupling evaluation and integration tasks. |
| **File Storage** | MinIO / S3 / Local | Storage for unstructured contract documents. |
| **Cache** | Redis | High-performance distributed cache. |
