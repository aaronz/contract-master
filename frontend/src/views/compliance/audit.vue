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
      <el-table v-loading="loading" :data="auditLogs" style="width: 100%" @row-click="showDiff">
        <el-table-column prop="createTime" label="Time" width="180" />
        <el-table-column prop="modifyUser" label="User" width="180">
           <template #default="{ row }">
             <div class="flex items-center gap-2">
               <el-avatar size="small">{{ row.modifyUser.charAt(0) }}</el-avatar>
               <span>{{ row.modifyUser }}</span>
             </div>
           </template>
        </el-table-column>
        <el-table-column prop="modifyType" label="Action" width="150">
           <template #default="{ row }">
             <el-tag :type="getActionType(row.modifyType)">{{ row.modifyType }}</el-tag>
           </template>
        </el-table-column>
        <el-table-column prop="contractId" label="Resource" />
        <el-table-column prop="fieldName" label="Field" width="150" />
        <el-table-column prop="summary" label="Summary">
          <template #default="{ row }">
            {{ row.fieldName }}: {{ row.modifyType }}
          </template>
        </el-table-column>
        <el-table-column width="80" align="center">
           <template #default>
             <el-icon class="text-gray-400"><ArrowRight /></el-icon>
           </template>
        </el-table-column>
      </el-table>
      <div class="pagination-bar">
        <el-pagination 
          background 
          layout="prev, pager, next" 
          :total="total" 
          v-model:current-page="currentPage"
        />
      </div>
    </div>

    <!-- Diff Drawer -->
    <el-drawer v-model="drawerVisible" title="Change Detail" size="800px">
      <div v-if="selectedLog" class="diff-container">
        <div class="diff-header">
          <div class="diff-meta">
            <div class="meta-item">
              <label>User</label>
              <span>{{ selectedLog.modifyUser }}</span>
            </div>
            <div class="meta-item">
              <label>Time</label>
              <span>{{ selectedLog.createTime }}</span>
            </div>
            <div class="meta-item">
              <label>Action</label>
              <span>{{ selectedLog.modifyType }}</span>
            </div>
          </div>
        </div>

        <div class="diff-viewer">
          <div class="diff-pane old">
            <div class="pane-label bg-red-50 text-red-600">Old Value ({{ selectedLog.fieldName }})</div>
            <pre>{{ formatValue(selectedLog.oldValue) }}</pre>
          </div>
          <div class="diff-pane new">
            <div class="pane-label bg-green-50 text-green-600">New Value ({{ selectedLog.fieldName }})</div>
            <pre>{{ formatValue(selectedLog.newValue) }}</pre>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ArrowRight, Download } from '@element-plus/icons-vue'
import request from '@/utils/request'

const dateRange = ref([])
const drawerVisible = ref(false)
const selectedLog = ref(null)
const loading = ref(false)
const auditLogs = ref([])
const total = ref(0)
const currentPage = ref(1)

const fetchLogs = async () => {
  loading.value = true
  try {
    const res = await request.get('/audit/logs')
    auditLogs.value = res.data.data
    total.value = res.data.data.length
  } catch (error) {
    console.error('Failed to fetch audit logs:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchLogs()
})

const getActionType = (action) => {
  const map = {
    'UPDATE': 'warning',
    'CREATE': 'success',
    'CREATED': 'success',
    'DELETE': 'danger',
    'SYNC': 'info',
    'MANUAL': 'info',
    'RE_EVALUATION_TRIGGERED': 'warning'
  }
  return map[action] || 'info'
}

const showDiff = (row) => {
  selectedLog.value = row
  drawerVisible.value = true
}

const formatValue = (val) => {
  if (val === null || val === undefined) return 'null'
  try {
    const parsed = JSON.parse(val)
    return JSON.stringify(parsed, null, 2)
  } catch (e) {
    return val
  }
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
