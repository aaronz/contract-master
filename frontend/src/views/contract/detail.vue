<template>
  <div class="contract-detail-page">
    <!-- Top Action Bar -->
    <div class="detail-header glass-blur">
      <div class="header-left">
        <el-button circle class="back-btn" @click="$router.back()">
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <div class="header-title">
          <div class="flex items-center gap-2">
            <span class="contract-no">{{ form.contractNo }}</span>
            <el-tag :type="getStatusType(form.contractStatus)" effect="dark" size="small" round>
              {{ form.contractStatus }}
            </el-tag>
          </div>
          <h1 class="contract-name">{{ form.contractName }}</h1>
        </div>
      </div>
      <div class="header-right">
        <div class="mode-toggle glass-panel-sm">
          <span :class="{ active: !isEditMode }" @click="isEditMode = false">View</span>
          <span :class="{ active: isEditMode }" @click="isEditMode = true">Edit</span>
        </div>
        
        <el-divider direction="vertical" />

        <el-button @click="handleAIAnalysis" class="glass-btn">
          <el-icon><Cpu /></el-icon> AI Analysis
        </el-button>
        <el-button type="primary" @click="saveContract" v-if="isEditMode">
          <el-icon><Check /></el-icon> Save Changes
        </el-button>
        <el-button circle @click="toggleSidePanel" class="glass-btn">
          <el-icon><ChatDotRound /></el-icon>
        </el-button>
      </div>
    </div>

    <div class="detail-container">
      <!-- Main Content (Left) -->
      <div class="main-form-area" :class="{ 'with-panel': showSidePanel }">
        <el-tabs v-model="activeTab" class="custom-tabs glass-card">
          <el-tab-pane label="General Info" name="general">
            <div class="section-grid">
              <!-- Basic Info -->
              <div class="form-section">
                <h3 class="section-header">Basic Information</h3>
                <div class="form-grid">
                  <div class="field-group">
                    <label>Contract Name</label>
                    <el-input v-if="isEditMode" v-model="form.contractName" />
                    <div v-else class="display-val primary">{{ form.contractName }}</div>
                  </div>
                  <div class="field-group">
                    <label>Contract No.</label>
                    <el-input v-if="isEditMode" v-model="form.contractNo" disabled />
                    <div v-else class="display-val mono">{{ form.contractNo }}</div>
                  </div>
                  <div class="field-group">
                    <label>Contract Type</label>
                    <el-select v-if="isEditMode" v-model="form.contractType" style="width: 100%">
                      <el-option label="Framework Agreement" value="Framework" />
                      <el-option label="Sales Contract" value="Sales" />
                      <el-option label="Service Agreement" value="Service" />
                    </el-select>
                    <div v-else class="display-val">{{ form.contractType }}</div>
                  </div>
                  <div class="field-group">
                    <label>CRM ID</label>
                    <el-input v-if="isEditMode" v-model="form.crmContractId" disabled />
                    <div v-else class="display-val mono">{{ form.crmContractId }}</div>
                  </div>
                </div>
              </div>

              <!-- Parties -->
              <div class="parties-wrapper">
                <div class="form-section party-card">
                  <h3 class="section-header text-blue">Party A (Us)</h3>
                  <div class="form-stack">
                    <div class="field-group">
                      <label>Entity Name</label>
                      <el-input v-if="isEditMode" v-model="form.partyAName" />
                      <div v-else class="display-val strong">{{ form.partyAName }}</div>
                    </div>
                    <div class="grid-2">
                      <div class="field-group">
                        <label>Contact</label>
                        <el-input v-if="isEditMode" v-model="form.partyAContact" />
                        <div v-else class="display-val">{{ form.partyAContact }}</div>
                      </div>
                      <div class="field-group">
                        <label>Phone</label>
                        <el-input v-if="isEditMode" v-model="form.partyAPhone" />
                        <div v-else class="display-val">{{ form.partyAPhone }}</div>
                      </div>
                    </div>
                    <div class="field-group">
                      <label>Address</label>
                      <el-input v-if="isEditMode" v-model="form.partyAAddress" type="textarea" :rows="2" />
                      <div v-else class="display-val">{{ form.partyAAddress }}</div>
                    </div>
                  </div>
                </div>

                <div class="form-section party-card">
                  <h3 class="section-header text-purple">Party B (Counterparty)</h3>
                  <div class="form-stack">
                    <div class="field-group">
                      <label>Entity Name</label>
                      <el-input v-if="isEditMode" v-model="form.partyBName" />
                      <div v-else class="display-val strong">{{ form.partyBName }}</div>
                    </div>
                    <div class="grid-2">
                      <div class="field-group">
                        <label>Contact</label>
                        <el-input v-if="isEditMode" v-model="form.partyBContact" />
                        <div v-else class="display-val">{{ form.partyBContact }}</div>
                      </div>
                      <div class="field-group">
                        <label>Phone</label>
                        <el-input v-if="isEditMode" v-model="form.partyBPhone" />
                        <div v-else class="display-val">{{ form.partyBPhone }}</div>
                      </div>
                    </div>
                    <div class="field-group">
                      <label>Address</label>
                      <el-input v-if="isEditMode" v-model="form.partyBAddress" type="textarea" :rows="2" />
                      <div v-else class="display-val">{{ form.partyBAddress }}</div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Third Party -->
              <div class="form-section">
                 <div class="field-group">
                    <label>Has Third Party?</label>
                    <el-switch v-if="isEditMode" v-model="form.thirdPartyFlag" />
                    <div v-else class="display-val">{{ form.thirdPartyFlag ? 'Yes' : 'No' }}</div>
                 </div>
                 <div class="field-group mt-4" v-if="form.thirdPartyFlag">
                   <label>Third Party Info</label>
                   <el-input v-if="isEditMode" v-model="form.thirdPartyInfo" type="textarea" />
                   <div v-else class="display-val">{{ form.thirdPartyInfo }}</div>
                 </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Financials" name="financials">
            <div class="form-section">
              <h3 class="section-header">Financial Details</h3>
              <div class="form-grid-3">
                <div class="field-group">
                  <label>Currency</label>
                  <el-select v-if="isEditMode" v-model="form.currencyType" style="width: 100%">
                    <el-option label="CNY" value="CNY" />
                    <el-option label="USD" value="USD" />
                    <el-option label="EUR" value="EUR" />
                  </el-select>
                  <div v-else class="display-val">{{ form.currencyType }}</div>
                </div>
                <div class="field-group">
                  <label>Contract Amount</label>
                  <el-input-number v-if="isEditMode" v-model="form.contractAmount" :precision="2" style="width: 100%" />
                  <div v-else class="display-val highlight">{{ formatCurrency(form.contractAmount, form.currencyType) }}</div>
                </div>
                <div class="field-group">
                  <label>Tax Rate (%)</label>
                  <el-input-number v-if="isEditMode" v-model="form.taxRate" :precision="2" style="width: 100%" />
                  <div v-else class="display-val">{{ form.taxRate }}%</div>
                </div>
                <div class="field-group">
                  <label>Tax Amount</label>
                  <el-input-number v-if="isEditMode" v-model="form.taxAmount" :precision="2" style="width: 100%" />
                  <div v-else class="display-val">{{ formatCurrency(form.taxAmount, form.currencyType) }}</div>
                </div>
                <div class="field-group">
                  <label>Total Amount (Inc. Tax)</label>
                  <el-input-number v-if="isEditMode" v-model="form.totalAmountWithTax" :precision="2" style="width: 100%" />
                  <div v-else class="display-val highlight">{{ formatCurrency(form.totalAmountWithTax, form.currencyType) }}</div>
                </div>
                <div class="field-group">
                  <label>Payment Method</label>
                  <el-select v-if="isEditMode" v-model="form.paymentMethod" style="width: 100%">
                    <el-option label="Bank Transfer" value="Bank Transfer" />
                    <el-option label="Check" value="Check" />
                    <el-option label="Cash" value="Cash" />
                  </el-select>
                  <div v-else class="display-val">{{ form.paymentMethod }}</div>
                </div>
                <div class="field-group full-width">
                  <label>Payment Terms</label>
                  <el-input v-if="isEditMode" v-model="form.paymentTerm" type="textarea" :rows="3" />
                  <div v-else class="display-val text-block">{{ form.paymentTerm }}</div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Performance & Subject" name="performance">
            <div class="form-section">
              <h3 class="section-header">Subject Matter</h3>
              <div class="form-grid-3">
                 <div class="field-group">
                   <label>Subject Type</label>
                   <el-select v-if="isEditMode" v-model="form.subjectType" style="width: 100%">
                     <el-option label="Goods" value="Goods" />
                     <el-option label="Services" value="Services" />
                   </el-select>
                   <div v-else class="display-val">{{ form.subjectType }}</div>
                 </div>
                 <div class="field-group">
                   <label>Quantity</label>
                   <el-input-number v-if="isEditMode" v-model="form.subjectQuantity" style="width: 100%" />
                   <div v-else class="display-val">{{ form.subjectQuantity }}</div>
                 </div>
                 <div class="field-group">
                   <label>Unit Price</label>
                   <el-input-number v-if="isEditMode" v-model="form.unitPrice" style="width: 100%" />
                   <div v-else class="display-val">{{ formatCurrency(form.unitPrice, form.currencyType) }}</div>
                 </div>
                 <div class="field-group full-width">
                   <label>Subject Description</label>
                   <el-input v-if="isEditMode" v-model="form.subjectDesc" type="textarea" :rows="3" />
                   <div v-else class="display-val text-block">{{ form.subjectDesc }}</div>
                 </div>
              </div>
            </div>

            <div class="form-section mt-6">
              <h3 class="section-header">Performance Terms</h3>
              <div class="form-grid-2">
                <div class="field-group">
                  <label>Performance Method</label>
                  <el-input v-if="isEditMode" v-model="form.performanceMethod" />
                  <div v-else class="display-val">{{ form.performanceMethod }}</div>
                </div>
                <div class="field-group">
                  <label>Location</label>
                  <el-input v-if="isEditMode" v-model="form.performanceLocation" />
                  <div v-else class="display-val">{{ form.performanceLocation }}</div>
                </div>
                <div class="field-group">
                  <label>Start Date</label>
                  <el-date-picker v-if="isEditMode" v-model="form.performanceStartDate" type="date" style="width: 100%" />
                  <div v-else class="display-val">{{ form.performanceStartDate }}</div>
                </div>
                <div class="field-group">
                  <label>End Date</label>
                  <el-date-picker v-if="isEditMode" v-model="form.performanceEndDate" type="date" style="width: 100%" />
                  <div v-else class="display-val">{{ form.performanceEndDate }}</div>
                </div>
                <div class="field-group full-width">
                  <label>Quality Standard</label>
                  <el-input v-if="isEditMode" v-model="form.qualityStandard" type="textarea" :rows="2" />
                  <div v-else class="display-val text-block">{{ form.qualityStandard }}</div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Legal & Dates" name="legal">
            <div class="form-section">
              <h3 class="section-header">Important Dates</h3>
              <div class="form-grid-3">
                <div class="field-group">
                  <label>Sign Date</label>
                  <el-date-picker v-if="isEditMode" v-model="form.signDate" type="date" style="width: 100%" />
                  <div v-else class="display-val">{{ form.signDate }}</div>
                </div>
                <div class="field-group">
                  <label>Effective Date</label>
                  <el-date-picker v-if="isEditMode" v-model="form.effectiveDate" type="date" style="width: 100%" />
                  <div v-else class="display-val">{{ form.effectiveDate }}</div>
                </div>
                <div class="field-group">
                  <label>Expire Date</label>
                  <el-date-picker v-if="isEditMode" v-model="form.expireDate" type="date" style="width: 100%" />
                  <div v-else class="display-val">{{ form.expireDate }}</div>
                </div>
              </div>
            </div>

            <div class="form-section mt-6">
              <h3 class="section-header">Legal Provisions</h3>
              <div class="form-grid-2">
                <div class="field-group">
                  <label>Dispute Resolution</label>
                  <el-select v-if="isEditMode" v-model="form.disputeResolution" style="width: 100%">
                    <el-option label="Negotiation" value="Negotiation" />
                    <el-option label="Arbitration" value="Arbitration" />
                    <el-option label="Litigation" value="Litigation" />
                  </el-select>
                  <div v-else class="display-val">{{ form.disputeResolution }}</div>
                </div>
                <div class="field-group">
                  <label>Governing Law</label>
                  <el-input v-if="isEditMode" v-model="form.governingLaw" />
                  <div v-else class="display-val">{{ form.governingLaw }}</div>
                </div>
                
                <div class="field-group full-width">
                  <label>Legal Review</label>
                  <div v-if="isEditMode" class="review-box">
                    <el-checkbox v-model="form.legalReviewFlag" label="Reviewed by Legal" border />
                    <el-input v-if="form.legalReviewFlag" v-model="form.legalReviewOpinion" placeholder="Legal Opinion..." />
                  </div>
                  <div v-else class="display-val">
                    <el-tag :type="form.legalReviewFlag ? 'success' : 'info'">{{ form.legalReviewFlag ? 'Reviewed' : 'Pending' }}</el-tag>
                    <p v-if="form.legalReviewFlag" class="mt-2">{{ form.legalReviewOpinion }}</p>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Attachments" name="attachments">
            <el-upload
              v-if="isEditMode"
              class="upload-demo"
              drag
              action="#"
              multiple
            >
              <el-icon class="el-icon--upload"><upload-filled /></el-icon>
              <div class="el-upload__text">
                Drop file here or <em>click to upload</em>
              </div>
            </el-upload>
            
            <div class="attachment-list">
               <div class="attachment-item glass-panel-sm" v-for="i in 3" :key="i">
                 <el-icon class="file-icon"><Document /></el-icon>
                 <div class="file-info">
                   <div class="file-name">Contract_Final_v{{i}}.pdf</div>
                   <div class="file-meta">2.4MB • Uploaded by Admin • 2024-01-1{{i}}</div>
                 </div>
                 <div class="file-actions">
                   <el-button link type="primary">Preview</el-button>
                   <el-button link type="primary">Download</el-button>
                 </div>
               </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- Right Side Panel (Annotations & Audit) -->
      <transition name="slide-left">
        <div class="side-panel glass-card" v-if="showSidePanel">
          <div class="panel-header">
            <h3>Collaboration</h3>
            <el-button circle text @click="showSidePanel = false"><el-icon><Close /></el-icon></el-button>
          </div>
          <el-tabs v-model="panelTab" class="panel-tabs" stretch>
            <el-tab-pane label="Comments" name="comments">
              <div class="comments-list">
                <div v-for="comment in comments" :key="comment.id" class="comment-item">
                  <el-avatar :size="32" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                  <div class="comment-content">
                    <div class="comment-header">
                      <span class="user">{{ comment.user }}</span>
                      <span class="time">{{ comment.time }}</span>
                    </div>
                    <div class="comment-text">{{ comment.text }}</div>
                  </div>
                </div>
              </div>
              <div class="comment-input">
                <el-input v-model="newComment" placeholder="Write a comment..." @keyup.enter="addComment">
                  <template #suffix>
                    <el-button link type="primary" @click="addComment">Send</el-button>
                  </template>
                </el-input>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="Audit Log" name="audit">
              <el-timeline style="padding-top: 20px; padding-right: 20px">
                <el-timeline-item
                  v-for="(log, index) in auditLogs"
                  :key="index"
                  :type="log.type"
                  :timestamp="log.time"
                  size="small"
                >
                  {{ log.content }}
                </el-timeline-item>
              </el-timeline>
            </el-tab-pane>
          </el-tabs>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ArrowLeft, Cpu, Check, ChatDotRound, UploadFilled, Document, Close } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('general')
