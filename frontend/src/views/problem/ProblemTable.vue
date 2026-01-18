<template>
  <div class="problem-table-container">
    <div class="table-header">
      <el-form :inline="true" :model="filters" size="small">
        <el-form-item label="Status">
          <el-select v-model="filters.status" placeholder="All" clearable @change="loadProblems" style="width: 120px">
            <el-option label="New" value="NEW" />
            <el-option label="Acknowledged" value="ACKNOWLEDGED" />
            <el-option label="Resolved" value="RESOLVED" />
            <el-option label="Ignored" value="IGNORED" />
          </el-select>
        </el-form-item>
        <el-button icon="Refresh" circle @click="loadProblems" />
      </el-form>
    </div>

    <el-table
      :data="problems"
      :loading="loading"
      size="small"
      stripe
      highlight-current-row
      @row-click="handleRowClick"
      style="width: 100%; flex: 1"
      height="100%"
    >
      <el-table-column prop="severity" label="!" width="50">
        <template #default="{ row }">
          <el-tag :type="getSeverityType(row.severity)" size="small" effect="dark">{{ row.severity?.substring(0,1) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="generatedMessage" label="Issue Description" min-width="150" show-overflow-tooltip />
      <el-table-column prop="status" label="Status" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)" size="small">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Actions" width="60" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" icon="Edit" @click.stop="handleEdit(row)" />
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-mini">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        small
        layout="prev, pager, next"
        :total="pagination.total"
        @current-change="loadProblems"
      />
    </div>

    <el-dialog v-model="editDialogVisible" title="Update Problem" width="400px" append-to-body>
      <el-form :model="editForm" label-width="80px" size="small">
        <el-form-item label="Status">
          <el-select v-model="editForm.status" class="w-full">
            <el-option label="New" value="NEW" />
            <el-option label="Acknowledged" value="ACKNOWLEDGED" />
            <el-option label="Resolved" value="RESOLVED" />
            <el-option label="Ignored" value="IGNORED" />
          </el-select>
        </el-form-item>
        <el-form-item label="Assignee">
          <el-input v-model="editForm.assigneeId" />
        </el-form-item>
        <el-form-item label="Notes">
          <el-input v-model="editForm.notes" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="editDialogVisible = false">Cancel</el-button>
        <el-button size="small" type="primary" :loading="saving" @click="saveProblem">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, defineExpose, defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'
import problemApi from '@/services/problemApi'

const props = defineProps({
  contractId: {
    type: String,
    default: null
  }
})

const emit = defineEmits(['problem-selected'])

const loading = ref(false)
const saving = ref(false)
const problems = ref([])
const editDialogVisible = ref(false)
const editForm = ref({
  id: null,
  status: 'NEW',
  assigneeId: '',
  notes: ''
})

const filters = ref({
  status: null
})

const pagination = ref({
  page: 1,
  size: 20,
  total: 0
})

const getStatusType = (status) => {
  const map = {
    'NEW': 'warning',
    'ACKNOWLEDGED': 'primary',
    'RESOLVED': 'success',
    'IGNORED': 'info'
  }
  return map[status] || ''
}

const getSeverityType = (severity) => {
  const map = {
    'HIGH': 'danger',
    'MEDIUM': 'warning',
    'LOW': 'info'
  }
  return map[severity] || ''
}

const loadProblems = async () => {
  if (!props.contractId) {
    problems.value = []
    return
  }
  loading.value = true
  try {
    const params = {
      contractId: props.contractId,
      page: pagination.value.page - 1,
      size: pagination.value.size
    }
    if (filters.value.status) params.status = filters.value.status

    const response = await problemApi.getProblems(params)
    // Check if response has content property (Spring Data Page) or is direct array
    if (response.data && response.data.content) {
      problems.value = response.data.content
      pagination.value.total = response.data.totalElements
    } else {
      problems.value = response.data
      pagination.value.total = response.data?.length || 0
    }
  } catch (error) {
    ElMessage.error('Failed to load problems')
  } finally {
    loading.value = false
  }
}

const handleRowClick = (row) => {
  emit('problem-selected', row)
}

const handleEdit = (row) => {
  editForm.value = {
    id: row.id,
    status: row.status,
    assigneeId: row.assigneeId || '',
    notes: row.notes || ''
  }
  editDialogVisible.value = true
}

const saveProblem = async () => {
  saving.value = true
  try {
    await problemApi.updateProblem(editForm.value.id, {
      status: editForm.value.status,
      assigneeId: editForm.value.assigneeId,
      notes: editForm.value.notes
    })
    ElMessage.success('Problem updated')
    editDialogVisible.value = false
    loadProblems()
  } catch (error) {
    ElMessage.error('Failed to update problem')
  } finally {
    saving.value = false
  }
}

watch(() => props.contractId, () => {
  loadProblems()
})

onMounted(() => {
  if (props.contractId) loadProblems()
})

defineExpose({
  loadProblems
})
</script>

<style scoped>
.problem-table-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 12px;
}

.table-header {
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
}

.pagination-mini {
  margin-top: 12px;
  display: flex;
  justify-content: center;
}

.w-full {
  width: 100%;
}
</style>
