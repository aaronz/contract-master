<template>
  <div class="integration-secrets p-6">
    <div class="glass-card p-6">
      <div class="flex justify-between items-center mb-6">
        <div>
          <h1 class="text-2xl font-bold">{{ $t('menu.secretsKeys') }}</h1>
          <p class="text-gray-400">Manage API keys and HMAC secrets for secure system integration</p>
        </div>
        <el-button type="primary" icon="Key" @click="handleGenerate">{{ $t('common.create') }}</el-button>
      </div>

      <el-alert
        title="Security Warning"
        type="warning"
        description="Treat your secret keys like passwords. Never share them or commit them to source control."
        show-icon
        class="mb-6"
      />

      <el-table :data="keys" border style="width: 100%">
        <el-table-column prop="label" :label="$t('common.label')" width="180" />
        <el-table-column prop="accessKey" :label="$t('common.accessKeyId')" />
        <el-table-column :label="$t('common.secretKey')" width="300">
          <template #default="{ row }">
            <div class="flex items-center">
              <span class="font-mono">{{ row.hidden ? '••••••••••••••••' : row.secretKey }}</span>
              <el-button 
                link 
                type="primary" 
                class="ml-2" 
                @click="row.hidden = !row.hidden"
              >
                {{ row.hidden ? $t('common.view') : $t('common.hide') || 'Hide' }}
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="lastUsed" :label="$t('common.lastUsed')" width="180" />
        <el-table-column :label="$t('common.actions')" width="120">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleRevoke(row)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="$t('menu.secretsKeys')" width="500px">
      <div v-if="!newKeyGenerated">
        <el-form label-position="top">
          <el-form-item :label="$t('common.label')">
            <el-input v-model="keyLabel" placeholder="e.g. ERP-Production-Sync" />
          </el-form-item>
        </el-form>
      </div>
      <div v-else class="text-center py-4">
        <el-result icon="success" title="Key Generated">
          <template #sub-title>
            Please copy your secret key now. You will not be able to see it again.
          </template>
        </el-result>
        <div class="bg-gray-900 p-4 rounded text-left mt-4">
          <p class="text-xs text-gray-500 uppercase font-bold mb-1">Access Key ID</p>
          <p class="text-white font-mono mb-4">AK_7F2D9K3L1M</p>
          <p class="text-xs text-gray-500 uppercase font-bold mb-1">Secret Key</p>
          <p class="text-yellow-500 font-mono break-all">sk_live_51P2dfG9Kl0mN3vXz8QpRrStUvWxYz1234567890</p>
        </div>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="confirmGenerate" v-if="!newKeyGenerated">Generate</el-button>
        <el-button type="success" @click="dialogVisible = false" v-else>Done</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Key } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const keys = ref([
  { id: 1, label: 'Default ERP Key', accessKey: 'AK_9A2B3C4D5E', secretKey: 'sk_test_123456789', lastUsed: '2026-01-14 10:30', hidden: true }
])

const dialogVisible = ref(false)
const newKeyGenerated = ref(false)
const keyLabel = ref('')

const handleGenerate = () => {
  keyLabel.value = ''
  newKeyGenerated.value = false
  dialogVisible.value = true
}

const confirmGenerate = () => {
  const newKey = {
    id: Date.now(),
    label: keyLabel.value || 'New Key',
    accessKey: 'AK_' + Math.random().toString(36).substring(2, 12).toUpperCase(),
    secretKey: 'sk_live_' + Math.random().toString(36).substring(2, 24),
    lastUsed: 'Never',
    hidden: true
  }
  keys.value.push(newKey)
  newKeyGenerated.value = true
}

const handleRevoke = (row) => {
  ElMessageBox.confirm('Are you sure you want to revoke this key? All integrations using it will fail immediately.', 'Security Alert').then(() => {
    ElMessage.success('Key revoked')
  })
}
</script>
