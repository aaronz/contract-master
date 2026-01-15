# UI Selectors: Contract Master Frontend

## Page Objects

### 1. LoginPage
- `input_username`: `input[name="username"]`
- `input_password`: `input[name="password"]`
- `input_tenant`: `input[name="tenantId"]`
- `btn_login`: `button.login-submit`

### 2. ContractListPage
- `search_input`: `input.contract-search`
- `contract_table`: `.el-table__body-wrapper`
- `first_view_btn`: `button.action-view:nth-child(1)`

### 3. ContractDetailPage
- `btn_ai_analysis`: `button:has-text("AI Analysis")`
- `progress_bar`: `.el-progress`
- `audit_history_tab`: `div.el-tabs__item:has-text("Audit History")`
- `field_project_code`: `input[name="custom_field_project_code"]`

### 4. SettingsPage
- `tab_fields`: `li:has-text("Field Management")`
- `btn_add_field`: `button:has-text("New Field")`
- `rule_toggle`: `.rule-item .el-switch`
