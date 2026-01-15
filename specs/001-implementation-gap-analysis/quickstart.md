# Gap Analysis Quickstart

This document provides instructions on how to run the automated portion of the gap analysis.

## 1. Multi-Tenant Check
Run the following bash script to identify entities not inheriting from `BaseTenantEntity`:
```bash
grep -L "BaseTenantEntity" backend/src/main/java/com/contract/master/domain/*.java
```

## 2. API Response Validation
Execute the following command to find controllers returning raw objects:
```bash
grep -r "@RestController" backend/src/main/java/com/contract/master/api/ | xargs grep -L "ApiResponse"
```

## 3. UI Source Attribution Check
Open the browser console in the frontend and verify that the `fill_type` is present in the `contract_extend_data` payload.
