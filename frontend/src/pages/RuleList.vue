<template>
  <div class="rule-list-page">
    <h1>Rule List
      <el-button link type="info" icon="QuestionFilled" @click="showTriggerScenarios" style="margin-left: 10px;"></el-button>
    </h1>
    <p>This is a placeholder for the Rule List page.</p>

    <el-table
      ref="multipleTableRef"
      :data="rules"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="Rule Name" />
      <el-table-column prop="id" label="Rule ID" />
    </el-table>

    <el-button
      v-if="selectedRules.length > 0"
      type="primary"
      @click="openContractSelectorForBatch"
      style="margin-top: 20px;"
    >
      Trigger Batch Evaluation ({{ selectedRules.length }} rules selected)
    </el-button>

    <!-- Contract Selector Modal for Batch Evaluation -->
    <ContractSelectorModal
      :visible="isContractSelectorVisible"
      :contracts="availableContracts"
      @update:visible="isContractSelectorVisible = $event"
      @confirm="handleContractSelectionForBatch"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import ContractSelectorModal from '../components/ContractSelectorModal.vue';
import evaluationApi from '../services/evaluationApi';
import { ElMessage, ElMessageBox } from 'element-plus';
import { QuestionFilled } from '@element-plus/icons-vue';

const rules = ref([
  { id: 'rule-a', name: 'Rule A - High Priority' },
  { id: 'rule-b', name: 'Rule B - Compliance Check' },
  { id: 'rule-c', name: 'Rule C - Fraud Detection' },
  { id: 'rule-d', name: 'Rule D - Discount Eligibility' },
]); // Mock rules

const selectedRules = ref([]);
const isContractSelectorVisible = ref(false);
const availableContracts = ref([]);

onMounted(async () => {
  try {
    const response = await evaluationApi.getContracts();
    availableContracts.value = response.data.map(c => ({ id: c.id, name: c.name }));
  } catch (error) {
    ElMessage.error('Failed to fetch contracts.');
    console.error('Error fetching contracts:', error);
  }
});

const handleSelectionChange = (val) => {
  selectedRules.value = val;
};

const openContractSelectorForBatch = () => {
  if (selectedRules.value.length === 0) {
    ElMessage.warning('Please select rules for batch evaluation.');
    return;
  }
  isContractSelectorVisible.value = true;
};

const handleContractSelectionForBatch = async (contractIds) => {
  try {
    const ruleIds = selectedRules.value.map(rule => rule.id);
    const response = await evaluationApi.triggerEvaluation(ruleIds, contractIds);
    ElMessage.success(`Batch evaluation job ${response.data.job_id} started.`);
    // Optionally clear selection after triggering
    // multipleTableRef.value.clearSelection();
  } catch (error) {
    ElMessage.error('Failed to trigger batch evaluation.');
    console.error('Error triggering batch evaluation:', error);
  }
};

const showTriggerScenarios = async () => {
  try {
    const response = await evaluationApi.getTriggerScenarios();
    const scenariosHtml = response.data.scenarios.map(s => `
      <p><strong>${s.name}:</strong> ${s.description}</p>
    `).join('');

    ElMessageBox.alert(scenariosHtml, 'Automatic Rule Trigger Scenarios', {
      dangerouslyUseHTMLString: true,
      confirmButtonText: 'OK',
    });
  } catch (error) {
    ElMessage.error('Failed to fetch trigger scenarios.');
    console.error('Error fetching trigger scenarios:', error);
  }
};
</script>

<style scoped>
.rule-list-page {
  padding: 20px;
}
</style>