const showSidePanel = ref(true)
const panelTab = ref('comments')
const newComment = ref('')
const isEditMode = ref(false)

const form = reactive({
  contractNo: 'CON-2024-001',
  contractName: 'Strategic Partnership Agreement',
  contractType: 'Framework',
  crmContractId: 'CRM-X-9982',
  partyAName: 'Acme Corp',
  partyAContact: 'John Doe',
  partyAPhone: '+1 234 567 890',
  partyAAddress: '123 Tech Blvd, Silicon Valley, CA',
  partyBName: 'TechSolutions Inc',
  partyBContact: 'Jane Smith',
  partyBPhone: '+1 987 654 321',
  partyBAddress: '456 Innovation Dr, Austin, TX',
  thirdPartyFlag: false,
  thirdPartyInfo: '',
  currencyType: 'USD',
  contractAmount: 1000000,
  taxRate: 10,
  taxAmount: 100000,
  totalAmountWithTax: 1100000,
  paymentMethod: 'Bank Transfer',
  invoiceType: 'VAT Special',
  taxpayerId: '9876543210',
  invoiceTitle: 'Acme Corp',
  paymentTerm: 'Net 30 days after invoice date.',
  subjectType: 'Services',
  subjectQuantity: 1,
  unitPrice: 1000000,
  subjectDesc: 'Comprehensive IT consulting services for 2024.',
  performanceMethod: 'On-site',
  performanceLocation: 'HQ',
  performanceStartDate: '2024-01-01',
  performanceEndDate: '2024-12-31',
  qualityStandard: 'ISO 9001 Standards',
  signDate: '2023-12-15',
  effectiveDate: '2024-01-01',
  expireDate: '2024-12-31',
  disputeResolution: 'Arbitration',
  governingLaw: 'California',
  guaranteeFlag: false,
  guaranteeType: '',
  guarantorInfo: '',
  legalReviewFlag: true,
  legalReviewOpinion: 'Approved with standard clauses.',
  contractStatus: 'Active'
})

