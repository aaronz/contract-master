# Quickstart: Verifying Metadata Extensions

## 1. Setup Custom Field
Define a new field using the Admin UI or via SQL:
```sql
INSERT INTO contract_extend_field (field_code, field_name, field_type, is_required, tenant_id)
VALUES ('project_tag', 'Project Tag', 'TEXT', true, 'tenant_1');
```

## 2. Verify Unified Metadata
Call the metadata endpoint:
```bash
curl -H "X-Tenant-ID: tenant_1" /api/metadata/contract-fields
```
Confirm `project_tag` appears in the list alongside `contractNo`.

## 3. Test Value Persistence
Save a contract with the extended field:
```bash
curl -X POST -H "X-Tenant-ID: tenant_1" -d '{
  "contractNo": "CON-001",
  "extendedFields": { "project_tag": "Internal-Ref-X" }
}' /api/contracts
```

## 4. Verify Export
Trigger a CSV export and check the columns:
```bash
curl -H "X-Tenant-ID: tenant_1" /api/contracts/export
```
Column `project_tag` should exist and have value `Internal-Ref-X`.
