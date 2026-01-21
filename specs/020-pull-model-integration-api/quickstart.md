# Quickstart: Pull-Model Integration

## 1. Register Downstream System
Obtain your `Access Key` from the **Integration Hub > Downstream Systems** dashboard.

## 2. Configure Field Mapping
Ensure you have defined **OUTBOUND** mappings for your system in the **Transformation Center**. Any field not mapped will be excluded from the API response for security.

## 3. Pull Data
```bash
curl -H "X-API-KEY: YOUR_ACCESS_KEY" \
     "https://api.contract-master.com/api/v1/integration/contracts?page=0&size=50"
```

## 4. Perform Delta Sync
To get only updated contracts since your last run:
```bash
curl -H "X-API-KEY: YOUR_ACCESS_KEY" \
     "https://api.contract-master.com/api/v1/integration/contracts?updatedSince=2026-01-21T12:00:00"
```
