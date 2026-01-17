<template>
  <div class="rules-config-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Rule Engine</h1>
        <p class="page-subtitle">Configure automated validation rules and risk alerts.</p>
      </div>
      <div class="header-actions">
        <el-button v-if="selectedRules.length > 0" type="success" icon="VideoPlay" @click="handleTriggerEvaluation">
          Run Evaluation ({{ selectedRules.length }})
        </el-button>
        <el-button v-if="selectedRules.length > 0" type="warning" icon="Edit" @click="openBatchEdit">
          Batch Edit
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

    <!-- Rule Editor Drawer -->
    <el-drawer
      v-model="editorVisible"
      :title="editingRule.id ? 'Edit Rule' : 'Create Rule'"
      size="800px"
      destroy-on-close
    >
      <RuleEditorForm 
        v-if="editorVisible"
        v-model="editingRule"
        :saving="saving"
        @save="saveRule"
        @cancel="editorVisible = false"
      />
    </el-drawer>

    <!-- Contract Selector Modal -->
    <ContractSelectorModal
      v-if="showContractSelector"
      v-model="showContractSelector"
      @confirm="onContractsSelected"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus, Delete, Warning, InfoFilled, Bell, Edit, VideoPlay } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import evaluationApi from '../services/evaluationApi'
import RuleEditorForm from '@/components/rules/RuleEditorForm.vue'
import ContractSelectorModal from '@/components/ContractSelectorModal.vue'

const rules = ref([])
const editorVisible = ref(false)
const saving = ref(false)
const editingRule = ref({})
const showContractSelector = ref(false)

onMounted(async () => {
  await fetchRules()
})

const handleTriggerEvaluation = () => {
  showContractSelector.value = true
}

const onContractsSelected = async (contractIds) => {
  showContractSelector.value = false
  const ruleIds = selectedRules.value.map(r => r.id)
  
  try {
    // If no rules selected, ruleIds will be empty (backend interprets as "all enabled rules")
    // But frontend UX implies we trigger for "Selected Rules" if selection exists.
    // If selection bar is visible, we trigger for selected rules.
    // If we add a global "Run All" button later, we'd pass empty ruleIds.
    
    // For now, let's assume this trigger is only available when rules are selected
    // OR we can make it available globally.
    // Based on the UI, let's add the button to the header actions as "Run Evaluation"
    // which runs for selected rules (if any) or ALL rules (if none selected, maybe prompt?)
    
    // Better UX: 
    // 1. If rules selected -> "Evaluate Selected Rules"
    // 2. If no rules selected -> "Evaluate All Rules"
    
    const response = await evaluationApi.triggerEvaluation(ruleIds, contractIds)
    if (response.status === 202) {
       ElMessage.success('Evaluation job started')
       // Optionally redirect to problem center or show a toast with link
    }
  } catch (error) {
    ElMessage.error('Failed to trigger evaluation')
    console.error(error)
  }
}


const fetchRules = async () => {
  try {
    const response = await evaluationApi.getRules()
    // Handle both wrapped ApiResponse and direct list (future proofing)
    const rawData = response.data.data || response.data
    
    if (Array.isArray(rawData)) {
      rules.value = rawData.map(r => ({
        ...r,
        id: r.ruleId,
        name: r.ruleName,
        level: r.ruleLevel,
        trigger: r.triggerTime,
        enabled: r.isEnabled,
        conditions: parseConditionForList(r.ruleCondition),
        logic: 'AND'
      }))
    } else {
      console.warn('Unexpected rules API format:', rawData)
      rules.value = []
    }
  } catch (error) {
    console.error('Failed to fetch rules', error)
    ElMessage.error('Failed to load rules')
  }
}

