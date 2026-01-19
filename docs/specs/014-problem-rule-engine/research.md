# Research: Problem Center and Rule Engine

**Date**: 2026-01-18
**Feature**: Problem Center and Rule Engine (014-problem-rule-engine)

## Phase 0 Research Findings

### 1. Script Engine Selection (Groovy & Regex)
**Decision**: Adopt **Groovy** as the primary engine for complex logic and **Regex** for pattern matching.
**Rationale**: 
- **Groovy** is natively supported in the project's current tech stack and allows for deep integration with JPA entities.
- **Sandboxing Implementation**: We will implement a robust sandbox using `SecureASTCustomizer` to:
    - Whitelist allowed classes (e.g., `java.math.BigDecimal`, `java.util.Date`).
    - Disallow dangerous method calls (e.g., `System.exit`, `Runtime.exec`).
    - Prevent closure definitions and attribute expressions to ensure scripts are pure logic.
- **Thread Safety**: The system will follow the "One Script Instance Per Thread" pattern, ensuring no state leakage between concurrent evaluation jobs.

### 2. PDF Localization Metadata
**Decision**: Store localization using a **Page-based coordinate system** within JSONB.
**Rationale**: 
- Structure: `{"page": 1, "rects": [{"left": 10, "top": 20, "width": 100, "height": 15}]}`.
- PDF.js and Element Plus integration: Coordinates are more stable than character offsets when dealing with different PDF generators.
- UX: This allows for "box-style" highlighting which is much more visible to users than simple text underline.

### 3. Asynchronous Evaluation via Kafka
**Decision**: Use a **Dedicated "Evaluation" Kafka Topic** (`contract-evaluation`).
**Rationale**: 
- Current setup in `ExtractionTaskService` shows an existing pattern for async extraction.
- Orchestration: `EvaluationApplicationService` will publish job IDs. A pool of consumers will handle the execution, allowing for horizontal scaling under high load (e.g., batch re-evaluation).

### 4. Persistence of "Problems"
**Decision**: Bulk insert generated problems at the end of a `EvaluationJob`.
**Rationale**: Minimizes DB roundtrips. High-volume rule execution (50+ rules per contract) could lead to performance bottlenecks if each hit is saved individually.

## Alternatives Considered
- **Hardcoded Java Rules**: Rejected because it requires full redeployment for any business rule change.
- **Drools Rule Engine**: Considered but rejected for Phase 1 as it introduces significant overhead and a steep learning curve compared to simple Groovy scripts for this specific domain.
