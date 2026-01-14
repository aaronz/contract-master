<template>
  <div class="audit-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Audit Logs</h1>
        <p class="page-subtitle">Complete history of system changes and user activities.</p>
      </div>
      <div class="header-actions">
        <el-date-picker 
          v-model="dateRange" 
          type="daterange" 
          range-separator="To" 
          start-placeholder="Start date" 
          end-placeholder="End date" 
          size="default"
        />
        <el-button icon="Download">Export CSV</el-button>
      </div>
    </div>

    <div class="glass-card table-wrapper">
      <el-table :data="auditLogs" style="width: 100%" @row-click="showDiff">
        <el-table-column prop="timestamp" label="Time" width="180" />
        <el-table-column prop="user" label="User" width="180">
           <template #default="{ row }">
             <div class="flex items-center gap-2">
               <el-avatar size="small" :src="row.avatar">{{ row.user.charAt(0) }}</el-avatar>
               <span>{{ row.user }}</span>
             </div>
           </template>
        </el-table-column>
        <el-table-column prop="action" label="Action" width="150">
           <template #default="{ row }">
             <el-tag :type="getActionType(row.action)">{{ row.action }}</el-tag>
           </template>
        </el-table-column>
        <el-table-column prop="resource" label="Resource" />
        <el-table-column prop="summary" label="Summary" />
        <el-table-column width="80" align="center">
           <template #default>
             <el-icon class="text-gray-400"><ArrowRight /></el-icon>
           </template>
        </el-table-column>
      </el-table>
      <div class="pagination-bar">
        <el-pagination background layout="prev, pager, next" :total="100" />
      </div>
    </div>

    <!-- Diff Drawer -->
    <el-drawer v-model="drawerVisible" title="Change Detail" size="800px">
      <div v-if="selectedLog" class="diff-container">
        <div class="diff-header">
          <div class="diff-meta">
            <div class="meta-item">
              <label>User</label>
              <span>{{ selectedLog.user }}</span>
            </div>
            <div class="meta-item">
              <label>Time</label>
              <span>{{ selectedLog.timestamp }}</span>
            </div>
            <div class="meta-item">
              <label>Action</label>
              <span>{{ selectedLog.action }}</span>
            </div>
          </div>
        </div>

        <div class="diff-viewer">
          <div class="diff-pane old">
            <div class="pane-label bg-red-50 text-red-600">Old Value</div>
            <pre>{{ formatJSON(selectedLog.oldValue) }}</pre>
          </div>
          <div class="diff-pane new">
            <div class="pane-label bg-green-50 text-green-600">New Value</div>
            <pre>{{ formatJSON(selectedLog.newValue) }}</pre>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ArrowRight, Download } from '@element-plus/icons-vue'

const dateRange = ref([])
const drawerVisible = ref(false)
const selectedLog = ref(null)

const auditLogs = ref([
  { 
    id: 1, 
    timestamp: '2024-01-15 14:30:22', 
    user: 'Alice Admin', 
    avatar: '', 
    action: 'UPDATE', 
    resource: 'Contract #2024-001', 
    summary: 'Modified payment terms',
    oldValue: {
      "payment_terms": "Net 30",
      "amount": 50000,
      "status": "DRAFT"
    },
    newValue: {
      "payment_terms": "Net 60",
      "amount": 50000,
      "status": "REVIEW"
    }
  },
  { 
    id: 2, 
    timestamp: '2024-01-15 12:15:00', 
    user: 'Bob Legal', 
    avatar: '', 
    action: 'DELETE', 
    resource: 'Risk Rule #88', 
    summary: 'Deleted obsolete risk rule',
    oldValue: {
      "rule_id": "88",
      "name": "Old Vendor Check",
      "active": true
    },
    newValue: null
  },
  { 
    id: 3, 
    timestamp: '2024-01-14 09:00:11', 
    user: 'System', 
    avatar: '', 
    action: 'SYNC', 
    resource: 'Salesforce Connector', 
    summary: 'Auto-sync completed',
    oldValue: null,
    newValue: { "synced_count": 450, "status": "SUCCESS" }
  }
])

const getActionType = (action) => {
  const map = {
    'UPDATE': 'warning',
    'CREATE': 'success',
    'DELETE': 'danger',
    'SYNC': 'info'
  }
  return map[action] || 'info'
}

const showDiff = (row) => {
  selectedLog.value = row
  drawerVisible.value = true
}

const formatJSON = (obj) => {
  if (!obj) return 'null'
  return JSON.stringify(obj, null, 2)
}
</script>

<style scoped>
.audit-page {
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
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

.header-actions {
  display: flex;
  gap: 12px;
}

.table-wrapper {
  padding: 0;
  overflow: hidden;
}

.pagination-bar {
  padding: 16px;
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid rgba(241, 245, 249, 0.8);
}

.diff-meta {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  background: #F8FAFC;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 24px;
}

.meta-item label {
  display: block;
  font-size: 11px;
  color: #94A3B8;
  text-transform: uppercase;
  margin-bottom: 4px;
  font-weight: 600;
}

.meta-item span {
  font-weight: 500;
  color: #1E293B;
}

.diff-viewer {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  height: 500px;
}

.diff-pane {
  display: flex;
  flex-direction: column;
}

.diff-pane.old {
  background: #FFF5F5;
  border-right: 1px solid #E2E8F0;
}

.diff-pane.new {
  background: #F0FDF4;
}

.pane-label {
  padding: 8px 12px;
  font-size: 12px;
  font-weight: 600;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}

pre {
  margin: 0;
  padding: 16px;
  font-family: 'Fira Code', monospace;
  font-size: 13px;
  overflow: auto;
  flex: 1;
  color: #334155;
}

:deep(.el-table__row) {
  cursor: pointer;
}
</style>
