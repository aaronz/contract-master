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
            <el-checkbox-group v-model="config.fields">
              <el-checkbox label="amount" class="w-full">Contract Amount</el-checkbox>
              <el-checkbox label="status" class="w-full">Status Badge</el-checkbox>
              <el-checkbox label="dates" class="w-full">Start/End Dates</el-checkbox>
              <el-checkbox label="risks" class="w-full">Risk Indicators</el-checkbox>
              <el-checkbox label="actions" class="w-full">Action Buttons</el-checkbox>
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
             <div class="mock-card" :style="{ borderTopColor: config.color, width: getWidth() }">
               <div class="card-header">
                 <div class="card-icon" :style="{ background: config.color }"><el-icon><Document /></el-icon></div>
                 <div class="card-meta">
                   <div class="card-title">Contract #2024-001</div>
                   <div class="card-sub">Procurement Agreement</div>
                 </div>
                 <div v-if="config.fields.includes('status')" class="status-badge" :style="{ color: config.color, background: hexToRgba(config.color, 0.1) }">Active</div>
               </div>
               
               <div class="card-body">
                 <div v-if="config.fields.includes('amount')" class="field-row">
                   <label>Amount</label>
                   <span class="amount">$1,250,000.00</span>
                 </div>
                 <div v-if="config.fields.includes('dates')" class="field-row">
                   <label>Period</label>
                   <span>Jan 01 - Dec 31, 2024</span>
                 </div>
                 <div v-if="config.fields.includes('risks')" class="risk-box">
                   <el-icon class="text-orange-500"><Warning /></el-icon> 2 Risks Detected
                 </div>
               </div>

               <div v-if="config.fields.includes('actions')" class="card-footer">
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
import { reactive, ref } from 'vue'
import { Document, Warning, CopyDocument } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const colors = ['#3B82F6', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6']

const config = reactive({
  target: 'web',
  color: '#3B82F6',
  fields: ['amount', 'status', 'dates', 'risks', 'actions'],
  size: 'medium'
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
  fields="${config.fields.join(',')}"
  size="${config.size}"
  api-key="YOUR_PUBLIC_KEY"
></contract-card>`
}

const copyCode = () => {
  navigator.clipboard.writeText(generateCode())
  ElMessage.success('Code copied to clipboard')
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
