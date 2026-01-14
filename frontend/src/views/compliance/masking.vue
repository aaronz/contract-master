<template>
  <div class="masking-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Data Masking & Privacy</h1>
        <p class="page-subtitle">Configure data obfuscation rules for sensitive information.</p>
      </div>
      <el-button type="primary" icon="Plus" @click="addRule">Add Masking Rule</el-button>
    </div>

    <div class="glass-card table-wrapper">
      <el-table :data="maskingRules" style="width: 100%">
        <el-table-column prop="field" label="Sensitive Field" width="200">
          <template #default="{ row }">
            <div class="flex items-center gap-2">
              <el-icon class="text-secondary"><Lock /></el-icon>
              <span class="font-medium">{{ row.field }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="strategy" label="Masking Strategy" width="220">
          <template #default="{ row }">
            <el-select v-model="row.strategy" size="small" @change="row.pattern = getPattern(row.strategy)">
              <el-option label="Partial Mask (Email)" value="email" />
              <el-option label="Last 4 Digits" value="last4" />
              <el-option label="First 2 Chars" value="first2" />
              <el-option label="Full Redaction" value="full" />
              <el-option label="Custom Regex" value="custom" />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column prop="pattern" label="Pattern / Regex" width="250">
          <template #default="{ row }">
            <el-input 
              v-model="row.pattern" 
              size="small" 
              :disabled="row.strategy !== 'custom'"
              font-family="monospace"
            />
          </template>
        </el-table-column>

        <el-table-column label="Live Preview" width="200">
          <template #default="{ row }">
            <div class="preview-box">
              <span class="original">{{ row.sample }}</span>
              <el-icon class="arrow"><Right /></el-icon>
              <span class="masked">{{ applyMask(row.sample, row.strategy, row.pattern) }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="Exempt Roles">
           <template #default="{ row }">
             <el-select v-model="row.exemptions" multiple collapse-tags size="small" placeholder="None">
               <el-option label="System Admin" value="admin" />
               <el-option label="Legal Manager" value="legal" />
               <el-option label="Compliance Officer" value="compliance" />
             </el-select>
           </template>
        </el-table-column>
        
        <el-table-column width="80" align="center">
          <template #default="{ row, $index }">
            <el-button type="danger" link icon="Delete" @click="maskingRules.splice($index, 1)" />
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Lock, Right, Delete, Plus } from '@element-plus/icons-vue'

const maskingRules = ref([
  { field: 'Tax ID', strategy: 'last4', pattern: '******$1', sample: '91310000X8761234', exemptions: ['admin', 'finance'] },
  { field: 'Contact Phone', strategy: 'last4', pattern: '*******$1', sample: '13812345678', exemptions: ['sales'] },
  { field: 'Email Address', strategy: 'email', pattern: 's****@$2', sample: 'john.doe@example.com', exemptions: ['admin'] },
  { field: 'Bank Account', strategy: 'full', pattern: '****************', sample: '6222023000123456', exemptions: ['finance'] },
])

const getPattern = (strategy) => {
  const map = {
    'email': '(\\w{1}).*(@.*)',
    'last4': '\\w+(\\w{4})',
    'first2': '(\\w{2})\\w+',
    'full': 'ALL',
    'custom': ''
  }
  return map[strategy] || ''
}

const applyMask = (value, strategy, pattern) => {
  if (!value) return ''
  if (strategy === 'full') return '••••••••••••'
  
  if (strategy === 'email') {
    const parts = value.split('@')
    if (parts.length < 2) return value
    return parts[0].substring(0, 1) + '****@' + parts[1]
  }
  
  if (strategy === 'last4') {
    return '******' + value.slice(-4)
  }
  
  if (strategy === 'first2') {
    return value.slice(0, 2) + '******'
  }
  
  if (strategy === 'custom') {
     // Mock regex replacement for demo
     try {
       return value.replace(new RegExp('(\\d{3})\\d+(\\d{4})'), '$1****$2')
     } catch (e) {
       return 'Invalid Regex'
     }
  }
  
  return value
}

const addRule = () => {
  maskingRules.value.push({
    field: 'New Field',
    strategy: 'full',
    pattern: 'ALL',
    sample: 'Sample Data',
    exemptions: []
  })
}
</script>

<style scoped>
.masking-page {
  display: flex;
  flex-direction: column;
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

.table-wrapper {
  padding: 0;
  overflow: hidden;
}

.preview-box {
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: 'Fira Code', monospace;
  font-size: 12px;
  background: #F8FAFC;
  padding: 4px 8px;
  border-radius: 4px;
}

.original { color: #94A3B8; text-decoration: line-through; }
.arrow { color: #CBD5E1; font-size: 10px; }
.masked { color: #1E293B; font-weight: 600; }

:deep(.el-table) {
  --el-table-header-bg-color: rgba(248, 250, 252, 0.5);
}
</style>
