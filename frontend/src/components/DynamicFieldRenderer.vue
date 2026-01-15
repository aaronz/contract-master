<template>
  <div class="dynamic-fields">
    <div v-for="field in fields" :key="field.id" class="field-item">
      <div class="field-header">
        <label>{{ field.fieldAlias || field.fieldCode }}</label>
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
import { ref, onMounted } from 'vue'

const props = defineProps(['contractData'])
const fields = ref([])
const data = ref(props.contractData || {})

onMounted(async () => {
  // Mock API call to get field configs
  fields.value = [
    { id: 1, fieldCode: 'custom_field_1', fieldAlias: 'Project Code', isVisible: true },
    { id: 2, fieldCode: 'custom_field_2', fieldAlias: 'Internal Priority', isVisible: true }
  ]
})
</script>

<style scoped>
.field-item { margin-bottom: 12px; }
.field-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
label { display: block; font-size: 12px; color: #666; }
</style>
