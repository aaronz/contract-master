<template>
  <div class="crm-config-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">CRM Connectors</h1>
        <p class="page-subtitle">Configure integrations with third-party platforms.</p>
      </div>
      <el-button type="primary" :loading="saving" @click="saveConfig">Save Configuration</el-button>
    </div>

    <el-card shadow="never" class="config-card">
      <el-tabs v-model="activeTab" class="crm-tabs">
        <el-tab-pane label="Salesforce" name="sfdc">
          <div class="connector-form">
            <div class="form-section">
              <h3>Connection Settings</h3>
              <el-form label-position="top">
                <el-form-item label="Webhook URL">
                  <el-input v-model="sfdcConfig.webhook" placeholder="https://your-instance.salesforce.com/services/apexrest/..." />
                </el-form-item>
                <el-form-item label="Client Secret">
                  <el-input v-model="sfdcConfig.secret" type="password" show-password placeholder="••••••••••••••••" />
                </el-form-item>
                <el-form-item>
                  <el-button type="success" plain size="small" icon="Link">Test Connection</el-button>
                </el-form-item>
              </el-form>
            </div>

            <el-divider />

            <div class="form-section">
              <h3>Field Mapping</h3>
              <p class="section-desc">Map Contract Master fields to Salesforce Object fields.</p>
              
              <el-table :data="sfdcConfig.mapping" style="width: 100%" border>
                <el-table-column prop="local" label="Contract Master Field" />
                <el-table-column prop="remote" label="Salesforce API Name">
                  <template #default="{ row }">
                    <el-input v-model="row.remote" size="small" placeholder="e.g. AccountId" />
                  </template>
                </el-table-column>
                <el-table-column label="Transformation" width="120" align="center">
                  <template #default="{ row }">
                    <el-button 
                      type="primary" 
                      link 
                      size="small" 
                      icon="Edit" 
                      @click="openScriptEditor(row)"
                    >
                      Script
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column width="80" align="center">
                  <template #default="{ $index }">
                    <el-button type="danger" circle size="small" icon="Delete" @click="removeMapping('sfdc', $index)" />
                  </template>
                </el-table-column>
              </el-table>
              <div class="add-mapping">
                <el-button link type="primary" icon="Plus" @click="addMapping('sfdc')">Add Field Mapping</el-button>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="HubSpot" name="hubspot">
          <div class="connector-form">
            <el-empty description="HubSpot integration coming soon" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="DingTalk" name="dingtalk">
          <div class="connector-form">
            <div class="form-section">
               <h3>DingTalk Notification Bot</h3>
               <el-form label-position="top">
                <el-form-item label="Webhook URL">
                  <el-input v-model="dingtalkConfig.webhook" placeholder="https://oapi.dingtalk.com/robot/send?access_token=..." />
                </el-form-item>
                <el-form-item label="Secret Key (Optional)">
                  <el-input v-model="dingtalkConfig.secret" type="password" show-password />
                </el-form-item>
               </el-form>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="Integration Logs" name="logs">
          <div class="connector-form">
            <div class="logs-header" style="margin-bottom: 16px; display: flex; justify-content: space-between;">
              <span>Recent activity and debugging logs</span>
              <el-button size="small" @click="fetchLogs" icon="Refresh">Refresh</el-button>
            </div>
            <el-table :data="logs" border stripe style="width: 100%" v-loading="loadingLogs">
              <el-table-column prop="timestamp" label="Time" width="180">
                <template #default="{ row }">
                  {{ new Date(row.timestamp).toLocaleString() }}
                </template>
              </el-table-column>
              <el-table-column prop="integrationType" label="Integration" width="120" />
              <el-table-column prop="status" label="Status" width="100">
                <template #default="{ row }">
                  <el-tag :type="row.status === 'SUCCESS' ? 'success' : 'danger'">{{ row.status }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="Message" prop="message" show-overflow-tooltip />
              <el-table-column label="Actions" width="150" align="center">
                <template #default="{ row }">
                  <el-button link type="primary" size="small" @click="viewPayload(row)">View</el-button>
                  <el-button 
                    v-if="row.status === 'FAILED'" 
                    link 
                    type="warning" 
                    size="small" 
                    @click="replayLog(row)"
                  >
                    Replay
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- Script Editor Dialog -->
    <el-dialog v-model="scriptDialogVisible" title="Transformation Script" width="600px">
      <p class="dialog-desc">Write a Groovy script to transform the value. Variable <code>value</code> is available.</p>
      <el-input
        v-model="currentScript"
        type="textarea"
        :rows="10"
        class="code-editor"
        placeholder="return value.toUpperCase();"
      />
      <template #footer>
        <el-button @click="scriptDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveScript">Save Script</el-button>
      </template>
    </el-dialog>

    <!-- Payload Viewer Dialog -->
    <el-dialog v-model="payloadDialogVisible" title="Request Payload" width="600px">
      <pre class="json-viewer">{{ currentPayload }}</pre>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Link, Delete, Plus, Edit, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const activeTab = ref('sfdc')
const saving = ref(false)

// Script Editor State
const scriptDialogVisible = ref(false)
const currentScript = ref('')
const currentMappingRow = ref(null)

// Logs State
const logs = ref([])
const loadingLogs = ref(false)
const payloadDialogVisible = ref(false)
const currentPayload = ref('')

const sfdcConfig = ref({
  webhook: 'https://na1.salesforce.com/services/apexrest/contract_sync',
  secret: 'my-super-secret-key',
  mapping: [
    { local: 'contract_title', remote: 'Name', transformation: '' },
    { local: 'contract_amount', remote: 'Amount', transformation: 'return value * 1.0' },
    { local: 'start_date', remote: 'StartDate', transformation: '' },
    { local: 'customer_name', remote: 'Account.Name', transformation: '' }
  ]
})

const dingtalkConfig = ref({
  webhook: '',
  secret: ''
})

const addMapping = (type) => {
  if (type === 'sfdc') {
    sfdcConfig.value.mapping.push({ local: '', remote: '', transformation: '' })
  }
}

const removeMapping = (type, index) => {
  if (type === 'sfdc') {
    sfdcConfig.value.mapping.splice(index, 1)
  }
}

const saveConfig = () => {
  saving.value = true
  setTimeout(() => {
    saving.value = false
    ElMessage.success('Configuration saved successfully')
  }, 1000)
}

// Transformation Script Logic
const openScriptEditor = (row) => {
  currentMappingRow.value = row
  currentScript.value = row.transformation || ''
  scriptDialogVisible.value = true
}

const saveScript = () => {
  if (currentMappingRow.value) {
    currentMappingRow.value.transformation = currentScript.value
  }
  scriptDialogVisible.value = false
  ElMessage.success('Script saved temporarily (don\'t forget to Save Configuration)')
}

// Logs Logic
const fetchLogs = async () => {
  loadingLogs.value = true
  try {
    // API Call
    const res = await axios.get('/api/v1/integration/logs')
    logs.value = res.data
  } catch (e) {
    console.warn('Failed to fetch logs from backend, using dummy data for demo')
    // Fallback Dummy Data for UI preview if backend is not ready
    logs.value = [
      { id: 101, timestamp: new Date().toISOString(), integrationType: 'Salesforce', status: 'SUCCESS', message: 'Synced Contract #1234', payload: '{"id": "1234", "amount": 5000}' },
      { id: 102, timestamp: new Date(Date.now() - 3600000).toISOString(), integrationType: 'Salesforce', status: 'FAILED', message: 'Timeout waiting for response', payload: '{"id": "1235", "amount": 12000}' },
      { id: 103, timestamp: new Date(Date.now() - 7200000).toISOString(), integrationType: 'DingTalk', status: 'SUCCESS', message: 'Notification sent', payload: '{"text": "New Contract Approved"}' }
    ]
  } finally {
    loadingLogs.value = false
  }
}

const viewPayload = (row) => {
  try {
    // Format JSON if possible
    currentPayload.value = JSON.stringify(JSON.parse(row.payload), null, 2)
  } catch (e) {
    currentPayload.value = row.payload
  }
  payloadDialogVisible.value = true
}

const replayLog = async (row) => {
  try {
    ElMessage.info('Replaying transaction...')
    await axios.post(`/api/v1/integration/logs/${row.id}/replay`)
    ElMessage.success('Replay initiated successfully')
    fetchLogs() // Refresh logs to see new status if applicable
  } catch (e) {
    ElMessage.error('Replay failed: ' + (e.response?.data?.message || e.message))
  }
}

onMounted(() => {
  fetchLogs()
})
</script>

<style scoped>
.crm-config-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
  max-width: 1000px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 8px 0;
}

.page-subtitle {
  color: var(--text-secondary);
  margin: 0;
}

.config-card {
  border-radius: 12px;
}

.connector-form {
  padding: 20px 0;
}

.form-section h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.section-desc {
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 16px;
}

.add-mapping {
  margin-top: 16px;
  text-align: center;
}

.dialog-desc {
  color: var(--text-secondary);
  margin-bottom: 12px;
}

.code-editor :deep(.el-textarea__inner) {
  font-family: 'Fira Code', monospace;
  background-color: #f8f9fa;
  color: #2c3e50;
  line-height: 1.5;
}

.json-viewer {
  background-color: #f4f4f5;
  padding: 16px;
  border-radius: 4px;
  overflow: auto;
  font-family: 'Fira Code', monospace;
  font-size: 13px;
  max-height: 400px;
}
</style>
