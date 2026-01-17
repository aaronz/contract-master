<template>
  <div class="dynamic-fields">
    <div v-for="field in fields" :key="field.id" class="field-item">
      <div class="field-header">
        <label :style="getFieldStyle(field.fieldCode)">{{ field.fieldName }}</label>
        <el-tag 
          v-if="data[field.fieldCode + '_source']" 
          :type="data[field.fieldCode + '_source'] === 'AI' ? 'warning' : 'info'"
          size="small"
          effect="plain"
        >
          {{ data[field.fieldCode + '_source'] }}
        </el-tag>
      </div>
      <el-input v-model="data[field.fieldCode]" v-if="field.isVisible" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useFieldStore } from '@/stores/fieldStore'

const fieldStore = useFieldStore()
const props = defineProps(['contractData'])
const data = ref(props.contractData || {})

const fields = computed(() => fieldStore.fields.filter(f => f.isVisible !== false))

const getFieldStyle = (fieldCode) => {
  const field = fieldStore.fields.find(f => f.fieldCode === fieldCode)
  if (!field) return {}
  return {
    color: field.fieldColor,
    fontWeight: field.fieldStyles?.includes('bold') ? 'bold' : 'normal',
    fontStyle: field.fieldStyles?.includes('italic') ? 'italic' : 'normal',
  }
}

onMounted(async () => {
  if (fieldStore.fields.length === 0) {
    await fieldStore.fetchFieldConfigs()
  }
})
</script>

<style scoped>
.field-item { margin-bottom: 12px; }
.field-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
label { display: block; font-size: 12px; color: #666; }
</style>
