<template>
  <div class="roles-page p-6">
    <div class="page-header">
      <div>
        <h1 class="page-title">Role Management</h1>
        <p class="page-subtitle">Define and manage user roles within your organization.</p>
      </div>
      <el-button type="primary" icon="Plus" @click="handleAdd">Add Role</el-button>
    </div>

    <div class="glass-card table-container" v-loading="loading">
      <el-table :data="roles" style="width: 100%">
        <el-table-column prop="roleName" :label="$t('common.name')" />
        <el-table-column prop="roleId" label="Role ID" width="250">
          <template #default="{ row }">
            <span class="font-mono text-xs">{{ row.roleId }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="roleType" :label="$t('contract.type')" width="150" />
        <el-table-column prop="status" :label="$t('common.status')" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? 'Active' : 'Disabled' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.actions')" width="150" align="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleDelete(row)" :disabled="row.roleId === 'admin'">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="$t('menu.roleManagement')" width="400px">
      <el-form :model="form" label-position="top">
        <el-form-item :label="$t('common.name')" required>
          <el-input v-model="form.roleName" :placeholder="$t('common.placeholder')" />
        </el-form-item>
        <el-form-item :label="$t('contract.type')">
          <el-select v-model="form.roleType" style="width: 100%">
            <el-option :label="$t('contract.enums.roleType.standard')" value="STANDARD" />
            <el-option :label="$t('contract.enums.roleType.system')" value="SYSTEM" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">{{ $t('common.save') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const roles = ref([])
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)

const form = ref({
  roleName: '',
  roleType: 'STANDARD'
})

const fetchRoles = async () => {
  loading.value = true
  try {
    const res = await request.get('/roles')
    roles.value = res.data.data || []
  } catch (error) {
    console.error('Failed to fetch roles', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchRoles()
})

const handleAdd = () => {
  form.value = { roleName: '', roleType: 'STANDARD' }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.value.roleName) return
  saving.value = true
  try {
    await request.post('/roles', form.value)
    ElMessage.success('Role created successfully')
    dialogVisible.value = false
    fetchRoles()
  } catch (error) {
    console.error('Failed to save role', error)
  } finally {
    saving.value = false
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`Are you sure you want to delete the role "${row.roleName}"? This will affect all users assigned to this role.`, 'Warning', {
    confirmButtonText: 'Delete',
    confirmButtonClass: 'el-button--danger',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/roles/${row.id}`)
      ElMessage.success('Role deleted')
      fetchRoles()
    } catch (e) {}
  })
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.page-title {
  font-size: 24px;
  font-weight: 700;
  margin: 0 0 4px 0;
}
.page-subtitle {
  color: #64748B;
  font-size: 14px;
  margin: 0;
}
.table-container {
  border-radius: 12px;
}
</style>