const parseConditionForList = (conditionStr) => {
  if (!conditionStr) return []
  try {
    const parsed = JSON.parse(conditionStr)
    if (parsed.children) return parsed.children.filter(c => c.type === 'rule')
    return Array.isArray(parsed) ? parsed : []
  } catch (e) {
    if (typeof conditionStr === 'string' && !conditionStr.startsWith('{') && !conditionStr.startsWith('[')) {
        const parts = conditionStr.split(' ')
        if (parts.length >= 3) {
            return [{
                field: parts[0],
                operator: parts[1],
                value: parts.slice(2).join(' ')
            }]
        }
        return [{ field: 'Raw', operator: ':', value: conditionStr }]
    }
    return []
  }
}

const parseCondition = (conditionStr) => {
  if (!conditionStr) return { type: 'group', operator: 'AND', children: [] }
  try {
    return JSON.parse(conditionStr)
  } catch (e) {
    // If not JSON, it might be a simple string condition like "amount > 1000"
    // We can try to parse simple expressions or just return it as a raw display
    if (typeof conditionStr === 'string' && !conditionStr.startsWith('{') && !conditionStr.startsWith('[')) {
        // Very basic parsing for display: "amount > 1000" -> { field: 'amount', operator: '>', value: '1000' }
        const parts = conditionStr.split(' ')
        if (parts.length >= 3) {
            return {
                type: 'group',
                operator: 'AND',
                children: [{
                    type: 'rule',
                    field: parts[0],
                    operator: convertOperator(parts[1]),
                    value: parts.slice(2).join(' ')
                }]
            }
        }
    }
    console.warn('Failed to parse rule condition:', conditionStr)
    return { type: 'group', operator: 'AND', children: [] }
  }
}

const convertOperator = (op) => {
    const map = { '>': 'gt', '<': 'lt', '==': 'eq', '!=': 'neq', '>=': 'gte', '<=': 'lte' }
    return map[op] || 'eq'
}

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
    await evaluationApi.batchUpdateRules(updates)
    await fetchRules() // Reload to get fresh data
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
    name: 'New Rule',
    level: 'info',
    trigger: 'create',
    ruleType: 'LOGIC',
    channels: ['system'],
    conditions: {
      type: 'group',
      operator: 'AND',
      children: []
    }
  }
  editorVisible.value = true
}

const editRule = async (rule) => {
  // Fetch fresh data to ensure we have the latest conditions
  try {
    const response = await evaluationApi.getRule(rule.id)
    const data = response.data.data // Wrapper handling
    editingRule.value = {
      ruleId: data.ruleId,
      id: data.ruleId,
      name: data.ruleName,
      level: data.ruleLevel,
      trigger: data.triggerTime,
      ruleType: data.ruleType,
      enabled: data.isEnabled,
      conditions: parseCondition(data.ruleCondition)
    }
    editorVisible.value = true
  } catch (error) {
    ElMessage.error('Failed to load rule details')
  }
}

const saveRule = async () => {
  saving.value = true
  const payload = {
    ruleName: editingRule.value.name,
    ruleLevel: editingRule.value.level,
    triggerTime: editingRule.value.trigger,
    ruleType: editingRule.value.ruleType,
    isEnabled: true,
    ruleCondition: JSON.stringify(editingRule.value.conditions),
  }

  try {
    if (editingRule.value.ruleId) {
      await evaluationApi.updateRule(editingRule.value.ruleId, payload)
    } else {
      await evaluationApi.createRule(payload)
    }
    ElMessage.success('Rule saved successfully')
    editorVisible.value = false
    fetchRules()
  } catch (error) {
    ElMessage.error('Failed to save rule')
    console.error(error)
  } finally {
    saving.value = false
  }
}

const formatTrigger = (t) => {
  const map = { 'ON_SAVE': 'On Save', 'ON_STATUS_CHANGE': 'Status Change', 'DAILY': 'Daily Schedule' }
  return map[t] || t
}

const getIconClass = (level) => {
  const map = { 'SEVERE': 'icon-severe', 'WARNING': 'icon-warning', 'INFO': 'icon-info' }
  return map[level]
}
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
</style>
