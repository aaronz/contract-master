<template>
  <div class="contract-detail-page p-6" v-loading="loading">
    <!-- Header -->
    <div class="detail-header glass-panel mb-6">
      <div class="header-left">
        <el-button link @click="router.back()" icon="ArrowLeft">{{ $t('common.back') }}</el-button>
        <h1 class="page-title">{{ form.contractName }}</h1>
        <el-tag :type="getStatusType(form.contractStatus)" effect="dark" round>{{ form.contractStatus }}</el-tag>
      </div>
      
      <div class="header-right">
        <div class="mode-toggle glass-panel-sm">
          <span :class="{ active: !isEditMode }" @click="isEditMode = false">{{ $t('common.view') }}</span>
          <span :class="{ active: isEditMode }" @click="isEditMode = true">{{ $t('common.edit') }}</span>
        </div>
        
        <el-divider direction="vertical" />

        <el-button type="warning" @click="confirmAiSuggestions" v-if="form.contractStatus === 'AI_EXTRACTED'">
          {{ $t('common.confirm') }}
        </el-button>
        
        <el-button type="success" @click="publishContract" v-if="form.contractStatus === 'VERIFIED'">
          {{ $t('menu.hubOverview') }}
        </el-button>

        <el-button type="primary" @click="openRuleSelector" class="glass-btn">
          <el-icon><Refresh /></el-icon> {{ $t('contract.reEvaluate') }}
        </el-button>
        
        <el-button type="primary" @click="saveContract" v-if="isEditMode" class="save-btn">
          <el-icon><Check /></el-icon> {{ $t('common.save') }}
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
          <el-tab-pane :label="$t('contract.generalInfo')" name="general">
            <!-- AI Re-Extraction Area -->
            <div v-if="isEditMode" class="ai-extraction-area glass-panel-sm p-4 mb-6 border-blue-100 bg-blue-50/30">
              <div class="flex items-center justify-between">
                <div>
                  <h4 class="m-0 text-blue-600 flex items-center gap-2">
                    <el-icon><MagicStick /></el-icon> AI Sync from Document
                  </h4>
                  <p class="text-xs text-gray-500 m-0 mt-1">Upload a new version of the contract to update fields using AI.</p>
                </div>
                <div class="flex gap-2">
                  <el-upload
                    action="#"
                    :auto-upload="false"
                    :show-file-list="false"
                    :on-change="handleAiFileChange"
                  >
                    <el-button type="primary" plain size="small" icon="Upload">New Version</el-button>
                  </el-upload>
                  <el-button 
                    type="primary" 
                    size="small" 
                    :loading="extracting" 
                    :disabled="!selectedAiFile"
                    @click="runAiExtraction"
                  >
                    Re-extract
                  </el-button>
                </div>
              </div>
            </div>

            <div class="section-grid">
              <!-- Basic Info -->
              <div class="form-section">
                <h3 class="section-header">Core Details</h3>
                <div class="form-grid">
                  <div class="field-group" v-if="isFieldVisible('contract_no')">
                    <label>{{ getFieldName('contract_no') }}</label>
                    <el-input v-if="isEditMode" v-model="form.contractNo" :class="{ 'ai-highlight': aiUpdatedFields.has('contractNo') }" />
                    <div v-else class="display-val font-mono" :style="getFieldStyle('contract_no')">{{ form.contractNo }}</div>
                  </div>
                  <div class="field-group" v-if="isFieldVisible('contract_name')">
                    <label>{{ getFieldName('contract_name') }}</label>
                    <el-input v-if="isEditMode" v-model="form.contractName" :class="{ 'ai-highlight': aiUpdatedFields.has('contractName') }" />
                    <div v-else class="display-val" :style="getFieldStyle('contract_name')">{{ form.contractName }}</div>
                  </div>
                  <div class="field-group" v-if="isFieldVisible('contract_type')">
                    <label>{{ getFieldName('contract_type') }}</label>
                    <el-input v-if="isEditMode" v-model="form.contractType" :class="{ 'ai-highlight': aiUpdatedFields.has('contractType') }" />
                    <div v-else class="display-val" :style="getFieldStyle('contract_type')">{{ form.contractType }}</div>
                  </div>
                  <div class="field-group" v-if="isFieldVisible('contract_status')">
                    <label>{{ getFieldName('contract_status') }}</label>
                    <el-select v-if="isEditMode" v-model="form.contractStatus" style="width: 100%">
                      <el-option :label="$t('contract.enums.status.active')" value="Active" />
                      <el-option :label="$t('contract.enums.status.draft')" value="Draft" />
                      <el-option :label="$t('contract.enums.status.pending')" value="Pending" />
                      <el-option :label="$t('contract.enums.status.expired')" value="Expired" />
                    </el-select>
                    <el-tag v-else :type="getStatusType(form.contractStatus)" round :style="getFieldStyle('contract_status')">{{ form.contractStatus }}</el-tag>
                  </div>
                </div>
              </div>

              <!-- Parties -->
              <div class="parties-wrapper">
                <div class="form-section party-card" v-if="isFieldVisible('party_a_name') || isFieldVisible('party_a_contact') || isFieldVisible('party_a_phone')">
                   <h3 class="section-header text-blue">{{ $t('contract.partyA') }}</h3>
                   <div class="form-stack">
                     <div class="field-group" v-if="isFieldVisible('party_a_name')">
                       <label>{{ getFieldName('party_a_name') }}</label>
                       <el-input v-if="isEditMode" v-model="form.partyAName" :class="{ 'ai-highlight': aiUpdatedFields.has('partyAName') }" />
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

                 <div class="form-section party-card" v-if="isFieldVisible('party_b_name') || isFieldVisible('party_b_contact') || isFieldVisible('party_b_phone') || isFieldVisible('party_b_address')">
                   <h3 class="section-header text-purple">{{ $t('contract.partyB') }}</h3>
                   <div class="form-stack">
                     <div class="field-group" v-if="isFieldVisible('party_b_name')">
                       <label>{{ getFieldName('party_b_name') }}</label>
                       <el-input v-if="isEditMode" v-model="form.partyBName" :class="{ 'ai-highlight': aiUpdatedFields.has('partyBName') }" />
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

          <el-tab-pane :label="$t('contract.financials')" name="financials">
            <div class="form-section">
              <h3 class="section-header">Financial Details</h3>
              <div class="form-grid-3">
                <div class="field-group" v-if="isFieldVisible('currency_type')">
                  <label>{{ getFieldName('currency_type') }}</label>
                  <el-select v-if="isEditMode" v-model="form.currencyType" style="width: 100%">
                    <el-option :label="$t('contract.enums.currency.cny')" value="CNY" />
                    <el-option :label="$t('contract.enums.currency.usd')" value="USD" />
                    <el-option :label="$t('contract.enums.currency.eur')" value="EUR" />
                  </el-select>
                  <div v-else class="display-val" :style="getFieldStyle('currency_type')">{{ form.currencyType }}</div>
                </div>
                <div class="field-group" v-if="isFieldVisible('contract_amount')">
                  <label>{{ getFieldName('contract_amount') }}</label>
                  <el-input-number v-if="isEditMode" v-model="form.contractAmount" :precision="2" style="width: 100%" :class="{ 'ai-highlight': aiUpdatedFields.has('contractAmount') }" />
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
                    <el-option :label="$t('contract.enums.payment.bank')" value="Bank Transfer" />
                    <el-option :label="$t('contract.enums.payment.check')" value="Check" />
                    <el-option :label="$t('contract.enums.payment.cash')" value="Cash" />
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

          <el-tab-pane :label="$t('contract.performance')" name="performance">
            <div class="form-section">
              <h3 class="section-header">Subject Matter</h3>
              <div class="form-grid-3">
                 <div class="field-group" v-if="isFieldVisible('subject_type')">
                   <label>{{ getFieldName('subject_type') }}</label>
                   <el-select v-if="isEditMode" v-model="form.subjectType" style="width: 100%">
                     <el-option :label="$t('contract.enums.subject.goods')" value="Goods" />
                     <el-option :label="$t('contract.enums.subject.services')" value="Services" />
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

          <el-tab-pane :label="$t('contract.legalDates')" name="legal">
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
                    <el-option :label="$t('contract.enums.resolution.negotiation')" value="Negotiation" />
                    <el-option :label="$t('contract.enums.resolution.arbitration')" value="Arbitration" />
                    <el-option :label="$t('contract.enums.resolution.litigation')" value="Litigation" />
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

          <el-tab-pane :label="$t('contract.attachments')" name="attachments">
            <el-upload
              v-if="isEditMode"
              class="upload-demo"
              drag
              action="#"
              :auto-upload="false"
              :on-change="handleNewAttachment"
            >
              <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
              <div class="el-upload__text">
                Drop file here or <em>click to upload</em>
              </div>
            </el-upload>
            
            <div class="attachment-list mt-6">
               <div class="attachment-item glass-panel-sm" v-for="item in attachments" :key="item.attachmentId">
                 <el-icon class="file-icon"><Document /></el-icon>
                 <div class="file-info">
                   <div class="file-name">{{ item.attachmentName }}</div>
                   <div class="file-meta">{{ (item.fileSize / 1024 / 1024).toFixed(2) }}MB • {{ item.fileFormat }} • {{ item.createTime }}</div>
                 </div>
                 <div class="file-actions">
                   <el-button link type="primary" @click="previewAttachment(item)">Preview</el-button>
                   <el-button link type="primary" @click="downloadAttachment(item)">Download</el-button>
                 </div>
               </div>
        <div v-if="attachments.length === 0" class="text-center p-10 text-gray-400">
          {{ $t('common.noNotifications') }}
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
            <el-tab-pane :label="$t('contract.comments')" name="comments">
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
            
            <el-tab-pane :label="$t('contract.auditLog')" name="audit">
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
import request from '@/utils/request'
import RuleSelectorModal from '@/components/RuleSelectorModal.vue' 

