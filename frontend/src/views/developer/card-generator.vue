<template>
  <div class="card-generator-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Embedded Card Generator</h1>
        <p class="page-subtitle">Generate Web Components to embed contract data into any CRM or external system.</p>
      </div>
    </div>

    <div class="generator-layout">
      <!-- Config Panel -->
      <div class="config-panel glass-card">
        <h3 class="panel-title">Configuration</h3>
        <el-form label-position="top">
          <el-form-item label="Target System">
            <el-select v-model="config.target" class="w-full">
              <el-option label="Salesforce Lightning" value="sfdc" />
              <el-option label="HubSpot" value="hubspot" />
              <el-option label="Custom Web App" value="web" />
            </el-select>
          </el-form-item>

          <el-form-item label="Theme Color">
            <div class="color-picker">
              <div 
                v-for="color in colors" 
                :key="color" 
                class="color-dot"
                :style="{ background: color }"
                :class="{ active: config.color === color }"
                @click="config.color = color"
              ></div>
            </div>
          </el-form-item>

          <el-form-item label="Fields to Display">
            <el-checkbox-group v-model="config.selectedFields">
              <div class="field-grid">
                <el-checkbox 
                  v-for="field in contractFields" 
                  :key="field.fieldCode" 
                  :label="field.fieldCode"
                  class="field-checkbox"
                >
                  {{ field.fieldName }}
                </el-checkbox>
              </div>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item label="Card Size">
             <el-radio-group v-model="config.size">
               <el-radio-button label="small">Small</el-radio-button>
               <el-radio-button label="medium">Medium</el-radio-button>
               <el-radio-button label="large">Large</el-radio-button>
             </el-radio-group>
          </el-form-item>
        </el-form>
      </div>

      <!-- Preview & Code -->
      <div class="preview-panel">
        <div class="panel-section">
          <h3 class="panel-title">Live Preview</h3>
          <div class="preview-stage glass-card-dark">
             <!-- Mock Card -->
             <div class="mock-card" :style="{ borderTop: `4px solid ${config.color}`, width: getWidth(), ...cardStyles.container }">
               <div class="card-header" :style="cardStyles.header">
                 <div class="card-icon" :style="{ background: config.color, ...cardStyles.icon }"><el-icon><Document /></el-icon></div>
                 <div class="card-meta">
                   <div class="card-title">Contract #2024-001</div>
                   <div class="card-sub">Procurement Agreement</div>
                 </div>
               </div>
               
               <div class="card-body">
                 <!-- General Section -->
                 <div v-if="hasFieldsInGroup('general')" class="card-section">
                   <div class="section-title" :style="cardStyles.sectionTitle">General</div>
                   <div v-for="field in getFieldsInGroup('general')" :key="field.fieldCode" class="field-row" :style="cardStyles.field">
                     <label :style="cardStyles.label">{{ field.fieldName }}</label>
                     <span :style="cardStyles.value">{{ getMockValue(field.fieldCode) }}</span>
                   </div>
                 </div>

                 <!-- Financials Section -->
                 <div v-if="hasFieldsInGroup('financials')" class="card-section">
                   <div class="section-title" :style="cardStyles.sectionTitle">Financials</div>
                   <div v-for="field in getFieldsInGroup('financials')" :key="field.fieldCode" class="field-row" :style="cardStyles.field">
                     <label :style="cardStyles.label">{{ field.fieldName }}</label>
                     <span :style="cardStyles.value">{{ getMockValue(field.fieldCode) }}</span>
                   </div>
                 </div>

                 <!-- Performance Section -->
                 <div v-if="hasFieldsInGroup('performance')" class="card-section">
                   <div class="section-title" :style="cardStyles.sectionTitle">Performance</div>
                   <div v-for="field in getFieldsInGroup('performance')" :key="field.fieldCode" class="field-row" :style="cardStyles.field">
                     <label :style="cardStyles.label">{{ field.fieldName }}</label>
                     <span :style="cardStyles.value">{{ getMockValue(field.fieldCode) }}</span>
                   </div>
                 </div>

                 <!-- Legal Section -->
                 <div v-if="hasFieldsInGroup('legal')" class="card-section">
                   <div class="section-title" :style="cardStyles.sectionTitle">Legal</div>
                   <div v-for="field in getFieldsInGroup('legal')" :key="field.fieldCode" class="field-row" :style="cardStyles.field">
                     <label :style="cardStyles.label">{{ field.fieldName }}</label>
                     <span :style="cardStyles.value">{{ getMockValue(field.fieldCode) }}</span>
                   </div>
                 </div>

                 <!-- Extended Section -->
                 <div v-if="hasFieldsInGroup('extended')" class="card-section">
                   <div class="section-title" :style="cardStyles.sectionTitle">Additional</div>
                   <div v-for="field in getFieldsInGroup('extended')" :key="field.fieldCode" class="field-row" :style="cardStyles.field">
                     <label :style="cardStyles.label">{{ field.fieldName }}</label>
                     <span :style="cardStyles.value">{{ getMockValue(field.fieldCode) }}</span>
                   </div>
                 </div>
               </div>

               <div class="card-footer">
                 <button class="action-btn" :style="{ background: config.color }">View Details</button>
               </div>
             </div>
          </div>
        </div>

        <div class="panel-section">
          <h3 class="panel-title">Embed Code</h3>
          <div class="code-block glass-input">
            <el-button size="small" icon="CopyDocument" class="copy-btn" @click="copyCode">Copy</el-button>
            <pre>{{ generateCode() }}</pre>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { Document, Warning, CopyDocument } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const colors = ['#3B82F6', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6']
const contractFields = ref([])

const config = reactive({
  target: 'web',
  color: '#3B82F6',
  selectedFields: ['contractAmount', 'contractStatus', 'effectiveDate', 'expireDate'],
  size: 'medium',
  customLabels: {}
})

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
  }
}

