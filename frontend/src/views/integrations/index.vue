<template>
  <div class="integrations-hub">
    <div class="page-header">
      <div>
        <h1 class="page-title">{{ $t('menu.integrationsHub') }}</h1>
        <p class="page-subtitle">{{ $t('dashboard.subtitle') }}</p>
      </div>
      <el-button type="primary" size="large" icon="Plus" @click="handleAddConnector">{{ $t('common.create') }}</el-button>
    </div>

    <!-- Connector Dialog -->
    <el-dialog v-model="showConnectorDialog" :title="isEdit ? $t('common.edit') : $t('common.create')" width="500px">
      <el-form :model="connectorForm" label-position="top">
        <el-form-item :label="$t('common.systemName')">
          <el-input v-model="connectorForm.systemName" placeholder="e.g. Salesforce Production" />
        </el-form-item>
        <el-form-item :label="$t('common.endpointUrl')">
          <el-input v-model="connectorForm.endpointUrl" placeholder="https://api.salesforce.com/..." />
        </el-form-item>
        <el-form-item :label="$t('common.authType')">
          <el-select v-model="connectorForm.authType" style="width: 100%">
            <el-option label="OAuth2" value="OAUTH2" />
            <el-option label="API Key" value="API_KEY" />
            <el-option label="Basic Auth" value="BASIC" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('common.accessKeyId')">
          <el-input v-model="connectorForm.accessKey" placeholder="Enter access key or client ID" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showConnectorDialog = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSaveConnector" :loading="saving">{{ $t('common.save') }}</el-button>
      </template>
    </el-dialog>

    <!-- Stats Row -->
    <div class="stats-grid health-metrics" v-loading="statsLoading">
      <div class="stat-card glass-card">
        <div class="stat-icon bg-blue-100 text-blue-600">
          <el-icon><Connection /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ hubStats.activeConnectors }}</div>
          <div class="stat-label">{{ $t('menu.connectors') }}</div>
        </div>
      </div>
      <div class="stat-card glass-card">
        <div class="stat-icon bg-green-100 text-green-600">
          <el-icon><Check /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ hubStats.syncSuccessRate }}</div>
          <div class="stat-label">{{ $t('common.success') }} Rate</div>
        </div>
      </div>
      <div class="stat-card glass-card">
        <div class="stat-icon bg-purple-100 text-purple-600">
          <el-icon><DataAnalysis /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ hubStats.recordsSyncedToday }}</div>
          <div class="stat-label">Records {{ $t('common.loading') }}</div>
        </div>
      </div>
      <div class="stat-card glass-card">
        <div class="stat-icon bg-orange-100 text-orange-600">
          <el-icon><Warning /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ hubStats.pendingIssues }}</div>
          <div class="stat-label">{{ $t('compliance.problems') }}</div>
        </div>
      </div>
    </div>

    <!-- Connectors Grid -->
    <div class="section-title">{{ $t('menu.connectors') }}</div>
    <div class="connectors-grid">
      <div v-for="connector in connectors" :key="connector.id" class="connector-card glass-card hover-scale">
        <div class="connector-header">
          <div class="connector-logo" :style="{ backgroundColor: connector.color }">
            {{ connector.name.substring(0,2) }}
          </div>
          <div class="connector-status" :class="connector.status">
            <span class="status-dot"></span>
            {{ connector.status }}
          </div>
        </div>
        <div class="connector-body">
          <h3>{{ connector.name }}</h3>
          <p>{{ connector.endpointUrl }}</p>
          <div class="sync-info">
            <span class="label">Last Sync:</span>
            <span class="value">{{ connector.lastSync }}</span>
          </div>
        </div>
        <div class="connector-footer">
          <el-button link type="primary" @click="handleEditConnector(connector)">{{ $t('common.edit') }}</el-button>
          <el-switch v-model="connector.active" size="small" />
        </div>
      </div>
      
      <!-- Add New Card -->
      <div class="connector-card glass-card dashed flex-center hover-scale" @click="handleAddConnector">
        <div class="text-center">
          <el-button circle size="large" class="add-btn">
            <el-icon><Plus /></el-icon>
          </el-button>
          <p class="mt-4 text-secondary font-medium">{{ $t('common.create') }}</p>
        </div>
      </div>
    </div>

    <!-- Recent Activity Table -->
    <div class="section-title mt-8">{{ $t('dashboard.recentActivity') }}</div>
    <div class="glass-card table-wrapper">
      <el-table :data="activities" style="width: 100%" v-loading="activitiesLoading">
        <el-table-column prop="sourceSystem" :label="$t('common.resource')" width="180">
          <template #default="{ row }">
            <div class="flex items-center gap-2">
              <div class="mini-dot" :style="{ background: '#3B82F6' }"></div>
              <span class="font-medium">{{ row.sourceSystem }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="eventType" :label="$t('common.action')" width="180">
          <template #default="{ row }">
            <el-tag size="small" :type="row.eventType === 'OUTBOUND_PUSH' ? 'primary' : 'success'" effect="plain" round>
              {{ row.eventType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recordsCount" label="Records" />
        <el-table-column prop="durationMs" label="Duration">
          <template #default="{ row }">
            {{ row.durationMs }}ms
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="$t('common.status')">
           <template #default="{ row }">
            <div class="flex items-center gap-2">
              <span :class="['status-text', row.status === 'SUCCESS' ? 'text-green' : 'text-red']">
                {{ row.status }}
              </span>
              <el-button v-if="row.status === 'FAILED'" size="small" circle class="retry-btn" @click="handleRetry(row)">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('common.time')" align="right">
          <template #default="{ row }">
            {{ row.createTime ? new Date(row.createTime).toLocaleTimeString() : '-' }}
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Connection, Check, DataAnalysis, Warning, Plus, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const connectors = ref([])
const activities = ref([])
const hubStats = ref({
  activeConnectors: 0,
  syncSuccessRate: '0%',
  recordsSyncedToday: 0,
  pendingIssues: 0
})

const statsLoading = ref(false)
const activitiesLoading = ref(false)
const showConnectorDialog = ref(false)
const isEdit = ref(false)
const saving = ref(false)

const connectorForm = ref({
  systemId: '',
  systemName: '',
  endpointUrl: '',
  authType: 'API_KEY',
  accessKey: '',
  isEnabled: true
})

const fetchHubStats = async () => {
  statsLoading.value = true
  try {
    const res = await request.get('/integration-hub/stats')
    if (res.data && res.data.data) {
      hubStats.value = res.data.data
    }
  } catch (e) {
    console.error('Failed to fetch hub stats', e)
  } finally {
    statsLoading.value = false
  }
}

const fetchActivities = async () => {
  activitiesLoading.value = true
  try {
    const res = await request.get('/integration-hub/activities')
    if (res.data && res.data.data) {
      activities.value = res.data.data
    }
  } catch (e) {
    console.error('Failed to fetch hub activities', e)
  } finally {
    activitiesLoading.value = false
  }
}

const handleAddConnector = () => {
  isEdit.value = false
  connectorForm.value = {
    systemId: '',
    systemName: '',
    endpointUrl: '',
    authType: 'API_KEY',
    accessKey: '',
    isEnabled: true
  }
  showConnectorDialog.value = true
}

const handleEditConnector = (connector) => {
  isEdit.value = true
  connectorForm.value = {
    systemId: connector.systemId,
    systemName: connector.name,
    endpointUrl: connector.endpointUrl,
    authType: connector.authType,
    accessKey: connector.accessKey,
    isEnabled: connector.active
  }
  showConnectorDialog.value = true
}

const handleSaveConnector = async () => {
  saving.value = true
  try {
    const url = isEdit.value 
      ? `/v1/settings/downstream/${connectorForm.value.systemId}`
      : '/v1/settings/downstream'
    
    let response;
    if (isEdit.value) {
      response = await request.put(url, connectorForm.value)
    } else {
      response = await request.post(url, connectorForm.value)
    }
    
    if (response.status === 200 || response.data.status === 200) {
      ElMessage.success(isEdit.value ? 'Connector updated' : 'Connector created')
      showConnectorDialog.value = false
      fetchConnectors()
      fetchHubStats()
    } else {
      ElMessage.error('Failed to save connector')
    }
  } catch (error) {
    console.error('Failed to save connector', error)
  } finally {
    saving.value = false
  }
}

const fetchConnectors = async () => {
  try {
    const res = await request.get('/v1/settings/downstream')
    if (res.data && res.data.data) {
      connectors.value = res.data.data.map(sys => ({
        id: sys.systemId,
        systemId: sys.systemId,
        name: sys.systemName,
        description: sys.endpointUrl,
        status: sys.isEnabled ? 'online' : 'offline',
        lastSync: 'N/A',
        active: sys.isEnabled,
        color: '#3B82F6',
        authType: sys.authType,
        accessKey: sys.accessKey,
        endpointUrl: sys.endpointUrl
      }))
    }
  } catch (error) {
    console.error('Failed to fetch connectors', error)
  }
}

onMounted(() => {
  fetchConnectors()
  fetchHubStats()
  fetchActivities()
})

const handleRetry = (row) => {
  ElMessage.success('Retry initiated for ' + row.sourceSystem)
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 40px;
}

.stat-card {
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1E293B;
  line-height: 1.2;
}

.stat-label {
  font-size: 12px;
  color: #64748B;
  font-weight: 500;
}

.bg-blue-100 { background: #EFF6FF; }
.text-blue-600 { color: #2563EB; }
.bg-green-100 { background: #F0FDF4; }
.text-green-600 { color: #16A34A; }
.bg-purple-100 { background: #FAF5FF; }
.text-purple-600 { color: #9333EA; }
.bg-orange-100 { background: #FFF7ED; }
.text-orange-600 { color: #EA580C; }

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #334155;
  margin-bottom: 16px;
}

.connectors-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.connector-card {
  padding: 24px;
  display: flex;
  flex-direction: column;
  height: 180px;
  position: relative;
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}

.connector-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}

.connector-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.5), transparent);
  transform: translateX(-100%);
  transition: transform 0.5s;
}

.connector-card:hover::before {
  transform: translateX(100%);
}

.connector-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.connector-logo {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 18px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.connector-status {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: 20px;
  text-transform: capitalize;
}

.connector-status.online {
  background: #F0FDF4;
  color: #16A34A;
}
.connector-status.offline {
  background: #FEF2F2;
  color: #EF4444;
}
.connector-status.syncing {
  background: #EFF6FF;
  color: #2563EB;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: currentColor;
}

.connector-status.syncing .status-dot {
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.5; transform: scale(1.5); }
  100% { opacity: 1; transform: scale(1); }
}

.connector-body h3 {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
}

.connector-body p {
  margin: 0;
  font-size: 13px;
  color: #64748B;
  line-height: 1.4;
  height: 36px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.sync-info {
  margin-top: 12px;
  font-size: 12px;
  display: flex;
  gap: 8px;
}

.sync-info .label { color: #94A3B8; }
.sync-info .value { color: #334155; font-weight: 500; }

.connector-footer {
  margin-top: auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid rgba(0,0,0,0.05);
}

.dashed {
  border: 2px dashed rgba(148, 163, 184, 0.4) !important;
  background: rgba(255, 255, 255, 0.3);
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-btn {
  width: 48px;
  height: 48px;
  font-size: 20px;
  background: white;
  color: #3B82F6;
  border: 1px solid #BFDBFE;
  box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.1);
  transition: all 0.3s;
}

.connector-card:hover .add-btn {
  background: #3B82F6;
  color: white;
  transform: scale(1.1) rotate(90deg);
}

.table-wrapper {
  padding: 0;
  overflow: hidden;
}

.mini-dot {
  width: 8px;
  height: 8px;
  border-radius: 2px;
}

.text-green { color: #16A34A; font-weight: 600; }
.text-red { color: #EF4444; font-weight: 600; }
</style>
