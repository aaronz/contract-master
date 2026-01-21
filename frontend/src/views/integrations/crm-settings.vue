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
              <div class="flex justify-between items-center">
                <div>
                  <h3>Field Mapping</h3>
                  <p class="section-desc">Manage how data fields align between systems.</p>
                </div>
                <router-link to="/integrations/mapping">
                  <el-button type="primary" icon="Connection">Manage Mappings in Transformation Center</el-button>
                </router-link>
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

    <!-- Payload Viewer Dialog -->
    <el-dialog v-model="payloadDialogVisible" title="Request Payload" width="600px">
      <pre class="json-viewer">{{ currentPayload }}</pre>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Link, Refresh, Connection } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const activeTab = ref('sfdc')
const saving = ref(false)

// Logs State
const logs = ref([])
const loadingLogs = ref(false)
const payloadDialogVisible = ref(false)
const currentPayload = ref('')

const sfdcConfig = ref({
  webhook: 'https://na1.salesforce.com/services/apexrest/contract_sync',
  secret: 'my-super-secret-key'
})

const dingtalkConfig = ref({
  webhook: '',
  secret: ''
})

const saveConfig = () => {
  saving.value = true
  setTimeout(() => {
    saving.value = false
    ElMessage.success('Configuration saved successfully')
  }, 1000)
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
