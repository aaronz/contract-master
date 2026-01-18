# Data Model: Backend DDD Refactor

## 1. 有界上下文 (Bounded Contexts)

### 1.1 合同管理上下文 (Contract Management Context)
*   **职责**: 核心业务领域，负责合同的生命周期管理、合同要素的提取与补充、合同规则的评估与预警。
*   **核心领域对象**: Contract (合同), Rule (规则)。

### 1.2 权限与租户上下文 (Auth & Tenant Context)
*   **职责**: 负责用户认证、授权、角色管理以及租户信息的管理与隔离。
*   **核心领域对象**: Tenant (租户), User (用户), Role (角色), Permission (权限)。

### 1.3 集成上下文 (Integration Context)
*   **职责**: 负责与外部系统（如CRM系统、财务系统、OA系统）进行数据同步和集成。
*   **核心领域对象**: IntegrationEvent (集成事件), ExternalSystemConfig (外部系统配置)。

---

## 2. 领域模型 (Domain Model) 示例

以下是**合同管理上下文**的核心领域模型示例。其他上下文的领域模型也将遵循类似原则进行设计和重构。

### 2.1 聚合根 (Aggregate Root)

#### **Contract (合同)**

*   **描述**: 代表一份完整的合同，是合同管理上下文的核心聚合根。所有对合同（包括其内部的附件、扩展字段数据）的修改和一致性保证都应通过 `Contract` 聚合根进行。
*   **唯一标识**: `ContractId` (值对象，封装UUID)。
*   **核心属性**:
    *   `tenantId`: 值对象，租户唯一标识，确保租户隔离。
    *   `contractNo`: 值对象，合同业务编号，确保业务层面的唯一性。
    *   `contractName`: String，合同全称。
    *   `status`: 枚举 `ContractStatus`，合同生命周期状态。
    *   `amount`: 值对象 `Money`，合同金额信息。
    *   `signDate`: LocalDate，合同签订日期。
    *   `effectiveDate`: LocalDate，合同生效日期。
    *   `expireDate`: LocalDate，合同到期日期。
    *   `partyA`: 值对象 `PartyInfo`，甲方信息。
    *   `partyB`: 值对象 `PartyInfo`，乙方信息。
    *   `mainAttachment`: 引用值对象 `AttachmentInfo`，主附件信息。
    *   `changeCount`: Integer，合同变更次数。
*   **包含对象**:
    *   `Collection<AttachmentInfo>` attachments: 作为值对象集合，代表合同的附件列表。
    *   `Collection<ExtendFieldData>` extendData: 作为值对象集合，代表合同的扩展字段数据。
*   **业务行为**:
    *   `sign(date)`: 签订合同。
    *   `activate(date)`: 生效合同。
    *   `terminate(reason)`: 终止合同。
    *   `addAttachment(attachmentInfo)`: 添加附件。
    *   `updateExtendField(fieldId, value, fillType, user)`: 更新扩展字段数据。
*   **业务规则**: 合同状态流转规则，金额校验等，这些规则将在 `Contract` 聚合根内部或通过领域服务实现。

### 2.2 实体 (Entities)

#### **Rule (规则)**

*   **描述**: 定义了合同评估、校验的业务规则。作为独立的实体，可独立于 `Contract` 进行管理和配置。
*   **唯一标识**: `RuleId` (值对象，封装UUID)。
*   **核心属性**:
    *   `tenantId`: 值对象，租户唯一标识。
    *   `ruleName`: String，规则名称。
    *   `ruleContent`: String，规则定义（例如，JSON格式的规则条件或Drools DRL）。
    *   `severity`: 枚举 `RuleSeverity`，规则触发时的严重级别。
    *   `isEnabled`: Boolean，规则是否启用。
    *   `triggerConditions`: 值对象 `TriggerCondition`，规则触发时机。
*   **关系**: 可被 `Contract` 评估时引用，但并非 `Contract` 聚合的一部分。

### 2.3 值对象 (Value Objects)

#### **ContractId**
*   **描述**: 合同的唯一标识符。
*   **属性**: `value` (UUID)。

#### **TenantId**
*   **描述**: 租户的唯一标识符。
*   **属性**: `value` (UUID)。

#### **ContractNo**
*   **描述**: 合同的业务编号，可能包含特定格式。
*   **属性**: `value` (String)。

#### **Money (金额)**
*   **描述**: 表示货币金额，包含数值和币种。
*   **属性**: `amount` (BigDecimal), `currency` (String, 枚举如CNY/USD)。
*   **行为**: 加、减、乘、除等货币运算，并处理精度问题。

#### **PartyInfo (合同方信息)**
*   **描述**: 合同甲方或乙方的详细信息。
*   **属性**: `partyId` (String), `partyName` (String), `contactPerson` (String), `contactPhone` (String), `address` (String)。

#### **AttachmentInfo (附件信息)**
*   **描述**: 合同附件的元数据。
*   **属性**: `attachmentId` (UUID), `fileName` (String), `fileType` (String), `storagePath` (String), `isMain` (boolean)。

#### **ExtendFieldData (扩展字段数据)**
*   **描述**: 合同自定义扩展字段的数据。
*   **属性**: `fieldId` (UUID), `fieldCode` (String), `value` (String/Object，根据字段类型存储实际值), `fillType` (枚举 `FillType`: AI/Manual), `fillUser` (String), `fillTime` (LocalDateTime)。

#### **TriggerCondition (触发条件)**
*   **描述**: 规则触发的时机。
*   **属性**: `type` (枚举), `config` (String, JSON格式的详细配置)。

### 2.4 仓储 (Repositories) (接口定义)

仓储接口将定义数据访问契约，位于领域层，其实现位于基础设施层。

*   `ContractRepository`:
    *   `findById(ContractId id)`: 根据ID查找合同聚合根。
    *   `save(Contract contract)`: 保存合同聚合根。
    *   `findByTenantIdAndContractNo(TenantId tenantId, ContractNo contractNo)`: 根据租户ID和合同编号查找合同。

*   `RuleRepository`:
    *   `findById(RuleId id)`: 根据ID查找规则实体。
    *   `save(Rule rule)`: 保存规则实体。
    *   `findByTenantId(TenantId tenantId)`: 查找指定租户的所有规则。

*   `TenantRepository`:
    *   `findById(TenantId id)`: 根据ID查找租户聚合根。
    *   `save(Tenant tenant)`: 保存租户聚合根。

---
## 3. 领域服务 (Domain Services)

用于封装跨多个领域对象或聚合的业务逻辑，这些逻辑不适合放在任何一个领域对象内部。

*   `ContractEvaluationService`: 协调 `Contract` 和 `Rule` 来评估合同。
*   `ContractLifecycleService`: 协调 `Contract` 的复杂生命周期转换。

---
## 4. 应用服务 (Application Services)

位于应用层，协调领域对象完成特定的用例。

*   `ContractApplicationService`: 处理合同的创建、修改、查询、审批等用例。
*   `RuleConfigurationService`: 处理规则的配置、启用、禁用等用例。
