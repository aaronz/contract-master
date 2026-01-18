# API Contract Verification: DDD Domain Restructure

This refactor is strictly architectural. The following external REST API base paths are preserved:

- `/api/contracts/**` -> Migrated to `contract` interface layer.
- `/api/identity/**` -> Migrated to `identity` interface layer.
- `/api/notifications/**` -> Migrated to `notification` interface layer.
- `/api/dashboard/**` -> Migrated to `dashboard` interface layer.

## Validation Plan
1.  **Swagger/OpenAPI Verification**: Run the application and compare the generated Swagger JSON with the pre-refactor state.
2.  **Postman/E2E Tests**: Execute the existing collection of E2E tests against the refactored endpoints.
3.  **No New Endpoints**: No new public endpoints are introduced in this phase.
