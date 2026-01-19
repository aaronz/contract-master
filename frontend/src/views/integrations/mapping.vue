<template>
  <div class="integration-mapping p-6">
    <div class="glass-card p-6">
      <div class="flex justify-between items-center mb-6">
        <div>
          <h1 class="text-2xl font-bold">{{ $t('menu.fieldMapping') }}</h1>
          <p class="text-gray-400">Map internal contract elements to downstream system fields</p>
        </div>
        <el-button type="primary" icon="Plus" @click="handleAdd">{{ $t('common.create') }}</el-button>
      </div>

      <el-table :data="mappings" border style="width: 100%">
        <el-table-column prop="internalField" :label="$t('common.internalField')" />
        <el-table-column prop="externalField" :label="$t('common.downstreamField')" />
        <el-table-column prop="transformation" :label="$t('common.transformation')" />
        <el-table-column :label="$t('common.status')" width="120">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'info'">{{ row.enabled ? 'Active' : 'Disabled' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.actions')" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">{{ $t('common.edit') }}</el-button>
            <el-button link type="danger" @click="handleDelete(row)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="$t('menu.fieldMapping')" width="500px">
      <el-form :model="form" label-position="top">
        <el-form-item :label="$t('common.internalField')">
          <el-select v-model="form.internalField" style="width: 100%" placeholder="Select contract field">
            <el-option 
              v-for="field in contractFields" 
              :key="field.fieldCode" 
              :label="field.fieldName" 
              :value="field.fieldCode"
            >
              <span style="float: left">{{ field.fieldName }}</span>
              <span style="float: right; color: var(--el-text-color-secondary); font-size: 13px">
                {{ field.source }}
              </span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('common.downstreamField')">
          <el-input v-model="form.externalField" placeholder="e.g. amount_total" />
        </el-form-item>
        <el-form-item :label="$t('common.transformation')">
          <el-select v-model="form.transformation" style="width: 100%">
            <el-option label="Direct Copy" value="NONE" />
            <el-option label="To Uppercase" value="UPPERCASE" />
            <el-option label="Format Date (YYYY-MM-DD)" value="DATE_ISO" />
          </el-select>
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

const mappings = ref([
  { id: 1, internalField: 'amount', externalField: 'contract_value', transformation: 'NONE', enabled: true },
  { id: 2, internalField: 'effectiveDate', externalField: 'start_date', transformation: 'DATE_ISO', enabled: true }
])

const dialogVisible = ref(false)
const form = ref({
  internalField: '',
  externalField: '',
  transformation: 'NONE',
  enabled: true
})

const contractFields = ref([])

const fetchMetadata = async () => {
  try {
    const response = await request.get('/metadata/contract-fields')
    if (response.data && response.data.status === 200) {
      contractFields.value = response.data.data
    } else {
      ElMessage.error('Failed to load contract fields')
    }
  } catch (error) {
    console.error('Failed to fetch metadata', error)
  }
}

const fetchMappings = async () => {
  try {
    const res = await request.get('/v1/settings/mapping')
    if (res.data && res.data.data) {
      mappings.value = res.data.data
    }
  } catch (error) {
    console.error('Failed to fetch mappings', error)
  }
}

onMounted(() => {
  fetchMetadata()
  fetchMappings()
})

const handleAdd = () => {
  form.value = { internalField: '', externalField: '', transformation: 'NONE', enabled: true }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    const url = '/v1/settings/mapping'
    const res = await request.post(url, form.value)
    
    if (res.data.status === 200 || res.status === 200) {
      ElMessage.success('Mapping configuration saved')
      dialogVisible.value = false
      fetchMappings()
    }
  } catch (error) {
    console.error('Save failed', error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('Are you sure you want to delete this mapping?', 'Warning').then(async () => {
    try {
      await request.delete(`/v1/settings/mapping/${row.id}`)
      ElMessage.success('Mapping deleted')
      fetchMappings()
    } catch (e) {}
  })
}
</script>
