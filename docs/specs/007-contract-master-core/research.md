# Research: Contract Master Core System

## Research Items

### 1. Spring AI vs Azure SDK for Contract Extraction
- **Context**: Need to extract structured data (dates, amounts, clauses) from contract PDFs.
- **Task**: Compare Spring AI's generic abstraction with the native Azure Document Intelligence (Form Recognizer) SDK for accuracy and feature support (like layout analysis).

### 2. Drools 9.x Multi-tenant Rule Loading
- **Context**: Rules need to be isolated per tenant.
- **Task**: Research the best pattern for loading `KieContainer` dynamically per `tenant_id` without excessive memory overhead.

### 3. CRM WebHook Signature Verification
- **Context**: Need to secure WebHooks from Salesforce, HubSpot, and DingTalk.
- **Task**: Identify the standard verification algorithms and headers for each CRM to implement a unified verification filter.

### 4. Playwright for Multi-tenant E2E Tests
- **Context**: Need to test full flows while switching tenant contexts.
- **Task**: Research Playwright's browser context isolation for managing different tenant sessions in parallel tests.

## Findings

### 1. AI Extraction Decision
- **Decision**: Use **Azure Document Intelligence (Native SDK)**.
- **Rationale**: Azure provides pre-built "Contract" models that are significantly more accurate than generic LLM prompts for extracting complex clauses and tabular data. Spring AI is better for generic chat, but the native SDK gives access to layout confidence scores and specific document structure.
- **Alternatives considered**: Spring AI with GPT-4o (less reliable for layout-heavy tables), AWS Textract (similar but Azure's contract model is more specialized).

### 2. Rule Engine Design
- **Decision**: **Drools 9.x with Redis-backed Rule Storage**.
- **Rationale**: Rules will be stored in PostgreSQL and cached in Redis. The application will use a `Map<String, KieContainer>` to maintain tenant-specific rule sessions, with a listener to invalidate/reload when a tenant updates their rules.
- **Alternatives considered**: EasyRules (too simple for complex contract logic), custom JS engine (harder to maintain for business users).

### 3. WebHook Security
- **Decision**: **Custom Security Filter Chain**.
- **Rationale**: Implement a `WebHookSignatureFilter` that dispatches to specific verifiers based on the source (Salesforce: HMAC-SHA256, HubSpot: HMAC-SHA256, DingTalk: Custom Signature). Standardizes integration.

### 4. E2E Testing Strategy
- **Decision**: **Playwright with Java Bindings**.
- **Rationale**: Allows the team to write E2E tests in the same language as the backend (Java), while leveraging Playwright's superior speed and built-in support for isolated browser contexts.
