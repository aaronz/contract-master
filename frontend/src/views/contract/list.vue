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
        <el-button type="primary" icon="Plus" @click="showNewContractDialog = true">New Contract</el-button>
      </div>
    </div>

    <!-- New Contract Dialog -->
    <el-dialog v-model="showNewContractDialog" title="Create New Contract" width="600px">
      <el-form :model="newContractForm" :rules="rules" ref="newContractFormRef" label-position="top">
        <div class="form-grid">
          <el-form-item label="Contract No." prop="contractNo">
            <el-input v-model="newContractForm.contractNo" placeholder="e.g. CON-2026-001" />
          </el-form-item>
          <el-form-item label="Contract Name" prop="contractName">
            <el-input v-model="newContractForm.contractName" />
          </el-form-item>
        </div>
        <div class="form-grid">
          <el-form-item label="Party A" prop="partyAName">
            <el-input v-model="newContractForm.partyAName" />
          </el-form-item>
          <el-form-item label="Party B" prop="partyBName">
            <el-input v-model="newContractForm.partyBName" />
          </el-form-item>
        </div>
        <div class="form-grid">
          <el-form-item label="Amount" prop="amount">
            <el-input-number v-model="newContractForm.amount" :precision="2" :step="1000" style="width: 100%" />
          </el-form-item>
          <el-form-item label="Tax Rate (%)" prop="taxRate">
            <el-input-number v-model="newContractForm.taxRate" :precision="2" :step="0.1" style="width: 100%" />
          </el-form-item>
        </div>
        <div class="form-grid">
          <el-form-item label="Currency" prop="currencyType">
            <el-select v-model="newContractForm.currencyType" style="width: 100%">
              <el-option label="USD" value="USD" />
              <el-option label="EUR" value="EUR" />
              <el-option label="CNY" value="CNY" />
            </el-select>
          </el-form-item>
          <el-form-item label="Contract Type" prop="contractType">
            <el-input v-model="newContractForm.contractType" />
          </el-form-item>
        </div>
        <div class="form-grid">
          <el-form-item label="Effective Date" prop="effectiveDate">
            <el-date-picker v-model="newContractForm.effectiveDate" type="date" style="width: 100%" />
          </el-form-item>
          <el-form-item label="Expire Date" prop="expireDate">
            <el-date-picker v-model="newContractForm.expireDate" type="date" style="width: 100%" />
          </el-form-item>
        </div>
        <el-form-item label="Remark" prop="remark">
          <el-input v-model="newContractForm.remark" type="textarea" />
        </el-form-item>

        <div class="form-grid">
          <el-form-item label="Currency" prop="currencyType">
            <el-select v-model="newContractForm.currencyType" style="width: 100%">
              <el-option label="USD" value="USD" />
              <el-option label="EUR" value="EUR" />
              <el-option label="CNY" value="CNY" />
            </el-select>
          </el-form-item>
          <el-form-item label="Contract Type" prop="contractType">
            <el-input v-model="newContractForm.contractType" />
          </el-form-item>
        </div>
        <div class="form-grid">
          <el-form-item label="Effective Date" prop="effectiveDate">
            <el-date-picker v-model="newContractForm.effectiveDate" type="date" style="width: 100%" />
          </el-form-item>
          <el-form-item label="Expire Date" prop="expireDate">
            <el-date-picker v-model="newContractForm.expireDate" type="date" style="width: 100%" />
          </el-form-item>
        </div>
        <el-form-item label="Remark" prop="remark">
          <el-input v-model="newContractForm.remark" type="textarea" />
        </el-form-item>
        
        <!-- Extended Fields -->
        <div v-if="contractFields.length > 0">
          <h3 class="section-header mt-4 mb-2" style="font-size: 14px; color: var(--text-secondary)">Additional Fields</h3>
          <div class="form-grid">
            <el-form-item 
              v-for="field in contractFields.filter(f => f.source === 'EXTEND')" 
              :key="field.fieldCode" 
              :label="field.fieldName"
            >
              <el-input-number 
                v-if="field.fieldType === 'NUMBER'" 
                v-model="newContractForm.extendedFields[field.fieldCode]" 
                style="width: 100%"
              />
              <el-date-picker
                v-else-if="field.fieldType === 'DATE'"
                v-model="newContractForm.extendedFields[field.fieldCode]"
                type="date"
                style="width: 100%"
              />
              <el-input 
                v-else 
                v-model="newContractForm.extendedFields[field.fieldCode]" 
              />
            </el-form-item>
          </div>
        </div>
      </el-form>
      <template #footer>
        <el-button @click="showNewContractDialog = false">Cancel</el-button>
        <el-button type="primary" @click="handleCreateContract" :loading="creating">Create</el-button>
      </template>
    </el-dialog>

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

        <!-- Dynamic Extended Columns -->
        <el-table-column 
          v-for="field in contractFields.filter(f => f.source === 'EXTEND')" 
          :key="field.fieldCode" 
          :label="field.fieldName"
          :prop="field.fieldCode"
          width="150"
        >
          <template #default="{ row }">
            {{ row.extendedFields ? row.extendedFields[field.fieldCode] : 'N/A' }}
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
          :total="total"
          :page-size="pageSize"
          v-model:current-page="currentPage"
          @current-change="handlePageChange"
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Filter, Search, Files, ArrowDown, ArrowRight, Download, User, Delete, Close, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const showFilterDrawer = ref(false)
const activeTab = ref('all')
const searchQuery = ref('')
const selectedRows = ref([])
const tableRef = ref(null)
const contracts = ref([])
const isSticky = ref(false)
const filterForm = ref({
  dateRange: [],
  type: '',
  minAmount: '',
  maxAmount: '',
  status: []
})

