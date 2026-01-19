# Data Model for Contract Re-evaluation

This document outlines the data model for the new entities required for the contract re-evaluation feature.

## Entity: EvaluationJob

Represents a single run of an evaluation for a specific contract against a set of rules.

**Fields**:

-   `jobId` (Primary Key, UUID): Unique identifier for the job.
-   `contractId` (Foreign Key to Contract): The contract being evaluated.
-   `ruleIds` (Array of UUIDs): The list of rules used for this evaluation.
-   `status` (String): The current status of the job (e.g., `PENDING`, `IN_PROGRESS`, `COMPLETED`, `FAILED`).
-   `triggeredBy` (String): The ID of the user who initiated the job.
-   `createdAt` (Timestamp): When the job was created.
-   `completedAt` (Timestamp, nullable): When the job was completed or failed.
-   `results` (JSONB, nullable): The results of the evaluation.
-   `tenantId` (String): The tenant identifier.

**Relationships**:

-   Belongs to a `Contract`.
-   Has many `EvaluationRules` (implicitly through `ruleIds`).
-   Belongs to a `User` (the triggerer).

**State Transitions**:

-   A new job is created with the `PENDING` status.
-   It transitions to `IN_PROGRESS` when a worker picks it up.
-   It transitions to `COMPLETED` upon successful evaluation.
-   It transitions to `FAILED` if an error occurs during evaluation.

## Entity: AuditLog

The existing `AuditLog` entity will be used to record the re-evaluation event.

**New Audit Event**:

-   **Action**: `RE_EVALUATION_TRIGGERED`
-   **Details**: Should include the `contractId` and the `ruleIds` used for the re-evaluation.
