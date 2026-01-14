<template>
  <div class="permissions-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Data Permissions</h1>
        <p class="page-subtitle">Manage data access scopes for different roles and departments.</p>
      </div>
      <el-button type="primary" icon="Plus" @click="addPermission">Add Permission Rule</el-button>
    </div>

    <el-card shadow="never" class="permissions-card">
      <el-table :data="permissions" style="width: 100%">
        <el-table-column prop="ruleName" label="Rule Name" width="200">
           <template #default="{ row }">
             <span class="font-medium">{{ row.ruleName }}</span>
           </template>
        </el-table-column>
        
        <el-table-column prop="dataType" label="Data Type" width="150">
           <template #default="{ row }">
             <el-tag effect="plain">{{ row.dataType }}</el-tag>
           </template>
        </el-table-column>
        
        <el-table-column prop="filterCondition" label="Filter Condition">
           <template #default="{ row }">
             <div class="code-block">{{ row.filterCondition }}</div>
           </template>
        </el-table-column>
        
        <el-table-column label="Status" width="100">
          <template #default="scope">
            <el-switch v-model="scope.row.isEnabled" active-color="#10B981" />
          </template>
        </el-table-column>
        
        <el-table-column label="Actions" width="150">
          <template #default="scope">
            <el-button link type="primary">Edit</el-button>
            <el-button link type="danger">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const permissions = ref([
  { ruleId: '1', ruleName: 'Sales North Dept', dataType: 'CONTRACT', filterCondition: '{"dept_id": ["D001", "D002"]}', isEnabled: true },
  { ruleId: '2', ruleName: 'Legal Global', dataType: 'CONTRACT', filterCondition: '{"dept_id": ["*"]}', isEnabled: true },
  { ruleId: '3', ruleName: 'Finance View Only', dataType: 'INVOICE', filterCondition: '{"amount": "> 0"}', isEnabled: false }
])

const addPermission = () => {
  ElMessage.info('Feature coming soon: Rule Builder UI')
}
</script>

<style scoped>
.permissions-page {
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

.permissions-card {
  border-radius: 12px;
  border: 1px solid var(--border-color);
}

.font-medium {
  font-weight: 500;
  color: var(--text-primary);
}

.code-block {
  font-family: 'JetBrains Mono', monospace;
  font-size: 12px;
  background: #F8FAFC;
  padding: 4px 8px;
  border-radius: 4px;
  border: 1px solid #E2E8F0;
  color: #475569;
}
</style>
