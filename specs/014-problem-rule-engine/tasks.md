# Implementation Tasks: Problem Center and Rule Engine Design

**Feature Name**: Problem Center and Rule Engine
**Branch**: `014-problem-rule-engine`
**Date**: 2026-01-18

## Implementation Strategy

本功能将采用纵向切片（Vertical Slice）的方式进行开发。首先构建核心的规则引擎（Rule Engine）基础设施，确保能够动态执行逻辑并生成结果。接着实现异步评估任务（Evaluation Job）流程，通过 Kafka 实现解耦。最后构建“问题中心”（Problem Center）管理驾驶舱，包括 PDF 高亮定位和生命周期管理。MVP 阶段将优先支持正则（REGEX）和 Groovy 逻辑，实现端到端的风险识别到处理闭环。

## Phase 1: Setup - Infrastructure & Project Structure

**Goal**: 初始化项目结构，配置 Kafka 话题和 Groovy 引擎沙箱。
**Independent Test Criteria**: Kafka 话题创建成功，Groovy 沙箱能够成功拦截非法类调用。

- [X] T001 Create `rule` domain and infrastructure packages in `backend/src/main/java/com/contract/master/rule/`
- [X] T002 Create `problemcenter` domain and infrastructure packages in `backend/src/main/java/com/contract/master/problemcenter/`
- [X] T003 Configure Kafka topic `contract-evaluation` in `backend/src/main/resources/application.yml`
- [X] T004 Implement `GroovySandboxConfig` with `SecureASTCustomizer` in `backend/src/main/java/com/contract/master/rule/infrastructure/script/`
- [X] T005 Setup base JPA repositories for `Rule`, `Problem`, and `EvaluationJob` in their respective infrastructure packages.

## Phase 2: Foundational - Rule Engine & Evaluation Lifecycle

**Goal**: 实现规则的动态执行引擎和异步评估任务流程。
**Independent Test Criteria**: 给定一个合同文本和一条 Groovy 规则，系统能通过 Kafka 触发评估并生成 `Problem` 实体。

- [X] T006 Define `Rule` aggregate root and `RuleType` enum in `backend/src/main/java/com/contract/master/rule/domain/model/`
- [X] T007 Implement `RuleExecutor` interface and its `GroovyRuleExecutor` / `RegexRuleExecutor` implementations.
- [X] T008 Implement `EvaluationJob` aggregate root and state machine logic in `backend/src/main/java/com/contract/master/problemcenter/domain/model/`
- [X] T009 Create `EvaluationProducer` to publish job IDs to Kafka in `backend/src/main/java/com/contract/master/problemcenter/infrastructure/messaging/`
- [X] T010 Create `EvaluationConsumer` to process jobs, execute rules, and persist results in `backend/src/main/java/com/contract/master/problemcenter/infrastructure/messaging/`
- [X] T011 [US1] Implement `RuleService.execute(contractId)` to trigger the full async evaluation flow.

## Phase 3: User Story 1 - 规则驱动的自动化风险识别 (Priority: P1)

**Goal**: 实现规则的管理 API 和基本的风险识别生成能力。
**Independent Test Criteria**: 用户可以通过 API 创建规则，并触发评估任务，最终在数据库中看到生成的 `Problem` 条目。

- [X] T012 [P] [US1] Create `RuleController` for CRUD operations in `backend/src/main/java/com/contract/master/rule/interface/rest/`
- [X] T013 [US1] Implement rule versioning logic: increment version on `logic_content` update.
- [X] T014 [US1] Implement `ProblemGenerator` to map rule hits to `Problem` entities with `generated_message`.
- [ ] T015 [US1] Integration Test: End-to-end flow from `POST /api/evaluations` to `Problem` persistence.

## Phase 4: User Story 2 - 问题中心的集中管理与跟踪 (Priority: P2)

**Goal**: 构建问题中心后端查询 API 和状态管理逻辑。
**Independent Test Criteria**: 用户可以过滤特定合同下的问题，并修改其状态，操作记录在审计日志中。

- [X] T016 [P] [US2] Create `ProblemController` with advanced filtering (contractId, status, severity) in `backend/src/main/java/com/contract/master/problemcenter/interface/rest/`
- [X] T017 [US2] Implement `ProblemService.updateStatus(id, newStatus, notes)` with validation.
- [X] T018 [US2] Integrate `AuditService` to log every status transition of a `Problem`.
- [X] T019 [US2] [Frontend] Create `ProblemList.vue` with filtering and sorting using Element Plus.

## Phase 5: User Story 3 - 合同原文的精准定位与高亮 (Priority: P3)

**Goal**: 实现 PDF 定位数据的存储和前端展示。
**Independent Test Criteria**: 点击问题列表中的项，PDF 预览器自动跳转至对应坐标并展示高亮矩形。

- [ ] T020 [US3] Extend `RuleExecutor` to return `PageCoordinate` data (page, x, y, w, h).
- [X] T021 [US3] Implement JSONB mapping for `location_in_contract` in `Problem` entity.
- [ ] T022 [US3] [Frontend] Update `PdfViewer.vue` to support `drawHighlight(coordinates)` using SVG overlay.
- [ ] T023 [US3] [Frontend] Implement Resizable Split Panes layout in `ProblemCockpit.vue`.
- [ ] T024 [US3] Link `ProblemList` click events to `PdfViewer` position updates.

## Final Phase: Polish & Cross-Cutting Concerns

**Goal**: 完善性能、文档和多租户隔离。
**Independent Test Criteria**: 跨租户无法访问规则/问题，系统在 50+ 规则下运行稳定。

- [ ] T025 Implement `TenantInterceptor` for all rule and problem center repositories.
- [ ] T026 Update `table.md` with full schemas for `rules`, `problems`, `evaluation_jobs`.
- [ ] T027 Update `api.md` with new endpoints for rules and problem center.
- [ ] T028 Performance Test: Measure end-to-end latency for a 10-page contract with 50 rules.

## Dependency Graph

**Story Completion Order**:
Phase 1 (Setup) -> Phase 2 (Foundational) -> Phase 3 (US1) -> Phase 4 (US2) -> Phase 5 (US3) -> Final Phase

**Parallel Execution Examples**:
- T012 (RuleController) and T016 (ProblemController) can be implemented in parallel.
- Frontend UI development (T019) can start once Phase 2 API contracts are defined.

## Implementation Strategy

我们将严格执行“先基建、后业务”的策略。Phase 1 和 2 建立的异步执行框架是整个功能的脊梁。在实现 US1 时，我们将重点放在规则的执行引擎上，确保 Groovy 脚本的安全性和性能。PDF 高亮（US3）虽然是 P3 优先级，但它对前端组件的设计有前置要求，因此在 Phase 1 的数据模型设计中已经预留了 JSONB 定位字段。
