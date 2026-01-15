# Research: UI Stability and Feature Completion

## Dashboard Data Strategy

**Decision**: Implement a `DashboardController` with an `/api/dashboard/stats` endpoint that returns aggregated data using JPA projections or native queries.

**Rationale**: 
- Currently, the dashboard uses hardcoded mock data in the frontend. 
- Real-time aggregation is needed for "Total Contracts", "Pending Approval", "Active Value", and "Risk Alerts".
- P95 response time must be < 500ms as per the Constitution. Aggregating across `contract_base` with tenant isolation is efficient enough for current scale.

**Alternatives considered**: 
- Materialized views (rejected for now to keep implementation simple).
- Pre-calculating stats in Redis (rejected as it introduces eventual consistency issues for core metrics).

## Report Export Format

**Decision**: Implement CSV export as the primary format for the operational dashboard, and basic PDF for individual contract summaries.

**Rationale**: 
- User requested "export report". CSV is the most versatile format for data analysis.
- PDF is better for document-centric views (individual contracts).
- Since `pom.xml` lacks heavy Excel libraries, CSV is a lightweight and robust choice for the first iteration.

**Alternatives considered**: 
- Excel (XLSX): Requires adding Apache POI, which increases bundle size significantly. Postponed to next phase if CSV is insufficient.

## Pagination Strategy

**Decision**: Implement Spring Data `Pageable` support in `ContractController` and update frontend to use `el-pagination` correctly.

**Rationale**: 
- The current frontend hardcodes `:total="100"`. 
- Server-side pagination is required for performance on large datasets.
- Frontend will pass `page` and `size` parameters to the backend.

## Metadata API for Field Configuration

**Decision**: Create an `/api/metadata/contract-fields` endpoint that returns a combined list of standard JPA entity fields and custom fields from `contract_extend_field`.

**Rationale**: 
- The "Field Config" and "Masking Rule" screens currently lack access to the full data model.
- A centralized metadata service ensures consistency across configuration screens.

## Connector Management Fix

**Decision**: Implement missing `PUT` and `POST` methods in `DownstreamSystemController` and connect them to the Vue 3 configuration modals.

**Rationale**: 
- The "Configure" and "New Connection" buttons are currently disconnected from backend persistence.
- Will follow the standard CRUD pattern already used in the project.
