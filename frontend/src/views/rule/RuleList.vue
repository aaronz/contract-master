<template>
  <div class="rules-config-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Rule Engine</h1>
        <p class="page-subtitle">Configure automated validation rules and risk alerts.</p>
      </div>
      <div class="header-actions">
        <el-button v-if="selectedRules.length > 0" type="success" icon="Edit" @click="openBatchEdit">
          Batch Edit ({{ selectedRules.length }})
        </el-button>
        <el-button type="primary" icon="Plus" @click="addRule">Add New Rule</el-button>
      </div>
    </div>

    <div class="selection-bar" v-if="selectedRules.length > 0">
      <el-alert type="info" :closable="false" show-icon>
        <template #title>
          <span>{{ selectedRules.length }} rules selected</span>
          <el-button link type="primary" @click="clearSelection" style="margin-left: 12px">Clear Selection</el-button>
        </template>
      </el-alert>
    </div>

    <div class="rules-grid">
      <el-card 
        v-for="rule in rules" 
        :key="rule.id" 
        class="rule-card" 
        :class="{ 'is-selected': isSelected(rule) }"
        shadow="hover"
        @click.ctrl="toggleSelection(rule)"
        @click.meta="toggleSelection(rule)"
      >
        <div class="selection-checkbox">
          <el-checkbox :model-value="isSelected(rule)" @change="toggleSelection(rule)" />
        </div>
        <div class="rule-header">
          <div class="rule-title-group">
             <div class="rule-icon" :class="getIconClass(rule.level)">
               <el-icon v-if="rule.level === 'SEVERE'"><Warning /></el-icon>
               <el-icon v-else-if="rule.level === 'WARNING'"><InfoFilled /></el-icon>
               <el-icon v-else><Bell /></el-icon>
             </div>
             <div>
               <h3 class="rule-name">{{ rule.name }}</h3>
               <span class="rule-trigger">Trigger: {{ formatTrigger(rule.trigger) }}</span>
             </div>
          </div>
          <el-switch v-model="rule.enabled" />
        </div>
        
        <div class="rule-body">
          <div class="condition-preview">
            <div v-for="(cond, idx) in rule.conditions" :key="idx" class="condition-tag">
              <span class="field">{{ cond.field }}</span>
              <span class="operator">{{ cond.operator }}</span>
              <span class="value">{{ cond.value }}</span>
              <span v-if="idx < rule.conditions.length - 1" class="logic">{{ rule.logic }}</span>
            </div>
          </div>
        </div>

        <div class="rule-footer">
           <el-button link type="primary" @click="editRule(rule)">Edit</el-button>
           <el-button link type="danger">Delete</el-button>
        </div>
      </el-card>
    </div>

    <!-- Batch Edit Dialog -->
    <el-dialog
      v-model="batchDialogVisible"
      title="Batch Edit Rules"
      width="500px"
    >
      <el-form :model="batchForm" label-position="top">
        <el-alert 
          title="Only selected fields will be updated for all checked rules." 
          type="warning" 
          :closable="false" 
          style="margin-bottom: 20px" 
        />
        
        <el-form-item label="Update Status">
          <el-checkbox v-model="batchUpdates.enabled">Update Enabled State</el-checkbox>
          <el-switch v-model="batchForm.enabled" :disabled="!batchUpdates.enabled" style="margin-left: 12px"/>
        </el-form-item>

        <el-form-item label="Update Risk Level">
          <el-checkbox v-model="batchUpdates.level">Update Risk Level</el-checkbox>
          <el-select v-model="batchForm.level" :disabled="!batchUpdates.level" style="width: 100%; margin-top: 8px">
             <el-option label="Info" value="INFO" />
             <el-option label="Warning" value="WARNING" />
             <el-option label="Severe" value="SEVERE" />
          </el-select>
        </el-form-item>

        <el-form-item label="Update Trigger">
          <el-checkbox v-model="batchUpdates.trigger">Update Trigger Event</el-checkbox>
          <el-select v-model="batchForm.trigger" :disabled="!batchUpdates.trigger" style="width: 100%; margin-top: 8px">
             <el-option label="On Save" value="ON_SAVE" />
             <el-option label="On Status Change" value="ON_STATUS_CHANGE" />
             <el-option label="Daily Schedule" value="DAILY" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveBatchUpdate">Update {{ selectedRules.length }} Rules</el-button>
      </template>
    </el-dialog>

    <!-- Rule Editor Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="editingRule.id ? 'Edit Rule' : 'Create Rule'"
      width="600px"
      destroy-on-close
    >
      <el-form :model="editingRule" label-position="top">
        <el-form-item label="Rule Name">
            <el-input v-model="editingRule.name" placeholder="e.g. High Value Contract Alert" />
        </el-form-item>

        <el-form-item label="Rule Type">
          <el-radio-group v-model="editingRule.logicType" size="small">
            <el-radio-button value="GROOVY">Groovy</el-radio-button>
            <el-radio-button value="REGEX">Regex</el-radio-button>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="Logic Content">
          <el-input 
            v-model="editingRule.logicContent" 
            type="textarea" 
            :rows="6" 
            placeholder="e.g. contract.amount > 1000000 (Groovy) or \d{10} (Regex)" 
          />
        </el-form-item>
        
        <el-form-item label="Action">
           <el-checkbox v-model="editingRule.sendEmail">Send Email</el-checkbox>
           <el-checkbox v-model="editingRule.blockProcess">Block Process</el-checkbox>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveRule">Save Rule</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Plus, Delete, Warning, InfoFilled, Bell, Edit } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import evaluationApi from '../../services/evaluationApi'

