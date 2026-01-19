<template>
  <div class="contract-list-page">
    <!-- Header & Actions -->
    <div class="page-header">
      <div>
        <h1 class="page-title">{{ $t('common.contracts') }}</h1>
        <p class="page-subtitle">{{ $t('contract.subtitle') }}</p>
      </div>
      <div class="header-actions">
        <el-button @click="showFilterDrawer = true">
          <el-icon><Filter /></el-icon> {{ $t('common.filter') }}
        </el-button>
        <el-button type="primary" icon="Plus" @click="showNewContractDialog = true">{{ $t('contract.create') }}</el-button>
      </div>
    </div>

    <!-- New Contract Dialog -->
    <el-dialog v-model="showNewContractDialog" :title="$t('contract.create')" width="900px" top="5vh">
      <el-form :model="newContractForm" :rules="rules" ref="newContractFormRef" label-position="top">
        <el-tabs v-model="activeNewContractTab" class="custom-tabs-dialog">
          
          <!-- General Info Tab -->
          <el-tab-pane :label="$t('contract.generalInfo')" name="general">
            <div class="form-section">
              <h3 class="section-header">Core Details</h3>
              <div class="form-grid">
                <div class="field-group" v-if="fieldStore.isFieldVisible('contract_no')">
                  <label>{{ fieldStore.getFieldByCode('contract_no')?.fieldName || $t('contract.no') }}</label>
                  <el-form-item prop="contractNo">
                    <el-input v-model="newContractForm.contractNo" placeholder="e.g. CON-2026-001" />
                  </el-form-item>
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('contract_name')">
                  <label>{{ fieldStore.getFieldByCode('contract_name')?.fieldName || $t('contract.name') }}</label>
                  <el-form-item prop="contractName">
                    <el-input v-model="newContractForm.contractName" />
                  </el-form-item>
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('contract_type')">
                  <label>{{ fieldStore.getFieldByCode('contract_type')?.fieldName || $t('contract.type') }}</label>
                  <el-input v-model="newContractForm.contractType" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('contract_status')">
                  <label>{{ fieldStore.getFieldByCode('contract_status')?.fieldName || $t('contract.status') }}</label>
                  <el-select v-model="newContractForm.contractStatus" style="width: 100%">
                    <el-option label="Draft" value="Draft" />
                    <el-option label="Active" value="Active" />
                    <el-option label="Pending" value="Pending" />
                  </el-select>
                </div>
              </div>
            </div>

            <div class="form-grid-2">
              <div class="form-section" v-if="fieldStore.isFieldVisible('party_a_name') || fieldStore.isFieldVisible('party_a_contact') || fieldStore.isFieldVisible('party_a_phone') || fieldStore.isFieldVisible('party_a_address')">
                <h3 class="section-header text-blue">{{ $t('contract.partyA') }}</h3>
                <div class="field-group" v-if="fieldStore.isFieldVisible('party_a_name')">
                  <label>{{ fieldStore.getFieldByCode('party_a_name')?.fieldName || 'Entity Name' }}</label>
                  <el-form-item prop="partyAName">
                    <el-input v-model="newContractForm.partyAName" />
                  </el-form-item>
                </div>
                <div class="form-grid">
                  <div class="field-group" v-if="fieldStore.isFieldVisible('party_a_contact')">
                    <label>{{ fieldStore.getFieldByCode('party_a_contact')?.fieldName || 'Contact' }}</label>
                    <el-input v-model="newContractForm.partyAContact" />
                  </div>
                  <div class="field-group" v-if="fieldStore.isFieldVisible('party_a_phone')">
                    <label>{{ fieldStore.getFieldByCode('party_a_phone')?.fieldName || 'Phone' }}</label>
                    <el-input v-model="newContractForm.partyAPhone" />
                  </div>
                </div>
                <div class="field-group mt-2" v-if="fieldStore.isFieldVisible('party_a_address')">
                  <label>{{ fieldStore.getFieldByCode('party_a_address')?.fieldName || 'Address' }}</label>
                  <el-input v-model="newContractForm.partyAAddress" type="textarea" :rows="2" />
                </div>
              </div>

              <div class="form-section" v-if="fieldStore.isFieldVisible('party_b_name') || fieldStore.isFieldVisible('party_b_contact') || fieldStore.isFieldVisible('party_b_phone') || fieldStore.isFieldVisible('party_b_address')">
                <h3 class="section-header text-purple">{{ $t('contract.partyB') }}</h3>
                <div class="field-group" v-if="fieldStore.isFieldVisible('party_b_name')">
                  <label>{{ fieldStore.getFieldByCode('party_b_name')?.fieldName || 'Entity Name' }}</label>
                  <el-form-item prop="partyBName">
                    <el-input v-model="newContractForm.partyBName" />
                  </el-form-item>
                </div>
                <div class="form-grid">
                  <div class="field-group" v-if="fieldStore.isFieldVisible('party_b_contact')">
                    <label>{{ fieldStore.getFieldByCode('party_b_contact')?.fieldName || 'Contact' }}</label>
                    <el-input v-model="newContractForm.partyBContact" />
                  </div>
                  <div class="field-group" v-if="fieldStore.isFieldVisible('party_b_phone')">
                    <label>{{ fieldStore.getFieldByCode('party_b_phone')?.fieldName || 'Phone' }}</label>
                    <el-input v-model="newContractForm.partyBPhone" />
                  </div>
                </div>
                <div class="field-group mt-2" v-if="fieldStore.isFieldVisible('party_b_address')">
                  <label>{{ fieldStore.getFieldByCode('party_b_address')?.fieldName || 'Address' }}</label>
                  <el-input v-model="newContractForm.partyBAddress" type="textarea" :rows="2" />
                </div>
              </div>
            </div>

            <!-- Extended Fields -->
            <div class="form-section" v-if="contractFields.some(f => f.source === 'EXTEND')">
              <h3 class="section-header">Additional Fields</h3>
              <div class="form-grid-3">
                <div 
                  v-for="field in contractFields.filter(f => f.source === 'EXTEND')" 
                  :key="field.fieldCode" 
                  class="field-group"
                >
                  <label>{{ field.fieldName }}</label>
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
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane :label="$t('contract.financials')" name="financials">
            <div class="form-section" v-if="fieldStore.isFieldVisible('currency_type') || fieldStore.isFieldVisible('contract_amount') || fieldStore.isFieldVisible('tax_rate') || fieldStore.isFieldVisible('tax_amount') || fieldStore.isFieldVisible('total_amount_with_tax') || fieldStore.isFieldVisible('payment_method') || fieldStore.isFieldVisible('payment_term')">
              <h3 class="section-header">Financial Details</h3>
              <div class="form-grid-3">
                <div class="field-group" v-if="fieldStore.isFieldVisible('currency_type')">
                  <label>{{ fieldStore.getFieldByCode('currency_type')?.fieldName || 'Currency' }}</label>
                  <el-select v-model="newContractForm.currencyType" style="width: 100%">
                    <el-option label="USD" value="USD" />
                    <el-option label="CNY" value="CNY" />
                    <el-option label="EUR" value="EUR" />
                  </el-select>
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('contract_amount')">
                  <label>{{ fieldStore.getFieldByCode('contract_amount')?.fieldName || $t('contract.amount') }}</label>
                  <el-input-number v-model="newContractForm.contractAmount" :precision="2" style="width: 100%" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('tax_rate')">
                  <label>{{ fieldStore.getFieldByCode('tax_rate')?.fieldName || 'Tax Rate (%)' }}</label>
                  <el-input-number v-model="newContractForm.taxRate" :precision="2" style="width: 100%" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('tax_amount')">
                  <label>{{ fieldStore.getFieldByCode('tax_amount')?.fieldName || 'Tax Amount' }}</label>
                  <el-input-number v-model="newContractForm.taxAmount" :precision="2" style="width: 100%" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('total_amount_with_tax')">
                  <label>{{ fieldStore.getFieldByCode('total_amount_with_tax')?.fieldName || 'Total Amount' }}</label>
                  <el-input-number v-model="newContractForm.totalAmountWithTax" :precision="2" style="width: 100%" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('payment_method')">
                  <label>{{ fieldStore.getFieldByCode('payment_method')?.fieldName || 'Payment Method' }}</label>
                  <el-select v-model="newContractForm.paymentMethod" style="width: 100%">
                    <el-option label="Bank Transfer" value="Bank Transfer" />
                    <el-option label="Check" value="Check" />
                    <el-option label="Cash" value="Cash" />
                  </el-select>
                </div>
                <div class="field-group full-width" v-if="fieldStore.isFieldVisible('payment_term')">
                  <label>{{ fieldStore.getFieldByCode('payment_term')?.fieldName || 'Payment Terms' }}</label>
                  <el-input v-model="newContractForm.paymentTerm" type="textarea" :rows="3" />
                </div>
              </div>
            </div>
            
            <div class="form-section" v-if="fieldStore.isFieldVisible('invoice_title') || fieldStore.isFieldVisible('taxpayer_id') || fieldStore.isFieldVisible('invoice_type')">
              <h3 class="section-header">Billing Info</h3>
              <div class="form-grid-2">
                <div class="field-group" v-if="fieldStore.isFieldVisible('invoice_title')">
                  <label>{{ fieldStore.getFieldByCode('invoice_title')?.fieldName || 'Invoice Title' }}</label>
                  <el-input v-model="newContractForm.invoiceTitle" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('taxpayer_id')">
                  <label>{{ fieldStore.getFieldByCode('taxpayer_id')?.fieldName || 'Taxpayer ID' }}</label>
                  <el-input v-model="newContractForm.taxpayerId" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('invoice_type')">
                  <label>{{ fieldStore.getFieldByCode('invoice_type')?.fieldName || 'Invoice Type' }}</label>
                  <el-input v-model="newContractForm.invoiceType" />
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane :label="$t('contract.performance')" name="performance">
            <div class="form-section" v-if="fieldStore.isFieldVisible('subject_type') || fieldStore.isFieldVisible('subject_quantity') || fieldStore.isFieldVisible('unit_price') || fieldStore.isFieldVisible('subject_desc')">
              <h3 class="section-header">Subject Matter</h3>
              <div class="form-grid-3">
                <div class="field-group" v-if="fieldStore.isFieldVisible('subject_type')">
                  <label>{{ fieldStore.getFieldByCode('subject_type')?.fieldName || 'Type' }}</label>
                  <el-select v-model="newContractForm.subjectType" style="width: 100%">
                    <el-option label="Goods" value="Goods" />
                    <el-option label="Services" value="Services" />
                  </el-select>
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('subject_quantity')">
                  <label>{{ fieldStore.getFieldByCode('subject_quantity')?.fieldName || 'Quantity' }}</label>
                  <el-input-number v-model="newContractForm.subjectQuantity" style="width: 100%" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('unit_price')">
                  <label>{{ fieldStore.getFieldByCode('unit_price')?.fieldName || 'Unit Price' }}</label>
                  <el-input-number v-model="newContractForm.unitPrice" :precision="2" style="width: 100%" />
                </div>
                <div class="field-group full-width" v-if="fieldStore.isFieldVisible('subject_desc')">
                  <label>{{ fieldStore.getFieldByCode('subject_desc')?.fieldName || 'Description' }}</label>
                  <el-input v-model="newContractForm.subjectDesc" type="textarea" :rows="2" />
                </div>
              </div>
            </div>

            <div class="form-section" v-if="fieldStore.isFieldVisible('performance_method') || fieldStore.isFieldVisible('performance_location') || fieldStore.isFieldVisible('performance_start_date') || fieldStore.isFieldVisible('performance_end_date') || fieldStore.isFieldVisible('quality_standard')">
              <h3 class="section-header">Performance Terms</h3>
              <div class="form-grid-2">
                <div class="field-group" v-if="fieldStore.isFieldVisible('performance_method')">
                  <label>{{ fieldStore.getFieldByCode('performance_method')?.fieldName || 'Method' }}</label>
                  <el-input v-model="newContractForm.performanceMethod" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('performance_location')">
                  <label>{{ fieldStore.getFieldByCode('performance_location')?.fieldName || 'Location' }}</label>
                  <el-input v-model="newContractForm.performanceLocation" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('performance_start_date')">
                  <label>{{ fieldStore.getFieldByCode('performance_start_date')?.fieldName || 'Start Date' }}</label>
                  <el-date-picker v-model="newContractForm.performanceStartDate" type="date" style="width: 100%" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('performance_end_date')">
                  <label>{{ fieldStore.getFieldByCode('performance_end_date')?.fieldName || 'End Date' }}</label>
                  <el-date-picker v-model="newContractForm.performanceEndDate" type="date" style="width: 100%" />
                </div>
                <div class="field-group full-width" v-if="fieldStore.isFieldVisible('quality_standard')">
                  <label>{{ fieldStore.getFieldByCode('quality_standard')?.fieldName || 'Quality Standard' }}</label>
                  <el-input v-model="newContractForm.qualityStandard" type="textarea" />
                </div>
              </div>
            </div>
          </el-tab-pane>

          <!-- Legal & Dates Tab -->
          <el-tab-pane :label="$t('contract.legalDates')" name="legal">
            <div class="form-section" v-if="fieldStore.isFieldVisible('sign_date') || fieldStore.isFieldVisible('effective_date') || fieldStore.isFieldVisible('expire_date')">
              <h3 class="section-header">Important Dates</h3>
              <div class="form-grid-3">
                <div class="field-group" v-if="fieldStore.isFieldVisible('sign_date')">
                  <label>{{ fieldStore.getFieldByCode('sign_date')?.fieldName || 'Sign Date' }}</label>
                  <el-date-picker v-model="newContractForm.signDate" type="date" style="width: 100%" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('effective_date')">
                  <label>{{ fieldStore.getFieldByCode('effective_date')?.fieldName || 'Effective Date' }}</label>
                  <el-date-picker v-model="newContractForm.effectiveDate" type="date" style="width: 100%" />
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('expire_date')">
                  <label>{{ fieldStore.getFieldByCode('expire_date')?.fieldName || 'Expire Date' }}</label>
                  <el-date-picker v-model="newContractForm.expireDate" type="date" style="width: 100%" />
                </div>
              </div>
            </div>

            <div class="form-section" v-if="fieldStore.isFieldVisible('dispute_resolution') || fieldStore.isFieldVisible('governing_law') || fieldStore.isFieldVisible('remark')">
              <h3 class="section-header">Legal Provisions</h3>
              <div class="form-grid-2">
                <div class="field-group" v-if="fieldStore.isFieldVisible('dispute_resolution')">
                  <label>{{ fieldStore.getFieldByCode('dispute_resolution')?.fieldName || 'Dispute Resolution' }}</label>
                  <el-select v-model="newContractForm.disputeResolution" style="width: 100%">
                    <el-option label="Negotiation" value="Negotiation" />
                    <el-option label="Arbitration" value="Arbitration" />
                    <el-option label="Litigation" value="Litigation" />
                  </el-select>
                </div>
                <div class="field-group" v-if="fieldStore.isFieldVisible('governing_law')">
                  <label>{{ fieldStore.getFieldByCode('governing_law')?.fieldName || 'Governing Law' }}</label>
                  <el-input v-model="newContractForm.governingLaw" />
                </div>
                <div class="field-group full-width" v-if="fieldStore.isFieldVisible('remark')">
                  <label>{{ fieldStore.getFieldByCode('remark')?.fieldName || 'Remark' }}</label>
                  <el-input v-model="newContractForm.remark" type="textarea" />
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <el-button @click="showNewContractDialog = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleCreateContract" :loading="creating">{{ $t('common.create') }}</el-button>
      </template>
    </el-dialog>

    <!-- Toolbar -->
    <div class="toolbar-container" :class="{ 'is-sticky': isSticky }">
      <div class="toolbar-left">
        <el-radio-group v-model="activeTab" size="large">
          <el-radio-button value="all">{{ $t('contract.allContracts') }}</el-radio-button>
          <el-radio-button value="my">{{ $t('contract.myPending') }}</el-radio-button>
          <el-radio-button value="shared">{{ $t('contract.sharedWithMe') }}</el-radio-button>
        </el-radio-group>
      </div>
      <div class="toolbar-right">
        <div class="search-input">
          <el-icon><Search /></el-icon>
          <input 
            v-model="searchQuery" 
            :placeholder="$t('contract.searchPlaceholder')" 
            @keyup.enter="handleSearch"
          />
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
          <span>{{ selectedRows.length }}</span> {{ $t('contract.itemsSelected') }}
        </div>
        <div class="batch-actions">
          <el-button text bg><el-icon><Download /></el-icon> {{ $t('common.export') }}</el-button>
          <el-button text bg><el-icon><User /></el-icon> {{ $t('contract.assign') }}</el-button>
          <el-button text bg type="danger" @click="handleBatchArchive"><el-icon><Delete /></el-icon> {{ $t('contract.archive') }}</el-button>
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
        <el-table-column v-if="fieldStore.isFieldVisible('contract_no')" :label="$t('contract.no')" prop="contractNo" width="160">
          <template #default="{ row }">
            <span class="font-mono font-medium" :style="getFieldStyle('contract_no')">{{ row.contractNo }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="fieldStore.isFieldVisible('contract_name')" :label="$t('contract.name')" min-width="250">
          <template #default="{ row }">
            <div class="contract-name-cell">
              <div class="name-text" :style="getFieldStyle('contract_name')">{{ row.contractName }}</div>
              <div class="party-text">{{ row.partyAName }} vs {{ row.partyBName }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-if="fieldStore.isFieldVisible('amount')" :label="$t('contract.amount')" prop="contractAmount" width="150" sortable>
          <template #default="{ row }">
            <span class="amount-text" :style="getFieldStyle('amount')">{{ formatCurrency(row.contractAmount || row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="fieldStore.isFieldVisible('status')" :label="$t('contract.status')" prop="status" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="light" round :style="getFieldStyle('status')">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="fieldStore.isFieldVisible('create_time')" :label="$t('contract.date')" prop="createTime" width="180" sortable>
          <template #default="{ row }">
            <div class="date-cell" :style="getFieldStyle('create_time')">
              <span>{{ formatDate(row.createTime) }}</span>
              <span class="sub-text">Created</span>
            </div>
          </template>
        </el-table-column>

        <!-- Dynamic Extended Columns -->
        <el-table-column 
          v-for="field in contractFields.filter(f => f.source === 'EXTEND' && fieldStore.isFieldVisible(f.fieldCode))" 
          :key="field.fieldCode" 
          :label="field.fieldName"
          :prop="field.fieldCode"
          width="150"
        >
          <template #default="{ row }">
            <span :style="getFieldStyle(field.fieldCode)">
              {{ row.extendedFields ? row.extendedFields[field.fieldCode] : 'N/A' }}
            </span>
          </template>
        </el-table-column>

        <el-table-column :label="$t('common.actions')" width="100" fixed="right">
          <template #default="{ row }">
            <el-button circle text type="primary" @click="viewDetail(row.contractId)">
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
    <el-drawer v-model="showFilterDrawer" :title="$t('contract.advancedFilters')" size="400px">
      <el-form label-position="top">
        <el-form-item :label="$t('contract.dateRange')">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="To"
            start-placeholder="Start"
            end-placeholder="End"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item :label="$t('contract.type')">
          <el-select v-model="filterForm.type" placeholder="Select type" style="width: 100%">
            <el-option label="NDA" value="nda" />
            <el-option label="Service Agreement" value="service" />
            <el-option label="Sales Contract" value="sales" />
          </el-select>
        </el-form-item>
        <el-form-item label="Amount Range">
          <div class="amount-range">
            <el-input v-model="filterForm.minAmount" :placeholder="$t('contract.min')" />
            <span class="separator">-</span>
            <el-input v-model="filterForm.maxAmount" :placeholder="$t('contract.max')" />
          </div>
        </el-form-item>
        <el-form-item :label="$t('contract.status')">
          <el-checkbox-group v-model="filterForm.status">
            <el-checkbox value="Draft">Draft</el-checkbox>
            <el-checkbox value="Pending">Pending</el-checkbox>
            <el-checkbox value="Active">Active</el-checkbox>
            <el-checkbox value="Expired">Expired</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="showFilterDrawer = false">{{ $t('common.cancel') }}</el-button>
          <el-button type="primary" @click="applyFilters">{{ $t('contract.applyFilters') }}</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Filter, Search, Files, ArrowDown, ArrowRight, Download, User, Delete, Close, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useFieldStore } from '@/stores/fieldStore'

const fieldStore = useFieldStore()
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

const activeNewContractTab = ref('general')

const newContractForm = ref({
  contractNo: '',
  contractName: '',
  contractType: '',
  contractStatus: 'Draft',
  partyAName: '',
  partyAContact: '',
  partyAPhone: '',
  partyAAddress: '',
  partyBName: '',
  partyBContact: '',
  partyBPhone: '',
  partyBAddress: '',
  thirdPartyFlag: false,
  thirdPartyInfo: '',
  currencyType: 'USD',
  contractAmount: 0,
  taxRate: 0,
  taxAmount: 0,
  totalAmountWithTax: 0,
  paymentMethod: '',
  paymentTerm: '',
  invoiceType: '',
  invoiceTitle: '',
  taxpayerId: '',
  signDate: '',
  effectiveDate: '',
  expireDate: '',
  performanceMethod: '',
  performanceLocation: '',
  performanceStartDate: '',
  performanceEndDate: '',
  qualityStandard: '',
  disputeResolution: '',
  governingLaw: '',
  legalReviewFlag: false,
  legalReviewOpinion: '',
  subjectType: '',
  subjectQuantity: 0,
  unitPrice: 0,
  subjectDesc: '',
  remark: '',
  extendedFields: {}
})

const rules = {
  contractNo: [{ required: true, message: 'Required', trigger: 'blur' }],
  contractName: [{ required: true, message: 'Required', trigger: 'blur' }],
  partyAName: [{ required: true, message: 'Required', trigger: 'blur' }],
  partyBName: [{ required: true, message: 'Required', trigger: 'blur' }]
}


const contractFields = ref([])

const fetchMetadata = async () => {
  try {
    const response = await fetch('/api/metadata/contract-fields', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'X-Tenant-ID': localStorage.getItem('tenantId')
      }
    })
    if (response.ok) {
      const result = await response.json()
      contractFields.value = result.data
    }
  } catch (error) {
    console.error('Failed to fetch metadata', error)
    ElMessage.error('Failed to load contract fields')
  }
}

const fetchContracts = async () => {
  try {
    let url = `/api/contracts?page=${currentPage.value - 1}&size=${pageSize.value}`
    if (searchQuery.value) {
      url += `&query=${encodeURIComponent(searchQuery.value)}`
    }
    const response = await fetch(url, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'X-Tenant-ID': localStorage.getItem('tenantId')
      }
    })
    if (response.ok) {
      const result = await response.json()
      contracts.value = result.data.content
      total.value = result.data.totalElements
    } else {
      ElMessage.error('Failed to fetch contracts: ' + response.statusText)
    }
  } catch (error) {
    console.error('Failed to fetch contracts', error)
    ElMessage.error('Network error while fetching contracts')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchContracts()
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
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'X-Tenant-ID': localStorage.getItem('tenantId')
          },
          body: JSON.stringify({
            ...newContractForm.value,
            contractStatus: 'DRAFT'
          })
        })
        if (response.ok) {
          ElMessage.success('Contract created successfully')
          showNewContractDialog.value = false
          currentPage.value = 1 // Reset to first page to see the new record
          fetchContracts()
        } else {
          ElMessage.error('Failed to create contract')
        }
      } catch (error) {
        console.error('Failed to create contract', error)
        ElMessage.error('System error while creating contract')
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
  fieldStore.fetchFieldConfigs()
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

const getFieldStyle = (fieldCode) => {
  const field = fieldStore.fields.find(f => f.fieldCode === fieldCode)
  if (!field) return {}
  return {
    color: field.fieldColor,
    fontWeight: field.fieldStyles?.includes('bold') ? 'bold' : 'normal',
    fontStyle: field.fieldStyles?.includes('italic') ? 'italic' : 'normal',
  }
}

// Helpers
const formatCurrency = (val) => {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(val || 0)
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

/* Detailed Form Styles */
.custom-tabs-dialog {
  min-height: 400px;
}

.form-section {
  margin-bottom: 24px;
  background: #F8FAFC;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

.section-header {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin: 0 0 16px 0;
  padding-bottom: 8px;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.form-grid-3 {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.form-grid-2 {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.full-width {
  grid-column: 1 / -1;
}

.field-group {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.field-group label {
  font-size: 11px;
  color: #64748B;
  font-weight: 600;
  text-transform: uppercase;
}

.text-blue { color: #2563EB; }
.text-purple { color: #8B5CF6; }

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
