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

        <el-button @click="handleAIAnalysis" class="glass-btn" :loading="analyzing">
          <el-icon><Cpu /></el-icon> AI Analysis
        </el-button>
        
        <el-button type="warning" @click="confirmAiSuggestions" v-if="form.contractStatus === 'AI_EXTRACTED'">
          Confirm Suggestions
        </el-button>
        
        <el-button type="success" @click="publishContract" v-if="form.contractStatus === 'VERIFIED'">
          Publish to Downstream
        </el-button>

        <el-button type="info" @click="showCardGenerator = true">
          <el-icon><IdCard /></el-icon> Card Generator
        </el-button>
        
        <el-progress 
          v-if="analyzing" 
          :percentage="progress" 
          :stroke-width="2" 
          :show-text="false" 
          class="ai-progress"
        />
        <el-button type="primary" @click="saveContract" v-if="isEditMode" class="save-btn">
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
                <h3 class="section-header">Core Details</h3>
                <div class="form-grid">
                  <div class="field-group">
                    <label>Contract Number</label>
                    <el-input v-if="isEditMode" v-model="form.contractNo" />
                    <div v-else class="display-val font-mono">{{ form.contractNo }}</div>
                  </div>
                  <div class="field-group">
                    <label>Contract Name</label>
                    <el-input v-if="isEditMode" v-model="form.contractName" />
                    <div v-else class="display-val">{{ form.contractName }}</div>
                  </div>
                  <div class="field-group">
                    <label>Contract Type</label>
                    <el-input v-if="isEditMode" v-model="form.contractType" />
                    <div v-else class="display-val">{{ form.contractType }}</div>
                  </div>
                  <div class="field-group">
                    <label>Status</label>
                    <el-select v-if="isEditMode" v-model="form.contractStatus" style="width: 100%">
                      <el-option label="Active" value="Active" />
                      <el-option label="Draft" value="Draft" />
                      <el-option label="Pending" value="Pending" />
                      <el-option label="Expired" value="Expired" />
                    </el-select>
                    <el-tag v-else :type="getStatusType(form.contractStatus)" round>{{ form.contractStatus }}</el-tag>
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

              <!-- Custom Fields -->
              <div class="form-section" v-if="customFields && customFields.length > 0">
                <h3 class="section-header">Additional Information</h3>
                <div class="form-grid">
                  <div class="field-group" v-for="field in customFields || []" :key="field.id">
                    <label>{{ field.fieldName }}</label>
                    <el-input 
                      v-if="isEditMode" 
                      v-model="form.customData[field.fieldCode]" 
                      :name="'custom_field_' + field.fieldCode"
                    />
                    <div v-else class="display-val">{{ form.customData[field.fieldCode] }}</div>
                  </div>
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
    <!-- Card Generator Dialog -->
    <el-dialog v-model="showCardGenerator" title="Contract Card Generator" width="800px">
      <div class="card-generator-container">
        <div class="field-selector">
          <h4 class="mb-4">Select Fields to Include</h4>
          <el-checkbox-group v-model="cardConfig.selectedFields">
            <div class="field-selection-grid">
              <div v-for="field in contractFields" :key="field.fieldCode" class="field-selection-item">
                <el-checkbox :label="field.fieldCode">{{ field.fieldName }}</el-checkbox>
                <el-input 
                  v-if="cardConfig.selectedFields.includes(field.fieldCode)"
                  v-model="cardConfig.customLabels[field.fieldCode]" 
                  size="small" 
                  placeholder="Custom Label"
                  class="mt-1"
                />
              </div>
            </div>
          </el-checkbox-group>
        </div>
        
        <div class="card-preview mt-8">
          <h4 class="mb-4">Preview</h4>
          <div class="contract-card-visual" :style="cardStyles.container">
            <div class="card-header" :style="cardStyles.header">
              <div class="card-title" :style="cardStyles.title">{{ form.contractName }}</div>
              <div class="card-subtitle" :style="cardStyles.subtitle">{{ form.contractNo }}</div>
            </div>
            
            <div class="card-section" v-if="hasFieldsInGroup('general')">
              <div class="section-title" :style="cardStyles.sectionTitle">General Info</div>
              <div class="card-grid" :style="cardStyles.grid">
                <div v-for="field in getFieldsInGroup('general')" :key="field.fieldCode" class="card-field" :style="cardStyles.field">
                  <div class="card-label" :style="cardStyles.label">{{ cardConfig.customLabels[field.fieldCode] || field.fieldName }}</div>
                  <div class="card-value" :style="cardStyles.value">{{ getFieldValue(field.fieldCode) }}</div>
                </div>
              </div>
            </div>

            <div class="card-section" v-if="hasFieldsInGroup('financials')">
              <div class="section-title" :style="cardStyles.sectionTitle">Financials</div>
              <div class="card-grid" :style="cardStyles.grid">
                <div v-for="field in getFieldsInGroup('financials')" :key="field.fieldCode" class="card-field" :style="cardStyles.field">
                  <div class="card-label" :style="cardStyles.label">{{ cardConfig.customLabels[field.fieldCode] || field.fieldName }}</div>
                  <div class="card-value" :style="cardStyles.value">{{ getFieldValue(field.fieldCode) }}</div>
                </div>
              </div>
            </div>

            <div class="card-section" v-if="hasFieldsInGroup('performance')">
              <div class="section-title" :style="cardStyles.sectionTitle">Performance</div>
              <div class="card-grid" :style="cardStyles.grid">
                <div v-for="field in getFieldsInGroup('performance')" :key="field.fieldCode" class="card-field" :style="cardStyles.field">
                  <div class="card-label" :style="cardStyles.label">{{ cardConfig.customLabels[field.fieldCode] || field.fieldName }}</div>
                  <div class="card-value" :style="cardStyles.value">{{ getFieldValue(field.fieldCode) }}</div>
                </div>
              </div>
            </div>

            <div class="card-section" v-if="hasFieldsInGroup('legal')">
              <div class="section-title" :style="cardStyles.sectionTitle">Legal</div>
              <div class="card-grid" :style="cardStyles.grid">
                <div v-for="field in getFieldsInGroup('legal')" :key="field.fieldCode" class="card-field" :style="cardStyles.field">
                  <div class="card-label" :style="cardStyles.label">{{ cardConfig.customLabels[field.fieldCode] || field.fieldName }}</div>
                  <div class="card-value" :style="cardStyles.value">{{ getFieldValue(field.fieldCode) }}</div>
                </div>
              </div>
            </div>

            <div class="card-section" v-if="hasFieldsInGroup('extended')">
              <div class="section-title" :style="cardStyles.sectionTitle">Additional</div>
              <div class="card-grid" :style="cardStyles.grid">
                <div v-for="field in getFieldsInGroup('extended')" :key="field.fieldCode" class="card-field" :style="cardStyles.field">
                  <div class="card-label" :style="cardStyles.label">{{ cardConfig.customLabels[field.fieldCode] || field.fieldName }}</div>
                  <div class="card-value" :style="cardStyles.value">{{ getFieldValue(field.fieldCode) }}</div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="showCardGenerator = false">Cancel</el-button>
        <el-button type="primary" @click="downloadCard">Download Card</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft, Cpu, Check, ChatDotRound, UploadFilled, Document, Close, Postcard } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const activeTab = ref('general')
