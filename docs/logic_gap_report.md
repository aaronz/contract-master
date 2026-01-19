# Functional Logic & Coherence Check Report
**Date:** 2026-01-17
**Analyzed By:** Sisyphus (Agentic AI)

## Executive Summary
The system has significant coherence issues between the **Rule Engine**, **Evaluation Execution**, and **Problem Center**. While the API supports batch operations, the backend logic is partially simulated, and the frontend navigation was hiding key features.

## 1. Frontend Navigation & UX Gaps (Fixed)
*   **Missing Pages**: The "Evaluation Rules" page (`/rules/list`), which is the primary interface for triggering batch evaluations, was **completely hidden** from the navigation menu.
    *   **Action Taken**: Added `/rules/list` to the "Configuration" menu in `frontend/src/layout/index.vue`.
*   **Orphaned Workflow**: The "Rule Editor" (`/rules/editor`) is reachable via routing but has no direct menu entry (likely intended to be reached via "Rule List").

## 2. Rule Engine -> Evaluation Disconnect
*   **Simulated Backend Processing**:
    *   The `EvaluationService` receives the batch request (list of rule/contract IDs) but **does not actually process them**.
    *   It logs the request via Audit Service (good) but then generates a **single fake result** with hardcoded IDs (`rule-simulated-id`, `contract-simulated-id`) in the Kafka listener.
    *   **Impact**: Users will trigger a batch job, see a success message, but the "Problem Center" will show nonsense data instead of real violations.

## 3. Evaluation -> Problem Center Disconnect
*   **Missing Service Layer**: `ProblemCenterController` bypasses the service layer and talks directly to `EvaluationResultRepository`.
*   **Hardcoded Tenancy**: The controller forces `tenantId = "default-tenant"`, ignoring the authenticated user's context. This breaks multi-tenancy.
*   **No "Problem" Entity**: There is no distinct "Problem" entity. The "Problem Center" is just a view over `EvaluationResult` entries. This is a valid simplified architecture but limits lifecycle management (e.g., assigning a problem to a user separate from the evaluation result).

## 4. Batch vs. Single Support
*   **API Level**: ✅ Fully Supported (`POST /api/evaluations` accepts lists).
*   **Service Level**: ⚠️ Partially Supported (Method signature exists, logic is missing).
*   **Data Level**: ❌ Not Supported (No persistence for the list of IDs in `EvaluationJob` table).

## Recommendations

1.  **Backend Logic Repair (High Priority)**:
    *   Update `EvaluationService.listen()` to iterate through the actual `ruleIds` and `contractIds` (requires persisting them first).
    *   Implement real rule execution logic (connect `DroolsRuleEngine` to `EvaluationService`).

2.  **Architecture Cleanup (Medium Priority)**:
    *   Create `ProblemCenterService`.
    *   Fix tenancy in `ProblemCenterController` using `TenantContext`.

3.  **Data Model Update**:
    *   Add `contracts` and `rules` JSON/ElementCollection fields to `EvaluationJob` to persist what was requested.
