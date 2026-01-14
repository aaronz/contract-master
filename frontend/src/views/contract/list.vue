<template>
  <div class="contract-list-page">
    <!-- Header & Actions -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Contracts</h1>
        <p class="page-subtitle">Manage all your contracts, approvals, and archives.</p>
      </div>
      <div class="header-actions">
        <el-button @click="showFilterDrawer = true">
          <el-icon><Filter /></el-icon> Filters
        </el-button>
        <el-button type="primary" icon="Plus">New Contract</el-button>
      </div>
    </div>

    <!-- Toolbar -->
    <div class="toolbar-container" :class="{ 'is-sticky': isSticky }">
      <div class="toolbar-left">
        <el-radio-group v-model="activeTab" size="large">
          <el-radio-button label="all">All Contracts</el-radio-button>
          <el-radio-button label="my">My Pending</el-radio-button>
          <el-radio-button label="shared">Shared with me</el-radio-button>
        </el-radio-group>
      </div>
      <div class="toolbar-right">
        <div class="search-input">
          <el-icon><Search /></el-icon>
          <input v-model="searchQuery" placeholder="Search contracts..." />
        </div>
        <el-dropdown trigger="click" @command="handleTemplateCommand">
          <el-button>
            <el-icon style="margin-right: 6px"><Files /></el-icon>
            Templates <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="expiring_soon">Expiring Soon (30 days)</el-dropdown-item>
              <el-dropdown-item command="high_value">High Value (>$1M)</el-dropdown-item>
              <el-dropdown-item divided command="save_current">Save Current View</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- Batch Action Bar (Floating) -->
    <transition name="slide-up">
      <div v-if="selectedRows.length > 0" class="batch-action-bar">
        <div class="selected-count">
          <span>{{ selectedRows.length }}</span> items selected
        </div>
        <div class="batch-actions">
          <el-button text bg><el-icon><Download /></el-icon> Export</el-button>
          <el-button text bg><el-icon><User /></el-icon> Assign</el-button>
          <el-button text bg type="danger" @click="handleBatchArchive"><el-icon><Delete /></el-icon> Archive</el-button>
          <div class="divider"></div>
          <el-button circle text @click="clearSelection"><el-icon><Close /></el-icon></el-button>
        </div>
      </div>
    </transition>

    <!-- Data Table -->
    <el-card class="table-card" shadow="never">
      <el-table
        ref="tableRef"
        :data="contracts"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        :header-cell-style="{ background: '#F8FAFC', color: '#64748B' }"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column label="Contract No." prop="contractNo" width="160">
          <template #default="{ row }">
            <span class="font-mono font-medium">{{ row.contractNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Contract Name" min-width="250">
          <template #default="{ row }">
            <div class="contract-name-cell">
              <div class="name-text">{{ row.contractName }}</div>
              <div class="party-text">{{ row.partyAName }} vs {{ row.partyBName }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Amount" prop="amount" width="150" sortable>
          <template #default="{ row }">
            <span class="amount-text">{{ formatCurrency(row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Status" prop="status" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="light" round>
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Date" prop="createTime" width="180" sortable>
          <template #default="{ row }">
            <div class="date-cell">
              <span>{{ formatDate(row.createTime) }}</span>
              <span class="sub-text">Created</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="100" fixed="right">
          <template #default="{ row }">
            <el-button circle text type="primary" @click="viewDetail(row.id)">
              <el-icon><ArrowRight /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-wrapper">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :total="100"
          :page-size="10"
        />
      </div>
    </el-card>

    <!-- Filter Drawer -->
    <el-drawer v-model="showFilterDrawer" title="Advanced Filters" size="400px">
      <el-form label-position="top">
        <el-form-item label="Date Range">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="To"
            start-placeholder="Start"
            end-placeholder="End"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="Contract Type">
          <el-select v-model="filterForm.type" placeholder="Select type" style="width: 100%">
            <el-option label="NDA" value="nda" />
            <el-option label="Service Agreement" value="service" />
            <el-option label="Sales Contract" value="sales" />
          </el-select>
        </el-form-item>
        <el-form-item label="Amount Range">
          <div class="amount-range">
            <el-input v-model="filterForm.minAmount" placeholder="Min" />
            <span class="separator">-</span>
            <el-input v-model="filterForm.maxAmount" placeholder="Max" />
          </div>
        </el-form-item>
        <el-form-item label="Status">
          <el-checkbox-group v-model="filterForm.status">
            <el-checkbox label="Draft" />
            <el-checkbox label="Pending" />
            <el-checkbox label="Active" />
            <el-checkbox label="Expired" />
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="showFilterDrawer = false">Cancel</el-button>
          <el-button type="primary" @click="applyFilters">Apply Filters</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Filter, Search, Files, ArrowDown, ArrowRight, Download, User, Delete, Close, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const showFilterDrawer = ref(false)
const activeTab = ref('all')
const searchQuery = ref('')
const selectedRows = ref([])
const tableRef = ref(null)

const filterForm = ref({
  dateRange: [],
  type: '',
  minAmount: '',
  maxAmount: '',
  status: []
})

// Mock Data
const contracts = ref([
  { id: 1, contractNo: 'CON-2024-001', contractName: 'Enterprise License Agreement', partyAName: 'Acme Corp', partyBName: 'TechSolutions Inc', amount: 150000, status: 'Active', createTime: '2024-01-10' },
  { id: 2, contractNo: 'CON-2024-002', contractName: 'NDA for Project X', partyAName: 'Globex', partyBName: 'TechSolutions Inc', amount: 0, status: 'Pending', createTime: '2024-01-12' },
  { id: 3, contractNo: 'CON-2024-003', contractName: 'Service Level Agreement', partyAName: 'Soylent Corp', partyBName: 'TechSolutions Inc', amount: 50000, status: 'Draft', createTime: '2024-01-14' },
  { id: 4, contractNo: 'CON-2024-004', contractName: 'Marketing Partnership', partyAName: 'Massive Dynamic', partyBName: 'TechSolutions Inc', amount: 750000, status: 'Active', createTime: '2024-01-08' },
])

const handleSelectionChange = (val) => {
  selectedRows.value = val
}

const clearSelection = () => {
  tableRef.value.clearSelection()
}

const viewDetail = (id) => {
  router.push(`/contracts/${id}`)
}

const handleTemplateCommand = (command) => {
  if (command === 'save_current') {
    ElMessageBox.prompt('Enter template name', 'Save View', {
      confirmButtonText: 'Save',
    }).then(({ value }) => {
      ElMessage.success(`View saved as "${value}"`)
    })
  } else {
    ElMessage.success(`Applied template: ${command}`)
  }
}

const handleBatchArchive = () => {
  ElMessageBox.prompt(
    'Please enter the 6-digit security verification code to confirm this high-risk batch operation.',
    'High-Risk Action Security Check',
    {
      confirmButtonText: 'Verify & Confirm',
      cancelButtonText: 'Cancel',
      inputPattern: /^[0-9]{6}$/,
      inputErrorMessage: 'Invalid code format. Please enter 6 digits.',
      type: 'warning'
    }
  ).then(({ value }) => {
    // In real app:
    // axios.post('/api/contracts/batch-archive', selectedRows.value.map(r => r.id), {
    //   headers: { 'X-Secondary-Confirm': 'VERIFIED' }
    // })
    ElMessage.success('Security verification successful. Batch archiving initiated.')
    clearSelection()
  }).catch(() => {
    ElMessage.info('Operation cancelled.')
  })
}

const applyFilters = () => {
  showFilterDrawer.value = false
  ElMessage.success('Filters applied')
}

// Helpers
const formatCurrency = (val) => {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(val)
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

const getStatusType = (status) => {
  const map = {
    'Active': 'success',
    'Pending': 'warning',
    'Draft': 'info',
    'Expired': 'danger'
  }
  return map[status] || 'info'
}
</script>

<style scoped>
.contract-list-page {
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

.toolbar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.toolbar-right {
  display: flex;
  gap: 12px;
}

.search-input {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input .el-icon {
  position: absolute;
  left: 12px;
  color: #94A3B8;
  z-index: 1;
}

.search-input input {
  padding: 8px 12px 8px 36px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  outline: none;
  width: 240px;
  transition: all 0.2s;
}

.search-input input:focus {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
}

.batch-action-bar {
  position: fixed;
  bottom: 40px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--primary-color);
  color: white;
  padding: 12px 24px;
  border-radius: 100px;
  display: flex;
  align-items: center;
  gap: 24px;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  z-index: 100;
}

.selected-count {
  font-weight: 600;
}

.batch-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.batch-actions .el-button {
  color: white;
  border: none;
}
.batch-actions .el-button:hover {
  background: rgba(255,255,255,0.1);
}

.divider {
  width: 1px;
  height: 20px;
  background: rgba(255,255,255,0.2);
  margin: 0 8px;
}

.contract-name-cell .name-text {
  font-weight: 600;
  color: var(--text-primary);
}
.contract-name-cell .party-text {
  font-size: 12px;
  color: var(--text-secondary);
}

.amount-text {
  font-family: 'JetBrains Mono', monospace;
  font-weight: 500;
}

.date-cell {
  display: flex;
  flex-direction: column;
}
.date-cell .sub-text {
  font-size: 11px;
  color: var(--text-secondary);
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  padding-top: 20px;
}

.amount-range {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* Animations */
.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-up-enter-from,
.slide-up-leave-to {
  opacity: 0;
  transform: translate(-50%, 20px);
}
</style>
