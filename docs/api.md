# Global API Registry

## Implementation Mapping (Domain-Centric)

| Domain | Controller Package | Responsibilities |
|--------|--------------------|------------------|
| Identity | `com.contract.master.identity.interfaces.rest` | Auth, User & Tenant Management |
| Contract | `com.contract.master.contract.interfaces.rest` | Contract Lifecycle, Metadata, Import/Publish |
| Integration | `com.contract.master.integration.interfaces.rest` | WebHooks, External Sync, Mappings |
| Dashboard | `com.contract.master.dashboard.interfaces.rest` | Stats & Analytics |
| Notification | `com.contract.master.notification.interfaces.rest` | Alerts & Messages |
| Evaluation | `com.contract.master.evaluation.interfaces.rest` | Legacy Rule Engine & Jobs |
| Rule | `com.contract.master.rule.interfaces.rest` | New Script-based Rule Engine |
| Problem Center | `com.contract.master.problemcenter.interfaces.rest` | Compliance Cockpit & Issue Tracking |

## Endpoints

| Endpoint | Method | Description | Version |
|----------|--------|-------------|---------|
| `/api/auth/login` | POST | User login and authentication | 1.0.0 |
| `/api/contracts` | GET | List contracts (paginated, supports filtering) | 1.1.0 |
| `/api/contracts` | POST | Create a new contract record | 1.0.0 |
| `/api/contracts/{id}` | GET | Get detailed information for a specific contract | 1.0.0 |
| `/api/contracts/{id}` | PUT | Update an existing contract record | 1.0.0 |
| `/api/contracts/{id}/audit` | GET | Retrieve full audit history for a contract | 1.0.0 |
| `/api/contracts/{id}/extensions` | POST | Update tenant-specific extended field data | 1.0.0 |
| `/api/contracts/{id}/verify` | POST | Trigger manual validation/verification of contract data | 1.0.0 |
| `/api/contracts/batch-archive` | POST | Archive multiple contracts in a single operation | 1.0.0 |
| `/api/contracts/ai-upload` | POST | Upload file for AI-based data extraction | 1.0.0 |
| `/api/problem-center/evaluations` | POST | Trigger new vertical slice evaluation job | 1.3.0 |
| `/api/problems` | GET | List compliance problems with advanced filtering | 1.3.0 |
| `/api/problems/{id}` | GET | Get problem details including localization | 1.3.0 |
| `/api/problems/{id}` | PUT | Update problem status, assignee, or notes | 1.3.0 |
| `/api/rules` | GET | List all active script-based rule configurations | 1.3.0 |
| `/api/rules` | POST | Create new Groovy/Regex validation rules | 1.3.0 |
| `/api/rules/{id}` | PUT | Update an existing rule configuration | 1.3.0 |
| `/api/rule-configs` | GET | [Legacy] List old rule configurations | 1.1.0 |
| `/api/rule-configs/validate/{contractId}` | POST | [Legacy] Run rule validation | 1.1.0 |
| `/api/metadata/contract-fields` | GET | Unified system and extended field metadata | 1.4.0 |
| `/api/settings/fields` | GET | Get all global and tenant field configurations | 1.4.0 |
| `/api/settings/extend-fields` | POST | Create new extended field definition | 1.4.0 |
| `/api/v1/settings/downstream` | GET | List configured downstream systems | 1.0.0 |
| `/api/webhook/{source}` | POST | Receive inbound sync data from CRM/external systems | 1.0.0 |
| `/api/dashboard/stats` | GET | Retrieve real-time operational dashboard metrics | 1.0.0 |
| `/api/problems/{id}/resolve` | POST | Mark a system issue or validation error as resolved | 1.0.0 |
