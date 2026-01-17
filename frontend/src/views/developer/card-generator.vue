<template>
  <div class="card-generator-page">
    <!-- Top Action Bar (Matching detail.vue) -->
    <div class="detail-header glass-blur">
      <div class="header-left">
        <el-button circle class="back-btn" @click="$router.back()">
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <div class="header-title">
          <div class="flex items-center gap-2">
            <span class="contract-no">DEVELOPER TOOLS</span>
            <el-tag type="info" size="small" round>BETA</el-tag>
          </div>
          <h1 class="contract-name">Embedded Card Generator</h1>
        </div>
      </div>
      <div class="header-right">
        <el-button type="primary" @click="copyCode" class="glass-btn">
          <el-icon><CopyDocument /></el-icon> Copy Embed Code
        </el-button>
      </div>
    </div>

    <div class="detail-container">
      <!-- Main Content (Left) - Preview Area -->
      <div class="main-form-area">
        <div class="preview-header mb-6">
          <h3 class="section-header">Live Preview</h3>
          <p class="text-sm text-slate-500">This is how the card will appear when embedded in external systems.</p>
        </div>

        <div class="preview-stage glass-card-dark">
          <!-- Mock Card with Tabs (Matching detail.vue logic) -->
          <div class="mock-card" :style="{ borderTop: `6px solid ${config.color}`, width: cardWidth, ...cardStyles.container }">
            <div class="card-header" :style="cardStyles.header">
              <div class="card-icon" :style="{ background: config.color, ...cardStyles.icon }">
                <el-icon><Document /></el-icon>
              </div>
              <div class="card-meta">
                <div class="card-title" :style="cardStyles.title">Contract #{{ mockData.contractNo }}</div>
                <div class="card-sub" :style="cardStyles.subtitle">{{ mockData.contractName }}</div>
              </div>
            </div>
            
            <div class="card-body-wrapper">
              <el-tabs v-model="activeTab" class="card-tabs-mini">
                <el-tab-pane label="General" name="general" v-if="hasFieldsInGroup('general')">
                  <div class="card-grid-mini">
                    <div v-for="field in getFieldsInGroup('general')" :key="field.fieldCode" class="field-row-mini" :style="cardStyles.field">
                      <label :style="cardStyles.label">{{ field.fieldName }}</label>
                      <span :style="cardStyles.value">{{ getMockValue(field.fieldCode) }}</span>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="Financials" name="financials" v-if="hasFieldsInGroup('financials')">
                  <div class="card-grid-mini">
                    <div v-for="field in getFieldsInGroup('financials')" :key="field.fieldCode" class="field-row-mini" :style="cardStyles.field">
                      <label :style="cardStyles.label">{{ field.fieldName }}</label>
                      <span :style="cardStyles.value">{{ getMockValue(field.fieldCode) }}</span>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="Performance" name="performance" v-if="hasFieldsInGroup('performance')">
                  <div class="card-grid-mini">
                    <div v-for="field in getFieldsInGroup('performance')" :key="field.fieldCode" class="field-row-mini" :style="cardStyles.field">
                      <label :style="cardStyles.label">{{ field.fieldName }}</label>
                      <span :style="cardStyles.value">{{ getMockValue(field.fieldCode) }}</span>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="Legal" name="legal" v-if="hasFieldsInGroup('legal')">
                  <div class="card-grid-mini">
                    <div v-for="field in getFieldsInGroup('legal')" :key="field.fieldCode" class="field-row-mini" :style="cardStyles.field">
                      <label :style="cardStyles.label">{{ field.fieldName }}</label>
                      <span :style="cardStyles.value">{{ getMockValue(field.fieldCode) }}</span>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="Extra" name="extended" v-if="hasFieldsInGroup('extended')">
                  <div class="card-grid-mini">
                    <div v-for="field in getFieldsInGroup('extended')" :key="field.fieldCode" class="field-row-mini" :style="cardStyles.field">
                      <label :style="cardStyles.label">{{ field.fieldName }}</label>
                      <span :style="cardStyles.value">{{ getMockValue(field.fieldCode) }}</span>
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>

            <div class="card-footer">
              <button class="action-btn" :style="{ background: config.color }">View Full Details</button>
            </div>
          </div>
        </div>

        <div class="mt-8">
          <h3 class="section-header">Embed Code</h3>
          <div class="code-block glass-input mt-4">
            <el-button size="small" icon="CopyDocument" class="copy-btn" @click="copyCode">Copy</el-button>
            <pre>{{ generateCode() }}</pre>
          </div>
        </div>
      </div>

      <!-- Right Side Panel - Configuration -->
      <div class="side-panel glass-card">
        <div class="panel-header">
          <h3>Configuration</h3>
        </div>
        
        <div class="config-content">
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

            <el-form-item label="Card Size">
              <el-radio-group v-model="config.size" class="w-full">
                <el-radio-button value="small">S</el-radio-button>
                <el-radio-button value="medium">M</el-radio-button>
                <el-radio-button value="large">L</el-radio-button>
              </el-radio-group>
            </el-form-item>

            <el-divider />

            <el-form-item label="Fields to Display">
              <el-input placeholder="Search fields..." class="mb-2" size="small" v-model="fieldSearch" />
              <div class="field-list-scrollable">
                <el-checkbox-group v-model="config.selectedFields">
                  <div class="field-item" v-for="field in filteredFields" :key="field.fieldCode">
                    <el-checkbox :value="field.fieldCode">
                      {{ field.fieldName }}
                    </el-checkbox>
                  </div>
                </el-checkbox-group>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue'
