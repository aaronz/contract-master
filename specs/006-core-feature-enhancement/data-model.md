# Data Model: Contract Master Core Enhancements

## Entity Updates

### RuleConfig (Enhanced)
- `rule_type`: String (New: "LOGIC" | "AI_PROMPT")
- `ai_prompt_template`: Text (New: specific instruction for LLM)
- `logic_condition`: Text (Existing `rule_condition`)

### ContractMetadata (Virtual / DTO)
- `fieldCode`: String
- `fieldName`: String
- `fieldType`: String
- `isStandard`: Boolean
- `isSystem`: Boolean

## API Contracts

### Metadata API (Refined)
`GET /api/metadata/fields`
- Returns list of `ContractMetadata` objects.
- Merges `ContractBase` reflection with `ContractExtendField` records.

### Rule Configuration API
`POST /api/rules`
- Payload supports `ruleType`.

### Problem Center API
`POST /api/problems/{id}/resolve`
- Updates issue status.
- Records `audit_log` of resolution.

## State Transitions

### Issue Resolution
1. `OPEN` -> Detected by system.
2. `RESOLVED` -> User clicks "Resolve" in Problem Center.
