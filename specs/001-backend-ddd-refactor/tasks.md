# Implementation Tasks: Backend DDD Refactor

**Feature Name**: Backend DDD Refactor
**Branch**: `001-backend-ddd-refactor`
**Date**: 2026年1月17日星期六

## Implementation Strategy

本重构将采用渐进式迁移策略，分阶段进行，以最大程度地降低风险并确保业务连续性。MVP将首先关注核心“合同管理上下文”的DDD重构，以实现代码可维护性的提升。后续阶段将扩展到其他有界上下文并增强可测试性与可扩展性。

## Phase 1: Setup - Initial Project Structure and Base DDD Elements

**Goal**: Establish the foundational DDD layer structure and core building blocks within the backend project.
**Independent Test Criteria**: New directory structure is in place, and base DDD classes/interfaces are defined. Maven build is successful.

- [X] T001 Create `application` package in `backend/src/main/java/com/contract/master/application/`
- [X] T002 Create `domain` package in `backend/src/main/java/com/contract/master/domain/`
- [X] T003 Create `infrastructure` package in `backend/src/main/java/com/contract/master/infrastructure/`
- [X] T004 Create `interfaces` package in `backend/src/main/java/com/contract/master/interfaces/`
- [X] T005 Create corresponding test packages under `backend/src/test/java/com/contract/master/`
- [X] T006 Define base `AggregateRoot` interface/abstract class in `backend/src/main/java/com/contract/master/domain/model/base/AggregateRoot.java`
- [X] T007 Define base `Entity` abstract class in `backend/src/main/java/com/contract/master/domain/model/base/Entity.java`
- [X] T008 Define base `ValueObject` interface/abstract class in `backend/src/main/java/com/contract/master/domain/model/base/ValueObject.java`
- [X] T009 Ensure `pom.xml` is configured for the new package structure and dependencies.
- [X] T010 Verify basic project setup using `mvn clean install` in `backend/`

## Phase 2: Foundational - Identifying Bounded Contexts and Core Domain Modeling

**Goal**: Identify the initial "Contract Management Context" and migrate its core domain models (entities, value objects, aggregates) into the new DDD structure.
**Independent Test Criteria**: Core domain models for Contract Management are defined, compile successfully, and initial repository interfaces are in place.

- [X] T011 Analyze existing `Contract` related codebase to identify domain boundaries for "合同管理上下文"
- [X] T012 Create `contract` package in `backend/src/main/java/com/contract/master/domain/model/contract/`
- [X] T013 [P] Define `ContractId` Value Object in `backend/src/main/java/com/contract/master/domain/model/contract/ContractId.java`
- [X] T014 [P] Define `TenantId` Value Object in `backend/src/main/java/com/contract/master/domain/model/shared/TenantId.java`
- [X] T015 [P] Define `ContractNo` Value Object in `backend/src/main/java/com/contract/master/domain/model/contract/ContractNo.java`
- [X] T016 [P] Define `Money` Value Object in `backend/src/main/java/com/contract/master/domain/model/shared/Money.java`
- [X] T017 [P] Define `PartyInfo` Value Object in `backend/src/main/java/com/contract/master/domain/model/shared/PartyInfo.java`
- [X] T018 [P] Define `AttachmentInfo` Value Object in `backend/src/main/java/com/contract/master/domain/model/contract/AttachmentInfo.java`
- [X] T019 [P] Define `ExtendFieldData` Value Object in `backend/src/main/java/com/contract/master/domain/model/contract/ExtendFieldData.java`
- [X] T020 Define `Contract` Aggregate Root in `backend/src/main/java/com/contract/master/domain/model/contract/Contract.java` (incorporating value objects and core behavior)
- [X] T021 Create `rule` package in `backend/src/main/java/com/contract/master/domain/model/rule/`
- [X] T022 [P] Define `RuleId` Value Object in `backend/src/main/java/com/contract/master/domain/model/rule/RuleId.java`
- [X] T023 [P] Define `Rule` Entity in `backend/src/main/java/com/contract/master/domain/model/rule/Rule.java`
- [X] T024 Define `ContractRepository` interface in `backend/src/main/java/com/contract/master/domain/repository/ContractRepository.java`
- [X] T025 Define `RuleRepository` interface in `backend/src/main/java/com/contract/master/domain/repository/RuleRepository.java`

