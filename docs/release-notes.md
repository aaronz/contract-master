# Release Notes - January 2026

## [1.6.0] - 2026-01-22

### Integration Hub ESB Evolution
The Integration module has been refactored into a robust Enterprise Service Bus (ESB) foundation.

#### 🚀 New Features
- **Kafka-based Outbox Pattern**: Outbound pushes are now persistent and reliable, surviving application restarts and supporting exponential backoff retries.
- **Pull-Model API**: Downstream systems can now poll for data using `X-API-KEY` with automatic tenant isolation and dynamic schema transformation.
- **Groovy Transformation Engine**: Support for sandboxed Groovy scripts in field mappings, allowing complex, zero-code data logic.
- **OAuth2 Support**: New credential manager for Client Credentials flow, enabling secure integration with Salesforce and other modern platforms.
- **Proactive Monitoring**: Added scheduled heartbeat checks and a "Test Connection" utility for downstream systems.
- **Event Replay**: Integration managers can now manually replay failed sync attempts directly from the log dashboard.

#### 🎨 UI/UX Improvements
- **Restructured Navigation**: Integration Hub is now a primary sidebar module, decoupled from general settings.
- **Payload Viewer**: Inspect the exact JSON payload for every outbound integration attempt.
- **Unified Transformation Center**: Centralized management for all inbound/outbound data protocols.

#### 🔒 Security & Governance
- **API Key Interceptor**: Strict tenant isolation for pull-model requests.
- **Data Masking**: Automatic exclusion of unmapped fields in external API responses.
- **Constitution v1.4.0**: Enshrined *Unified Metadata-Driven Design* as a core project principle.

---

## [1.4.0] - 2026-01-21

### Transparent Multi-tenancy
Comprehensive architectural upgrade to make multi-tenancy a cross-cutting concern.

#### 🚀 New Features
- **Transparent Filtering**: Automated data isolation via Hibernate Filters and AOP Pointcuts.
- **Full-Link Propagation**: Tenant context now automatically flows through Web, Async (@Async), and Messaging (Kafka) boundaries.
- **System Bypass**: Secure `executeAsSystem` utility for cross-tenant global operations.

#### 🔒 Security & Governance
- **BaseTenantEntity**: Standardized all business entities (29+) to inherit logical isolation.
- **Constitution v1.3.0**: Mandated infrastructure-level isolation and zero manual `tenant_id` handling in services.
