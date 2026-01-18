<template>
  <el-dialog
    v-model="dialogVisible"
    title="Select Rules for Evaluation"
    width="50%"
    :before-close="handleClose"
  >
    <div class="filter-bar mb-4">
      <el-input v-model="searchQuery" placeholder="Search rules..." style="width: 300px">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
    </div>

    <el-table
      v-loading="loading"
      ref="multipleTableRef"
      :data="filteredRules"
      style="width: 100%"
      height="300"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="ruleName" label="Rule Name" />
      <el-table-column prop="ruleType" label="Type" width="120" />
      <el-table-column prop="ruleLevel" label="Level" width="100" />
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
import evaluationApi from '@/services/evaluationApi'; // Assuming getRules is available here

const props = defineProps({
  modelValue: Boolean
});

const emit = defineEmits(['update:modelValue', 'confirm']);

const dialogVisible = ref(props.modelValue);
const loading = ref(false);
const rules = ref([]);
const multipleSelection = ref([]);
const searchQuery = ref('');

const filteredRules = computed(() => {
  if (!searchQuery.value) return rules.value;
  const lowerQuery = searchQuery.value.toLowerCase();
  return rules.value.filter(r =>
    (r.ruleName && r.ruleName.toLowerCase().includes(lowerQuery)) ||
    (r.ruleType && r.ruleType.toLowerCase().includes(lowerQuery))
  );
});

watch(() => props.modelValue, (newVal) => {
  dialogVisible.value = newVal;
  if (newVal) {
    fetchRules();
  }
});

watch(dialogVisible, (newVal) => {
  emit('update:modelValue', newVal);
});

const fetchRules = async () => {
  loading.value = true;
  try {
    const response = await evaluationApi.getRules(); 
    const rawData = response.data;
    // Robust data extraction: direct array, or nested in .data (ApiResponse) or .content (Page)
    rules.value = Array.isArray(rawData) ? rawData : (rawData.data || rawData.content || []);
  } catch (error) {
    console.error('Failed to fetch rules', error);
    ElMessage.error('Failed to load rules');
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
    ElMessage.warning('Please select at least one rule.');
    return;
  }
  emit('confirm', multipleSelection.value.map(rule => rule.ruleId));
  handleClose(); // Close modal after confirming
};

onMounted(() => {
  // If modal is initially visible, fetch rules
  if (props.modelValue) {
    fetchRules();
  }
});
</script>

<style scoped>
.mb-4 { margin-bottom: 16px; }
</style>