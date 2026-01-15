<template>
  <div class="downstream-config">
    <div class="page-header">
      <h1 class="page-title">Downstream Systems</h1>
      <el-button type="primary" icon="Plus" @click="handleAdd">Add System</el-button>
    </div>

    <el-table :data="systems" border style="width: 100%; margin-top: 20px">
      <el-table-column prop="systemName" label="System Name" />
      <el-table-column prop="endpointUrl" label="Endpoint URL" />
      <el-table-column prop="authType" label="Auth Type" width="120" />
      <el-table-column label="Status" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isEnabled ? 'success' : 'info'">{{ row.isEnabled ? 'Active' : 'Disabled' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Actions" width="150">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">Edit</el-button>
          <el-button link type="success" @click="testConnection(row)">Test</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="System Configuration" width="500px">
      <el-form :model="form" label-position="top">
        <el-form-item label="System Name">
          <el-input v-model="form.systemName" />
        </el-form-item>
        <el-form-item label="Endpoint URL">
          <el-input v-model="form.endpointUrl" />
        </el-form-item>
        <el-form-item label="Auth Type">
          <el-select v-model="form.authType" style="width: 100%">
            <el-option label="API Key" value="API_KEY" />
            <el-option label="OAuth2" value="OAUTH2" />
          </el-select>
        </el-form-item>
        <el-form-item label="Enabled">
          <el-switch v-model="form.isEnabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSave">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const systems = ref([])
const dialogVisible = ref(false)
const form = ref({
  systemName: '',
  endpointUrl: '',
  authType: 'API_KEY',
  isEnabled: true
})

const handleAdd = () => {
  form.value = { systemName: '', endpointUrl: '', authType: 'API_KEY', isEnabled: true }
  dialogVisible.value = true
}

const handleSave = () => {
  ElMessage.success('Configuration saved')
  dialogVisible.value = false
}

const testConnection = (row) => {
  ElMessage.info(`Testing connection to ${row.systemName}...`)
}
</script>
