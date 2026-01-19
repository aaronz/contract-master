<template>
  <div class="role-matrix-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">{{ $t('menu.permissionMatrix') }}</h1>
        <p class="page-subtitle">Configure granular access controls and data scopes for each role.</p>
      </div>
      <div class="header-actions">
        <el-button icon="Refresh">Reset</el-button>
        <el-button type="primary" :loading="saving" icon="Check" @click="savePermissions">{{ $t('common.save') }}</el-button>
      </div>
    </div>

    <div class="glass-card table-container">
      <el-table 
        :data="modules" 
        style="width: 100%" 
        row-key="id" 
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        class="matrix-table"
      >
        <el-table-column prop="name" :label="$t('common.moduleAction')" width="300" fixed>
           <template #default="{ row }">
             <div class="module-cell">
               <el-icon v-if="row.icon" class="mr-2"><component :is="row.icon" /></el-icon>
               <span :class="{ 'font-bold': row.children, 'text-secondary': !row.children }">{{ $t(row.name) }}</span>
             </div>
           </template>
        </el-table-column>

        <el-table-column v-for="role in roles" :key="role.id" :label="$t('common.roles.' + role.id)" width="180" align="center">
          <template #default="{ row }">
            <div v-if="!row.children" class="permission-cell">
              <!-- Access Checkbox -->
              <el-checkbox 
                v-model="permissionMap[role.id][row.id].enabled" 
                :disabled="role.id === 'admin'"
                @change="(val) => handleCheckChange(val, role.id, row.id)"
              />
              
              <!-- Data Scope Trigger -->
              <el-popover
                v-if="permissionMap[role.id][row.id].enabled"
                placement="bottom"
                title="Data Scope"
                :width="250"
                trigger="click"
                popper-class="scope-popover"
              >
                <template #reference>
                  <div class="scope-badge" :class="getScopeClass(permissionMap[role.id][row.id].scope)">
                    {{ getScopeLabel(permissionMap[role.id][row.id].scope) }}
                  </div>
                </template>
                
                <!-- Popover Content -->
                <div class="scope-selector">
                  <div class="scope-option" @click="setScope(role.id, row.id, 'all')">
                    <div class="dot all"></div> All Data
                  </div>
                  <div class="scope-option" @click="setScope(role.id, row.id, 'dept')">
                    <div class="dot dept"></div> Department Only
                  </div>
                  <div class="scope-option" @click="setScope(role.id, row.id, 'self')">
                    <div class="dot self"></div> Self Only
                  </div>
                  <div class="scope-divider"></div>
                  <div class="scope-option" @click="setScope(role.id, row.id, 'custom')">
                    <div class="dot custom"></div> Custom Region...
                  </div>
                </div>
              </el-popover>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Document, Connection, Warning, Operation, Setting, Refresh, Check 
} from '@element-plus/icons-vue'

const saving = ref(false)

const roles = [
  { id: 'admin', name: 'System Admin' },
  { id: 'legal_mgr', name: 'Legal Manager' },
  { id: 'sales_lead', name: 'Sales Director' },
  { id: 'sales', name: 'Sales Rep' },
  { id: 'finance', name: 'Finance Audit' }
]

const modules = [
  {
    id: 'contract',
    name: 'contract.parties',
    icon: 'Document',
    children: [
      { id: 'contract_view', name: 'common.view' },
      { id: 'contract_create', name: 'common.create' },
      { id: 'contract_edit', name: 'common.edit' },
      { id: 'contract_delete', name: 'common.delete' },
      { id: 'contract_export', name: 'common.export' }
    ]
  },
  {
    id: 'integrations',
    name: 'common.integrations',
    icon: 'Connection',
    children: [
      { id: 'integration_view', name: 'menu.hubOverview' },
      { id: 'integration_config', name: 'menu.connectors' },
      { id: 'webhook_mgmt', name: 'menu.webhooks' }
    ]
  },
  {
    id: 'compliance',
    name: 'common.compliance',
    icon: 'Warning',
    children: [
      { id: 'risk_view', name: 'menu.problemCenter' },
      { id: 'risk_approve', name: 'common.confirm' },
      { id: 'audit_view', name: 'menu.auditLogs' }
    ]
  },
  {
    id: 'rules',
    name: 'menu.ruleEngine',
    icon: 'Operation',
    children: [
      { id: 'rule_view', name: 'common.view' },
      { id: 'rule_edit', name: 'common.edit' }
    ]
  }
]

