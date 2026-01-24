# System Context Diagram - Contract Master

The System Context diagram provides a high-level view of the Contract Master system and its interactions with users and external systems.

```mermaid
C4Context
  title System Context - Contract Master

  Person(user, "Enterprise User", "Enterprise employees, contract managers, and admins who manage and monitor contracts.")
  
  System(contractMaster, "Contract Master", "Contract management middleware that bridges CRMs and enterprise systems, providing compliance risk detection and AI insights.")
  
  System_Ext(crm, "CRM Systems", "External SaaS CRM platforms (Salesforce, HubSpot, DingTalk) providing contract data.")
  System_Ext(enterpriseSystems, "Enterprise Systems", "Downstream internal business systems (ERP, Finance) receiving verified contract data.")
  System_Ext(llmProviders, "AI Providers", "LLM providers (OpenAI, DeepSeek, SiliconFlow) for AI-driven document parsing.")

  Rel(user, contractMaster, "Uses", "HTTPS/Vue")
  Rel(crm, contractMaster, "Synchronizes contract data to", "Webhooks/API")
  Rel(contractMaster, enterpriseSystems, "Pushes verified data to", "Webhooks/HTTPS")
  Rel(contractMaster, llmProviders, "Sends documents for parsing to", "OpenAI-compatible API")
```

## Elements

| Element | Description |
|---------|-------------|
| **Enterprise User** | Users who interact with the system to manage contracts, define rules, and monitor compliance. |
| **Contract Master** | The central system for contract lifecycle management, rule evaluation, and AI insights. |
| **CRM Systems** | Sources of contract data that trigger integration flows. |
| **Enterprise Systems** | Destinations for verified and standardized contract data. |
| **AI Providers** | External services that provide advanced AI capabilities for document extraction and analysis. |
