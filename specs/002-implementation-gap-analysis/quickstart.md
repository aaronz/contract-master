# Gap Analysis Quickstart (Deep Audit)

## Automated Audit Scripts

### 1. Kafka Listener Context Check
Run this to find Kafka listeners that don't explicitly set `TenantContext`:
```bash
grep -r "@KafkaListener" backend/src/main/java/com/contract/master/service/ | xargs grep -L "TenantContext.setCurrentTenant"
```

### 2. Entity Inheritance Audit
Verify all domain objects inherit from `BaseTenantEntity`:
```bash
grep -L "extends BaseTenantEntity" backend/src/main/java/com/contract/master/domain/*.java
```

### 3. Controller Wrapper Audit
Find controllers not using the standardized `ApiResponse` type:
```bash
grep -r "@RestController" backend/src/main/java/com/contract/master/api/ | xargs grep -L "ApiResponse"
```