## Phase 3: User Story 1 - 提升代码可维护性 (Contract Management Domain Refactor)

**Goal**: 通过重构核心合同管理领域，实现代码可维护性的显著提升。
**Independent Test**: 通过选择一个现有业务功能（如合同创建或更新），在不修改其他功能代码的前提下，修改其核心业务规则，并验证其正确性。新开发者能够快速理解合同的核心业务逻辑。

- [X] T026 [US1] Create `ContractManagementContext` infrastructure package in `backend/src/main/java/com/contract/master/infrastructure/contract/`
- [X] T027 [US1] Implement `ContractRepository` (Spring Data JPA) in `backend/src/main/java/com/contract/master/infrastructure/contract/ContractRepositoryImpl.java`
- [X] T028 [US1] Implement `RuleRepository` (Spring Data JPA) in `backend/src/main/java/com/contract/master/infrastructure/contract/RuleRepositoryImpl.java`
- [X] T029 [US1] Create `contract` package for domain services in `backend/src/main/java/com/contract/master/domain/service/contract/`
- [X] T030 [US1] Define `ContractEvaluationService` in `backend/src/main/java/com/contract/master/domain/service/contract/ContractEvaluationService.java`
- [X] T031 [US1] Define `ContractLifecycleService` in `backend/src/main/java/com/contract/master/domain/service/contract/ContractLifecycleService.java`
- [X] T032 [US1] Create `contract` package for application services in `backend/src/main/java/com/contract/master/application/contract/`
- [X] T033 [US1] Define `ContractApplicationService` in `backend/src/main/java/com/contract/master/application/contract/ContractApplicationService.java`
- [X] T034 [US1] Refactor existing contract creation logic to use `ContractApplicationService` and `Contract` aggregate root.
- [X] T035 [US1] Refactor existing contract update logic to use `ContractApplicationService` and `Contract` aggregate root.
- [X] T036 [US1] Refactor existing contract query logic to use `ContractRepository`.
- [X] T037 [US1] Migrate existing REST endpoints for contract management to `backend/src/main/java/com/contract/master/interfaces/contract/ContractController.java`
- [X] T038 [US1] Ensure `ContractController` uses `ContractApplicationService`.
- [X] T039 [US1] Run existing regression tests to verify no functional regressions introduced for contract management.
- [X] T040 [US1] Conduct manual testing for contract creation, update, and query to confirm behavior matches existing functionality.

## Phase 4: User Story 2 - 增强系统可测试性 (Contract Management Domain Testing)

**Goal**: 确保重构后的合同管理领域具有高度可测试性。
**Independent Test**: 编写全面的单元测试和集成测试，验证合同领域的各个组件能够独立运行且逻辑正确。

