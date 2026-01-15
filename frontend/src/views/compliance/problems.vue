<template>
  <div class="problem-center-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Problem Center</h1>
        <p class="page-subtitle">Track, investigate, and resolve contract compliance issues.</p>
      </div>
      <div class="header-filters">
        <el-input 
          v-model="search" 
          placeholder="Search issues..." 
          prefix-icon="Search" 
          class="glass-input-sm"
          style="width: 240px"
        />
        <el-select v-model="filterSeverity" placeholder="Severity" style="width: 120px">
          <el-option label="All" value="all" />
          <el-option label="Critical" value="critical" />
          <el-option label="Warning" value="warning" />
        </el-select>
        <el-button icon="Refresh" circle />
      </div>
    </div>

    <div class="kanban-board">
      <div 
        v-for="column in columns" 
        :key="column.id" 
        class="kanban-column"
      >
        <div class="column-header">
          <div class="column-title">
            <span class="dot" :class="column.id"></span>
            {{ column.name }}
          </div>
          <div class="column-count">{{ column.tasks.length }}</div>
        </div>

        <draggable 
          v-model="column.tasks" 
          group="tasks" 
          item-key="id"
          class="column-draggable"
          ghost-class="ghost-card"
          @change="(e) => handleChange(e, column.id)"
        >
          <template #item="{ element }">
            <div class="kanban-card glass-card hover-scale" @click="openDetail(element)">
              <div class="card-badges">
                <el-tag 
                  size="small" 
                  :type="getSeverityType(element.severity)" 
                  effect="dark"
                  class="severity-tag"
                >
                  {{ element.severity }}
                </el-tag>
                <span class="time-ago">{{ element.timeAgo }}</span>
              </div>
              
              <h4 class="card-title">{{ element.title }}</h4>
              <p class="card-desc">{{ element.desc }}</p>
              
              <div class="card-footer">
                <div class="contract-ref">
                  <el-icon><Document /></el-icon> {{ element.contractNo }}
                </div>
                <el-avatar 
                  v-if="element.assignee" 
                  size="small" 
                  :src="element.assigneeAvatar" 
                  class="assignee-avatar"
                />
                <el-button 
                  v-else 
                  size="small" 
                  type="primary" 
                  link 
                  @click.stop="claimTask(element)"
                >Claim</el-button>
              </div>
            </div>
          </template>
        </draggable>
      </div>
    </div>

    <!-- Task Detail Drawer -->
    <el-drawer v-model="drawerVisible" title="Issue Details" size="500px">
      <div v-if="currentTask" class="drawer-content">
        <div class="detail-header">
          <el-tag :type="getSeverityType(currentTask.severity)">{{ currentTask.severity }}</el-tag>
          <span class="detail-id">ID: {{ currentTask.id }}</span>
        </div>
        <h2>{{ currentTask.title }}</h2>
        <div class="detail-section">
          <label>Description</label>
          <p>{{ currentTask.desc }}</p>
        </div>
        <div class="detail-section">
          <label>Contract Context</label>
          <div class="context-box glass-panel-sm">
             <div class="flex justify-between mb-2">
               <span>Contract:</span>
               <a href="#" class="text-blue-500">{{ currentTask.contractNo }}</a>
             </div>
             <div class="flex justify-between">
               <span>Amount:</span>
               <span>$1,200,000.00</span>
             </div>
          </div>
        </div>
        <div class="detail-section">
          <label>Audit Trail</label>
          <el-timeline>
            <el-timeline-item timestamp="Today, 10:00" type="primary">
              Issue Detected by Rule Engine
            </el-timeline-item>
            <el-timeline-item timestamp="Today, 10:05" type="warning">
              Notification sent to Sales Mgr
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <template #footer>
        <div class="drawer-actions">
           <el-button @click="drawerVisible = false">Cancel</el-button>
           <el-button type="primary" @click="handleResolve" :loading="resolving">Resolve Issue</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import draggable from 'vuedraggable'
import { Search, Refresh, Document } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const search = ref('')
const filterSeverity = ref('all')
const drawerVisible = ref(false)
const currentTask = ref(null)
const resolving = ref(false)

