<template>
  <div class="role-matrix-page p-6">
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

    <div class="glass-card table-container" v-loading="loading">
      <el-table 
        :data="modules" 
        style="width: 100%" 
        row-key="id" 
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        class="matrix-table"
        height="100%"
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
            <div v-if="!row.children" class="cell-action">
              <el-checkbox 
                v-model="permissionMap[role.id][row.id].enabled" 
                @change="(val) => handleCheckChange(val, role.id, row.id)"
              />
              
              <!-- Data Scope Trigger -->
              <el-popover
                v-if="permissionMap[role.id][row.id].enabled"
                placement="bottom"
                :title="$t('settings.dataScope')"
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
                    <div class="dot all"></div> {{ $t('settings.scopes.all') }}
                  </div>
                  <div class="scope-option" @click="setScope(role.id, row.id, 'dept')">
                    <div class="dot dept"></div> {{ $t('settings.scopes.dept') }}
                  </div>
                  <div class="scope-option" @click="setScope(role.id, row.id, 'self')">
                    <div class="dot self"></div> {{ $t('settings.scopes.self') }}
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
import { ref, reactive, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import { 
  Document, Connection, Warning, Operation, Setting, Refresh, Check 
} from '@element-plus/icons-vue'
import request from '@/utils/request'

const { t } = useI18n()
const saving = ref(false)
const loading = ref(false)

const roles = ref([])

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

const fetchRoles = async () => {
  try {
    const res = await request.get('/roles')
    if (res.data && res.data.data) {
      roles.value = res.data.data.map(r => ({
        id: r.roleId,
        name: r.roleName
      }))
      
      // Initialize map for fetched roles
      roles.value.forEach(role => {
        if (!permissionMap[role.id]) {
          permissionMap[role.id] = {}
          modules.forEach(mod => {
            if (mod.children) {
              mod.children.forEach(child => {
                permissionMap[role.id][child.id] = { enabled: false, scope: 'self' }
              })
            }
          })
        }
      })
    }
  } catch (e) {
    console.error('Failed to fetch roles', e)
  }
}

const fetchPermissions = async () => {
  loading.value = true
  try {
    await fetchRoles()
    const res = await request.get('/permissions/matrix')
    if (res.data && res.data.data) {
      const backendMatrix = res.data.data
      // Merge with map
      Object.keys(backendMatrix).forEach(roleId => {
        if (permissionMap[roleId]) {
          Object.keys(backendMatrix[roleId]).forEach(modId => {
            if (permissionMap[roleId][modId]) {
              permissionMap[roleId][modId] = backendMatrix[roleId][modId]
            }
          })
        }
      })
    }
  } catch (error) {
    console.error('Failed to fetch permissions', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchPermissions()
})

const handleCheckChange = (val, roleId, modId) => {
  if (val) {
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
    'all': t('settings.scopes.all'),
    'dept': t('settings.scopes.dept'),
    'self': t('settings.scopes.self'),
    'custom': 'Custom'
  }
  return map[scope]
}

const savePermissions = async () => {
  saving.value = true
  try {
    await request.post('/permissions/matrix', permissionMap)
    ElMessage.success({
      message: 'Permissions and data scopes updated successfully',
      type: 'success',
      plain: true,
    })
  } catch (error) {
    console.error('Failed to save permissions', error)
    ElMessage.error('Failed to save permissions')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.role-matrix-page {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 120px);
}

.page-header {
  flex-shrink: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.table-container {
  flex: 1;
  overflow: hidden;
  border-radius: 16px;
  display: flex;
  flex-direction: column;
}

.matrix-table {
  flex: 1;
}

.module-cell {
  display: flex;
  align-items: center;
}

.text-secondary {
  color: #64748B;
  font-size: 13px;
}

.cell-action {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.scope-badge {
  font-size: 10px;
  font-weight: 700;
  padding: 2px 8px;
  border-radius: 10px;
  cursor: pointer;
  text-transform: uppercase;
  transition: all 0.2s;
}

.scope-all { background: #DBEAFE; color: #2563EB; }
.scope-dept { background: #FEF3C7; color: #D97706; }
.scope-self { background: #F1F5F9; color: #475569; }

.scope-selector {
  padding: 8px 0;
}

.scope-option {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  cursor: pointer;
  transition: background 0.2s;
  font-size: 13px;
  font-weight: 500;
}

.scope-option:hover {
  background: #F8FAFC;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.dot.all { background: #2563EB; }
.dot.dept { background: #D97706; }
.dot.self { background: #475569; }

.glass-card {
  background: white;
  border: 1px solid #E2E8F0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}
</style>
