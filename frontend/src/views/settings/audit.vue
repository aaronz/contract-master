<template>
  <div class="audit-log-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Audit Logs</h1>
        <p class="page-subtitle">Monitor system activity and data changes.</p>
      </div>
      <el-button icon="Download">Export Logs</el-button>
    </div>

    <!-- Filters -->
    <el-card shadow="never" class="filter-card">
      <div class="filter-row">
        <el-input v-model="filters.keyword" placeholder="Search User or Resource ID" prefix-icon="Search" style="width: 300px" />
        <el-date-picker
          v-model="filters.dateRange"
          type="daterange"
          range-separator="To"
          start-placeholder="Start date"
          end-placeholder="End date"
        />
        <el-select v-model="filters.action" placeholder="Action Type" clearable>
          <el-option label="Create" value="create" />
          <el-option label="Update" value="update" />
          <el-option label="Delete" value="delete" />
          <el-option label="Login" value="login" />
        </el-select>
        <el-button type="primary" @click="handleSearch">Search</el-button>
      </div>
    </el-card>

    <!-- Table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="logs" style="width: 100%">
        <el-table-column prop="timestamp" label="Timestamp" width="180">
          <template #default="{ row }">
            <span class="text-secondary">{{ row.timestamp }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="user" label="User" width="150">
           <template #default="{ row }">
             <div class="user-cell">
               <el-avatar :size="24" :src="row.avatar">{{ row.initials }}</el-avatar>
               <span>{{ row.user }}</span>
             </div>
           </template>
        </el-table-column>

        <el-table-column prop="action" label="Action" width="120">
          <template #default="{ row }">
            <el-tag :type="getActionType(row.action)" size="small" effect="light">{{ row.action }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="resource" label="Resource" />

        <el-table-column prop="ip" label="IP Address" width="140">
           <template #default="{ row }">
             <span class="mono-text">{{ row.ip }}</span>
           </template>
        </el-table-column>
        
        <el-table-column label="Details" width="100" align="center">
          <template #default="{ row }">
            <el-button v-if="row.hasDiff" link type="primary" size="small" @click="showDiff(row)">View Diff</el-button>
            <span v-else class="text-gray">-</span>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-wrapper">
        <el-pagination layout="prev, pager, next" :total="100" />
      </div>
    </el-card>

    <!-- Diff Dialog -->
    <el-dialog
      v-model="diffVisible"
      title="Change Details"
      width="600px"
      class="diff-dialog"
    >
      <div v-if="selectedLog" class="diff-content">
        <div class="diff-header">
          <div class="diff-col">Field</div>
          <div class="diff-col old">Old Value</div>
          <div class="diff-col new">New Value</div>
        </div>
        <div v-for="(change, idx) in selectedLog.changes" :key="idx" class="diff-row">
          <div class="diff-cell field">{{ change.field }}</div>
          <div class="diff-cell old-val">{{ change.old }}</div>
          <div class="diff-cell new-val">{{ change.new }}</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Search, Download } from '@element-plus/icons-vue'

const filters = ref({
  keyword: '',
  dateRange: [],
  action: ''
})

const logs = ref([
  { 
    timestamp: '2024-01-14 10:42:05', 
    user: 'Admin User', 
    initials: 'AU',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    action: 'UPDATE', 
    resource: 'Contract #2024-089', 
    ip: '192.168.1.42',
    hasDiff: true,
    changes: [
      { field: 'Status', old: 'Draft', new: 'Review' },
      { field: 'Amount', old: '$10,000', new: '$12,500' }
    ]
  },
  { 
    timestamp: '2024-01-14 09:15:22', 
    user: 'Sarah Connor', 
    initials: 'SC',
    avatar: '',
    action: 'LOGIN', 
    resource: 'System', 
    ip: '203.0.113.5',
    hasDiff: false 
  },
  { 
    timestamp: '2024-01-13 16:20:01', 
    user: 'John Smith', 
    initials: 'JS',
    avatar: '',
    action: 'CREATE', 
    resource: 'Vendor: Cyberdyne Systems', 
    ip: '198.51.100.24',
    hasDiff: true,
    changes: [
       { field: 'Name', old: 'null', new: 'Cyberdyne Systems' },
       { field: 'Type', old: 'null', new: 'Hardware' }
    ]
  }
])

const diffVisible = ref(false)
const selectedLog = ref(null)

const getActionType = (action) => {
  const map = { UPDATE: 'warning', CREATE: 'success', DELETE: 'danger', LOGIN: 'info' }
  return map[action] || 'info'
}

const showDiff = (row) => {
  selectedLog.value = row
  diffVisible.value = true
}

const handleSearch = () => {
  // Mock search
  console.log('Search with', filters.value)
}
</script>

<style scoped>
.audit-log-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
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

.filter-card {
  border-radius: 12px;
}

.filter-row {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.table-card {
  border-radius: 12px;
}

.user-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.mono-text {
  font-family: monospace;
  color: #64748B;
  font-size: 12px;
}

.text-secondary {
  color: var(--text-secondary);
  font-size: 13px;
}

.text-gray {
  color: #CBD5E1;
}

.pagination-wrapper {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

/* Diff Dialog Styles */
.diff-header {
  display: flex;
  background: #F8FAFC;
  padding: 8px 12px;
  border-bottom: 1px solid #E2E8F0;
  font-weight: 600;
  font-size: 13px;
  color: var(--text-secondary);
}

.diff-row {
  display: flex;
  border-bottom: 1px solid #F1F5F9;
}

.diff-col, .diff-cell {
  flex: 1;
  padding: 8px 12px;
  font-size: 13px;
}

.diff-col.field, .diff-cell.field {
  flex: 0 0 120px;
  font-weight: 500;
  color: var(--text-primary);
  background: #F8FAFC;
  border-right: 1px solid #E2E8F0;
}

.diff-cell.old-val {
  background-color: #FEF2F2;
  color: #B91C1C;
  text-decoration: line-through;
}

.diff-cell.new-val {
  background-color: #F0FDF4;
  color: #15803D;
}
</style>
