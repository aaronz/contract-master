# Data Model: Manual Rule Evaluation

**Feature**: Manual Rule Evaluation
**Date**: 2026-01-16

This document describes the data models for the entities involved in this feature, based on the feature specification.

---

### 1. Rule

Represents a single rule with conditions and actions. This is an existing entity that the feature will use.

-   **Fields**:
    -   `id` (Primary Key): Unique identifier for the rule.
    -   `tenant_id` (String): The identifier for the tenant who owns this rule.
    -   `name` (String): The human-readable name of the rule.
    -   `definition` (JSON/Text): The logical definition of the rule (conditions, actions).
    -   `version` (Integer): The version number of the rule.
-   **Relationships**:
    -   Has many `EvaluationResults`.

---

### 2. Contract

Represents the data entity that rules are evaluated against. This is an existing entity.

-   **Fields**:
    -   `id` (Primary Key): Unique identifier for the contract.
    -   `tenant_id` (String): The identifier for the tenant who owns this contract.
    -   `name` (String): The name or title of the contract.
    -   `data` (JSON): The structured data of the contract that the rule engine will process.
-   **Relationships**:
    -   Has many `EvaluationResults`.

---

### 3. Evaluation Job

Represents a request to evaluate one or more rules against a set of contracts. This is a new entity.

-   **Fields**:
    -   `id` (Primary Key): Unique identifier for the evaluation job.
    -   `tenant_id` (String): The identifier for the tenant who initiated the job.
    -   `status` (Enum): The current status of the job.
        -   Values: `PENDING`, `IN_PROGRESS`, `COMPLETED`, `FAILED`.
    -   `trigger_type` (Enum): How the job was initiated.
        -   Values: `MANUAL`, `AUTOMATIC`.
    -   `created_at` (Timestamp): When the job was created.
    -   `completed_at` (Timestamp, Nullable): When the job finished.
    -   `triggered_by` (String): The ID of the user who triggered the job.
-   **Relationships**:
    -   Has many `EvaluationResults`.
    -   (Implicitly) associated with many `Rules` and `Contracts` via `EvaluationResult`.

---

### 4. Evaluation Result

Represents the outcome of a single rule's evaluation against a single contract. This is a new entity.

-   **Fields**:
    -   `id` (Primary Key): Unique identifier for the result.
    -   `tenant_id` (String): The identifier for the tenant.
    -   `job_id` (Foreign Key): The `EvaluationJob` this result belongs to.
    -   `rule_id` (Foreign Key): The `Rule` that was evaluated.
    -   `contract_id` (Foreign Key): The `Contract` that was evaluated against.
    -   `status` (Enum): The outcome of the evaluation.
        -   Values: `PASS`, `FAIL`, `ERROR`.
    -   `details` (Text, Nullable): Detailed output, logs, or error messages from the evaluation.
    -   `timestamp` (Timestamp): When the evaluation occurred.
-   **Relationships**:
    -   Belongs to one `EvaluationJob`.
    -   Belongs to one `Rule`.
    -   Belongs to one `Contract`.
