<template>
  <div class="problem-center">
    <div class="page-header">
      <div>
        <h1 class="page-title">Problem Center</h1>
        <p class="page-subtitle">Track and resolve contract compliance issues.</p>
      </div>
      <div class="header-actions">
        <el-button-group>
          <el-button :type="viewMode === 'kanban' ? 'primary' : ''" @click="viewMode = 'kanban'">
            <el-icon><collection /></el-icon> Kanban
          </el-button>
          <el-button :type="viewMode === 'list' ? 'primary' : ''" @click="viewMode = 'list'">
            <el-icon><list /></el-icon> List
          </el-button>
        </el-button-group>
        <el-button type="primary" icon="Plus">New Issue</el-button>
      </div>
    </div>

    <!-- Kanban View -->
    <div v-if="viewMode === 'kanban'" class="kanban-board">
      <div v-for="column in columns" :key="column.id" class="kanban-column">
        <div class="column-header">
          <span class="column-title">{{ column.title }}</span>
          <el-tag size="small" round :type="column.type">{{ getColumnCount(column.id) }}</el-tag>
        </div>
        
        <div class="column-content custom-scrollbar">
          <div v-for="task in getTasksByStatus(column.id)" :key="task.id" class="kanban-card">
            <el-card shadow="hover" class="issue-card">
              <div class="card-badges">
                <el-tag size="small" :type="getPriorityType(task.priority)" effect="light">{{ task.priority }}</el-tag>
                <span class="card-id">#{{ task.id }}</span>
              </div>
              <h3 class="issue-title">{{ task.title }}</h3>
              <p class="issue-desc">{{ task.description }}</p>
              
              <div class="card-footer">
                <div class="assignee">
                  <el-avatar :size="24" :src="task.assigneeAvatar">{{ task.assigneeInitials }}</el-avatar>
                  <span class="assignee-name">{{ task.assigneeName }}</span>
                </div>
                <div class="actions">
                  <el-button v-if="column.id === 'claim'" type="primary" link size="small">Claim</el-button>
                  <el-button v-else-if="column.id === 'handle'" type="primary" link size="small">Handle</el-button>
                  <el-button v-else-if="column.id === 'review'" type="success" link size="small">Approve</el-button>
                  <el-button link size="small">View</el-button>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </div>

    <!-- List View Placeholder -->
    <el-card v-else shadow="never" class="list-view-card">
       <el-empty description="List view coming soon" />
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Collection, List, Plus } from '@element-plus/icons-vue'

const viewMode = ref('kanban')

const columns = [
  { id: 'claim', title: 'To Claim', type: 'danger' },
  { id: 'handle', title: 'Processing', type: 'warning' },
  { id: 'review', title: 'In Review', type: 'primary' },
  { id: 'closed', title: 'Closed Loop', type: 'success' }
]

const tasks = ref([
  { 
    id: 'ISS-001', 
    title: 'Missing Indemnity Clause', 
    description: 'Contract #2024-089 is missing standard indemnity protection.', 
    status: 'claim', 
    priority: 'High',
    assigneeName: 'Unassigned',
    assigneeInitials: '?',
    assigneeAvatar: ''
  },
  { 
    id: 'ISS-002', 
    title: 'Payment Terms Mismatch', 
    description: 'Invoice terms (Net 30) do not match PO (Net 60).', 
    status: 'handle', 
    priority: 'Medium',
    assigneeName: 'Sarah C.',
    assigneeInitials: 'SC',
    assigneeAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  },
  { 
    id: 'ISS-003', 
    title: 'Expired Certificate', 
    description: 'Vendor insurance certificate expired last week.', 
    status: 'review', 
    priority: 'Low',
    assigneeName: 'Mike R.',
    assigneeInitials: 'MR',
    assigneeAvatar: ''
  },
    { 
    id: 'ISS-004', 
    title: 'Unauthorized Edit', 
    description: 'Detected unauthorized change to liability cap.', 
    status: 'claim', 
    priority: 'Critical',
    assigneeName: 'Unassigned',
    assigneeInitials: '?',
    assigneeAvatar: ''
  }
])

const getTasksByStatus = (status) => tasks.value.filter(t => t.status === status)
const getColumnCount = (status) => getTasksByStatus(status).length
const getPriorityType = (p) => {
  const map = { Critical: 'danger', High: 'warning', Medium: 'info', Low: 'success' }
  return map[p] || 'info'
}

</script>

<style scoped>
.problem-center {
  height: calc(100vh - 140px);
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 24px;
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

.kanban-board {
  flex: 1;
  display: flex;
  gap: 24px;
  overflow-x: auto;
  padding-bottom: 12px;
}

.kanban-column {
  flex: 1;
  min-width: 300px;
  background: #F8FAFC;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  border: 1px solid var(--border-color);
}

.column-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 0 4px;
}

.column-title {
  font-weight: 600;
  color: var(--text-primary);
  font-size: 14px;
}

.column-content {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-right: 4px;
}

.issue-card {
  border-radius: 8px;
  border: 1px solid transparent;
  transition: all 0.2s;
  cursor: grab;
}

.issue-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--box-shadow-md);
  border-color: var(--accent-color);
}

.issue-card :deep(.el-card__body) {
  padding: 16px;
}

.card-badges {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}

.card-id {
  font-size: 11px;
  color: #94A3B8;
  font-family: monospace;
}

.issue-title {
  font-size: 14px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: var(--text-primary);
  line-height: 1.4;
}

.issue-desc {
  font-size: 12px;
  color: var(--text-secondary);
  margin: 0 0 16px 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #F1F5F9;
  padding-top: 12px;
}

.assignee {
  display: flex;
  align-items: center;
  gap: 8px;
}

.assignee-name {
  font-size: 12px;
  color: var(--text-secondary);
}

.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #CBD5E1;
  border-radius: 4px;
}
</style>
