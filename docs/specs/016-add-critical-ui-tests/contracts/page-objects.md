# Page Object Contracts

This defines the public interface for the Page Objects used in tests.

## LoginPO

- `navigate()`: Go to login page.
- `login(username, password, tenantId)`: Perform login, returns `DashboardPO`.
- `getErrorMessage()`: Returns visible error text.

## DashboardPO

- `navigateToContracts()`: Returns `ContractListPO`.
- `navigateToRisk()`: Returns `ProblemCenterPO`.
- `navigateToRules()`: Returns `RuleManagementPO`.

## ContractListPO

- `createContract()`: Click "New", returns `ContractDetailPO` (edit mode).
- `search(query)`: Filter list.
- `openContract(contractNo)`: Click first match, returns `ContractDetailPO`.
- `getRowCount()`: Returns number of visible contracts.

## ContractDetailPO

- `fillForm(contractData)`: Fill inputs based on map.
- `save()`: Click save, wait for success toast.
- `getFieldValue(fieldName)`: Read current value.
- `triggerAIAnalysis()`: Click AI button, wait for completion.

## ProblemCenterPO

- `getProblemCount()`: Returns number of issues.
- `resolveProblem(problemId)`: Click resolve, confirm dialog.

## RuleManagementPO

- `createRule(ruleData)`: Open modal, fill form, save.
- `getRuleList()`: Return list of rule names.
