<template>
  <div class="integration-webhooks p-6">
    <div class="glass-card p-6">
      <div class="flex justify-between items-center mb-6">
        <div>
          <h1 class="text-2xl font-bold">{{ $t('menu.webhooks') }}</h1>
          <p class="text-gray-400">Configure real-time push endpoints for verified contract data</p>
        </div>
        <el-button type="primary" icon="Plus" @click="handleAdd">{{ $t('common.create') }}</el-button>
      </div>

      <el-table :data="webhooks" border style="width: 100%">
        <el-table-column prop="name" :label="$t('common.name')" width="180" />
        <el-table-column prop="url" :label="$t('common.targetUrl')" />
        <el-table-column prop="events" :label="$t('common.events')" width="200">
          <template #default="{ row }">
            <el-tag v-for="evt in row.events" :key="evt" size="small" class="mr-1">{{ evt }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.status')" width="120">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'info'">{{ row.enabled ? 'Active' : 'Disabled' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.actions')" width="180">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">{{ $t('common.edit') }}</el-button>
            <el-button link type="success" @click="testWebHook(row)">Test</el-button>
            <el-button link type="danger" @click="handleDelete(row)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="$t('menu.webhooks')" width="600px">
      <el-form :model="form" label-position="top">
        <el-form-item :label="$t('common.name')">
          <el-input v-model="form.name" placeholder="e.g. ERP Finance Sync" />
        </el-form-item>
        <el-form-item :label="$t('common.targetUrl')">
          <el-input v-model="form.url" placeholder="https://api.yourcompany.com/webhooks/contracts" />
        </el-form-item>
        <el-form-item :label="$t('common.events')">
          <el-checkbox-group v-model="form.events">
            <el-checkbox value="CONTRACT_VERIFIED">Verification Completed</el-checkbox>
            <el-checkbox value="CONTRACT_PUBLISHED">Contract Published</el-checkbox>
            <el-checkbox value="AI_EXTRACTION_FAILED">Extraction Failure</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item :label="$t('common.authType')">
          <el-radio-group v-model="form.authType">
            <el-radio value="HMAC">HMAC Signature</el-radio>
            <el-radio value="BEARER">Bearer Token</el-radio>
            <el-radio value="NONE">None</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('common.enabled')">
          <el-switch v-model="form.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSave">{{ $t('common.save') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const webhooks = ref([
  { id: 1, name: 'Main ERP Sync', url: 'https://erp.internal/api/contracts', events: ['CONTRACT_PUBLISHED'], enabled: true, authType: 'HMAC' }
])

const dialogVisible = ref(false)
const form = ref({
  name: '',
  url: '',
  events: ['CONTRACT_PUBLISHED'],
  authType: 'HMAC',
  enabled: true
})

const handleAdd = () => {
  form.value = { name: '', url: '', events: ['CONTRACT_PUBLISHED'], authType: 'HMAC', enabled: true }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSave = () => {
  if (form.value.id) {
    const idx = webhooks.value.findIndex(w => w.id === form.value.id)
    webhooks.value[idx] = { ...form.value }
  } else {
    webhooks.value.push({ ...form.value, id: Date.now() })
  }
  ElMessage.success('WebHook configuration saved')
  dialogVisible.value = false
}

const testWebHook = (row) => {
  ElMessage.info(`Sending test ping to ${row.url}...`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm('This will stop all data sync to this endpoint. Continue?', 'Warning').then(() => {
    ElMessage.success('WebHook deleted')
  })
}
</script>
