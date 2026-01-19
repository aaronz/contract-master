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
            <el-tag :type="row.enabled ? 'success' : 'info'">{{ row.enabled ? $t('contract.enums.status.active') : $t('common.hide') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.actions')" width="180">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">{{ $t('common.edit') }}</el-button>
            <el-button link type="success" @click="testWebHook(row)">{{ $t('common.view') }}</el-button>
            <el-button link type="danger" @click="handleDelete(row)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="$t('menu.webhooks')" width="600px">
      <el-form :model="form" label-position="top">
        <el-form-item :label="$t('common.name')">
          <el-input v-model="form.name" :placeholder="$t('common.placeholder')" />
        </el-form-item>
        <el-form-item :label="$t('common.targetUrl')">
          <el-input v-model="form.url" placeholder="https://api.yourcompany.com/webhooks/contracts" />
        </el-form-item>
        <el-form-item :label="$t('common.events')">
          <el-checkbox-group v-model="form.events">
            <el-checkbox value="CONTRACT_VERIFIED">{{ $t('contract.enums.status.verified') }}</el-checkbox>
            <el-checkbox value="CONTRACT_PUBLISHED">{{ $t('contract.enums.status.published') }}</el-checkbox>
            <el-checkbox value="AI_EXTRACTION_FAILED">{{ $t('common.error') }}</el-checkbox>
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
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

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

const fetchWebhooks = async () => {
  try {
    const res = await request.get('/webhook/configs')
    if (res.data && res.data.data) {
      webhooks.value = res.data.data.map(w => ({
        ...w,
        events: typeof w.events === 'string' ? w.events.split(',') : (w.events || [])
      }))
    }
  } catch (e) {}
}

onMounted(() => {
  fetchWebhooks()
})

const handleAdd = () => {
  form.value = { name: '', url: '', events: ['CONTRACT_PUBLISHED'], authType: 'HMAC', enabled: true }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.value = { 
    ...row,
    events: Array.isArray(row.events) ? row.events : (row.events ? row.events.split(',') : [])
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    const payload = {
      ...form.value,
      events: Array.isArray(form.value.events) ? form.value.events.join(',') : form.value.events
    }
    const res = await request.post('/webhook/configs', payload)
    if (res.data.status === 200 || res.status === 200) {
      ElMessage.success('WebHook configuration saved')
      dialogVisible.value = false
      fetchWebhooks()
    }
  } catch (error) {
    console.error('Save failed', error)
  }
}

const testWebHook = (row) => {
  ElMessage.info(`Sending test ping to ${row.url}...`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm('This will stop all data sync to this endpoint. Continue?', 'Warning').then(async () => {
    try {
      await request.delete(`/webhook/configs/${row.id}`)
      ElMessage.success('WebHook deleted')
      fetchWebhooks()
    } catch (e) {}
  })
}
</script>