onMounted(() => {
  fetchMetadata()
})

const getWidth = () => {
  if (config.size === 'small') return '280px'
  if (config.size === 'medium') return '320px'
  return '100%'
}

const hexToRgba = (hex, alpha) => {
  // Simplified for demo
  return hex // In real app, convert hex to rgba
}

const generateCode = () => {
  return `<!-- Contract Master Embed Code -->
<script src="https://cdn.contract-master.com/sdk/v1.js"><\/script>

<contract-card
  id="ctr_123456"
  theme="${config.color}"
  fields="${config.selectedFields.join(',')}"
  size="${config.size}"
  api-key="YOUR_PUBLIC_KEY"
></contract-card>`
}

// Unified visual styles
const cardStyles = {
  container: {
    background: 'white',
    borderRadius: '12px',
    boxShadow: '0 10px 30px rgba(0,0,0,0.2)',
    overflow: 'hidden',
    fontFamily: "'Inter', sans-serif"
  },
  header: {
    padding: '16px',
    display: 'flex',
    alignItems: 'flex-start',
    gap: '12px',
    borderBottom: '1px solid rgba(0,0,0,0.05)'
  },
  icon: {
    width: '32px',
    height: '32px',
    borderRadius: '8px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    color: 'white'
  },
  sectionTitle: {
    fontSize: '12px',
    fontWeight: '600',
    color: '#94A3B8',
    textTransform: 'uppercase',
    letterSpacing: '0.5px',
    marginBottom: '8px',
    marginTop: '12px',
    borderBottom: '1px dashed #E2E8F0',
    paddingBottom: '4px'
  },
  field: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'baseline',
    padding: '4px 0',
    fontSize: '12px'
  },
  label: {
    color: '#64748B'
  },
  value: {
    fontWeight: '500',
    color: '#1E293B',
    textAlign: 'right',
    maxWidth: '60%'
  }
}

// Field Groups for Layout
const fieldGroups = {
  general: ['contractNo', 'contractName', 'contractType', 'contractStatus', 'crmContractId', 'partyAName', 'partyAContact', 'partyAPhone', 'partyAAddress', 'partyBName', 'partyBContact', 'partyBPhone', 'partyBAddress'],
  financials: ['contractAmount', 'taxRate', 'taxAmount', 'totalAmountWithTax', 'currencyType', 'paymentMethod', 'paymentTerm', 'invoiceTitle', 'invoiceType', 'taxpayerId'],
  performance: ['subjectType', 'subjectDesc', 'subjectQuantity', 'unitPrice', 'performanceMethod', 'performanceLocation', 'performanceStartDate', 'performanceEndDate', 'qualityStandard'],
  legal: ['signDate', 'effectiveDate', 'expireDate', 'disputeResolution', 'governingLaw', 'legalReviewFlag', 'legalReviewOpinion', 'remark', 'createUser', 'createTime']
}

