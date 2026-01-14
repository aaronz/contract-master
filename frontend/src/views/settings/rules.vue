<template>
  <div class="rules-config-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Rule Engine</h1>
        <p class="page-subtitle">Configure automated validation rules and risk alerts.</p>
      </div>
      <el-button type="primary" icon="Plus" @click="addRule">Add New Rule</el-button>
    </div>

    <div class="rules-grid">
      <el-card v-for="rule in rules" :key="rule.id" class="rule-card" shadow="hover">
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
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Risk Level">
               <el-select v-model="editingRule.level" style="width: 100%">
                 <el-option label="Info" value="INFO" />
                 <el-option label="Warning" value="WARNING" />
                 <el-option label="Severe" value="SEVERE" />
               </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Trigger Event">
               <el-select v-model="editingRule.trigger" style="width: 100%">
                 <el-option label="On Save" value="ON_SAVE" />
                 <el-option label="On Status Change" value="ON_STATUS_CHANGE" />
                 <el-option label="Daily Schedule" value="DAILY" />
               </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="Conditions">
           <div class="conditions-builder">
              <div v-for="(cond, idx) in editingRule.conditions" :key="idx" class="condition-row">
                 <el-select v-model="cond.field" placeholder="Field" style="width: 140px">
                    <el-option label="Amount" value="amount" />
                    <el-option label="Status" value="status" />
                    <el-option label="Sign Date" value="signDate" />
                    <el-option label="Type" value="type" />
                 </el-select>
                 <el-select v-model="cond.operator" placeholder="Op" style="width: 100px">
                    <el-option label=">" value=">" />
                    <el-option label="<" value="<" />
                    <el-option label="==" value="==" />
                    <el-option label="Contains" value="contains" />
                 </el-select>
                 <el-input v-model="cond.value" placeholder="Value" style="flex: 1" />
                 <el-button circle type="danger" link @click="removeCondition(idx)">
                   <el-icon><Delete /></el-icon>
                 </el-button>
              </div>
              <el-button type="dashed" class="add-btn" @click="addCondition">
                <el-icon><Plus /></el-icon> Add Condition
              </el-button>
              
              <div class="logic-toggle" v-if="editingRule.conditions.length > 1">
                 <span>Logic:</span>
                 <el-radio-group v-model="editingRule.logic" size="small">
                   <el-radio-button label="AND" />
                   <el-radio-button label="OR" />
                 </el-radio-group>
              </div>
           </div>
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
import { Plus, Delete, Warning, InfoFilled, Bell } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

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

const addRule = () => {
  editingRule.value = {
    name: '',
    level: 'INFO',
    trigger: 'ON_SAVE',
    logic: 'AND',
    conditions: [{ field: '', operator: '', value: '' }],
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

const saveRule = () => {
  if (editingRule.value.id) {
    const idx = rules.value.findIndex(r => r.id === editingRule.value.id)
    rules.value[idx] = { ...editingRule.value }
  } else {
    editingRule.value.id = Date.now()
    rules.value.push({ ...editingRule.value })
  }
  dialogVisible.value = false
  ElMessage.success('Rule saved successfully')
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