const showSidePanel = ref(true)
const panelTab = ref('comments')
const newComment = ref('')
const isEditMode = ref(false)
const analyzing = ref(false)
const progress = ref(0)
const showCardGenerator = ref(false)
const contractFields = ref([])
const cardConfig = ref({
  selectedFields: ['contractNo', 'partyAName', 'partyBName', 'amount'],
  customLabels: {}
})

// Unified visual styles to match detail page
const cardStyles = {
  container: {
    background: 'rgba(255, 255, 255, 0.9)',
    borderRadius: '16px',
    boxShadow: '0 8px 32px rgba(0, 0, 0, 0.1)',
    padding: '32px',
    maxWidth: '450px',
    border: '1px solid rgba(255, 255, 255, 0.5)',
    backdropFilter: 'blur(12px)',
    borderTop: '6px solid var(--primary-color)'
  },
  header: {
    marginBottom: '24px',
    borderBottom: '1px solid rgba(0,0,0,0.05)',
    paddingBottom: '16px'
  },
  title: {
    fontSize: '20px',
    fontWeight: '700',
    color: '#1E293B',
    marginBottom: '8px',
    letterSpacing: '-0.5px'
  },
  subtitle: {
    fontFamily: "'Fira Code', monospace",
    fontSize: '12px',
    color: '#64748B'
  },
  sectionTitle: {
    fontSize: '14px',
    fontWeight: '600',
    color: '#475569',
    borderBottom: '1px solid rgba(0,0,0,0.05)',
    paddingBottom: '4px',
    marginTop: '16px',
    marginBottom: '8px',
    textTransform: 'uppercase',
    letterSpacing: '0.5px'
  },
  grid: {
    display: 'grid',
    gridTemplateColumns: '1fr 1fr',
    gap: '12px',
    marginBottom: '12px'
  },
  field: {
    display: 'flex',
    flexDirection: 'column',
    gap: '4px',
    padding: '4px 0'
  },
  label: {
    fontSize: '10px',
    color: '#94A3B8',
    fontWeight: '600',
    textTransform: 'uppercase',
    letterSpacing: '0.5px'
  },
  value: {
    fontSize: '13px',
    fontWeight: '500',
    color: '#334155',
    wordBreak: 'break-word'
  }
}