const comments = ref([
  { id: 1, user: 'Legal Team', time: '2 hours ago', text: 'Section 4.2 looks good.' },
  { id: 2, user: 'Finance', time: '1 hour ago', text: 'Please confirm tax rate.' }
])

const auditLogs = ref([
  { content: 'Contract created by Admin', time: '2023-12-10 09:00', type: 'primary' },
  { content: 'Legal review completed', time: '2023-12-12 14:30', type: 'success' },
  { content: 'Amount updated to $1.1M', time: '2023-12-14 10:15', type: 'warning' }
])

const toggleSidePanel = () => {
  showSidePanel.value = !showSidePanel.value
}

const handleAIAnalysis = () => {
  ElMessage.info('AI Analysis started...')
}

const saveContract = () => {
  isEditMode.value = false
  ElMessage.success('Contract saved successfully')
}

const addComment = () => {
  if (!newComment.value) return
  comments.value.push({
    id: Date.now(),
    user: 'You',
    time: 'Just now',
    text: newComment.value
  })
  newComment.value = ''
}

const getStatusType = (status) => {
  return status === 'Active' ? 'success' : 'warning'
}

const formatCurrency = (val, currency) => {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency }).format(val)
}
</script>

<style scoped>
.contract-detail-page {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 80px); /* Adjust based on global header */
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

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.mode-toggle {
  display: flex;
  background: rgba(255,255,255,0.5);
  border-radius: 20px;
  padding: 4px;
  cursor: pointer;
  border: 1px solid rgba(203, 213, 225, 0.5);
}

