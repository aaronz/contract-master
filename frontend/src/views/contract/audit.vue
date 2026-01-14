<template>
  <div class="ai-audit-view">
    <div class="audit-header">
      <div class="header-left">
        <el-button @click="$router.back()" circle icon="ArrowLeft" />
        <div class="title-block">
          <h1>Contract Audit: #2024-089</h1>
          <el-tag type="warning" effect="dark">AI Review In Progress</el-tag>
        </div>
      </div>
      <div class="header-actions">
        <span class="progress-text">12/15 Fields Verified</span>
        <el-progress :percentage="80" :width="40" type="circle" :stroke-width="4" />
        <el-divider direction="vertical" />
        <el-button type="primary" icon="Check">Complete Audit</el-button>
      </div>
    </div>

    <div class="audit-container">
      <!-- Left: Document View -->
      <div class="document-panel">
        <div class="panel-header">
          <span>Original Document</span>
          <el-button-group size="small">
            <el-button icon="ZoomOut" />
            <el-button icon="ZoomIn" />
          </el-button-group>
        </div>
        <div class="document-viewer">
          <!-- Placeholder for PDF Viewer -->
          <div class="pdf-placeholder">
            <el-icon :size="48"><Document /></el-icon>
            <p>PDF Viewer Component</p>
            <p class="sub">Showing: Service_Agreement_v2.pdf</p>
          </div>
        </div>
      </div>

      <!-- Right: AI Extraction -->
      <div class="extraction-panel">
        <div class="panel-header">
          <span>AI Extraction Results</span>
          <el-switch v-model="showConfidence" active-text="Show Confidence" />
        </div>
        
        <div class="fields-list custom-scrollbar">
          <div v-for="(field, index) in extractedFields" :key="index" class="field-item" :class="{ verified: field.status === 'verified' }">
            <div class="field-header">
              <span class="field-label">{{ field.label }}</span>
              <div class="field-status">
                <el-tag v-if="showConfidence" size="small" :type="getConfidenceType(field.confidence)">
                  {{ field.confidence }}% Match
                </el-tag>
                <el-tag v-if="field.status === 'verified'" type="success" size="small" effect="dark">
                  <el-icon><Check /></el-icon> Verified
                </el-tag>
                <el-button v-else type="primary" link size="small" @click="field.status = 'verified'">
                  Verify
                </el-button>
              </div>
            </div>
            
            <div class="field-input-wrapper">
              <el-input 
                v-model="field.value" 
                :readonly="field.status === 'verified'"
                type="textarea" 
                autosize
              >
                <template #suffix>
                   <el-icon v-if="field.status === 'edited'" class="edit-icon"><Edit /></el-icon>
                </template>
              </el-input>
              <div class="field-actions" v-if="field.status !== 'verified'">
                 <el-button size="small" icon="Edit" @click="field.status = 'edited'">Edit</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ArrowLeft, Check, Document, ZoomIn, ZoomOut, Edit } from '@element-plus/icons-vue'

const showConfidence = ref(true)

const extractedFields = ref([
  { label: 'Contract Title', value: 'Master Service Agreement', confidence: 98, status: 'verified' },
  { label: 'Effective Date', value: '2024-01-15', confidence: 95, status: 'verified' },
  { label: 'Termination Notice', value: '30 days written notice required.', confidence: 88, status: 'pending' },
  { label: 'Total Value', value: '$150,000.00 USD', confidence: 92, status: 'pending' },
  { label: 'Governing Law', value: 'State of California', confidence: 99, status: 'verified' },
  { label: 'Counterparty', value: 'Acme Corp Inc.', confidence: 96, status: 'verified' },
  { label: 'Liability Cap', value: 'Limited to the total fees paid in the preceding 12 months.', confidence: 75, status: 'edited' },
  { label: 'Renewal Terms', value: 'Automatic renewal for 1 year terms unless cancelled.', confidence: 85, status: 'pending' }
])

const getConfidenceType = (val) => {
  if (val >= 90) return 'success'
  if (val >= 70) return 'warning'
  return 'danger'
}
</script>

<style scoped>
.ai-audit-view {
  height: calc(100vh - 100px);
  display: flex;
  flex-direction: column;
}

.audit-header {
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid var(--border-color);
  margin-bottom: 16px;
  padding-bottom: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.title-block h1 {
  font-size: 18px;
  margin: 0;
  display: inline-block;
  margin-right: 12px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.progress-text {
  font-size: 13px;
  color: var(--text-secondary);
  font-weight: 500;
}

.audit-container {
  flex: 1;
  display: flex;
  gap: 24px;
  overflow: hidden;
}

.document-panel, .extraction-panel {
  flex: 1;
  background: white;
  border-radius: 12px;
  border: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  height: 48px;
  padding: 0 16px;
  border-bottom: 1px solid #F1F5F9;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #F8FAFC;
  font-weight: 600;
  font-size: 14px;
  color: var(--text-primary);
}

.document-viewer {
  flex: 1;
  background: #334155;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pdf-placeholder {
  text-align: center;
  color: white;
  opacity: 0.7;
}

.pdf-placeholder .sub {
  font-size: 12px;
  opacity: 0.5;
}

.fields-list {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.field-item {
  padding: 16px;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  transition: all 0.2s;
}

.field-item:hover {
  border-color: var(--accent-color);
  background: #F8FAFC;
}

.field-item.verified {
  border-color: var(--success-color);
  background: #F0FDF4;
}

.field-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.field-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.field-status {
  display: flex;
  align-items: center;
  gap: 8px;
}

.field-input-wrapper {
  display: flex;
  gap: 8px;
  align-items: flex-start;
}

.field-input-wrapper .el-textarea {
  flex: 1;
}

.field-actions {
  display: flex;
  flex-direction: column;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #CBD5E1;
  border-radius: 4px;
}
</style>
