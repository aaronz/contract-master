# Global API Registry

| Endpoint | Method | Description | Version |
|----------|--------|-------------|---------|
| `/api/auth/login` | POST | User login | 1.0.0 |
| `/api/contracts` | GET | List contracts (paginated) | 1.1.0 |
| `/api/contracts` | GET | List contracts available for evaluation selection | 1.0.0 |
| `/api/evaluations` | POST | Trigger a manual rule evaluation | 1.0.0 |
| `/api/metadata/contract-fields` | GET | Unified field metadata | 1.1.0 |
| `/api/problem-center/evaluation-jobs` | GET | Retrieve a paginated list of evaluation jobs | 1.0.0 |
| `/api/problem-center/evaluation-jobs/{jobId}/results` | GET | Retrieve detailed results for a specific evaluation job | 1.0.0 |
| `/api/problems/{id}/resolve` | POST | Resolve system issue | 1.0.0 |
| `/api/rules` | POST | Create hybrid rules | 1.1.0 |
| `/api/rules/trigger-scenarios` | GET | Retrieve documentation for automatic rule trigger scenarios | 1.0.0 |
| `/api/settings/extend-fields` | POST | Create extended contract field | 1.0.0 |
| `/api/settings/extend-fields` | GET | List extended fields | 1.0.0 |