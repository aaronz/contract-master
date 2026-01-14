<template>
  <div class="integrations-hub">
    <div class="page-header">
      <div>
        <h1 class="page-title">Integrations Hub</h1>
        <p class="page-subtitle">Manage your CRM connections and data synchronization pipelines.</p>
      </div>
      <el-button type="primary" size="large" icon="Plus">New Connection</el-button>
    </div>

    <!-- Stats Row -->
    <div class="stats-grid">
      <div class="stat-card glass-card">
        <div class="stat-icon bg-blue-100 text-blue-600">
          <el-icon><Connection /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">12</div>
          <div class="stat-label">Active Connectors</div>
        </div>
      </div>
      <div class="stat-card glass-card">
        <div class="stat-icon bg-green-100 text-green-600">
          <el-icon><Check /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">98.5%</div>
          <div class="stat-label">Sync Success Rate</div>
        </div>
      </div>
      <div class="stat-card glass-card">
        <div class="stat-icon bg-purple-100 text-purple-600">
          <el-icon><DataAnalysis /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">45k</div>
          <div class="stat-label">Records Synced Today</div>
        </div>
      </div>
      <div class="stat-card glass-card">
        <div class="stat-icon bg-orange-100 text-orange-600">
          <el-icon><Warning /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">3</div>
          <div class="stat-label">Pending Issues</div>
        </div>
      </div>
    </div>

    <!-- Connectors Grid -->
    <div class="section-title">Active Connectors</div>
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
          <p>{{ connector.description }}</p>
          <div class="sync-info">
            <span class="label">Last Sync:</span>
            <span class="value">{{ connector.lastSync }}</span>
          </div>
        </div>
        <div class="connector-footer">
          <el-button link type="primary">Configure</el-button>
          <el-switch v-model="connector.active" size="small" />
        </div>
      </div>
      
      <!-- Add New Card -->
      <div class="connector-card glass-card dashed flex-center hover-scale">
        <div class="text-center">
          <el-button circle size="large" class="add-btn">
            <el-icon><Plus /></el-icon>
          </el-button>
          <p class="mt-4 text-secondary font-medium">Add Connector</p>
        </div>
      </div>
    </div>

    <!-- Recent Activity Table -->
    <div class="section-title mt-8">Recent Synchronization Activity</div>
    <div class="glass-card table-wrapper">
      <el-table :data="activities" style="width: 100%">
        <el-table-column prop="source" label="Source" width="180">
          <template #default="{ row }">
            <div class="flex items-center gap-2">
              <div class="mini-dot" :style="{ background: row.color }"></div>
              <span class="font-medium">{{ row.source }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="Event Type" width="180">
          <template #default="{ row }">
            <el-tag size="small" :type="row.type === 'Full Sync' ? 'primary' : 'success'" effect="plain" round>
              {{ row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="records" label="Records" />
        <el-table-column prop="duration" label="Duration" />
        <el-table-column prop="status" label="Status">
           <template #default="{ row }">
            <span :class="['status-text', row.status === 'Success' ? 'text-green' : 'text-red']">
              {{ row.status }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="Time" align="right" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Connection, Check, DataAnalysis, Warning, Plus } from '@element-plus/icons-vue'

const connectors = ref([
  { 
    id: 1, 
    name: 'Salesforce', 
    description: 'Main CRM for North America Sales Team.', 
    status: 'online', 
    lastSync: '2 mins ago', 
    active: true,
    color: '#00A1E0' 
  },
  { 
    id: 2, 
    name: 'HubSpot', 
    description: 'Marketing automation and lead tracking.', 
    status: 'online', 
    lastSync: '15 mins ago', 
    active: true,
    color: '#FF7A59' 
  },
  { 
    id: 3, 
    name: 'DingTalk', 
    description: 'Internal approval workflows sync.', 
    status: 'syncing', 
    lastSync: 'Syncing...', 
    active: true,
    color: '#0089FF' 
  },
  { 
    id: 4, 
    name: 'SAP ERP', 
    description: 'Financial data and invoice reconciliation.', 
    status: 'offline', 
    lastSync: '2 days ago', 
    active: false,
    color: '#0FAAFF' 
  },
])

const activities = ref([
  { source: 'Salesforce', color: '#00A1E0', type: 'Incremental', records: '124', duration: '1.2s', status: 'Success', time: '10:42 AM' },
  { source: 'HubSpot', color: '#FF7A59', type: 'Full Sync', records: '4,521', duration: '45s', status: 'Success', time: '10:30 AM' },
  { source: 'DingTalk', color: '#0089FF', type: 'Webhook', records: '1', duration: '0.4s', status: 'Success', time: '10:15 AM' },
  { source: 'SAP ERP', color: '#0FAAFF', type: 'Scheduled', records: '0', duration: '0s', status: 'Failed', time: '09:00 AM' },
  { source: 'Salesforce', color: '#00A1E0', type: 'Incremental', records: '45', duration: '0.8s', status: 'Success', time: '08:45 AM' },
])
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