const getFieldsInGroup = (group) => {
  const selected = config.selectedFields
  if (group === 'extended') {
    return contractFields.value.filter(f => f.source === 'EXTEND' && selected.includes(f.fieldCode))
  }
  const groupFields = fieldGroups[group] || []
  return contractFields.value.filter(f => groupFields.includes(f.fieldCode) && selected.includes(f.fieldCode))
}

const hasFieldsInGroup = (group) => {
  return getFieldsInGroup(group).length > 0
}

const getFieldName = (code) => {
  const f = contractFields.value.find(field => field.fieldCode === code)
  return f ? f.fieldName : code
}

// Mock Data for Preview
const mockData = {
  contractNo: 'CON-2024-001',
  contractName: 'Strategic Partnership Agreement',
  contractType: 'Framework',
  contractStatus: 'Active',
  contractAmount: 1250000,
  taxRate: 10,
  currencyType: 'USD',
  effectiveDate: '2024-01-01',
  expireDate: '2024-12-31',
  partyAName: 'Acme Corp',
  partyBName: 'TechSolutions Inc',
  legalReviewFlag: true,
  legalReviewOpinion: 'Approved',
  performanceLocation: 'Headquarters',
  qualityStandard: 'ISO 9001'
}

const getMockValue = (code) => {
  if (mockData[code] !== undefined) {
    if (code === 'contractAmount' || code === 'totalAmountWithTax') {
      return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(mockData[code])
    }
    if (code === 'effectiveDate' || code === 'expireDate') {
      return mockData[code]
    }
    return mockData[code]
  }
  return 'Sample Value'
}
</script>

<style scoped>
.card-generator-page {
  display: flex;
  flex-direction: column;
}

.page-header {
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

.generator-layout {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 32px;
  align-items: start;
}

.glass-card {
  padding: 24px;
  border-radius: 16px;
}

.glass-card-dark {
  background: #0F172A;
  border-radius: 16px;
  padding: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  border: 1px solid rgba(255,255,255,0.1);
}

.panel-title {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin: 0 0 20px 0;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(226, 232, 240, 0.6);
}

.color-picker {
  display: flex;
  gap: 12px;
}

.color-dot {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  cursor: pointer;
  border: 2px solid transparent;
  transition: transform 0.2s;
}

.color-dot.active {
  transform: scale(1.2);
  border-color: #fff;
  box-shadow: 0 0 0 2px #94A3B8;
}

.w-full {
  width: 100%;
}

.w-full :deep(.el-checkbox__label) {
  width: 100%;
}

/* Mock Card Styles */
.mock-card {
  background: white;
  border-radius: 12px;
  border-top: 4px solid #3B82F6;
  box-shadow: 0 10px 30px rgba(0,0,0,0.2);
  overflow: hidden;
  font-family: 'Inter', sans-serif;
}

.card-header {
  padding: 16px;
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.card-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.card-meta {
  flex: 1;
}

.card-title {
  font-size: 14px;
  font-weight: 700;
  color: #1E293B;
}

.card-sub {
  font-size: 11px;
  color: #64748B;
}

.status-badge {
  font-size: 10px;
  font-weight: 600;
  padding: 2px 6px;
  border-radius: 4px;
}

.card-body {
  padding: 0 16px 16px 16px;
}

.field-row {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
}

.field-row label {
  color: #64748B;
}

.field-row span {
  font-weight: 500;
  color: #1E293B;
}

.field-row .amount {
  font-size: 16px;
  font-weight: 700;
}

.risk-box {
  margin-top: 12px;
  background: #FFF7ED;
  color: #C2410C;
  font-size: 11px;
  padding: 6px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.card-footer {
  padding: 12px 16px;
  border-top: 1px solid #F1F5F9;
  text-align: center;
}

.action-btn {
  border: none;
  color: white;
  padding: 6px 16px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  width: 100%;
}

.code-block {
  background: #1E293B;
  padding: 16px;
  border-radius: 8px;
  position: relative;
}

.code-block pre {
  margin: 0;
  color: #E2E8F0;
  font-family: 'Fira Code', monospace;
  font-size: 12px;
  white-space: pre-wrap;
}

.copy-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  background: rgba(255,255,255,0.1);
  border: none;
  color: white;
}

.panel-section + .panel-section {
  margin-top: 32px;
}
</style>
