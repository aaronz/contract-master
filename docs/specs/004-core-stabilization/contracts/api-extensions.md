# API Contract Extensions: Stabilization

## New Endpoints

### 1. Contract Verification
- **Endpoint**: `POST /api/v1/contracts/{id}/verify`
- **Request**: `{ "verified_fields": ["field_1", "field_2"] }`
- **Response**: `ApiResponse<ContractDTO>`
- **Behavior**: Updates `fill_type` to `MANUAL` for confirmed fields and transitions status to `VERIFIED`.

### 2. Downstream Management
- **Endpoint**: `GET /api/v1/settings/downstream`
- **Endpoint**: `POST /api/v1/settings/downstream`
- **Behavior**: Standard CRUD for `DownstreamSystem` entities.

### 3. Publication Trigger
- **Endpoint**: `POST /api/v1/contracts/{id}/publish`
- **Behavior**: Final Drools check -> Push to configured downstream systems -> Update to `PUBLISHED`.
