# Global Database Schema Manifest

## Core Tables

| Table Name | Description | Key Fields |
|------------|-------------|------------|
| `tenant_info` | Organizations using the platform | `tenant_id` (PK), `tenant_name`, `status` |
| `user_info` | System users with credentials | `user_id` (PK), `user_name`, `password`, `user_type` |
| `contract_base` | Central contract repository | `contract_id` (PK), `contract_no`, `amount`, `status`, `tenant_id` |
| `contract_attachment` | File references for contracts | `attachment_id` (PK), `contract_id`, `storage_path`, `is_main` |

## Extension & Metadata

| Table Name | Description | Key Fields |
|------------|-------------|------------|
| `contract_extend_field` | Dynamic field definitions | `db_id` (PK), `field_id`, `field_name`, `field_code`, `field_type`, `tenant_id` |
| `contract_extend_data` | Values for dynamic fields | `db_id` (PK), `contract_id`, `field_id`, `field_value`, `fill_type`, `tenant_id` |
| `field_config` | UI visibility & display settings | `db_id` (PK), `field_code`, `is_visible`, `display_order`, `tenant_id` |

## Rules & Evaluation (New Domain)

| Table Name | Description | Key Fields |
|------------|-------------|------------|
| `rules` | Vertical Slice rule definitions | `db_id` (PK), `name`, `logic_type`, `logic_content`, `severity`, `version`, `status`, `tenant_id` |
| `problem_evaluation_jobs` | Asynchronous job tracking | `db_id` (PK), `contract_id`, `status`, `triggered_by`, `started_at`, `finished_at`, `tenant_id` |
| `problems` | Identified risks and violations | `db_id` (PK), `evaluation_job_id`, `rule_id`, `contract_id`, `location_in_contract` (JSONB), `highlighted_text`, `generated_message`, `status`, `assignee_id`, `tenant_id` |

## Rules & Evaluation (Legacy/Stabilization)

| Table Name | Description | Key Fields |
|------------|-------------|------------|
| `rule_config` | Validation rule definitions | `rule_id` (PK), `rule_name`, `rule_type`, `rule_condition`, `tenant_id` |
| `evaluation_jobs` | Rule execution job tracking | `id` (PK), `status`, `triggered_by`, `created_at`, `tenant_id` |
| `evaluation_results` | Detailed rule validation outcomes | `id` (PK), `job_id`, `contract_id`, `rule_id`, `result_status` |

## Integration & Operations

| Table Name | Description | Key Fields |
|------------|-------------|------------|
| `downstream_system` | External ERP/Finance targets | `system_id` (PK), `endpoint_url`, `access_key`, `auth_type` |
| `field_mapping` | Data translation for external sync | `id` (PK), `internal_field`, `external_field`, `transformation` |
| `audit_log` | Field-level modification history | `id` (PK), `contract_id`, `field_name`, `old_value`, `new_value` |
| `notification` | System alerts and task reminders | `id` (PK), `user_id`, `title`, `content`, `is_read` |
| `webhook_config` | Outbound event subscriptions | `id` (PK), `url`, `events`, `is_enabled` |

## Relationship Tables

| Table Name | Description |
|------------|-------------|
| `user_role_rel` | Maps users to one or more roles |
| `role_permission_rel` | Maps roles to system permissions |
| `role_info` | Defines system roles (ADMIN, USER, etc.) |
| `permission_info` | Defines discrete system capabilities |
