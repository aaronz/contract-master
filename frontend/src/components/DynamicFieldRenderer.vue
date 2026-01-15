<template>
  <div class="dynamic-fields">
    <div v-for="field in fields" :key="field.id" class="field-item">
      <label>{{ field.fieldAlias || field.fieldCode }}</label>
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
label { display: block; font-size: 12px; color: #666; margin-bottom: 4px; }
</style>
