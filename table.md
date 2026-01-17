# Global Database Schema Manifest

| Table Name | Description | Key Changes |
|------------|-------------|-------------|
| `user_info` | System users | Added initialization |
| `contract_base` | Core contract data | Added standard fields |
| `rule_config` | Rule & AI config | Added `rule_type`, `ai_prompt_template` |
| `downstream_system` | External connectors | Added access key generation |
| `notification` | System alerts/issues | Added resolution logic |
| `contract_extend_field` | Custom field definitions | Added tenant-scoped extensions |
| `evaluation_jobs` | Stores details of rule evaluation jobs |
    - `job_id` (PK, UUID): Unique identifier for the job.
    - `contract_id` (FK to `contract_base`): The contract being evaluated.
    - `rule_ids` (TEXT Array): The list of rules used for this evaluation.
    - `status` (VARCHAR): The current status of the job (`PENDING`, `IN_PROGRESS`, `COMPLETED`, `FAILED`).
    - `triggered_by` (VARCHAR): The ID of the user who initiated the job.
    - `created_at` (TIMESTAMP): When the job was created.
    - `completed_at` (TIMESTAMP, nullable): When the job was completed or failed.
    - `results` (JSONB, nullable): The results of the evaluation.
    - `tenant_id` (VARCHAR): The tenant identifier. |
| `evaluation_results` | Stores individual results of rule evaluations | New table for detailed outcomes per rule-contract pair |