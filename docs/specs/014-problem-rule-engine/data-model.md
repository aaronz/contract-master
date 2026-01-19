# Data Model: Problem Center and Rule Engine

## Entities

### **Rule**
- `id`: BIGSERIAL (PK)
- `tenant_id`: UUID (FK to Tenant)
- `name`: VARCHAR(255)
- `description`: TEXT
- `category`: VARCHAR(100) (e.g., "Liability", "Payment")
- `logic_type`: ENUM (GROOVY, REGEX)
- `logic_content`: TEXT
- `severity`: ENUM (HIGH, MEDIUM, LOW)
- `version`: INT (Default 1)
- `status`: ENUM (ACTIVE, INACTIVE, DRAFT)
- `created_at`: TIMESTAMP
- `updated_at`: TIMESTAMP

### **EvaluationJob**
- `id`: BIGSERIAL (PK)
- `tenant_id`: UUID (FK to Tenant)
- `contract_id`: UUID (FK to Contract)
- `status`: ENUM (PENDING, RUNNING, COMPLETED, FAILED)
- `triggered_by`: VARCHAR(255) (User ID or "SYSTEM")
- `error_details`: TEXT (Captured exception info)
- `started_at`: TIMESTAMP
- `finished_at`: TIMESTAMP

### **Problem**
- `id`: BIGSERIAL (PK)
- `tenant_id`: UUID (FK to Tenant)
- `evaluation_job_id`: BIGINT (FK to EvaluationJob)
- `rule_id`: BIGINT (FK to Rule)
- `contract_id`: UUID (FK to Contract)
- `location_in_contract`: JSONB (e.g., `{"page": 1, "rects": [...]}`)
- `highlighted_text`: TEXT (The snippet that triggered the rule)
- `generated_message`: TEXT (Rule-defined error message)
- `status`: ENUM (NEW, ACKNOWLEDGED, RESOLVED, IGNORED)
- `assignee_id`: VARCHAR(255)
- `notes`: TEXT
- `created_at`: TIMESTAMP
- `updated_at`: TIMESTAMP

## Relationships
- `Rule` (1) -> `Problem` (N)
- `EvaluationJob` (1) -> `Problem` (N)
- `Contract` (1) -> `EvaluationJob` (N)
- `Contract` (1) -> `Problem` (N)

## State Transitions
- **EvaluationJob**: `PENDING` -> `RUNNING` -> `COMPLETED` | `FAILED`
- **Problem**: `NEW` -> `ACKNOWLEDGED` -> `RESOLVED` | `IGNORED`
