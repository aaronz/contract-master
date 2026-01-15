# Discrepancy Matrix: Design vs Code

| Clause in design.md | Implementation Status | Discrepancy / Gap |
|---------------------|-----------------------|-------------------|
| Mandatory Tenant Filter | ⚠ Partial | Kafka listeners are vulnerable. |
| AI Suggested vs Verified | ✗ Not Implemented | UI only shows "Suggested", no verify flow. |
| Drools Validation Gate | ⚠ Incomplete | Not integrated into CRM Sync yet. |
| Standardized ApiResponse | ⚠ Inconsistent | Export and WebHook controllers bypass wrapper. |
| Immutable Audit Logs | ✅ Pass | Metadata is captured, but diffs are missing. |
