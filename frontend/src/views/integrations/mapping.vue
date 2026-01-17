<template>
  <div class="integration-mapping p-6">
    <div class="glass-card p-6">
      <div class="flex justify-between items-center mb-6">
        <div>
          <h1 class="text-2xl font-bold">Field Mapping</h1>
          <p class="text-gray-400">Map internal contract elements to downstream system fields</p>
        </div>
        <el-button type="primary" icon="Plus" @click="handleAdd">Add Mapping</el-button>
      </div>

      <el-table :data="mappings" border style="width: 100%">
        <el-table-column prop="internalField" label="Internal Field" />
        <el-table-column prop="externalField" label="Downstream Field" />
        <el-table-column prop="transformation" label="Transformation" />
        <el-table-column label="Status" width="120">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'info'">{{ row.enabled ? 'Active' : 'Disabled' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">Edit</el-button>
            <el-button link type="danger" @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" title="Field Mapping Configuration" width="500px">
      <el-form :model="form" label-position="top">
        <el-form-item label="Internal Field (Contract Element)">
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
        <el-form-item label="Downstream Field Name">
          <el-input v-model="form.externalField" placeholder="e.g. amount_total" />
        </el-form-item>
        <el-form-item label="Transformation Logic">
          <el-select v-model="form.transformation" style="width: 100%">
            <el-option label="Direct Copy" value="NONE" />
            <el-option label="To Uppercase" value="UPPERCASE" />
            <el-option label="Format Date (YYYY-MM-DD)" value="DATE_ISO" />
          </el-select>
        </el-form-item>
        <el-form-item label="Enabled">
          <el-switch v-model="form.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSave">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

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
    const response = await fetch('/api/metadata/contract-fields', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'X-Tenant-ID': localStorage.getItem('tenantId')
      }
    })
    if (response.ok) {
      const result = await response.json()
      contractFields.value = result.data
    } else {
      ElMessage.error('Failed to load contract fields')
    }
  } catch (error) {
    console.error('Failed to fetch metadata', error)
    ElMessage.error('Network error loading mapping fields')
  }
}

onMounted(() => {
  fetchMetadata()
})

const handleAdd = () => {
  form.value = { internalField: '', externalField: '', transformation: 'NONE', enabled: true }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSave = () => {
  if (form.value.id) {
    const idx = mappings.value.findIndex(m => m.id === form.value.id)
    mappings.value[idx] = { ...form.value }
  } else {
    mappings.value.push({ ...form.value, id: Date.now() })
  }
  ElMessage.success('Mapping configuration saved')
  dialogVisible.value = false
}

const handleDelete = (row) => {
  ElMessageBox.confirm('Are you sure you want to delete this mapping?', 'Warning').then(() => {
    ElMessage.success('Mapping deleted')
  })
}
</script>
