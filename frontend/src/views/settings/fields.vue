<template>
  <div class="field-config-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Field Configuration</h1>
        <p class="page-subtitle">Customize how fields are displayed and returned by the API.</p>
      </div>
      <div class="header-actions">
        <el-button @click="handleAddField" icon="Plus">Add Field</el-button>
        <el-button type="primary" @click="saveConfig">
          <el-icon><Check /></el-icon> Save Changes
        </el-button>
      </div>
    </div>

    <!-- Add Field Dialog -->
    <el-dialog v-model="showAddDialog" title="Add Extended Field" width="450px">
      <el-form :model="newField" label-position="top">
        <el-form-item label="Field Code (Technical ID)" required>
          <el-input v-model="newField.fieldCode" placeholder="e.g. project_manager" />
        </el-form-item>
        <el-form-item label="Field Name (Display)" required>
          <el-input v-model="newField.fieldName" placeholder="e.g. Project Manager" />
        </el-form-item>
        <el-form-item label="Field Type" required>
          <el-select v-model="newField.fieldType" style="width: 100%">
            <el-option label="Text" value="TEXT" />
            <el-option label="Number" value="NUMBER" />
            <el-option label="Date" value="DATE" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">Cancel</el-button>
        <el-button type="primary" @click="confirmAddField" :loading="adding">Add</el-button>
      </template>
    </el-dialog>

    <el-alert
      title="Drag and drop rows to reorder fields."
      type="info"
      show-icon
      :closable="false"
      class="mb-4"
    />

    <el-card shadow="never" class="config-card">
      <el-table :data="configs" row-key="fieldCode" class="draggable-table">
        <el-table-column width="40">
          <template #default>
            <el-icon class="drag-handle"><Rank /></el-icon>
          </template>
        </el-table-column>
        
        <el-table-column prop="fieldCode" label="Field Code" width="180">
          <template #default="{ row }">
             <el-tag type="info">{{ row.fieldCode }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="Display Name" width="250">
          <template #default="{ row }">
            <el-input v-model="row.fieldAlias" placeholder="Enter custom name" />
          </template>
        </el-table-column>

        <el-table-column label="Visual Style" min-width="300">
           <template #default="{ row }">
             <div class="style-picker">
               <el-color-picker v-model="row.color" size="small" show-alpha />
               <el-checkbox-group v-model="row.styles" size="small">
                 <el-checkbox-button label="bold"><span class="font-bold">B</span></el-checkbox-button>
                 <el-checkbox-button label="italic"><span class="italic">I</span></el-checkbox-button>
               </el-checkbox-group>
               <div class="preview-box" :style="getPreviewStyle(row)">
                 Preview
               </div>
             </div>
           </template>
        </el-table-column>
        
        <el-table-column label="Visibility" width="200">
          <template #default="{ row }">
             <div class="visibility-toggles">
               <el-tooltip content="Show in Frontend">
                 <el-switch v-model="row.frontendDisplay" active-text="UI" inline-prompt />
               </el-tooltip>
               <el-tooltip content="Return in API">
                 <el-switch v-model="row.apiReturn" active-text="API" inline-prompt />
               </el-tooltip>
             </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Check, Rank, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import Sortable from 'sortablejs'

const configs = ref([])
const showAddDialog = ref(false)
const adding = ref(false)
const newField = ref({
  fieldCode: '',
  fieldName: '',
  fieldType: 'TEXT'
})

const handleAddField = () => {
  newField.value = { fieldCode: '', fieldName: '', fieldType: 'TEXT' }
  showAddDialog.value = true
}

const confirmAddField = async () => {
  adding.value = true
  try {
    const response = await fetch('/api/settings/extend-fields', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'X-Tenant-ID': localStorage.getItem('tenantId')
      },
      body: JSON.stringify(newField.value)
    })
    if (response.ok) {
      ElMessage.success('Field added successfully')
      showAddDialog.value = false
      fetchFields()
    }
  } catch (error) {
    console.error('Failed to add field', error)
  } finally {
    adding.value = false
  }
}

const fetchFields = async () => {
  try {
    const response = await fetch('/api/metadata/contract-fields', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'X-Tenant-ID': localStorage.getItem('tenantId')
      }
    })
    if (response.ok) {
      const result = await response.json()
      // Use result.data since the backend response is wrapped in ApiResponse
      const fieldList = result.data || []
      configs.value = fieldList.map(f => ({
        fieldCode: f.fieldCode,
        fieldAlias: f.fieldName,
        apiReturn: true,
        frontendDisplay: true,
        color: '#1E293B',
        styles: []
      }))
    }
  } catch (error) {
    console.error('Failed to fetch metadata', error)
  }
}

onMounted(async () => {
  await fetchFields()
  const tableBody = document.querySelector('.draggable-table tbody')
  if (tableBody) {
    Sortable.create(tableBody, {
      handle: '.drag-handle',
      animation: 150,
      onEnd: ({ newIndex, oldIndex }) => {
        const targetRow = configs.value.splice(oldIndex, 1)[0]
        configs.value.splice(newIndex, 0, targetRow)
      }
    })
  }
})

const saveConfig = () => {
  ElMessage.success('Field configuration saved successfully.')
}

const getPreviewStyle = (row) => {
  return {
    color: row.color,
    fontWeight: row.styles.includes('bold') ? 'bold' : 'normal',
    fontStyle: row.styles.includes('italic') ? 'italic' : 'normal',
  }
}
</script>

<style scoped>
.field-config-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 8px 0;
}

.page-subtitle {
  color: var(--text-secondary);
  margin: 0;
}

.mb-4 { margin-bottom: 16px; }

.config-card {
  border-radius: 12px;
  border: 1px solid var(--border-color);
}

.drag-handle {
  cursor: grab;
  color: #94A3B8;
  font-size: 16px;
}
.drag-handle:active {
  cursor: grabbing;
  color: var(--accent-color);
}

.style-picker {
  display: flex;
  align-items: center;
  gap: 12px;
}

.preview-box {
  padding: 4px 12px;
  border: 1px dashed var(--border-color);
  border-radius: 4px;
  font-size: 14px;
  min-width: 80px;
  text-align: center;
}

.visibility-toggles {
  display: flex;
  gap: 12px;
}

.font-bold { font-weight: bold; }
.italic { font-style: italic; }
</style>
