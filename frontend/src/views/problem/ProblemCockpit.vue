<template>
  <div class="problem-cockpit">
    <div class="cockpit-header glass-panel">
      <div class="header-left">
        <h1 class="cockpit-title">Compliance Cockpit</h1>
        <p class="cockpit-subtitle">Investigate risks in contract: {{ selectedContractNo || 'Select a contract' }}</p>
      </div>
      <div class="header-right">
        <el-select v-model="selectedContractId" placeholder="Select Contract" clearable filterable @change="handleContractChange" style="width: 280px">
          <el-option v-for="contract in contracts" :key="contract.value" :label="contract.label" :value="contract.value" />
        </el-select>
        <el-button type="primary" :disabled="!selectedContractId" :loading="evaluating" @click="triggerEvaluation" icon="VideoPlay">Run Rules</el-button>
      </div>
    </div>

    <div class="cockpit-content">
      <div class="list-pane glass-card full-width">
        <ProblemTable 
          ref="problemTableRef"
          :contract-id="selectedContractId"
          @problem-selected="handleProblemSelected"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import ProblemTable from './ProblemTable.vue'
import contractApi from '@/services/contractApi'
import problemApi from '@/services/problemApi'

const selectedContractId = ref(null)
const selectedContractNo = ref('')
const contracts = ref([])
const evaluating = ref(false)
const problemTableRef = ref(null)

const handleContractChange = async (val) => {
  if (!val) {
    selectedContractNo.value = ''
    return
  }
  const contract = contracts.value.find(c => c.value === val)
  selectedContractNo.value = contract?.label || ''
  
  if (problemTableRef.value) {
    problemTableRef.value.loadProblems()
  }
}

const loadContracts = async () => {
  try {
    const response = await contractApi.getContracts({ size: 1000 });
    const result = response.data?.data || response.data;
    let contractList = [];
    
    if (result) {
      if (Array.isArray(result)) {
        contractList = result;
      } else if (result.content && Array.isArray(result.content)) {
        contractList = result.content;
      }
    }
    
    contracts.value = contractList.map(c => ({
      value: c.contractId,
      label: c.contractNo + ' - ' + c.contractName
    }));
  } catch (error) {
    console.error('Failed to load contracts', error)
    ElMessage.error('Failed to load contracts for selection')
  }
}

const triggerEvaluation = async () => {
  if (!selectedContractId.value) return
  evaluating.value = true
  try {
    await problemApi.triggerEvaluation(selectedContractId.value)
    ElMessage.success('Evaluation started. Polling for results...')
    
    // Robust polling: Check 5 times every 2 seconds
    let attempts = 0
    const maxAttempts = 5
    
    const poll = async () => {
      if (attempts >= maxAttempts) {
        evaluating.value = false
        return
      }
      
      attempts++
      setTimeout(async () => {
        if (problemTableRef.value) {
          await problemTableRef.value.loadProblems()
        }
        // Continue polling
        if (attempts < maxAttempts) {
           poll()
        } else {
           evaluating.value = false
           ElMessage.info('Evaluation polling completed.')
        }
      }, 2000)
    }
    
    poll()
  } catch (error) {
    ElMessage.error('Failed to trigger evaluation')
    evaluating.value = false
  }
}

const handleProblemSelected = (problem) => {
  // Logic for highlighting in PDF removed as PDF view is disabled
}

onMounted(() => {
  loadContracts()
})
</script>

<style scoped>
.problem-cockpit {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 120px);
  padding: 16px;
  gap: 16px;
}

.cockpit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-radius: 12px;
}

.cockpit-title {
  font-size: 20px;
  font-weight: 700;
  margin: 0;
}

.cockpit-subtitle {
  font-size: 13px;
  color: #64748B;
  margin: 4px 0 0 0;
}

.header-right {
  display: flex;
  gap: 12px;
}

.cockpit-content {
  flex: 1;
  display: flex;
  min-height: 0;
}

.list-pane {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border-radius: 12px;
  width: 100%;
}

.glass-panel {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.glass-card {
  background: white;
  border: 1px solid #E2E8F0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}
</style>