- [X] T041 [US2] Write unit tests for `ContractId`, `Money`, `PartyInfo` Value Objects in `backend/src/test/java/.../domain/model/shared/` and `backend/src/test/java/.../domain/model/contract/`
- [X] T042 [US2] Write unit tests for `Contract` Aggregate Root's core behaviors in `backend/src/test/java/.../domain/model/contract/ContractTest.java`
- [X] T043 [US2] Write unit tests for `Rule` Entity in `backend/src/test/java/.../domain/model/rule/RuleTest.java`
- [X] T044 [US2] Write unit tests for `ContractEvaluationService` in `backend/src/test/java/.../domain/service/contract/ContractEvaluationServiceTest.java`
- [X] T045 [US2] Write unit tests for `ContractLifecycleService` in `backend/src/test/java/.../domain/service/contract/ContractLifecycleServiceTest.java`
- [X] T046 [US2] Write integration tests for `ContractRepositoryImpl` using a test-specific or in-memory database in `backend/src/test/java/.../infrastructure/contract/ContractRepositoryImplTest.java`
- [X] T047 [US2] Write integration tests for `RuleRepositoryImpl` using a test-specific or in-memory database in `backend/src/test/java/.../infrastructure/contract/RuleRepositoryImplTest.java`
- [X] T048 [US2] Write unit/integration tests for `ContractApplicationService` in `backend/src/test/java/.../application/contract/ContractApplicationServiceTest.java`
- [X] T049 [US2] Ensure all new tests pass and integrate into the existing build process.
- [X] T050 [US3] Analyze existing Auth & Tenant related codebase to identify domain boundaries for "权限与租户上下文"
- [X] T051 [US3] Create `auth` and `tenant` packages in `backend/src/main/java/com/contract/master/domain/model/`
- [X] T052 [US3] Define `Tenant` Aggregate Root in `backend/src/main/java/com/contract/master/domain/model/tenant/Tenant.java`
- [X] T053 [US3] Define `User` Entity in `backend/src/main/java/com/contract/master/domain/model/auth/User.java`
- [X] T054 [US3] Define `Role` Entity in `backend/src/main/java/com/contract/master/domain/model/auth/Role.java`
- [X] T055 [US3] Define `Permission` Entity/Value Object in `backend/src/main/java/com/contract/master/domain/model/auth/Permission.java`
- [X] T056 [US3] Define `TenantRepository` interface in `backend/src/main/java/com/contract/master/domain/repository/TenantRepository.java`
- [X] T057 [US3] Define `UserRepository` interface in `backend/src/main/java/com/contract/master/domain/repository/UserRepository.java`
- [X] T058 [US3] Create `auth` and `tenant` infrastructure packages in `backend/src/main/java/com/contract/master/infrastructure/`
- [X] T059 [US3] Implement `TenantRepository` (Spring Data JPA) in `backend/src/main/java/com/contract/master/infrastructure/tenant/TenantRepositoryImpl.java`
- [X] T060 [US3] Implement `UserRepository` (Spring Data JPA) in `backend/src/main/java/com/contract/master/infrastructure/auth/UserRepositoryImpl.java`
- [X] T061 [US3] Create `auth` and `tenant` domain services in `backend/src/main/java/com/contract/master/domain/service/`
- [X] T062 [US3] Define `AuthDomainService` and `TenantDomainService`.
- [X] T063 [US3] Create `auth` and `tenant` application services in `backend/src/main/java/com/contract/master/application/`
- [X] T064 [US3] Define `AuthApplicationService` and `TenantApplicationService`.
- [X] T065 [US3] Refactor existing authentication/authorization logic to use the new DDD structure.
- [X] T066 Verify existing external API contracts are fully compatible and unchanged.
- [X] T067 Update `features.md` to reflect the completed backend DDD refactor.
- [X] T068 Update `api.md` for any implicit changes to internal API interactions or if any external API definitions changed (should be minimal).
- [X] T069 Update `table.md` for any database schema changes resulting from DDD modeling.
- [X] T070 Review global logging and error handling mechanisms to ensure they align with the new layered architecture.
- [X] T071 Conduct performance testing to ensure the refactor has not degraded performance (SC-004).
- [X] T072 Update internal developer documentation with DDD concepts, migration guide, and new project structure overview.

## Dependency Graph

**Story Completion Order**:
Phase 1 (Setup) -> Phase 2 (Foundational) -> Phase 3 (US1) -> Phase 4 (US2) -> Phase 5 (US3) -> Final Phase (Polish)

**Parallel Execution Examples**:

*   **Within Phase 1 (Setup)**: Tasks T001-T008 (package creation and base DDD elements definition) can be done in parallel.
*   **Within Phase 2 (Foundational)**: Tasks T013-T019 (defining Value Objects) can be parallelized. T020 (Contract Aggregate Root) and T023 (Rule Entity) can also be done in parallel once their respective value objects are defined.
*   **Within Phase 3 (US1)**: T027 (ContractRepositoryImpl) and T028 (RuleRepositoryImpl) can be parallelized. T030 (ContractEvaluationService) and T031 (ContractLifecycleService) can be parallelized.
*   **Within Phase 4 (US2)**: T041-T045 (writing unit tests for domain layer components) are largely parallelizable. T046-T047 (integration tests for repositories) can also be done in parallel.
*   **Within Phase 5 (US3)**: T052-T055 (defining models for Auth & Tenant context) can be done in parallel. Similarly for repository implementations and service definitions.

## Implementation Strategy

我们将采用“逐个有界上下文”的渐进式策略。首先集中精力完成“合同管理上下文”的重构，确保其稳定性和功能的完整性。一旦第一个上下文稳定并经过充分测试，我们将利用所学经验继续重构“权限与租户上下文”，并最终处理“集成上下文”。每个阶段都将伴随着持续的测试和验证，以确保质量和兼容性。MVP 将聚焦于完成 Phase 1 到 Phase 3 的所有任务，确保核心合同管理领域的代码可维护性得到提升，并且功能保持稳定。