import { Document, Warning, CopyDocument, ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const colors = ['#3B82F6', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6']
const contractFields = ref([])
const activeTab = ref('general')
const fieldSearch = ref('')

const config = reactive({
  target: 'web',
  color: '#3B82F6',
  selectedFields: ['contractNo', 'contractAmount', 'contractStatus', 'effectiveDate', 'expireDate'],
  size: 'medium',
  customLabels: {}
})

const filteredFields = computed(() => {
  if (!fieldSearch.value) return contractFields.value
  return contractFields.value.filter(f => 
    f.fieldName.toLowerCase().includes(fieldSearch.value.toLowerCase()) ||
    f.fieldCode.toLowerCase().includes(fieldSearch.value.toLowerCase())
  )
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
    } else {
      ElMessage.error('Failed to load available fields')
    }
  } catch (error) {
    console.error('Failed to fetch metadata', error)
    ElMessage.error('Network error loading card fields')
  }
}

onMounted(() => {
  fetchMetadata()
})

const cardWidth = computed(() => {
  const map = {
    'small': '300px',
    'medium': '400px',
    'large': '550px'
  }
  return map[config.size] || '400px'
})

// Unified visual styles to match detail.vue card generator
const cardStyles = {
  container: {
    background: 'white',
    borderRadius: '16px',
    boxShadow: '0 20px 50px rgba(0,0,0,0.3)',
    overflow: 'hidden',
    fontFamily: "'Inter', sans-serif",
    display: 'flex',
    flexDirection: 'column'
  },
  header: {
    padding: '24px',
    display: 'flex',
    alignItems: 'center',
    gap: '16px',
    background: '#F8FAFC',
    borderBottom: '1px solid #E2E8F0'
  },
  title: {
    fontSize: '18px',
    fontWeight: '700',
    color: '#1E293B',
    marginBottom: '2px'
  },
  subtitle: {
    fontSize: '12px',
    color: '#64748B'
  },
  icon: {
    width: '40px',
    height: '40px',
    borderRadius: '10px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    color: 'white',
    fontSize: '20px'
  },
  field: {
    display: 'flex',
    justifyContent: 'space-between',
    padding: '8px 0',
    borderBottom: '1px solid #F1F5F9'
  },
  label: {
    fontSize: '11px',
    color: '#94A3B8',
    fontWeight: '600',
    textTransform: 'uppercase',
    letterSpacing: '0.5px'
  },
  value: {
    fontSize: '13px',
    fontWeight: '500',
    color: '#334155'
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
    if (typeof mockData[code] === 'boolean') return mockData[code] ? 'Yes' : 'No'
    return mockData[code]
  }
  return 'Sample Value'
}

const generateCode = () => {
  return `<iframe src="http://localhost:5173/embed/contract/123?color=${config.color.replace('#', '')}&fields=${config.selectedFields.join(',')}&size=${config.size}" width="${cardWidth.value}" height="600" frameborder="0"></iframe>`
}

const copyCode = () => {
  navigator.clipboard.writeText(generateCode())
  ElMessage.success('Embed code copied to clipboard')
}
</script>

<style scoped>
.card-generator-page {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 80px);
}

