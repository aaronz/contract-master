# Global Database Schema Manifest

| Table Name | Description | Key Changes |
|------------|-------------|-------------|
| `user_info` | System users | Added initialization |
| `contract_base` | Core contract data | Added standard fields |
| `rule_config` | Rule & AI config | Added `rule_type`, `ai_prompt_template` |
| `downstream_system` | External connectors | Added access key generation |
| `notification` | System alerts/issues | Added resolution logic |
| `contract_extend_field` | Custom field definitions | Added tenant-scoped extensions |
| `evaluation_jobs` | Stores details of rule evaluation jobs | New table for manual/automatic rule evaluations |
| `evaluation_results` | Stores individual results of rule evaluations | New table for detailed outcomes per rule-contract pair |