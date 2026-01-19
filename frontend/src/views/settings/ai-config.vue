<template>
  <div class="ai-config-page p-6">
    <div class="page-header mb-6">
      <div>
        <h1 class="page-title">{{ $t('ai.configTitle') }}</h1>
        <p class="page-subtitle">{{ $t('ai.configSubtitle') }}</p>
      </div>
      <el-button type="primary" :loading="saving" icon="Check" @click="handleSave">
        {{ $t('common.save') }}
      </el-button>
    </div>

    <div class="glass-card config-container p-8" v-loading="loading">
      <el-form :model="form" label-position="top">
        <el-row :gutter="40">
          <!-- LLM Connection -->
          <el-col :span="12">
            <h3 class="section-title mb-4">{{ $t('ai.modelConnection') }}</h3>
            <el-form-item :label="$t('ai.provider')">
              <el-select v-model="form.provider" style="width: 100%">
                <el-option label="Mock (Demo Mode)" value="MOCK" />
                <el-option label="SiliconFlow" value="SILICONFLOW" />
                <el-option label="DeepSeek" value="DEEPSEEK" />
                <el-option label="OpenAI" value="OPENAI" />
                <el-option label="Azure OpenAI" value="AZURE" />
              </el-select>
            </el-form-item>
            
            <el-form-item :label="$t('ai.modelName')">
              <el-input v-model="form.modelName" :placeholder="$t('common.placeholder')" />
            </el-form-item>

            <el-form-item :label="$t('ai.apiKey')">
              <el-input v-model="form.apiKey" type="password" show-password :placeholder="$t('common.placeholder')" />
            </el-form-item>

            <el-form-item :label="$t('ai.endpointUrl')">
              <el-input v-model="form.endpointUrl" :placeholder="$t('common.placeholder')" />
            </el-form-item>
          </el-col>

          <!-- Prompt Engineering -->
          <el-col :span="12">
            <h3 class="section-title mb-4">{{ $t('ai.extractionLogic') }}</h3>
            <el-form-item :label="$t('ai.systemPrompt')">
              <el-input 
                v-model="form.extractionPrompt" 
                type="textarea" 
                :rows="12" 
                :placeholder="$t('ai.promptTip')"
              />
              <div class="text-xs text-gray-400 mt-2">
                {{ $t('ai.promptTip') }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const saving = ref(false)
const form = ref({
  provider: 'MOCK',
  modelName: '',
  apiKey: '',
  endpointUrl: '',
  extractionPrompt: ''
})

const fetchSettings = async () => {
  loading.value = true
  try {
    const res = await request.get('/ai/settings')
    if (res.data && res.data.data) {
      form.value = res.data.data
    }
  } catch (error) {
    console.error('Failed to fetch AI settings', error)
  } finally {
    loading.value = false
  }
}

const handleSave = async () => {
  saving.value = true
  try {
    await request.post('/ai/settings', form.value)
    ElMessage.success('AI configuration saved successfully')
  } catch (error) {
    console.error('Failed to save AI settings', error)
    ElMessage.error('Failed to save configuration')
  } finally {
    saving.value = false
  }
}

onMounted(fetchSettings)
</script>

<style scoped>
.page-title { font-size: 24px; font-weight: 700; margin: 0 0 4px 0; }
.page-subtitle { color: #64748B; font-size: 14px; margin: 0; }
.section-title { font-size: 16px; font-weight: 600; color: #1E293B; border-left: 4px solid #3B82F6; padding-left: 12px; }
.config-container { border-radius: 16px; }
</style>
