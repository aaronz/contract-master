<template>
  <div class="downstream-config">
    <div class="page-header">
      <h1 class="page-title">{{ $t('menu.connectors') }}</h1>
      <el-button type="primary" icon="Plus" @click="handleAdd">{{ $t('common.create') }}</el-button>
    </div>

    <el-table :data="systems" border style="width: 100%; margin-top: 20px">
      <el-table-column prop="systemName" :label="$t('common.systemName')" />
      <el-table-column prop="endpointUrl" :label="$t('common.endpointUrl')" />
      <el-table-column prop="authType" :label="$t('common.authType')" width="120" />
      <el-table-column :label="$t('common.status')" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isEnabled ? 'success' : 'info'">{{ row.isEnabled ? 'Active' : 'Disabled' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Health" width="100">
        <template #default="{ row }">
          <el-tag :type="getHealthStatusType(row.healthStatus)">{{ row.healthStatus || 'UNKNOWN' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Last Heartbeat" width="180">
        <template #default="{ row }">
          {{ row.lastHeartbeat ? new Date(row.lastHeartbeat).toLocaleString() : '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.actions')" width="150">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">{{ $t('common.edit') }}</el-button>
          <el-button link type="success" @click="testConnection(row)">Test</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="$t('menu.connectors')" width="500px">
      <el-form :model="form" label-position="top">
        <el-form-item :label="$t('common.systemName')">
          <el-input v-model="form.systemName" />
        </el-form-item>
        <el-form-item :label="$t('common.endpointUrl')">
          <el-input v-model="form.endpointUrl" />
        </el-form-item>
        <el-form-item :label="$t('common.authType')">
          <el-select v-model="form.authType" style="width: 100%">
            <el-option label="API Key" value="API_KEY" />
            <el-option label="OAuth2" value="OAUTH2" />
          </el-select>
        </el-form-item>
        
        <template v-if="form.authType === 'OAUTH2'">
          <el-form-item label="Client ID">
            <el-input v-model="form.clientId" placeholder="OAuth2 Client ID" />
          </el-form-item>
          <el-form-item label="Client Secret">
            <el-input v-model="form.clientSecret" type="password" show-password placeholder="OAuth2 Client Secret" />
          </el-form-item>
          <el-form-item label="Token URL">
            <el-input v-model="form.tokenUrl" placeholder="https://auth.example.com/oauth/token" />
          </el-form-item>
        </template>

        <el-form-item :label="$t('common.enabled')">
          <el-switch v-model="form.isEnabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSave">{{ $t('common.save') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const systems = ref([])
const dialogVisible = ref(false)
const form = ref({
  systemName: '',
  endpointUrl: '',
  authType: 'API_KEY',
  isEnabled: true,
  clientId: '',
  clientSecret: '',
  tokenUrl: ''
})

const getHealthStatusType = (status) => {
  switch (status) {
    case 'HEALTHY': return 'success'
    case 'DOWN': return 'danger'
    case 'DEGRADED': return 'warning'
    default: return 'info'
  }
}

const handleAdd = () => {
  form.value = { 
    systemName: '', 
    endpointUrl: '', 
    authType: 'API_KEY', 
    isEnabled: true,
    clientId: '',
    clientSecret: '',
    tokenUrl: ''
  }
  dialogVisible.value = true
}

const handleSave = () => {
  // In a real app, this would be an API call
  // axios.post('/api/v1/settings/downstream', form.value).then(...)
  const newSystem = { ...form.value, id: Date.now(), healthStatus: 'UNKNOWN', lastHeartbeat: null }
  systems.value.push(newSystem)
  ElMessage.success('Configuration saved')
  dialogVisible.value = false
}

const testConnection = async (row) => {
  try {
    ElMessage.info(`Testing connection to ${row.systemName}...`)
    
    // API Call
    await axios.post(`/api/v1/settings/downstream/${row.id}/test-connection`)
    
    // If successful (no error thrown)
    row.healthStatus = 'HEALTHY'
    row.lastHeartbeat = new Date().toISOString()
    ElMessage.success('Connection established successfully')
    
  } catch (error) {
    // If 404/500 or network error
    console.error(error)
    row.healthStatus = 'DOWN'
    ElMessage.error('Connection failed: ' + (error.response?.data?.message || error.message))
  }
}

// Initial dummy data
onMounted(() => {
  systems.value = [
    {
      id: 1,
      systemName: 'ERP Core',
      endpointUrl: 'https://erp.internal/api',
      authType: 'API_KEY',
      isEnabled: true,
      healthStatus: 'HEALTHY',
      lastHeartbeat: '2023-10-27T10:30:00Z'
    }
  ]
})
</script>
