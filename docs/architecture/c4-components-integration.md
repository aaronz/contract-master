# Component Diagram - Integration Hub

The Component diagram for the Integration Hub shows the internal structure of the integration-related functionality within the API Application.

```mermaid
C4Component
  title Component Diagram - Integration Hub

  Container(spa, "Single-Page App", "Vue 3", "User Interface")
  ContainerDb(db, "Main Database", "PostgreSQL", "Stores integration settings and logs")
  ContainerQueue(kafka, "Message Broker", "Kafka", "Asynchronous integration events")

  Container_Boundary(apiApp, "API Application") {
    Component(integrationController, "Integration Controller", "Spring REST Controller", "Handles integration management requests from UI.")
    Component(integrationHubService, "Integration Hub Service", "Spring Service", "Orchestrates integration logic, field mapping, and system health checks.")
    Component(crmIntegrationService, "CRM Integration Service", "Spring Service", "Manages connections and data flow with specific CRM providers.")
    Component(webhookManager, "Webhook Manager", "Spring Service", "Handles registration and dispatching of outbound webhooks.")
    Component(mappingEngine, "Mapping Engine", "Java", "Translates data between CRM-specific formats and the internal contract model.")
    Component(integrationKafkaProducer, "Integration Producer", "Spring Kafka", "Publishes integration events to Kafka.")
    Component(integrationKafkaConsumer, "Integration Consumer", "Spring Kafka", "Processes incoming integration events from Kafka.")
  }

  System_Ext(crm, "CRM Systems", "External CRM API")
  System_Ext(enterpriseSystems, "Enterprise Systems", "External Webhooks")

  Rel(spa, integrationController, "Manages integration settings", "JSON/HTTPS")
  Rel(integrationController, integrationHubService, "Delegates work to")
  
  Rel(integrationHubService, crmIntegrationService, "Uses to pull/push data")
  Rel(integrationHubService, webhookManager, "Uses to notify external systems")
  Rel(integrationHubService, mappingEngine, "Uses to translate data")
  Rel(integrationHubService, integrationKafkaProducer, "Enqueues integration tasks")
  Rel(integrationKafkaConsumer, integrationHubService, "Triggers integration tasks")

  Rel(integrationHubService, db, "Reads/Writes integration state", "JPA")
  Rel(integrationKafkaProducer, kafka, "Publishes events")
  Rel(kafka, integrationKafkaConsumer, "Delivers events")

  Rel(crmIntegrationService, crm, "Calls REST APIs", "HTTPS")
  Rel(webhookManager, enterpriseSystems, "Dispatches webhooks", "HTTPS")
```

## Components

| Component | Technology | Description |
|-----------|------------|-------------|
| **Integration Controller** | Spring REST | REST API endpoints for configuring integrations and monitoring logs. |
| **Integration Hub Service** | Spring Service | Orchestrator for all integration workflows (Pull, Push, Replay). |
| **CRM Integration Service** | Spring Service | Handles provider-specific logic and OAuth2 token management. |
| **Webhook Manager** | Spring Service | Manages registration, signing, and reliable delivery of outbound webhooks. |
| **Mapping Engine** | Java | Core logic for mapping external CRM fields to internal contract metadata. |
| **Integration Producer/Consumer** | Spring Kafka | Enables reliable, asynchronous integration processing. |
