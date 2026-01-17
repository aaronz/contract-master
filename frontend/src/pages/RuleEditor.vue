<template>
  <div class="rule-editor-page">
    <h1>Rule Editor</h1>
    <p>This is a placeholder for the Rule Editor page.</p>

    <!-- Trigger Evaluation Button -->
    <el-button type="primary" @click="openContractSelector">Trigger Evaluation</el-button>

    <!-- Contract Selector Modal -->
    <ContractSelectorModal
      :visible="isContractSelectorVisible"
      :contracts="availableContracts"
      @update:visible="isContractSelectorVisible = $event"
      @confirm="handleContractSelection"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import ContractSelectorModal from '../components/ContractSelectorModal.vue';
import evaluationApi from '../services/evaluationApi';
import { ElMessage } from 'element-plus';

const isContractSelectorVisible = ref(false);
const availableContracts = ref([]);
const selectedRuleId = ref('rule-123'); // Placeholder for the current rule being edited

onMounted(async () => {
  try {
    const response = await evaluationApi.getContracts();
    availableContracts.value = response.data.map(c => ({ id: c.id, name: c.name }));
  } catch (error) {
    ElMessage.error('Failed to fetch contracts.');
    console.error('Error fetching contracts:', error);
  }
});

const openContractSelector = () => {
  isContractSelectorVisible.value = true;
};

const handleContractSelection = async (contractIds) => {
  try {
    // Assuming a single rule is being edited, so we send a single ruleId
    const response = await evaluationApi.triggerEvaluation([selectedRuleId.value], contractIds);
    ElMessage.success(`Evaluation job ${response.data.id} started.`);
  } catch (error) {
    ElMessage.error('Failed to trigger evaluation.');
    console.error('Error triggering evaluation:', error);
  }
};
</script>

<style scoped>
.rule-editor-page {
  padding: 20px;
}
</style>