// Field Groups for Card Layout
const fieldGroups = {
  general: ['contractNo', 'contractName', 'contractType', 'contractStatus', 'crmContractId', 'partyAName', 'partyAContact', 'partyAPhone', 'partyAAddress', 'partyBName', 'partyBContact', 'partyBPhone', 'partyBAddress'],
  financials: ['contractAmount', 'taxRate', 'taxAmount', 'totalAmountWithTax', 'currencyType', 'paymentMethod', 'paymentTerm', 'invoiceTitle', 'invoiceType', 'taxpayerId'],
  performance: ['subjectType', 'subjectDesc', 'subjectQuantity', 'unitPrice', 'performanceMethod', 'performanceLocation', 'performanceStartDate', 'performanceEndDate', 'qualityStandard'],
  legal: ['signDate', 'effectiveDate', 'expireDate', 'disputeResolution', 'governingLaw', 'legalReviewFlag', 'legalReviewOpinion', 'remark', 'createUser', 'createTime']
}

const getFieldsInGroup = (group) => {
  const selected = cardConfig.value.selectedFields
  if (group === 'extended') {
    return contractFields.value.filter(f => f.source === 'EXTEND' && selected.includes(f.fieldCode))
  }
  const groupFields = fieldGroups[group] || []
  return contractFields.value.filter(f => groupFields.includes(f.fieldCode) && selected.includes(f.fieldCode))
}

const hasFieldsInGroup = (group) => {
  return getFieldsInGroup(group).length > 0
}

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

const getFieldName = (code) => {
  const f = contractFields.value.find(field => field.fieldCode === code)
  return f ? f.fieldName : code
}

const getFieldValue = (code) => {
  if (form.hasOwnProperty(code)) return form[code]
  if (form.customData && form.customData.hasOwnProperty(code)) return form.customData[code]
  if (form.extendedFields && form.extendedFields.hasOwnProperty(code)) return form.extendedFields[code]
  return 'N/A'
}

const downloadCard = () => {
  ElMessage.success('Card generation started...')
  showCardGenerator.value = false
}

const fetchContractDetail = async () => {
  const contractId = router.currentRoute.value.params.id
  try {
    const response = await fetch(`/api/contracts/${contractId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'X-Tenant-ID': localStorage.getItem('tenantId')
      }
    })
    if (response.ok) {
      const result = await response.json()
      Object.assign(form, result.data)
    }
  } catch (error) {
    console.error('Failed to fetch contract detail', error)
  }
}

onMounted(() => {
  fetchMetadata()
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
  customData: {}
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
  analyzing.value = true
  progress.value = 0
  const interval = setInterval(() => {
    progress.value += 10
    if (progress.value >= 100) {
      clearInterval(interval)
      analyzing.value = false
      ElMessage.success('AI Analysis completed')
      form.contractStatus = 'AI_EXTRACTED'
    }
  }, 200)
}

const saveContract = async () => {
  try {
    const response = await fetch(`/api/contracts/${form.contractId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'X-Tenant-ID': localStorage.getItem('tenantId')
      },
      body: JSON.stringify(form)
    })
    if (response.ok) {
      isEditMode.value = false
      ElMessage.success('Contract updated successfully')
      fetchContractDetail()
    }
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

.ai-progress {
  position: absolute;
  bottom: -10px;
  left: 0;
  right: 0;
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
.field-selection-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.field-selection-item {
  display: flex;
  flex-direction: column;
  padding: 8px;
  border: 1px solid var(--border-color);
  border-radius: 4px;
}

.contract-card-visual {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  padding: 24px;
  max-width: 400px;
  border-top: 4px solid var(--primary-color);
}

.contract-card-visual .card-header {
  margin-bottom: 20px;
}

.contract-card-visual .card-title {
  font-weight: 700;
  font-size: 18px;
  color: var(--text-primary);
}

.contract-card-visual .card-subtitle {
  font-size: 12px;
  color: var(--text-secondary);
  font-family: monospace;
}

.contract-card-visual .card-body {
  display: grid;
  gap: 12px;
}

.contract-card-visual .card-field {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}

.contract-card-visual .card-label {
  color: var(--text-secondary);
}

.contract-card-visual .card-value {
  font-weight: 500;
  color: var(--text-primary);
}
</style>
