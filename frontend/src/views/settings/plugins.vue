<template>
  <div class="plugin-mgmt">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Plugin & Extension Registry</span>
          <el-button type="primary">Register Plugin</el-button>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="8" v-for="p in plugins" :key="p.id">
          <el-card shadow="hover" class="plugin-card">
            <template #header>
              <div class="plugin-header">
                <b>{{ p.pluginName }}</b>
                <el-tag :type="p.status === 1 ? 'success' : 'info'">{{ p.status === 1 ? 'Active' : 'Inactive' }}</el-tag>
              </div>
            </template>
            <p class="type-text">Type: {{ p.pluginType }}</p>
            <div class="actions">
              <el-switch v-model="p.status" :active-value="1" :inactive-value="0" @change="togglePlugin(p)" />
              <el-button link type="primary">Config</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const plugins = ref([
  { id: 'p1', pluginName: 'Salesforce Adapter', pluginType: 'CRM_ADAPTER', status: 1 },
  { id: 'p2', pluginName: 'Kingdee Finance Sync', pluginType: 'FINANCE_SYNC', status: 0 },
  { id: 'p3', pluginName: 'Contract AI Parser', pluginType: 'LEGAL_AI', status: 1 }
])

const togglePlugin = (p) => {
  ElMessage.success(`${p.pluginName} ${p.status === 1 ? 'enabled' : 'disabled'}.`)
}
</script>

<style scoped>
.plugin-card { margin-bottom: 20px; }
.plugin-header { display: flex; justify-content: space-between; align-items: center; }
.type-text { font-size: 12px; color: #909399; }
.actions { margin-top: 15px; display: flex; justify-content: space-between; align-items: center; }
</style>