const fieldStore = useFieldStore()
const router = useRouter()
const activeTab = ref('general')
const showSidePanel = ref(true)
const panelTab = ref('comments')
const newComment = ref('')
const isEditMode = ref(false)
const showRuleSelectorModal = ref(false)
const loading = ref(false)

// AI Extraction State
const extracting = ref(false)
const selectedAiFile = ref(null)
const aiUpdatedFields = ref(new Set())

const handleAiFileChange = (file) => {
  selectedAiFile.value = file.raw
}

const runAiExtraction = async () => {
  if (!selectedAiFile.value) return
  extracting.value = true
  
  const formData = new FormData()
  formData.append('file', selectedAiFile.value)
  formData.append('contractId', form.contractId || router.currentRoute.value.params.id)
  
  try {
    const response = await request.post('/contracts/extract', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (response.data && response.data.data) {
      const extracted = response.data.data
      // Apply extracted fields to form
      Object.keys(extracted).forEach(key => {
        if (key in form) {
          form[key] = extracted[key]
          aiUpdatedFields.value.add(key)
        }
      })
      ElMessage.success(t('common.success'))
      // Important: if we didn't have a contractId before, refresh everything
      if (!form.contractId) {
        fetchContractDetail()
      } else {
        fetchAttachments()
      }
    }
  } catch (error) {
    console.error('AI Extraction failed', error)
    ElMessage.error(t('common.error'))
  } finally {
    extracting.value = false
  }
}

const handleNewAttachment = async (file) => {
  const formData = new FormData()
  formData.append('file', file.raw)
  try {
    await request.post(`/contracts/${form.contractId}/attachments`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    ElMessage.success('Attachment uploaded')
    fetchAttachments()
  } catch (e) {}
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

const isFieldVisible = (fieldCode) => fieldStore.isFieldVisible(fieldCode)

const getFieldName = (code) => {
  const f = fieldStore.fields.find(field => field.fieldCode === code)
  return f ? f.fieldName : code
}

const fetchContractDetail = async () => {
  const contractId = router.currentRoute.value.params.id
  if (!contractId) return
  
  loading.value = true
  try {
    const response = await contractApi.getContractDetail(contractId)
    const result = response.data
    if (result && result.data) {
      Object.assign(form, result.data)
      if (result.data.contractId) {
        form.contractId = result.data.contractId
        // Fetch attachments only after we have a valid contractId
        fetchAttachments()
      }
    } else {
      ElMessage.error('Invalid response format from server')
    }
  } catch (error) {
    console.error('Failed to fetch contract detail:', error)
  } finally {
    loading.value = false
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

const attachments = ref([])
const fetchAttachments = async () => {
  if (!form.contractId) return
  try {
    const res = await request.get(`/contracts/${form.contractId}/attachments`)
    if (res.data && res.data.data) {
      attachments.value = res.data.data
    }
  } catch (e) {
    console.error('Failed to fetch attachments', e)
  }
}

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
    aiUpdatedFields.value.clear()
    ElMessage.success('Contract updated successfully')
    fetchContractDetail()
  } catch (error) {
    console.error('Update failed', error)
  }
}

const getStatusType = (status) => {
  const map = {
    'Active': 'success',
    'Draft': 'info',
    'Pending': 'warning',
    'Expired': 'danger',
    'AI_EXTRACTED': 'warning',
    'VERIFIED': 'primary',
    'PUBLISHED': 'success'
  }
  return map[status] || 'info'
}

const formatCurrency = (val, currency = 'USD') => {
  if (val === null || val === undefined) return '-'
  return new Intl.NumberFormat('en-US', { style: 'currency', currency }).format(val)
}

const confirmAiSuggestions = async () => {
  try {
    await request.post(`/contracts/${form.contractId}/verify`)
    form.contractStatus = 'VERIFIED'
    ElMessage.success('AI suggestions verified successfully')
  } catch (e) {}
}

const publishContract = async () => {
  try {
    const res = await request.post(`/contracts/${form.contractId}/publish`)
    if (res.data.status === 200 || res.status === 200) {
      form.contractStatus = 'PUBLISHED'
      ElMessage.success('Contract published to all downstream systems')
    }
  } catch (error) {
    console.error('Publication failed', error)
  }
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
    console.error('Re-evaluation failed:', error)
  }
}

const addComment = () => {
  if (!newComment.value.trim()) return
  comments.value.unshift({
    id: Date.now(),
    user: 'Admin',
    time: 'Just now',
    text: newComment.value
  })
  newComment.value = ''
}

const previewAttachment = (item) => {
  ElMessage.info(`Opening preview for ${item.attachmentName}...`)
  const url = `${window.location.origin}/api/attachments/view/${item.attachmentId}`
  window.open(url, '_blank')
}

const downloadAttachment = (item) => {
  ElMessage.info(`Starting download for ${item.attachmentName}...`)
  const link = document.createElement('a')
  link.href = `/api/attachments/download/${item.attachmentId}`
  link.download = item.attachmentName
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}
</script>

<style scoped>
.contract-detail-page {
  min-height: 100vh;
  background: #F8FAFC;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-radius: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.page-title {
  font-size: 20px;
  font-weight: 700;
  color: #1E293B;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.mode-toggle {
  display: flex;
  padding: 4px;
  background: rgba(0,0,0,0.05);
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
}

.mode-toggle span {
  padding: 6px 16px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.2s;
  color: #64748B;
}

.mode-toggle span.active {
  background: white;
  color: #3B82F6;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.detail-container {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}

.main-form-area {
  flex: 1;
  transition: all 0.3s;
  min-width: 0;
}

.main-form-area.with-panel {
  margin-right: 0;
}

.side-panel {
  width: 350px;
  height: calc(100vh - 120px);
  position: sticky;
  top: 20px;
  display: flex;
  flex-direction: column;
}

.panel-header {
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}

.panel-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 700;
}

.section-header {
  font-size: 14px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-bottom: 20px;
  color: #64748B;
}

.form-section {
  padding: 24px;
  background: rgba(255,255,255,0.5);
  border-radius: 12px;
  border: 1px solid rgba(0,0,0,0.02);
  margin-bottom: 24px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.form-grid-3 {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.field-group label {
  display: block;
  font-size: 12px;
  font-weight: 600;
  color: #94A3B8;
  margin-bottom: 8px;
}

.display-val {
  font-size: 14px;
  color: #1E293B;
  min-height: 32px;
  display: flex;
  align-items: center;
}

.display-val.strong { font-weight: 600; }
.display-val.highlight { color: #3B82F6; font-weight: 700; font-size: 16px; }

:deep(.ai-highlight .el-input__wrapper) {
  box-shadow: 0 0 0 1px #3B82F6 inset !important;
  background-color: rgba(59, 130, 246, 0.05) !important;
}

:deep(.ai-highlight.el-input-number .el-input__wrapper) {
  box-shadow: 0 0 0 1px #3B82F6 inset !important;
  background-color: rgba(59, 130, 246, 0.05) !important;
}

.comments-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.comment-item {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.comment-content {
  flex: 1;
  background: #F8FAFC;
  padding: 12px;
  border-radius: 12px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
}

.comment-header .user { font-weight: 600; font-size: 13px; }
.comment-header .time { font-size: 11px; color: #94A3B8; }
.comment-text { font-size: 13px; color: #475569; line-height: 1.4; }

.comment-input {
  padding: 16px;
  border-top: 1px solid rgba(0,0,0,0.05);
}

.glass-panel {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.glass-card {
  background: white;
  border: 1px solid #E2E8F0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border-radius: 16px;
}

.slide-left-enter-active, .slide-left-leave-active { transition: all 0.3s ease; }
.slide-left-enter-from, .slide-left-leave-to { transform: translateX(100%); opacity: 0; }
</style>