// Structure: { roleId: { moduleId: { enabled: boolean, scope: 'all'|'dept'|'self'|'custom' } } }
const permissionMap = reactive({})

// Initialize Data
roles.forEach(role => {
  permissionMap[role.id] = {}
  modules.forEach(mod => {
    if (mod.children) {
      mod.children.forEach(child => {
        let enabled = false
        let scope = 'self'

        if (role.id === 'admin') {
          enabled = true
          scope = 'all'
        } else if (role.id === 'legal_mgr') {
           enabled = true
           scope = 'all'
           if (child.id.includes('integration')) enabled = false
        } else if (role.id === 'sales_lead') {
           if (child.id.includes('contract')) {
             enabled = true
             scope = 'dept'
           }
        } else if (role.id === 'sales') {
           if (child.id === 'contract_view' || child.id === 'contract_create') {
             enabled = true
             scope = 'self'
           }
        }
        
        permissionMap[role.id][child.id] = { enabled, scope }
      })
    }
  })
})

const handleCheckChange = (val, roleId, modId) => {
  if (val) {
    // Default scope when enabling
    permissionMap[roleId][modId].scope = 'self'
  }
}

const setScope = (roleId, modId, scope) => {
  permissionMap[roleId][modId].scope = scope
}

const getScopeClass = (scope) => {
  const map = {
    'all': 'scope-all',
    'dept': 'scope-dept',
    'self': 'scope-self',
    'custom': 'scope-custom'
  }
  return map[scope]
}

const getScopeLabel = (scope) => {
   const map = {
    'all': 'Global',
    'dept': 'Dept',
    'self': 'Self',
    'custom': 'Custom'
  }
  return map[scope]
}

const savePermissions = () => {
  saving.value = true
  setTimeout(() => {
    saving.value = false
    ElMessage.success({
      message: 'Permissions and data scopes updated successfully',
      type: 'success',
      plain: true,
    })
  }, 1000)
}
</script>

<style scoped>
.role-matrix-page {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #1E293B;
  margin: 0 0 8px 0;
  letter-spacing: -0.5px;
}

.page-subtitle {
  color: #64748B;
  margin: 0;
  font-size: 14px;
}

.table-container {
  overflow: hidden;
  border-radius: 16px;
}

.module-cell {
  display: flex;
  align-items: center;
}

.font-bold {
  font-weight: 700;
  color: #1E293B;
  font-size: 14px;
}

.text-secondary {
  color: #64748B;
  font-size: 13px;
  padding-left: 8px;
}

.permission-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  height: 28px;
}

/* Scope Badge Styles */
.scope-badge {
  font-size: 10px;
  padding: 2px 8px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
  text-transform: uppercase;
  transition: all 0.2s;
  border: 1px solid transparent;
}

.scope-badge:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.scope-all { background: #ECFDF5; color: #059669; border-color: #A7F3D0; }
.scope-dept { background: #EFF6FF; color: #2563EB; border-color: #BFDBFE; }
.scope-self { background: #F1F5F9; color: #64748B; border-color: #E2E8F0; }
.scope-custom { background: #FFF7ED; color: #EA580C; border-color: #FED7AA; }

/* Popover Content */
.scope-selector {
  padding: 4px 0;
}

.scope-option {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  cursor: pointer;
  font-size: 13px;
  border-radius: 6px;
  transition: background 0.2s;
  color: #334155;
}

.scope-option:hover {
  background: #F8FAFC;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 10px;
}

.dot.all { background: #10B981; }
.dot.dept { background: #3B82F6; }
.dot.self { background: #94A3B8; }
.dot.custom { background: #F97316; }

.scope-divider {
  height: 1px;
  background: #F1F5F9;
  margin: 4px 0;
}

:deep(.el-table) {
  --el-table-header-bg-color: rgba(248, 250, 252, 0.5);
}

:deep(.el-checkbox__inner) {
  border-radius: 4px;
}
</style>
