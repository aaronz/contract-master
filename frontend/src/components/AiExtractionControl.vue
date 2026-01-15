<template>
  <div class="ai-extraction-control">
    <el-button-group>
      <el-button 
        type="primary" 
        :loading="loading" 
        @click="handleExtract"
        icon="MagicStick"
      >
        {{ loading ? 'Extracting...' : 'Extract Elements' }}
      </el-button>
      <el-button 
        type="success" 
        icon="Check" 
        @click="handleConfirm"
        :disabled="loading"
      >
        Confirm Suggestions
      </el-button>
    </el-button-group>
    
    <el-progress 
      v-if="loading" 
      :percentage="percentage" 
      class="mt-4"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps(['contractId'])
const loading = ref(false)
const percentage = ref(0)

const handleExtract = () => {
  loading.value = true
  percentage.value = 0
  
  const timer = setInterval(() => {
    percentage.value += 10
    if (percentage.value >= 100) {
      clearInterval(timer)
      loading.value = false
      ElMessage.success('AI Extraction completed successfully')
    }
  }, 500)
}

const handleConfirm = () => {
  ElMessage.success('AI suggestions verified and confirmed')
}
</script>

<style scoped>
.mt-4 { margin-top: 16px; }
</style>