.mode-toggle span {
  padding: 6px 16px;
  border-radius: 16px;
  font-size: 12px;
  font-weight: 600;
  color: #64748B;
  transition: all 0.2s;
}

.mode-toggle span.active {
  background: #3B82F6;
  color: white;
  box-shadow: 0 2px 5px rgba(59, 130, 246, 0.3);
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

.custom-tabs {
  background: rgba(255, 255, 255, 0.6);
  border-radius: 16px;
  padding: 20px;
  min-height: 100%;
}

:deep(.el-tabs__nav-wrap::after) {
  height: 1px;
  background-color: rgba(0,0,0,0.05);
}

.form-section {
  margin-bottom: 32px;
  background: rgba(255,255,255,0.4);
  padding: 24px;
  border-radius: 16px;
  border: 1px solid rgba(255,255,255,0.4);
}

.section-header {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin: 0 0 20px 0;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}

.form-grid, .form-grid-2, .form-grid-3, .grid-2 {
  display: grid;
  gap: 24px;
}

.form-grid { grid-template-columns: repeat(2, 1fr); }
.form-grid-3 { grid-template-columns: repeat(3, 1fr); }
.form-grid-2 { grid-template-columns: repeat(2, 1fr); }
.grid-2 { grid-template-columns: repeat(2, 1fr); }

.full-width { grid-column: 1 / -1; }

.field-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.field-group label {
  font-size: 11px;
  color: #94A3B8;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.display-val {
  font-size: 14px;
  color: #334155;
  padding: 8px 0;
  border-bottom: 1px solid transparent;
  min-height: 24px;
}

.display-val.mono { font-family: 'Fira Code', monospace; font-size: 13px; }
.display-val.strong { font-weight: 600; font-size: 15px; }
.display-val.highlight { font-size: 18px; font-weight: 700; color: #1E293B; }
.display-val.text-block { line-height: 1.6; color: #475569; }

.parties-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.party-card {
  margin-bottom: 0;
  height: 100%;
}

.text-blue { color: #2563EB; }
.text-purple { color: #8B5CF6; }

/* Side Panel */
.side-panel {
  width: 340px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(255,255,255,0.4);
}

.panel-header {
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}

.comments-list {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  flex: 1;
  overflow-y: auto;
}

.comment-item {
  display: flex;
  gap: 12px;
}

.comment-content {
  background: white;
  padding: 10px 14px;
  border-radius: 0 12px 12px 12px;
  box-shadow: 0 1px 2px rgba(0,0,0,0.05);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
  font-size: 11px;
}

.user { font-weight: 700; }
.time { color: #94A3B8; }

.comment-input {
  padding: 16px;
  border-top: 1px solid rgba(0,0,0,0.05);
  background: rgba(255,255,255,0.5);
}

.glass-panel-sm {
  background: rgba(255, 255, 255, 0.5);
  border: 1px solid rgba(255,255,255,0.5);
}

.glass-btn {
  background: rgba(255,255,255,0.5);
  border: 1px solid rgba(255,255,255,0.5);
}

.glass-btn:hover {
  background: white;
}

/* Attachment Styles */
.attachment-list {
  display: grid;
  gap: 12px;
}

.attachment-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 12px;
}

.file-icon { font-size: 28px; margin-right: 16px; color: #3B82F6; }
.file-info { flex: 1; }
.file-name { font-weight: 600; font-size: 14px; }
.file-meta { font-size: 12px; color: #94A3B8; margin-top: 4px; }

/* Transitions */
.slide-left-enter-active,
.slide-left-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-left-enter-from,
.slide-left-leave-to {
  width: 0;
  opacity: 0;
  transform: translateX(20px);
}
</style>
