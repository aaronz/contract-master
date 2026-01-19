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

        <el-button type="warning" @click="confirmAiSuggestions" v-if="form.contractStatus === 'AI_EXTRACTED'">
          Confirm Suggestions
        </el-button>
        
        <el-button type="success" @click="publishContract" v-if="form.contractStatus === 'VERIFIED'">
          Publish to Downstream
        </el-button>

        <el-button type="primary" @click="openRuleSelector" class="glass-btn">
          <el-icon><Refresh /></el-icon> Re-evaluate
        </el-button>
        
        <el-button type="primary" @click="saveContract" v-if="isEditMode" class="save-btn">
          <el-icon><Check /></el-icon> Save Changes
        </el-button>
        <el-button circle @click="toggleSidePanel" class="glass-btn">
          <el-icon><ChatDotRound /></el-icon>
        </el-button>
      </div>
    </div>

    <RuleSelectorModal v-model="showRuleSelectorModal" @confirm="handleReEvaluation" />

    <div class="detail-container">
      <!-- Main Content (Left) -->
      <div class="main-form-area" :class="{ 'with-panel': showSidePanel }">
        <el-tabs v-model="activeTab" class="custom-tabs glass-card">
          <el-tab-pane label="General Info" name="general">
            <div class="section-grid">
              <!-- Basic Info -->
              <div class="form-section">
                <h3 class="section-header">Core Details</h3>
                <div class="form-grid">
                  <div class="field-group" v-if="isFieldVisible('contract_no')">
                    <label>{{ getFieldName('contract_no') }}</label>
                    <el-input v-if="isEditMode" v-model="form.contractNo" />
                    <div v-else class="display-val font-mono" :style="getFieldStyle('contract_no')">{{ form.contractNo }}</div>
                  </div>
                  <div class="field-group" v-if="isFieldVisible('contract_name')">
                    <label>{{ getFieldName('contract_name') }}</label>
                    <el-input v-if="isEditMode" v-model="form.contractName" />
                    <div v-else class="display-val" :style="getFieldStyle('contract_name')">{{ form.contractName }}</div>
                  </div>
                  <div class="field-group" v-if="isFieldVisible('contract_type')">
                    <label>{{ getFieldName('contract_type') }}</label>
                    <el-input v-if="isEditMode" v-model="form.contractType" />
                    <div v-else class="display-val" :style="getFieldStyle('contract_type')">{{ form.contractType }}</div>
                  </div>
                  <div class="field-group" v-if="isFieldVisible('contract_status')">
                    <label>{{ getFieldName('contract_status') }}</label>
                    <el-select v-if="isEditMode" v-model="form.contractStatus" style="width: 100%">
                      <el-option label="Active" value="Active" />
                      <el-option label="Draft" value="Draft" />
                      <el-option label="Pending" value="Pending" />
                      <el-option label="Expired" value="Expired" />
                    </el-select>
                    <el-tag v-else :type="getStatusType(form.contractStatus)" round :style="getFieldStyle('contract_status')">{{ form.contractStatus }}</el-tag>
                  </div>
                </div>
              </div>

              <!-- Parties -->
              <div class="parties-wrapper">
                <div class="form-section party-card" v-if="isFieldVisible('party_a_name') || isFieldVisible('party_a_contact') || isFieldVisible('party_a_phone')">
                   <h3 class="section-header text-blue">Party A (Us)</h3>
                   <div class="form-stack">
                     <div class="field-group" v-if="isFieldVisible('party_a_name')">
                       <label>{{ getFieldName('party_a_name') }}</label>
                       <el-input v-if="isEditMode" v-model="form.partyAName" />
                       <div v-else class="display-val strong" :style="getFieldStyle('party_a_name')">{{ form.partyAName }}</div>
                     </div>
                     <div class="grid-2">
                       <div class="field-group" v-if="isFieldVisible('party_a_contact')">
                         <label>{{ getFieldName('party_a_contact') }}</label>
                         <el-input v-if="isEditMode" v-model="form.partyAContact" />
                         <div v-else class="display-val" :style="getFieldStyle('party_a_contact')">{{ form.partyAContact }}</div>
                       </div>
                       <div class="field-group" v-if="isFieldVisible('party_a_phone')">
                         <label>{{ getFieldName('party_a_phone') }}</label>
                         <el-input v-if="isEditMode" v-model="form.partyAPhone" />
                         <div v-else class="display-val" :style="getFieldStyle('party_a_phone')">{{ form.partyAPhone }}</div>
                       </div>
                     </div>
                     <div class="field-group" v-if="isFieldVisible('party_a_address')">
                       <label>{{ getFieldName('party_a_address') }}</label>
                       <el-input v-if="isEditMode" v-model="form.partyAAddress" type="textarea" :rows="2" />
                       <div v-else class="display-val" :style="getFieldStyle('party_a_address')">{{ form.partyAAddress }}</div>
                     </div>
                   </div>
                 </div>

                 <div class="form-section party-card" v-if="isFieldVisible('party_b_name') || isFieldVisible('party_b_contact') || isFieldVisible('party_b_phone')">
                   <h3 class="section-header text-purple">Party B (Counterparty)</h3>
                   <div class="form-stack">
                     <div class="field-group" v-if="isFieldVisible('party_b_name')">
                       <label>{{ getFieldName('party_b_name') }}</label>
                       <el-input v-if="isEditMode" v-model="form.partyBName" />
                       <div v-else class="display-val strong" :style="getFieldStyle('party_b_name')">{{ form.partyBName }}</div>
                     </div>
                     <div class="grid-2">
                       <div class="field-group" v-if="isFieldVisible('party_b_contact')">
                         <label>{{ getFieldName('party_b_contact') }}</label>
                         <el-input v-if="isEditMode" v-model="form.partyBContact" />
                         <div v-else class="display-val" :style="getFieldStyle('party_b_contact')">{{ form.partyBContact }}</div>
                       </div>
                       <div class="field-group" v-if="isFieldVisible('party_b_phone')">
                         <label>{{ getFieldName('party_b_phone') }}</label>
                         <el-input v-if="isEditMode" v-model="form.partyBPhone" />
                         <div v-else class="display-val" :style="getFieldStyle('party_b_phone')">{{ form.partyBPhone }}</div>
                       </div>
                     </div>
                     <div class="field-group" v-if="isFieldVisible('party_b_address')">
                       <label>{{ getFieldName('party_b_address') }}</label>
                       <el-input v-if="isEditMode" v-model="form.partyBAddress" type="textarea" :rows="2" />
                       <div v-else class="display-val" :style="getFieldStyle('party_b_address')">{{ form.partyBAddress }}</div>
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

              <!-- Custom Fields -->
              <div class="form-section" v-if="fieldStore.fields.some(f => f.source === 'EXTEND')">
                <h3 class="section-header">Additional Information</h3>
                <div class="form-grid">
                  <div 
                    class="field-group" 
                    v-for="field in fieldStore.fields.filter(f => f.source === 'EXTEND' && isFieldVisible(f.fieldCode))" 
                    :key="field.fieldCode"
                  >
                    <label>{{ field.fieldName }}</label>
                    <el-input 
                      v-if="isEditMode" 
                      v-model="form.extendedFields[field.fieldCode]" 
                    />
                    <div v-else class="display-val" :style="getFieldStyle(field.fieldCode)">{{ form.extendedFields[field.fieldCode] || 'N/A' }}</div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Financials" name="financials">
            <div class="form-section">
              <h3 class="section-header">Financial Details</h3>
              <div class="form-grid-3">
                <div class="field-group" v-if="isFieldVisible('currency_type')">
                  <label>{{ getFieldName('currency_type') }}</label>
                  <el-select v-if="isEditMode" v-model="form.currencyType" style="width: 100%">
                    <el-option label="CNY" value="CNY" />
                    <el-option label="USD" value="USD" />
                    <el-option label="EUR" value="EUR" />
                  </el-select>
                  <div v-else class="display-val" :style="getFieldStyle('currency_type')">{{ form.currencyType }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('contract_amount')">
                  <label>{{ getFieldName('contract_amount') }}</label>
                  <el-input-number v-if="isEditMode" v-model="form.contractAmount" :precision="2" style="width: 100%" />
                  <div v-else class="display-val highlight" :style="getFieldStyle('contract_amount')">{{ formatCurrency(form.contractAmount, form.currencyType) }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('tax_rate')">
                  <label>{{ getFieldName('tax_rate') }}</label>
                  <el-input-number v-if="isEditMode" v-model="form.taxRate" :precision="2" style="width: 100%" />
                  <div v-else class="display-val" :style="getFieldStyle('tax_rate')">{{ form.taxRate }}%</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('tax_amount')">
                  <label>{{ getFieldName('tax_amount') }}</label>
                  <el-input-number v-if="isEditMode" v-model="form.taxAmount" :precision="2" style="width: 100%" />
                  <div v-else class="display-val" :style="getFieldStyle('tax_amount')">{{ formatCurrency(form.taxAmount, form.currencyType) }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('total_amount_with_tax')">
                  <label>{{ getFieldName('total_amount_with_tax') }}</label>
                  <el-input-number v-if="isEditMode" v-model="form.totalAmountWithTax" :precision="2" style="width: 100%" />
                  <div v-else class="display-val highlight" :style="getFieldStyle('total_amount_with_tax')">{{ formatCurrency(form.totalAmountWithTax, form.currencyType) }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('payment_method')">
                  <label>{{ getFieldName('payment_method') }}</label>
                  <el-select v-if="isEditMode" v-model="form.paymentMethod" style="width: 100%">
                    <el-option label="Bank Transfer" value="Bank Transfer" />
                    <el-option label="Check" value="Check" />
                    <el-option label="Cash" value="Cash" />
                  </el-select>
                  <div v-else class="display-val" :style="getFieldStyle('payment_method')">{{ form.paymentMethod }}</div>
                </div>
                <div class="field-group full-width" v-if="isFieldVisible('payment_term')">
                  <label>{{ getFieldName('payment_term') }}</label>
                  <el-input v-if="isEditMode" v-model="form.paymentTerm" type="textarea" :rows="3" />
                  <div v-else class="display-val text-block" :style="getFieldStyle('payment_term')">{{ form.paymentTerm }}</div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Performance & Subject" name="performance">
            <div class="form-section">
              <h3 class="section-header">Subject Matter</h3>
              <div class="form-grid-3">
                 <div class="field-group" v-if="isFieldVisible('subject_type')">
                   <label>{{ getFieldName('subject_type') }}</label>
                   <el-select v-if="isEditMode" v-model="form.subjectType" style="width: 100%">
                     <el-option label="Goods" value="Goods" />
                     <el-option label="Services" value="Services" />
                   </el-select>
                   <div v-else class="display-val" :style="getFieldStyle('subject_type')">{{ form.subjectType }}</div>
                 </div>
                 <div class="field-group" v-if="isFieldVisible('subject_quantity')">
                   <label>{{ getFieldName('subject_quantity') }}</label>
                   <el-input-number v-if="isEditMode" v-model="form.subjectQuantity" style="width: 100%" />
                   <div v-else class="display-val" :style="getFieldStyle('subject_quantity')">{{ form.subjectQuantity }}</div>
                 </div>
                 <div class="field-group" v-if="isFieldVisible('unit_price')">
                   <label>{{ getFieldName('unit_price') }}</label>
                   <el-input-number v-if="isEditMode" v-model="form.unitPrice" style="width: 100%" />
                   <div v-else class="display-val" :style="getFieldStyle('unit_price')">{{ formatCurrency(form.unitPrice, form.currencyType) }}</div>
                 </div>
                 <div class="field-group full-width" v-if="isFieldVisible('subject_desc')">
                   <label>{{ getFieldName('subject_desc') }}</label>
                   <el-input v-if="isEditMode" v-model="form.subjectDesc" type="textarea" :rows="3" />
                   <div v-else class="display-val text-block" :style="getFieldStyle('subject_desc')">{{ form.subjectDesc }}</div>
                 </div>
              </div>
            </div>

            <div class="form-section mt-6">
              <h3 class="section-header">Performance Terms</h3>
              <div class="form-grid-2">
                <div class="field-group" v-if="isFieldVisible('performance_method')">
                  <label>{{ getFieldName('performance_method') }}</label>
                  <el-input v-if="isEditMode" v-model="form.performanceMethod" />
                  <div v-else class="display-val" :style="getFieldStyle('performance_method')">{{ form.performanceMethod }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('performance_location')">
                  <label>{{ getFieldName('performance_location') }}</label>
                  <el-input v-if="isEditMode" v-model="form.performanceLocation" />
                  <div v-else class="display-val" :style="getFieldStyle('performance_location')">{{ form.performanceLocation }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('performance_start_date')">
                  <label>{{ getFieldName('performance_start_date') }}</label>
                  <el-date-picker v-if="isEditMode" v-model="form.performanceStartDate" type="date" style="width: 100%" />
                  <div v-else class="display-val" :style="getFieldStyle('performance_start_date')">{{ form.performanceStartDate }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('performance_end_date')">
                  <label>{{ getFieldName('performance_end_date') }}</label>
                  <el-date-picker v-if="isEditMode" v-model="form.performanceEndDate" type="date" style="width: 100%" />
                  <div v-else class="display-val" :style="getFieldStyle('performance_end_date')">{{ form.performanceEndDate }}</div>
                </div>
                <div class="field-group full-width" v-if="isFieldVisible('quality_standard')">
                  <label>{{ getFieldName('quality_standard') }}</label>
                  <el-input v-if="isEditMode" v-model="form.qualityStandard" type="textarea" :rows="2" />
                  <div v-else class="display-val text-block" :style="getFieldStyle('quality_standard')">{{ form.qualityStandard }}</div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Legal & Dates" name="legal">
            <div class="form-section">
              <h3 class="section-header">Important Dates</h3>
              <div class="form-grid-3">
                <div class="field-group" v-if="isFieldVisible('sign_date')">
                  <label>{{ getFieldName('sign_date') }}</label>
                  <el-date-picker v-if="isEditMode" v-model="form.signDate" type="date" style="width: 100%" />
                  <div v-else class="display-val" :style="getFieldStyle('sign_date')">{{ form.signDate }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('effective_date')">
                  <label>{{ getFieldName('effective_date') }}</label>
                  <el-date-picker v-if="isEditMode" v-model="form.effectiveDate" type="date" style="width: 100%" />
                  <div v-else class="display-val" :style="getFieldStyle('effective_date')">{{ form.effectiveDate }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('expire_date')">
                  <label>{{ getFieldName('expire_date') }}</label>
                  <el-date-picker v-if="isEditMode" v-model="form.expireDate" type="date" style="width: 100%" />
                  <div v-else class="display-val" :style="getFieldStyle('expire_date')">{{ form.expireDate }}</div>
                </div>
              </div>
            </div>

            <div class="form-section mt-6">
              <h3 class="section-header">Legal Provisions</h3>
              <div class="form-grid-2">
                <div class="field-group" v-if="isFieldVisible('dispute_resolution')">
                  <label>{{ getFieldName('dispute_resolution') }}</label>
                  <el-select v-if="isEditMode" v-model="form.disputeResolution" style="width: 100%">
                    <el-option label="Negotiation" value="Negotiation" />
                    <el-option label="Arbitration" value="Arbitration" />
                    <el-option label="Litigation" value="Litigation" />
                  </el-select>
                  <div v-else class="display-val" :style="getFieldStyle('dispute_resolution')">{{ form.disputeResolution }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('governing_law')">
                  <label>{{ getFieldName('governing_law') }}</label>
                  <el-input v-if="isEditMode" v-model="form.governingLaw" />
                  <div v-else class="display-val" :style="getFieldStyle('governing_law')">{{ form.governingLaw }}</div>
                </div>
                
                <div class="field-group full-width" v-if="isFieldVisible('legal_review_flag')">
                  <label>{{ getFieldName('legal_review_flag') }}</label>
                  <div v-if="isEditMode" class="review-box">
                    <el-checkbox v-model="form.legalReviewFlag" label="Reviewed by Legal" border />
                    <el-input v-if="form.legalReviewFlag" v-model="form.legalReviewOpinion" placeholder="Legal Opinion..." />
                  </div>
                  <div v-else class="display-val">
                    <el-tag :type="form.legalReviewFlag ? 'success' : 'info'" :style="getFieldStyle('legal_review_flag')">{{ form.legalReviewFlag ? 'Reviewed' : 'Pending' }}</el-tag>
                    <p v-if="form.legalReviewFlag && isFieldVisible('legal_review_opinion')" class="mt-2" :style="getFieldStyle('legal_review_opinion')">{{ form.legalReviewOpinion }}</p>
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
              <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
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
                  size="normal"
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft, Check, ChatDotRound, UploadFilled, Document, Close, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

import { useFieldStore } from '@/stores/fieldStore'
import problemApi from '@/services/problemApi' 
import contractApi from '@/services/contractApi' 
import RuleSelectorModal from '@/components/RuleSelectorModal.vue' 

const fieldStore = useFieldStore()
const router = useRouter()
const activeTab = ref('general')
const showSidePanel = ref(true)
const panelTab = ref('comments')
const newComment = ref('')
const isEditMode = ref(false)
const showRuleSelectorModal = ref(false)

const getFieldStyle = (fieldCode) => {
  const field = fieldStore.fields.find(f => f.fieldCode === fieldCode)
  if (!field) return {}
  return {
    color: field.fieldColor,
    fontWeight: field.fieldStyles?.includes('bold') ? 'bold' : 'normal',
    fontStyle: field.fieldStyles?.includes('italic') ? 'italic' : 'normal',
  }
}

const isFieldVisible = (fieldCode) => fieldStore.isFieldVisible(fieldCode)

const getFieldName = (code) => {
  const f = fieldStore.fields.find(field => field.fieldCode === code)
  return f ? f.fieldName : code
}

const fetchContractDetail = async () => {
  const contractId = router.currentRoute.value.params.id
  if (!contractId) return
  
  try {
    const response = await contractApi.getContractDetail(contractId)
    const result = response.data
    if (result && result.data) {
      Object.assign(form, result.data)
      if (result.data.contractId) {
        form.contractId = result.data.contractId
      }
    } else {
      ElMessage.error('Invalid response format from server')
    }
  } catch (error) {
    console.error('Failed to fetch contract detail:', error)
  }
}

onMounted(() => {
  fieldStore.fetchFieldConfigs()
  fetchContractDetail()
})

const form = reactive({
  contractId: '',
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
  contractStatus: 'Active',
  customData: {},
  extendedFields: {}
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

const saveContract = async () => {
  try {
    await contractApi.updateContract(form.contractId, form)
    isEditMode.value = false
    ElMessage.success('Contract updated successfully')
    fetchContractDetail()
  } catch (error) {
    console.error('Update failed', error)
  }
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
  const map = {
    'Active': 'success',
    'AI_EXTRACTED': 'warning',
    'VERIFIED': 'primary',
    'PUBLISHED': 'success',
    'Draft': 'info'
  }
  return map[status] || 'warning'
}

const formatCurrency = (val, currency) => {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: currency || 'USD' }).format(val)
}

const formatDate = (dateStr) => {
  if (!dateStr) return 'N/A'
  return new Date(dateStr).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

const confirmAiSuggestions = () => {
  form.contractStatus = 'VERIFIED'
  ElMessage.success('Suggestions confirmed')
}

const publishContract = () => {
  form.contractStatus = 'PUBLISHED'
  ElMessage.success('Publication initiated')
}

const openRuleSelector = () => {
  showRuleSelectorModal.value = true
}

const handleReEvaluation = async (ruleIds) => {
  if (!form.contractId) {
    ElMessage.error('Contract ID is missing for re-evaluation.');
    return;
  }
  if (!ruleIds || ruleIds.length === 0) {
    ElMessage.warning('No rules selected for re-evaluation.');
    return;
  }

  try {
    const response = await problemApi.triggerEvaluation(form.contractId, ruleIds);
    const responseData = response.data.data || response.data;
    if (responseData) {
      ElMessage.success(`Re-evaluation started successfully!`);
    } else {
      ElMessage.error('Failed to start re-evaluation.');
    }
  } catch (error) {
    console.error('Re-evaluation failed:', error);
    // Handle specific error codes from backend (e.g., 409 Conflict for in-progress evaluation)
    if (error.response && error.response.status === 409) {
      ElMessage.warning('An evaluation is already in progress for this contract.');
    } else {
      ElMessage.error('Failed to start re-evaluation: ' + (error.response?.data?.message || error.message));
    }
  }
}
</script>

<style scoped>
.contract-detail-page {
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

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
  position: relative;
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