.detail-header {
  padding: 16px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(255,255,255,0.2);
}

.glass-blur {
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(12px);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.back-btn {
  background: transparent;
  border: 1px solid rgba(0,0,0,0.1);
}

.contract-no {
  font-family: 'Fira Code', monospace;
  font-size: 12px;
  color: #64748B;
}

.contract-name {
  font-size: 24px;
  font-weight: 700;
  color: #1E293B;
  margin: 0;
  letter-spacing: -0.5px;
}

.detail-container {
  display: flex;
  gap: 24px;
  flex: 1;
  overflow: hidden;
  padding: 24px 32px;
}

.main-form-area {
  flex: 1;
  overflow-y: auto;
  padding-right: 8px;
}

.side-panel {
  width: 320px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(255,255,255,0.4);
  overflow: hidden;
}

.panel-header {
  padding: 16px;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}

.panel-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
}

.config-content {
  padding: 20px;
  overflow-y: auto;
}

.section-header {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin: 0;
}

.preview-stage {
  background: #0F172A;
  border-radius: 20px;
  padding: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 450px;
  border: 1px solid rgba(255,255,255,0.1);
  box-shadow: inset 0 2px 20px rgba(0,0,0,0.5);
}

.card-body-wrapper {
  padding: 0 24px 24px 24px;
  flex: 1;
  overflow-y: auto;
  max-height: 350px;
}

.card-tabs-mini :deep(.el-tabs__header) {
  margin-bottom: 16px;
}

.card-tabs-mini :deep(.el-tabs__item) {
  font-size: 12px;
  height: 32px;
  line-height: 32px;
}

.card-grid-mini {
  display: flex;
  flex-direction: column;
}

.card-footer {
  padding: 16px 24px;
  background: #F8FAFC;
  border-top: 1px solid #E2E8F0;
}

.action-btn {
  border: none;
  color: white;
  padding: 10px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  width: 100%;
  transition: opacity 0.2s;
}

.action-btn:hover {
  opacity: 0.9;
}

.color-picker {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.color-dot {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.2s;
}

.color-dot.active {
  transform: scale(1.15);
  box-shadow: 0 0 0 2px #CBD5E1;
}

.field-list-scrollable {
  max-height: 300px;
  overflow-y: auto;
  padding: 4px;
  border: 1px solid #F1F5F9;
  border-radius: 8px;
  background: white;
}

.field-item {
  padding: 4px 8px;
}

.field-item:hover {
  background: #F8FAFC;
}

.code-block {
  background: #1E293B;
  padding: 24px;
  border-radius: 12px;
  position: relative;
  border: 1px solid rgba(255,255,255,0.1);
}

.code-block pre {
  margin: 0;
  color: #94A3B8;
  font-family: 'Fira Code', monospace;
  font-size: 12px;
  white-space: pre-wrap;
  line-height: 1.6;
}

.copy-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.1);
  color: #E2E8F0;
}

.copy-btn:hover {
  background: rgba(255,255,255,0.1);
  color: white;
}

.glass-btn {
  background: rgba(255,255,255,0.5);
  border: 1px solid rgba(255,255,255,0.5);
}

.w-full {
  width: 100%;
}
</style>
