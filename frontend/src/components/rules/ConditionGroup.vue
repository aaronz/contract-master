<template>
  <div class="condition-group glass-panel-sm">
    <div class="group-header">
      <div class="logic-toggle">
        <span 
          class="toggle-opt" 
          :class="{ active: modelValue.operator === 'AND' }"
          @click="updateOperator('AND')"
        >AND</span>
        <span 
          class="toggle-opt" 
          :class="{ active: modelValue.operator === 'OR' }"
          @click="updateOperator('OR')"
        >OR</span>
      </div>
      <div class="group-actions">
        <el-button size="small" link icon="Plus" @click="addRule">Add Rule</el-button>
        <el-button size="small" link icon="FolderAdd" @click="addGroup">Add Group</el-button>
        <el-button v-if="!isRoot" size="small" link type="danger" icon="Delete" @click="$emit('remove')"></el-button>
      </div>
    </div>

    <div class="group-body">
      <div v-for="(child, index) in modelValue.children" :key="index" class="condition-row">
        <!-- Connecting Line -->
        <div class="connector-line"></div>
        
        <!-- Recursive Group -->
        <condition-group
          v-if="child.type === 'group'"
          v-model="modelValue.children[index]"
          @remove="removeChild(index)"
        />

        <!-- Single Rule -->
        <div v-else class="rule-item glass-input">
          <el-select v-model="child.field" placeholder="Select Field" size="small" style="width: 140px" filterable>
            <el-option 
              v-for="opt in fieldOptions" 
              :key="opt.value" 
              :label="opt.label" 
              :value="opt.value" 
            />
          </el-select>
          
          <el-select v-model="child.operator" placeholder="Op" size="small" style="width: 100px">
            <el-option label="Equals" value="eq" />
            <el-option label="Not Equals" value="neq" />
            <el-option label="Greater Than" value="gt" />
            <el-option label="Less Than" value="lt" />
            <el-option label="Contains" value="contains" />
            <el-option label="Is Empty" value="empty" />
          </el-select>

          <el-input v-model="child.value" placeholder="Value" size="small" style="width: 160px" />
          
          <el-button 
            type="danger" 
            link 
            icon="Close" 
            size="small" 
            class="remove-rule-btn"
            @click="removeChild(index)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, onMounted, computed } from 'vue'
import { Plus, FolderAdd, Delete, Close } from '@element-plus/icons-vue'
import { useFieldStore } from '@/stores/fieldStore'

const props = defineProps({
  modelValue: Object,
  isRoot: Boolean
})

const emit = defineEmits(['update:modelValue', 'remove'])
const fieldStore = useFieldStore()

onMounted(() => {
  if (fieldStore.fields.length === 0) {
    fieldStore.fetchFieldConfigs()
  }
})

const fieldOptions = computed(() => {
  return fieldStore.fields.map(f => ({
    label: f.fieldName,
    value: f.fieldCode
  }))
})

const updateOperator = (op) => {
  const newVal = { ...props.modelValue, operator: op }
  emit('update:modelValue', newVal)
}

const addRule = () => {
  const newVal = { ...props.modelValue }
  newVal.children.push({ type: 'rule', field: '', operator: 'eq', value: '' })
  emit('update:modelValue', newVal)
}

const addGroup = () => {
  const newVal = { ...props.modelValue }
  newVal.children.push({ 
    type: 'group', 
    operator: 'AND', 
    children: [{ type: 'rule', field: '', operator: 'eq', value: '' }] 
  })
  emit('update:modelValue', newVal)
}

const removeChild = (index) => {
  const newVal = { ...props.modelValue }
  newVal.children.splice(index, 1)
  emit('update:modelValue', newVal)
}
</script>

<style scoped>
.condition-group {
  padding: 16px;
  border-radius: 12px;
  position: relative;
  border: 1px solid rgba(226, 232, 240, 0.6);
  background: rgba(255, 255, 255, 0.4);
}

.group-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.logic-toggle {
  background: #F1F5F9;
  border-radius: 6px;
  padding: 2px;
  display: flex;
  font-size: 11px;
  font-weight: 700;
  cursor: pointer;
}

.toggle-opt {
  padding: 4px 12px;
  border-radius: 4px;
  color: #94A3B8;
  transition: all 0.2s;
}

.toggle-opt.active {
  background: #fff;
  color: #3B82F6;
  box-shadow: 0 1px 2px rgba(0,0,0,0.1);
}

.group-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-left: 24px;
  border-left: 2px solid rgba(226, 232, 240, 0.6);
}

.condition-row {
  position: relative;
}

.connector-line {
  position: absolute;
  left: -24px;
  top: 18px;
  width: 24px;
  height: 2px;
  background: rgba(226, 232, 240, 0.6);
}

.rule-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 8px;
  background: #fff;
  border: 1px solid rgba(226, 232, 240, 0.8);
}

.glass-panel-sm {
  /* Inherit from global or define here */
  backdrop-filter: blur(4px);
}
</style>
