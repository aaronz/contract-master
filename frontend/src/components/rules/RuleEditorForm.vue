<template>
  <div class="rule-editor-form">
    <div class="builder-layout">
      <!-- Left Panel: Rule Settings -->
      <div class="settings-panel">
        <h3 class="panel-title">Rule Settings</h3>
        <el-form label-position="top">
          <el-form-item label="Rule Name">
            <el-input v-model="localConfig.name" placeholder="e.g. High Value Contract Check" />
          </el-form-item>

          <el-form-item label="Rule Type">
            <el-select v-model="localConfig.ruleType" class="w-full">
              <el-option label="Visual Builder (Logic)" value="LOGIC" />
              <el-option label="Groovy Script" value="GROOVY" />
              <el-option label="Regular Expression" value="REGEX" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="Severity Level">
            <el-select v-model="localConfig.level" class="w-full">
              <el-option label="Info / Hint" value="info">
                <span class="text-blue-500">Info / Hint</span>
              </el-option>
              <el-option label="Warning" value="warning">
                <span class="text-orange-500">Warning</span>
              </el-option>
              <el-option label="Critical Error" value="error">
                <span class="text-red-500">Critical Error</span>
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="Trigger Event">
             <el-select v-model="localConfig.trigger" class="w-full">
              <el-option label="On Create" value="create" />
              <el-option label="On Update" value="update" />
              <el-option label="Before Approval" value="pre_approval" />
            </el-select>
          </el-form-item>

          <el-divider />

          <h3 class="panel-title">Actions</h3>
          <el-form-item label="Notify Channels">
            <el-checkbox-group v-model="localConfig.channels">
              <el-checkbox value="email">Email</el-checkbox>
              <el-checkbox value="sms">SMS</el-checkbox>
              <el-checkbox value="system">System</el-checkbox>
              <el-checkbox value="webhook">Webhook</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
      </div>

      <!-- Right Panel: Logic Builder -->
      <div class="logic-panel">
        <h3 class="panel-title flex justify-between">
          <span>Logic Editor</span>
          <el-tag size="small" type="info">{{ localConfig.ruleType === 'LOGIC' ? 'SpEL Preview' : 'Script Content' }}</el-tag>
        </h3>
        
        <!-- Visual Builder -->
        <div v-if="localConfig.ruleType === 'LOGIC'" class="canvas-wrapper">
          <condition-group 
            v-model="localConfig.conditions" 
            :is-root="true"
          />
        </div>

        <!-- Script/Regex Editor -->
        <div v-else class="script-editor-wrapper">
          <el-input
            v-model="localConfig.logicContent"
            type="textarea"
            :rows="12"
            placeholder="Enter Groovy script or Regex pattern..."
            class="font-mono"
          />
        </div>

        <div v-if="localConfig.ruleType === 'LOGIC'" class="preview-box">
          <div class="code-label">Expression Preview:</div>
          <div class="code-content">{{ generateSpEL(localConfig.conditions) }}</div>
        </div>
      </div>
    </div>
    
    <div class="form-actions">
      <el-button @click="$emit('cancel')">Cancel</el-button>
      <el-button type="primary" :loading="saving" @click="handleSave">Save Rule</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch, defineProps, defineEmits, ref } from 'vue'
import ConditionGroup from '@/components/rules/ConditionGroup.vue'

const props = defineProps({
  modelValue: {
    type: Object,
    required: true
  },
  saving: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'save', 'cancel'])

const localConfig = reactive({ ...props.modelValue })

watch(() => props.modelValue, (newVal) => {
  Object.assign(localConfig, newVal)
}, { deep: true })

watch(localConfig, (newVal) => {
  emit('update:modelValue', newVal)
}, { deep: true })

const handleSave = () => {
  emit('save')
}

const generateSpEL = (node) => {
  if (!node) return ''
  if (node.type === 'rule') {
    const opMap = {
      'eq': '==',
      'neq': '!=',
      'gt': '>',
      'lt': '<',
      'contains': '.contains',
      'empty': '.isEmpty()'
    }
    
    if (node.operator === 'contains') {
      return `#${node.field || '?'}.contains('${node.value}')`
    }
    if (node.operator === 'empty') {
      return `#${node.field || '?'}.isEmpty()`
    }
    
    return `#${node.field || '?'} ${opMap[node.operator] || '=='} '${node.value}'`
  }
  
  if (node.type === 'group' && node.children) {
    const childrenExpr = node.children.map(generateSpEL).join(` ${node.operator} `)
    return `(${childrenExpr})`
  }
  return ''
}
</script>

<style scoped>
.rule-editor-form {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.builder-layout {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 24px;
  align-items: start;
  flex: 1;
  overflow: hidden;
}

.settings-panel, .logic-panel {
  background: #fff;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 20px;
  height: 100%;
  overflow-y: auto;
}

.panel-title {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin: 0 0 20px 0;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(226, 232, 240, 0.6);
}

.canvas-wrapper {
  padding: 10px;
  min-height: 300px;
  background: #F8FAFC;
  border-radius: 8px;
}

.script-editor-wrapper {
  min-height: 300px;
}

.font-mono {
  font-family: 'Fira Code', 'Roboto Mono', monospace;
}

.preview-box {
  margin-top: 24px;
  background: #0F172A;
  border-radius: 8px;
  padding: 16px;
  color: #E2E8F0;
  font-family: 'Fira Code', monospace;
  font-size: 12px;
}

.code-label {
  color: #64748B;
  margin-bottom: 8px;
  text-transform: uppercase;
  font-size: 10px;
  letter-spacing: 1px;
}

.code-content {
  line-height: 1.5;
  word-break: break-all;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
  margin-top: 20px;
}

.text-blue-500 { color: #3B82F6; }
.text-orange-500 { color: #F59E0B; }
.text-red-500 { color: #EF4444; }
.w-full { width: 100%; }
</style>
