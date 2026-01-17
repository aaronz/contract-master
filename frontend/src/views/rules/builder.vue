<template>
  <div class="rule-builder-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Dynamic Rule Builder</h1>
        <p class="page-subtitle">Create complex business logic without writing code.</p>
      </div>
      <div class="header-actions">
        <el-button icon="Close">Cancel</el-button>
        <el-button type="primary" :loading="saving" icon="Check" @click="saveRule">Save Rule</el-button>
      </div>
    </div>

    <div class="builder-layout">
      <!-- Left Panel: Rule Settings -->
      <div class="settings-panel glass-card">
        <h3 class="panel-title">Rule Settings</h3>
        <el-form label-position="top">
          <el-form-item label="Rule Name">
            <el-input v-model="ruleConfig.name" placeholder="e.g. High Value Contract Check" />
          </el-form-item>
          
          <el-form-item label="Severity Level">
            <el-select v-model="ruleConfig.level" class="w-full">
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
             <el-select v-model="ruleConfig.trigger" class="w-full">
              <el-option label="On Create" value="create" />
              <el-option label="On Update" value="update" />
              <el-option label="Before Approval" value="pre_approval" />
            </el-select>
          </el-form-item>

          <el-divider />

          <h3 class="panel-title">Actions</h3>
          <el-form-item label="Notify Channels">
            <el-checkbox-group v-model="ruleConfig.channels">
              <el-checkbox value="email">Email</el-checkbox>
              <el-checkbox value="sms">SMS</el-checkbox>
              <el-checkbox value="system">System</el-checkbox>
              <el-checkbox value="webhook">Webhook</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
      </div>

      <!-- Right Panel: Logic Builder -->
      <div class="logic-panel glass-card">
        <h3 class="panel-title flex justify-between">
          <span>Logic Editor</span>
          <el-tag size="small" type="info">SpEL Expression Preview</el-tag>
        </h3>
        
        <div class="canvas-wrapper">
          <condition-group 
            v-model="ruleConfig.conditions" 
            :is-root="true"
          />
        </div>

        <div class="preview-box">
          <div class="code-label">Expression Preview:</div>
          <div class="code-content">{{ generateSpEL(ruleConfig.conditions) }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import ConditionGroup from '@/components/rules/ConditionGroup.vue'
import evaluationApi from '@/services/evaluationApi'

const route = useRoute()
const router = useRouter()
const saving = ref(false)
const isEditMode = ref(false)

const ruleConfig = reactive({
  ruleId: null,
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
})

onMounted(async () => {
  if (route.query.id) {
    isEditMode.value = true
    try {
      const response = await evaluationApi.getRule(route.query.id)
      const data = response.data
      if (data) {
        ruleConfig.ruleId = data.ruleId
        ruleConfig.name = data.ruleName
        ruleConfig.level = data.ruleLevel
        ruleConfig.trigger = data.triggerTime
        ruleConfig.ruleType = data.ruleType
        ruleConfig.conditions = data.ruleCondition ? JSON.parse(data.ruleCondition) : { type: 'group', operator: 'AND', children: [] }
        // Map other fields if needed
      }
    } catch (error) {
      ElMessage.error('Failed to load rule')
    }
  }
})

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

const saveRule = async () => {
  saving.value = true
  const payload = {
    ruleName: ruleConfig.name,
    ruleLevel: ruleConfig.level,
    triggerTime: ruleConfig.trigger,
    ruleType: ruleConfig.ruleType,
    isEnabled: true,
    ruleCondition: JSON.stringify(ruleConfig.conditions),
    // Additional fields mapping
  }

  try {
    if (isEditMode.value) {
      await evaluationApi.updateRule(ruleConfig.ruleId, payload)
    } else {
      await evaluationApi.createRule(payload)
    }
    ElMessage.success('Rule saved successfully')
    router.push('/rules/list')
  } catch (error) {
    ElMessage.error('Failed to save rule')
    console.error(error)
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.rule-builder-page {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
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

.builder-layout {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 24px;
  align-items: start;
}

.glass-card {
  padding: 24px;
  border-radius: 16px;
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
  min-height: 400px;
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

.text-blue-500 { color: #3B82F6; }
.text-orange-500 { color: #F59E0B; }
.text-red-500 { color: #EF4444; }
.w-full { width: 100%; }
</style>
