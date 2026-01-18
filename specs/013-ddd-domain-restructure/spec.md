# Feature Specification: DDD Domain Restructure

**Feature Branch**: `013-ddd-domain-restructure`  
**Created**: 2026-01-18  
**Status**: Draft  
**Input**: User description: "结合领域驱动设计，重构后端服务代码。注意当前contract/master目录下有application、domain、dto、infrastructure、interface目录，同时也在每个domain里面也包含类似的目录结构，例如contract、dashboard、identity、notification等，我希望才用后一种方式，将标准目录结构建立在每个domain的目录中。"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 统一领域目录结构 (Priority: P1)

作为一名后端开发人员，我希望所有的业务模块（如 contract, dashboard, identity, notification 等）都采用统一的内部目录结构（application, domain, infrastructure, interface, dto），以便我能够快速定位和修改特定领域的代码。

**Why this priority**: 消除现有的混合结构冲突，建立清晰的 DDD 规范，是后续所有开发工作的基础。

**Independent Test**: 可以通过验证 `contract` 或 `identity` 模块的内部结构是否完全包含标准 DDD 层级且不依赖于外部顶层 layered 目录来测试。

**Acceptance Scenarios**:

1. **Given** 开发人员打开特定领域目录（如 `contract`），**When** 查看其子目录，**Then** 必须看到标准的 `application`, `domain`, `infrastructure`, `interface`, `dto` 结构。
2. **Given** 一个特定的业务逻辑（如合同审核），**When** 查找其相关代码，**Then** 所有的核心逻辑、仓储实现和接口定义都应在同一领域目录下找到。

---

### User Story 2 - 消除顶层 Layered 目录 (Priority: P2)

作为一名架构师，我希望消除项目根目录下的全局 `application`, `domain`, `dto`, `infrastructure`, `interface` 目录，将代码归位到各自的有界上下文（Bounded Context）中，以实现真正的领域驱动设计。

**Why this priority**: 彻底解决“代码冲突”和“职责不明”的问题，确保系统的可维护性。

**Independent Test**: 验证根目录下不再存在全局业务分层目录，所有业务逻辑均已迁移至领域模块中。

**Acceptance Scenarios**:

1. **Given** 重构完成后的工程结构，**When** 检查根目录，**Then** 只能看到领域模块目录（如 `contract`, `identity`）和真正的共享组件，而非业务分层。

---

### User Story 3 - 模块化新领域创建 (Priority: P3)

作为一名开发人员，当我需要增加一个新领域（如 `billing`）时，我能够按照现有的模式快速搭建标准的 DDD 结构。

**Why this priority**: 提高团队开发效率，确保新代码符合一致的架构规范。

**Independent Test**: 尝试创建一个新的空领域模块，并验证其是否符合约定的结构。

**Acceptance Scenarios**:

1. **Given** 统一的结构标准，**When** 创建新领域模块，**Then** 结构与已有模块保持 100% 一致，且能被系统正确扫描和加载。

---

### Edge Cases

- **共享逻辑迁移**: 位于顶层全局目录中被多个领域引用的“共享代码”如何处理？（应迁移至 `shared` 领域或 `common` 模块）
- **依赖循环**: 重构后，领域间的依赖关系是否变得清晰，是否存在循环依赖？
- **配置文件与扫包路径**: 重构后，Spring Boot 或相关框架的组件扫描、Mapper 扫描路径是否需要同步更新？

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: 每个领域模块（Contract, Dashboard, Identity, Notification 等）必须包含独立的 `domain`, `application`, `infrastructure`, `interface`, `dto` 子目录。
- **FR-002**: 核心业务逻辑（Aggregate Roots, Entities, Value Objects）必须位于 `[domain_name]/domain/` 目录下。
- **FR-003**: 仓储接口必须定义在 `[domain_name]/domain/`，而其具体实现（如 JPA, MyBatis）必须位于 `[domain_name]/infrastructure/`。
- **FR-004**: 外部 API 定义（Controllers）必须位于 `[domain_name]/interface/`。
- **FR-005**: 领域间通信必须遵循应用层协调或领域事件机制，严禁跨领域直接操作其他领域的持久化层。
- **FR-006**: 必须清理并移除 `contract/master` 下旧有的顶层 `application`, `domain`, `dto`, `infrastructure`, `interface` 目录。
- **FR-007**: 系统的外部功能表现（API 契约、业务逻辑行为）必须与重构前保持 100% 一致。

### Key Entities *(include if feature involves data)*

- **Bounded Context (有界上下文)**: 代表一个独立的领域模块，如 `Contract` 或 `Identity`。
- **Standard DDD Layers (标准分层)**: 每个上下文内部的五层结构。
- **Shared Domain (共享领域)**: 存放被多个上下文引用的通用值对象或基础类。

## Constitution Check *(mandatory)*

- [x] **I. Tenant Isolation**: 重构后，租户隔离逻辑应在各领域的 `infrastructure` 或全局 Filter 中统一维护。
- [x] **II. AI-Manual Synergy**: 目录结构的清晰有助于 AI 辅助编程时准确识别上下文。
- [x] **III. Rule Governance**: 业务规则集中在各领域的 `domain` 层。
- [x] **IV. Middleware Std**: 基础设施层解耦中间件依赖。
- [x] **V. Auditability**: 统一结构便于在 `application` 层插入审计切面。
- [x] **VI. E2E Testing**: 结构调整不应影响现有的 E2E 测试。
- [x] **VII. Doc Sync**: 更新架构说明文档。
- [x] **VIII. UX/Completeness**: 提升开发者的代码阅读和维护体验。
- [x] **IX. Manifest Sync**: 检查包名变更对 API 映射的影响。

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 完成 100% 的领域模块重构，使其内部结构符合新标准。
- **SC-002**: 项目根目录下的全局业务分层目录数量减少为 0。
- **SC-003**: 现有自动化回归测试通过率达到 100%。
- **SC-004**: 新开发人员寻找特定领域逻辑的路径深度保持一致，定位时间减少（主观评估）。
- **SC-005**: 编译耗时和启动耗时波动范围控制在 ±5% 以内。
