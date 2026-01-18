# Feature Specification: Problem Center and Rule Engine Design

**Feature Branch**: `014-problem-rule-engine`  
**Created**: 2026-01-18  
**Status**: Draft  
**Input**: User description: "“问题中心” (Problem Center) 和“规则” (Rule) 之间的关系、进行详细设计，并评估当前实现与目标设计之间的差距..."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 规则驱动的自动化风险识别 (Priority: P1)

作为一名规则管理员，我希望能够定义不同类型的业务规则（如 GROOVY, REGEX, JSON_LOGIC），并将其应用到合同评估任务中，以便系统能够自动发现合同中的不合规项。

**Why this priority**: 这是整个风险审查流程的“生产者”，没有规则就没有问题。

**Independent Test**: 可以通过创建一个带有特定条件的规则（例如：“合同金额 > 100万”），上传一个触发该条件的合同，验证系统是否生成了一个对应的“问题”实体。

**Acceptance Scenarios**:

1. **Given** 规则库中存在一条“活跃”状态的 GROOVY 规则，**When** 启动针对某合同的评估任务，**Then** 该规则被正确执行并输出预期的“问题”描述。
2. **Given** 规则定义的严重等级为 HIGH，**When** 该规则命中，**Then** 生成的“问题”实体也必须继承该严重等级。

---

### User Story 2 - 问题中心的集中管理与跟踪 (Priority: P2)

作为一名法务人员，我希望在“问题中心”查看合同的评估报告，能够对每个发现的问题进行状态管理（如“已解决”、“接受风险”）、指派人员和添加备注，确保风险得到闭环处理。

**Why this priority**: 这是风险审查流程的“消费者”和“管理者”，确保识别出的风险被妥善处置。

**Independent Test**: 可以通过 API 或 UI 修改一个“NEW”状态问题的状态为“RESOLVED”，并验证审计日志和系统状态是否同步更新。

**Acceptance Scenarios**:

1. **Given** 合同评估产生了一系列问题，**When** 用户访问问题中心，**Then** 能够看到所有问题的概览，并能根据状态和严重程度进行筛选。
2. **Given** 一个处于“NEW”状态的问题，**When** 用户将其指派给特定处理人并添加处理建议，**Then** 问题的状态、指派人和备注信息被正确持久化。

---

### User Story 3 - 合同原文的精准定位与高亮 (Priority: P3)

作为一名业务审查员，我希望在查看问题详情时，能够通过点击问题自动定位到合同原文的对应位置并进行高亮展示，以便 I 快速核实问题的上下文。

**Why this priority**: 极大地提升用户体验和审查效率，是实现“驾驶舱”交互核心的关键。

**Independent Test**: 在 PDF 视图中点击一个带有位置信息的问题卡片，验证视图是否滚动到指定页码并高亮了对应的文本区域。

**Acceptance Scenarios**:

1. **Given** 一个包含 PDF 定位信息（页码、坐标）的问题，**When** 在问题中心点击该问题，**Then** PDF 渲染器应展示对应页面并视觉化高亮该位置。

---

### Edge Cases

- **规则逻辑冲突**: 多条规则命中同一段文字或逻辑冲突时，如何处理生成的多个问题？
- **超大合规报告**: 当规则极其复杂或合同极长导致生成成百上千个问题时，系统性能是否受限？
- **规则版本回溯**: 如果一个问题是由旧版本的规则生成的，用户能否查看生成该问题时的规则快照？

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: 系统 MUST 支持三种核心实体：`Rule`（规则）、`Problem`（问题）和 `EvaluationJob`（评估任务）。
- **FR-002**: `Rule` MUST 具备多逻辑类型支持（MUST include: GROOVY, REGEX）。
- **FR-003**: 规则执行过程 MUST 是异步的，通过 Kafka 等消息队列触发 `EvaluationService` 处理器。
- **FR-004**: `Problem` MUST 存储精准定位信息（JSONB 格式，包含页码、字符偏移或坐标）。
- **FR-005**: 系统 MUST 提供 `RuleService`, `EvaluationService` 和 `ProblemService` 三个核心后端服务。
- **FR-006**: 问题中心 MUST 支持状态生命周期管理（NEW -> ACKNOWLEDGED -> RESOLVED/IGNORED）。
- **FR-007**: 系统 MUST 具备规则版本控制能力，确保问题的产生背景可追溯。

### Key Entities *(include if feature involves data)*

- **Rule (规则)**: 定义风险识别逻辑。包含名称、描述、逻辑内容（logic）、逻辑类型、严重等级、分类、版本和状态。
- **EvaluationJob (评估任务)**: 一次合同评估的执行实例。记录合同 ID、状态（PENDING, RUNNING, COMPLETED, FAILED）、触发者和起止时间。
- **Problem (问题)**: 规则命中产生的风险项。关联评估任务、规则、合同，并包含定位信息（location_in_contract）、命中片段、生成消息、状态和处理信息。

## Constitution Check *(mandatory)*

- [x] **I. Tenant Isolation**: 所有实体（Rule, Problem, Job）必须包含 `tenant_id` 以确保逻辑隔离。
- [x] **II. AI-Manual Synergy**: 规则逻辑类型应明确标识其来源（如系统预置或 AI 生成）。
- [x] **III. Rule Governance**: 核心逻辑通过 `Rule` 实体集中治理。
- [x] **IV. Middleware Std**: 使用 Kafka 处理异步评估，使用 JSONB 存储灵活的定位数据。
- [x] **V. Auditability**: 问题处理的所有状态变更必须记录在 `AuditLog` 中。
- [x] **VI. E2E Testing**: 用户故事涵盖了从规则定义到问题闭环的完整流程。
- [x] **VII. Doc Sync**: 详细设计已包含数据模型和 API 定义。
- [x] **VIII. UX/Completeness**: 左右分栏布局和精准定位是提升审查体验的核心。
- [x] **IX. Manifest Sync**: 需要更新 `table.md` 以包含 rules, problems, evaluation_jobs 表结构。

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 规则执行的自动化率达到 100%（即所有定义的逻辑必须能在评估任务中被触发）。
- **SC-002**: 问题定位的准确率 MUST 达到 95% 以上（点击问题后定位到的原文内容与命中片段一致）。
- **SC-003**: 单次合同评估（包含 50+ 规则）的端到端耗时（从触发到生成问题）在中等负载下应在 5 秒内完成。
- **SC-004**: 用户在问题中心处理单个问题的平均点击次数应减少 30%（相比于手动查找定位）。
- **SC-005**: 系统重构后，100% 的规则执行异常必须被捕获并记录在 `EvaluationJob` 的错误详情中。
