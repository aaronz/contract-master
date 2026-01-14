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
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Link, Delete, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('sfdc')
const saving = ref(false)

const sfdcConfig = ref({
  webhook: 'https://na1.salesforce.com/services/apexrest/contract_sync',
  secret: 'my-super-secret-key',
  mapping: [
    { local: 'contract_title', remote: 'Name' },
    { local: 'contract_amount', remote: 'Amount' },
    { local: 'start_date', remote: 'StartDate' },
    { local: 'customer_name', remote: 'Account.Name' }
  ]
})

const dingtalkConfig = ref({
  webhook: '',
  secret: ''
})

const addMapping = (type) => {
  if (type === 'sfdc') {
    sfdcConfig.value.mapping.push({ local: '', remote: '' })
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
</style>