const rules = ref([
  {
    id: 1,
    name: 'High Value Approval',
    level: 'SEVERE',
    trigger: 'ON_SAVE',
    enabled: true,
    logic: 'AND',
    conditions: [
      { field: 'amount', operator: '>', value: '1000000' }
    ],
    sendEmail: true,
    blockProcess: true
  },
  {
    id: 2,
    name: 'Missing Tax ID',
    level: 'WARNING',
    trigger: 'ON_SAVE',
    enabled: true,
    logic: 'AND',
    conditions: [
      { field: 'type', operator: '==', value: 'Sales' },
      { field: 'taxId', operator: '==', value: 'null' }
    ],
    sendEmail: false,
    blockProcess: false
  }
])

const dialogVisible = ref(false)
const editingRule = ref({})

// Batch Selection Logic
const selectedRules = ref([])
const isSelected = (rule) => selectedRules.value.some(r => r.id === rule.id)

const toggleSelection = (rule) => {
  const idx = selectedRules.value.findIndex(r => r.id === rule.id)
  if (idx > -1) {
    selectedRules.value.splice(idx, 1)
  } else {
    selectedRules.value.push(rule)
  }
}

const clearSelection = () => {
  selectedRules.value = []
}

// Batch Edit Logic
const batchDialogVisible = ref(false)
const batchForm = ref({ enabled: false, level: 'INFO', trigger: 'ON_SAVE' })
const batchUpdates = ref({ enabled: false, level: false, trigger: false })

const openBatchEdit = () => {
  batchUpdates.value = { enabled: false, level: false, trigger: false }
  batchDialogVisible.value = true
}

const saveBatchUpdate = async () => {
  const updates = selectedRules.value.map(rule => {
    const updatedRule = { ...rule }
    if (batchUpdates.value.enabled) updatedRule.enabled = batchForm.value.enabled
    if (batchUpdates.value.level) updatedRule.level = batchForm.value.level
    if (batchUpdates.value.trigger) updatedRule.trigger = batchForm.value.trigger
    return updatedRule
  })

  try {
    // Send to backend
    await evaluationApi.batchUpdateRules(updates)
    
    // Update local state
    updates.forEach(updated => {
      const idx = rules.value.findIndex(r => r.id === updated.id)
      if (idx > -1) rules.value[idx] = updated
    })
    
    ElMessage.success(`Successfully updated ${updates.length} rules`)
    batchDialogVisible.value = false
    clearSelection()
  } catch (error) {
    ElMessage.error('Failed to update rules')
    console.error(error)
  }
}

const addRule = () => {
  editingRule.value = {
    name: '',
    ruleType: 'LOGIC',
    level: 'INFO',
    trigger: 'ON_SAVE',
    logic: 'AND',
    conditions: [{ field: '', operator: '', value: '' }],
    aiPromptTemplate: '',
    sendEmail: false,
    blockProcess: false
  }
  dialogVisible.value = true
}

const editRule = (rule) => {
  // Deep copy
  editingRule.value = JSON.parse(JSON.stringify(rule))
  dialogVisible.value = true
}

const addCondition = () => {
  editingRule.value.conditions.push({ field: '', operator: '', value: '' })
}

const removeCondition = (idx) => {
  editingRule.value.conditions.splice(idx, 1)
}

const saveRule = async () => {
  try {
    if (editingRule.value.id) {
      await problemApi.updateRule(editingRule.value.id, editingRule.value)
    } else {
      await problemApi.createRule(editingRule.value)
    }
    loadRules()
    dialogVisible.value = false
    ElMessage.success('Rule saved successfully')
  } catch (error) {
    ElMessage.error('Failed to save rule')
  }
}

const loadRules = async () => {
  try {
    const response = await problemApi.getRules()
    rules.value = response.data
  } catch (error) {
    ElMessage.error('Failed to load rules')
  }
}

onMounted(() => {
  loadRules()
})
</script>

<style scoped>
.rules-config-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.selection-bar {
  margin-bottom: 16px;
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

.rules-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 24px;
}

.rule-card {
  border-radius: 12px;
  border: 1px solid var(--border-color);
  transition: all 0.2s;
  position: relative;
}

.rule-card.is-selected {
  border-color: #3B82F6;
  background-color: #EFF6FF;
}

.selection-checkbox {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 10;
}

.rule-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--box-shadow-md);
}

.rule-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.rule-title-group {
  display: flex;
  gap: 12px;
}

.rule-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.icon-severe { background: #FEF2F2; color: #DC2626; }
.icon-warning { background: #FFF7ED; color: #EA580C; }
.icon-info { background: #EFF6FF; color: #2563EB; }

.rule-name {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
}

.rule-trigger {
  font-size: 12px;
  color: var(--text-secondary);
}

.rule-body {
  background: #F8FAFC;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 16px;
  min-height: 60px;
}

.condition-tag {
  display: inline-flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  font-size: 13px;
  line-height: 1.6;
}

.condition-tag .field { font-weight: 600; color: var(--text-primary); }
.condition-tag .operator { color: var(--accent-color); font-family: monospace; }
.condition-tag .value { background: white; padding: 0 4px; border-radius: 4px; border: 1px solid #E2E8F0; }
.condition-tag .logic { font-weight: bold; color: #94A3B8; margin: 0 4px; font-size: 11px; }

.rule-footer {
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid var(--border-color);
  padding-top: 12px;
  margin-top: 12px;
}

.conditions-builder {
  background: #F8FAFC;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

.condition-row {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.add-btn {
  width: 100%;
  border-style: dashed;
}

.logic-toggle {
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
}
</style>
