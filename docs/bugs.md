# Global Bugs Manifest

## Resolved Issues

| Bug ID | Description | Feature | Status | Resolution |
|--------|-------------|---------|--------|------------|
| B001 | Secrets & Keys generate new key not shown | Integration Hub | Fixed | Corrected UI response handling to display generated keys. |
| B002 | Problem center clicks resolve nothing happens | Problem Center | Fixed | Fixed API mapping and resolve permission logic. |
| B003 | Mock data in dashboard | Dashboard | Fixed | Connected dashboard metrics to real database statistics. |
| B004 | Paging not working | Contract List | Fixed | Fixed Pageable implementation in JPA repository calls. |
| B005 | JWT Token Expiry (ExpiredJwtException) | Auth | Fixed | Adjusted clock skew and token duration in security config. |
| B006 | Repository method overloading conflict | Core | Fixed | Renamed conflicting JPA repository method signatures. |
| B007 | NullPointerException in Contract Filtering | Search | Fixed | Added null checks for tenant-specific field configurations. |
| B008 | Case inconsistency in Metadata | Metadata | Fixed | Standardized camelCase vs snake_case mapping in field config. |

## Open Issues & Technical Debt

| ID | Description | Feature | Severity | Notes |
|----|-------------|---------|----------|-------|
| D001 | Stale Cache Risks in Field Config | Cache | Low | Cache requires manual clearing in tests; potential sync issues if multi-node. |
| D002 | Hardcoded PII Masking Logic | Security | Medium | Phone masking logic is hardcoded in Service; needs flexible framework. |
| D003 | Development Logging in Production | Config | Low | `DEBUG` logging enabled for security in `application.yml`. |
| D004 | Incomplete Async Job Tracking | Evaluation | Medium | Re-evaluation job progress tracking needs persistent heartbeats. |
