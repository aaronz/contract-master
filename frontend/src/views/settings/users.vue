<template>
  <div class="users-page p-6">
    <div class="page-header">
      <div>
        <h1 class="page-title">User Management</h1>
        <p class="page-subtitle">Assign roles and manage data access for organization members.</p>
      </div>
    </div>

    <div class="glass-card table-container" v-loading="loading">
      <el-table :data="users" style="width: 100%">
        <el-table-column prop="realName" :label="$t('common.user')" />
        <el-table-column prop="userName" :label="$t('login.username')" />
        <el-table-column prop="userId" label="User ID" width="200">
          <template #default="{ row }">
            <span class="font-mono text-xs">{{ row.userId }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.roles.admin')" min-width="250">
          <template #default="{ row }">
            <div class="flex flex-wrap gap-1">
              <el-tag 
                v-for="role in userRoles[row.userId]" 
                :key="role" 
                size="small" 
                effect="light"
              >
                {{ getRoleName(role) }}
              </el-tag>
              <span v-if="!userRoles[row.userId] || userRoles[row.userId].length === 0" class="text-gray-400 text-xs">{{ $t('common.noNotifications') }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.actions')" width="150" align="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEditRoles(row)">{{ $t('common.edit') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Role Assignment Dialog -->
    <el-dialog v-model="dialogVisible" :title="$t('menu.roleManagement')" width="450px">
      <div v-if="selectedUser" class="mb-4">
        <p class="text-sm text-gray-500">{{ $t('common.user') }}: <b>{{ selectedUser.realName }}</b> ({{ selectedUser.userName }})</p>
      </div>
      <el-form label-position="top">
        <el-form-item :label="$t('menu.roleManagement')">
          <el-select 
            v-model="selectedRoleIds" 
            multiple 
            collapse-tags 
            collapse-tags-indicator
            :placeholder="$t('common.selectPlaceholder')"
            style="width: 100%"
          >
            <el-option 
              v-for="role in allRoles" 
              :key="role.id" 
              :label="role.name" 
              :value="role.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveUserRoles" :loading="saving">Update Roles</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const users = ref([])
const allRoles = ref([])
const userRoles = reactive({})
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const selectedUser = ref(null)
const selectedRoleIds = ref([])

const fetchRoles = async () => {
  try {
    const res = await request.get('/roles')
    if (res.data && res.data.data) {
      allRoles.value = res.data.data.map(r => ({
        id: r.roleId,
        name: r.roleName
      }))
    }
  } catch (e) {}
}

const getRoleName = (roleId) => {
  const role = allRoles.value.find(r => r.id === roleId)
  return role ? role.name : roleId
}

const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await request.get('/users')
    if (res.data && res.data.data) {
      users.value = res.data.data
      // Fetch roles for each user
      for (const user of users.value) {
        const roleRes = await request.get(`/users/${user.userId}/roles`)
        if (roleRes.data && roleRes.data.data) {
          userRoles[user.userId] = roleRes.data.data
        }
      }
    }
  } catch (error) {
    console.error('Failed to fetch users', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchRoles()
  fetchUsers()
})

const handleEditRoles = (user) => {
  selectedUser.value = user
  selectedRoleIds.value = [...(userRoles[user.userId] || [])]
  dialogVisible.value = true
}

const saveUserRoles = async () => {
  if (!selectedUser.value) return
  saving.value = true
  try {
    await request.post(`/users/${selectedUser.value.userId}/roles`, selectedRoleIds.value)
    ElMessage.success('Roles assigned successfully')
    userRoles[selectedUser.value.userId] = [...selectedRoleIds.value]
    dialogVisible.value = false
  } catch (error) {
    console.error('Failed to save user roles', error)
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.page-header {
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