const columns = reactive([
  {
    id: 'open',
    name: 'Open Issues',
    tasks: [
      { id: 'T-101', title: 'Missing Tax ID', desc: 'Vendor contract #2045 lacks mandatory Tax ID field.', severity: 'Critical', timeAgo: '2h', contractNo: 'CTR-2024-001', assignee: null },
      { id: 'T-102', title: 'Payment Term Mismatch', desc: 'Payment terms exceed standard 60 days limit.', severity: 'Warning', timeAgo: '4h', contractNo: 'CTR-2024-005', assignee: null },
    ]
  },
  {
    id: 'progress',
    name: 'Investigating',
    tasks: [
      { id: 'T-098', title: 'Currency Variance', desc: 'Exchange rate fluctuation exceeds 5% threshold.', severity: 'Warning', timeAgo: '1d', contractNo: 'CTR-2023-889', assignee: 'Jane Doe', assigneeAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
    ]
  },
  {
    id: 'review',
    name: 'Pending Review',
    tasks: [
       { id: 'T-095', title: 'Clause Deviation', desc: 'Indemnity clause modified without Legal approval.', severity: 'Critical', timeAgo: '2d', contractNo: 'CTR-2023-850', assignee: 'John Smith', assigneeAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png' },
    ]
  },
  {
    id: 'closed',
    name: 'Resolved',
    tasks: [
       { id: 'T-090', title: 'Signature Missing', desc: 'Party B signature page missing from scan.', severity: 'Info', timeAgo: '5d', contractNo: 'CTR-2023-800', assignee: 'System', assigneeAvatar: '' },
    ]
  }
])

const getSeverityType = (sev) => {
  if (sev === 'Critical') return 'danger'
  if (sev === 'Warning') return 'warning'
  return 'info'
}

const openDetail = (task) => {
  currentTask.value = task
  drawerVisible.value = true
}

const claimTask = (task) => {
  task.assignee = 'You'
  task.assigneeAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  ElMessage.success('Task claimed successfully')
}

const handleResolve = async () => {
  if (!currentTask.value) return
  resolving.value = true
  try {
    // In real app, currentTask.id would be a numeric primary key for this endpoint
    // Since mock uses T-101, we take the digits for demo
    const numericId = currentTask.value.id.split('-')[1] || 1
    const response = await fetch(`/api/problems/${numericId}/resolve`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    if (response.ok) {
      ElMessage.success('Issue resolved')
      // Move to resolved column in UI
      const foundCol = columns.find(c => c.tasks.some(t => t.id === currentTask.value.id))
      if (foundCol) {
        const taskIdx = foundCol.tasks.findIndex(t => t.id === currentTask.value.id)
        const [task] = foundCol.tasks.splice(taskIdx, 1)
        columns.find(c => c.id === 'closed').tasks.unshift(task)
      }
      drawerVisible.value = false
    }
  } catch (error) {
    console.error('Resolution failed', error)
  } finally {
    resolving.value = false
  }
}

const handleChange = (evt, colId) => {
  // Handle drag events if needed (backend sync)
  console.log('Moved', evt, colId)
}
</script>

<style scoped>
.problem-center-page {
  display: flex;
  flex-direction: column;
  height: 100%;
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

.header-filters {
  display: flex;
  gap: 12px;
}

.kanban-board {
  flex: 1;
  display: flex;
  gap: 24px;
  overflow-x: auto;
  padding-bottom: 20px;
}

.kanban-column {
  flex: 1;
  min-width: 280px;
  display: flex;
  flex-direction: column;
  background: rgba(241, 245, 249, 0.5);
  border-radius: 16px;
  padding: 16px;
  border: 1px solid rgba(255,255,255,0.2);
}

.column-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 0 4px;
}

.column-title {
  font-weight: 700;
  color: #334155;
  display: flex;
  align-items: center;
  gap: 8px;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}
.dot.open { background: #3B82F6; }
.dot.progress { background: #F59E0B; }
.dot.review { background: #8B5CF6; }
.dot.closed { background: #10B981; }

.column-count {
  background: rgba(255,255,255,0.6);
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  color: #64748B;
  font-weight: 600;
}

.column-draggable {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
  min-height: 200px;
}

.kanban-card {
  padding: 16px;
  cursor: pointer;
  transition: all 0.2s;
  background: white;
}

.card-badges {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.time-ago {
  font-size: 11px;
  color: #94A3B8;
}

.card-title {
  margin: 0 0 8px 0;
  font-size: 15px;
  font-weight: 600;
  color: #1E293B;
}

.card-desc {
  margin: 0 0 16px 0;
  font-size: 13px;
  color: #64748B;
  line-height: 1.4;
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

.contract-ref {
  font-size: 12px;
  color: #94A3B8;
  display: flex;
  align-items: center;
  gap: 4px;
}

.ghost-card {
  opacity: 0.5;
  background: #F1F5F9;
  border: 1px dashed #94A3B8;
}

/* Detail Drawer Styles */
.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.detail-id {
  color: #94A3B8;
  font-family: monospace;
}

.detail-section {
  margin-top: 24px;
}

.detail-section label {
  display: block;
  font-size: 12px;
  color: #64748B;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 8px;
  font-weight: 600;
}

.context-box {
  padding: 12px;
  border-radius: 8px;
  font-size: 14px;
}

.drawer-actions {
  display: flex;
  justify-content: flex-end;
}
</style>