const showNewContractDialog = ref(false)
const creating = ref(false)
const newContractFormRef = ref(null)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const newContractForm = ref({
  contractNo: '',
  contractName: '',
  partyAName: '',
  partyBName: '',
  amount: 0,
  taxRate: 0,
  currencyType: 'USD',
  contractType: '',
  effectiveDate: '',
  expireDate: '',
  remark: '',
  extendedFields: {}
})


const rules = {
  contractNo: [{ required: true, message: 'Please input contract number', trigger: 'blur' }],
  contractName: [{ required: true, message: 'Please input contract name', trigger: 'blur' }],
  partyAName: [{ required: true, message: 'Please input Party A', trigger: 'blur' }],
  partyBName: [{ required: true, message: 'Please input Party B', trigger: 'blur' }]
}

const contractFields = ref([])

const fetchMetadata = async () => {
  try {
    const response = await fetch('/api/metadata/contract-fields', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    if (response.ok) {
      const result = await response.json()
      contractFields.value = result.data
    }
  } catch (error) {
    console.error('Failed to fetch metadata', error)
  }
}

const fetchContracts = async () => {
  try {
    const response = await fetch(`/api/contracts?page=${currentPage.value - 1}&size=${pageSize.value}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    if (response.ok) {
      const result = await response.json()
      contracts.value = result.data.content
      total.value = result.data.totalElements
    }
  } catch (error) {
    console.error('Failed to fetch contracts', error)
  }
}

const handleCreateContract = async () => {
  if (!newContractFormRef.value) return
  await newContractFormRef.value.validate(async (valid) => {
    if (valid) {
      creating.value = true
      try {
        const response = await fetch('/api/contracts', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify({
            ...newContractForm.value,
            status: 'DRAFT'
          })
        })
        if (response.ok) {
          ElMessage.success('Contract created successfully')
          showNewContractDialog.value = false
          currentPage.value = 1 // Reset to first page to see the new record
          fetchContracts()
        }
      } catch (error) {
        console.error('Failed to create contract', error)
      } finally {
        creating.value = false
      }
    }
  })
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchContracts()
}

onMounted(() => {
  fetchContracts()
  fetchMetadata()
})

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
