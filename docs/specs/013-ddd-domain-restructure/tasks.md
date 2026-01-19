# Implementation Tasks: DDD Domain Restructure

**Feature Name**: DDD Domain Restructure
**Branch**: `013-ddd-domain-restructure`
**Date**: 2026-01-18

## Implementation Strategy

本重构将采用“纵向切片”迁移策略。首先建立 `shared` 共享内核，然后逐个领域（Bounded Context）进行代码归位和结构标准化。每个领域的重构都包含打破循环依赖和统一领域模型的过程。

## Phase 1: Setup - Architecture Foundation & Shared Kernel

**Goal**: Establish the `shared` module and move common DDD base classes and shared Value Objects.
**Independent Test Criteria**: Base classes compile and are usable by other modules. No domain logic in `shared`.

- [ ] T001 Create `shared` package structure in `backend/src/main/java/com/contract/master/shared/`
- [ ] T002 Move DDD base classes (`AggregateRoot`, `Entity`, `ValueObject`) to `com.contract.master.shared.domain.base/`
- [ ] T003 Move common Value Objects (`Money`, `TenantId`) to `com.contract.master.shared.domain.model/`
- [ ] T004 Define generic `DownstreamProvider` interface in `shared` if applicable, or plan its location in Contract domain.
- [ ] T005 Verify `shared` module compiles independently using `mvn compile`.

## Phase 2: User Story 1 - Contract Management Domain Restructure (Priority: P1)

**Goal**: Standardize the `contract` module structure and consolidate the dual Contract models.
**Independent Test Criteria**: All contract-related code is within `com.contract.master.contract`. The system uses a single `Contract` entity. Existing tests pass.

- [ ] T006 [P] [US1] Create standard sub-packages for `contract`: `application`, `domain`, `infrastructure`, `interface`, `dto`.
- [ ] T007 [US1] Consolidate `Contract` entity: migrate all logic to `com.contract.master.contract.domain.model.Contract`.
- [ ] T008 [US1] Move contract repositories and domain services to `com.contract.master.contract.domain/`.
- [ ] T009 [US1] Move contract JPA entities and implementations to `com.contract.master.contract.infrastructure/`.
- [ ] T010 [US1] Move contract controllers to `com.contract.master.contract.interface/`.
- [ ] T011 [US1] Break cycle: Implement `FieldConfigChangedEvent` in `metadata` and listen in `contract` to clear cache.
- [ ] T012 [US1] Run `mvn test -Dtest=Contract*` to verify functionality.

## Phase 3: User Story 1 - Identity & Access Domain Restructure (Priority: P1)

**Goal**: Standardize the `identity` module structure and move Auth/User/Tenant logic.
**Independent Test Criteria**: All identity-related code is within `com.contract.master.identity`. Login and permissions work as before.

- [ ] T013 [P] [US1] Create standard sub-packages for `identity`: `application`, `domain`, `infrastructure`, `interface`, `dto`.
- [ ] T014 [US1] Move `User`, `Tenant` aggregates to `com.contract.master.identity.domain/`.
- [ ] T015 [US1] Move `AuthApplicationService` and security logic to `com.contract.master.identity.application/`.
- [ ] T016 [US1] Move identity persistence implementations to `com.contract.master.identity.infrastructure/`.
- [ ] T017 [US1] Move identity controllers to `com.contract.master.identity.interface/`.
- [ ] T018 [US1] Verify authentication works by running integration tests.

## Phase 4: User Story 1 - Integration & Other Subdomains (Priority: P2)

**Goal**: Restructure `integration`, `notification`, `dashboard`, `evaluation` modules.
**Independent Test Criteria**: Each module is self-contained. Circular dependencies with `contract` are resolved.

- [ ] T019 [P] [US1] Standardize `integration` module structure and move CRM sync logic.
- [ ] T020 [US1] Decouple Integration: Implement `DownstreamProvider` in `integration.infrastructure` for `contract.application`.
- [ ] T021 [P] [US1] Standardize `notification`, `dashboard`, `evaluation`, and `ai` modules.
- [ ] T022 [US1] Move DTOs from global `dto` package to their respective domain `dto` sub-packages.

## Phase 5: User Story 2 - Elimination of Top-Level Layered Packages (Priority: P2)

**Goal**: Clean up the root directory and remove legacy global packages.
**Independent Test Criteria**: `com.contract.master.application`, `.domain`, `.dto`, `.infrastructure`, `.interface` packages are empty and removed.

- [ ] T023 Verify all files from global `application/` are migrated.
- [ ] T024 Verify all files from global `domain/` are migrated (including `model/` and `repository/`).
- [ ] T025 Verify all files from global `infrastructure/` are migrated.
- [ ] T026 Verify all files from global `interface/` are migrated.
- [ ] T027 Remove empty top-level layered packages from `backend/src/main/java/com/contract/master/`.
- [ ] T028 Update `ContractManagementApplication` component scan and entity scan paths if necessary.

## Final Phase: Polish & Validation

**Goal**: Ensure system stability, performance, and documentation accuracy.
**Independent Test Criteria**: Full regression test pass. No circular dependencies. Documentation is updated.

- [ ] T029 Update `features.md` to mark restructure as complete.
- [ ] T030 Update `api.md` with updated controller package paths.
- [ ] T031 Update `table.md` if any JPA mapping changes were made (should be minimal).
- [ ] T032 Verify SC-003: 100% pass on automated regression tests.
- [ ] T033 Verify SC-005: Perform a smoke test to ensure startup time is within ±5%.

## Dependency Graph

**Story Completion Order**:
Phase 1 (Setup) -> Phase 2 (Contract P1) -> Phase 3 (Identity P1) -> Phase 4 (Subdomains P2) -> Phase 5 (Cleanup P2) -> Final Phase

**Parallel Execution Examples**:
- T006, T013, T019, T021 (Package creation for different domains) can run in parallel.
- Integration tests for separate domains (Phase 2, 3, 4) can run in parallel if databases are isolated or reset.

## Implementation Strategy

我们将严格遵循“领域内聚”原则。在迁移每个领域之前，首先通过 IDE 依赖分析工具确认其输入和输出依赖。优先处理 `shared` 和基础领域（如 `identity`），最后处理业务重叠最多的 `contract` 领域，以降低重构过程中的编译错误。每一个纵向切片的迁移完成后，必须确保该领域的单元测试和相关集成测试通过。
