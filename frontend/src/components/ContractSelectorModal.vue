<template>
  <el-dialog
    v-model="dialogVisible"
    title="Select Contracts for Evaluation"
    width="60%"
    :before-close="handleClose"
  >
    <div class="filter-bar mb-4">
      <el-input v-model="searchQuery" placeholder="Search contracts..." style="width: 300px">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
    </div>

    <el-table
      v-loading="loading"
      ref="multipleTableRef"
      :data="filteredContracts"
      style="width: 100%"
      height="400"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="contractNo" label="Contract No" width="150" />
      <el-table-column prop="contractName" label="Contract Name" min-width="200" />
      <el-table-column prop="partyAName" label="Party A" width="150" />
      <el-table-column prop="partyBName" label="Party B" width="150" />
      <el-table-column prop="amount" label="Amount" width="120">
        <template #default="{ row }">
          {{ row.currencyType }} {{ row.amount }}
        </template>
      </el-table-column>
    </el-table>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">Cancel</el-button>
        <el-button type="primary" @click="confirmSelection" :disabled="multipleSelection.length === 0">
          Run Evaluation ({{ multipleSelection.length }})
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { Search } from '@element-plus/icons-vue';
import evaluationApi from '@/services/evaluationApi';

const props = defineProps({
  modelValue: Boolean
});

const emit = defineEmits(['update:modelValue', 'confirm']);

const dialogVisible = ref(props.modelValue);
const loading = ref(false);
const contracts = ref([]);
const multipleSelection = ref([]);
const searchQuery = ref('');

const filteredContracts = computed(() => {
  if (!searchQuery.value) return contracts.value;
  const lowerQuery = searchQuery.value.toLowerCase();
  return contracts.value.filter(c => 
    (c.contractName && c.contractName.toLowerCase().includes(lowerQuery)) ||
    (c.contractNo && c.contractNo.toLowerCase().includes(lowerQuery))
  );
});

watch(() => props.modelValue, (newVal) => {
  dialogVisible.value = newVal;
  if (newVal && contracts.value.length === 0) {
    fetchContracts();
  }
});

watch(dialogVisible, (newVal) => {
  emit('update:modelValue', newVal);
});

const fetchContracts = async () => {
  loading.value = true;
  try {
    const response = await evaluationApi.getContracts();
    contracts.value = response.data.data || [];
  } catch (error) {
    console.error('Failed to fetch contracts', error);
    ElMessage.error('Failed to load contracts');
  } finally {
    loading.value = false;
  }
};

const handleSelectionChange = (val) => {
  multipleSelection.value = val;
};

const handleClose = () => {
  emit('update:modelValue', false);
};

const confirmSelection = () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('Please select at least one contract.');
    return;
  }
  emit('confirm', multipleSelection.value.map(contract => contract.contractId));
};
</script>

<style scoped>
.mb-4 { margin-bottom: 16px; }
</style>
