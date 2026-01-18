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
      <div class="pdf-pane glass-card" :style="{ width: splitWidth + '%' }">
        <PdfViewer 
          ref="pdfViewerRef"
          :url="pdfUrl"
          :highlights="currentHighlights"
          :fileName="selectedContractNo"
        />
      </div>
      <div class="resizer" @mousedown="startResizing"></div>
      <div class="list-pane glass-card" :style="{ width: (100 - splitWidth) + '%' }">
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
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import PdfViewer from '@/components/pdf/PdfViewer.vue'
import ProblemTable from './ProblemTable.vue'
import contractApi from '@/services/contractApi'
import problemApi from '@/services/problemApi'

const selectedContractId = ref(null)
const selectedContractNo = ref('')
const contracts = ref([])
const evaluating = ref(false)
const splitWidth = ref(60)
const pdfViewerRef = ref(null)
const problemTableRef = ref(null)
const pdfUrl = ref('')

const currentHighlights = ref([])

const handleContractChange = async (val) => {
  if (!val) {
    pdfUrl.value = ''
    selectedContractNo.value = ''
    currentHighlights.value = []
    return
  }
  const contract = contracts.value.find(c => c.value === val)
  selectedContractNo.value = contract?.label || ''
  
  // In a real app, we'd get the actual PDF URL from the contract detail
  // For demo, we'll use a placeholder or assume a standard path
  pdfUrl.value = `/api/contracts/${val}/pdf`
  
  if (problemTableRef.value) {
    problemTableRef.value.loadProblems()
  }
}

const loadContracts = async () => {
  try {
    const response = await contractApi.getContracts()
    const contractList = Array.isArray(response.data) ? response.data : response.data.content;
    contracts.value = contractList.map(c => ({
      value: c.contractId,
      label: c.contractNo
    }))
  } catch (error) {
    console.error('Failed to load contracts', error)
  }
}

const triggerEvaluation = async () => {
  if (!selectedContractId.value) return
  evaluating.value = true
  try {
    await problemApi.triggerEvaluation(selectedContractId.value)
    ElMessage.success('Evaluation started asynchronously')
    // Poll for results after a short delay
    setTimeout(() => {
      if (problemTableRef.value) problemTableRef.value.loadProblems()
    }, 2000)
  } catch (error) {
    ElMessage.error('Failed to trigger evaluation')
  } finally {
    evaluating.value = false
  }
}

const handleProblemSelected = (problem) => {
  if (!problem.locationInContract) return
  
  try {
    const location = JSON.parse(problem.locationInContract)
    currentHighlights.value = [location]
    
    if (pdfViewerRef.value) {
      pdfViewerRef.value.scrollToPage(location.page)
    }
  } catch (e) {
    console.error('Failed to parse location data', e)
  }
}

const startResizing = (e) => {
  const startX = e.clientX
  const startWidth = splitWidth.value
  
  const handleMouseMove = (moveEvent) => {
    const delta = ((moveEvent.clientX - startX) / window.innerWidth) * 100
    splitWidth.value = Math.max(20, Math.min(80, startWidth + delta))
  }
  
  const handleMouseUp = () => {
    document.removeEventListener('mousemove', handleMouseMove)
    document.removeEventListener('mouseup', handleMouseUp)
  }
  
  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mouseup', handleMouseUp)
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

.pdf-pane, .list-pane {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border-radius: 12px;
}

.resizer {
  width: 8px;
  cursor: col-resize;
  background: transparent;
  transition: background 0.2s;
}

.resizer:hover {
  background: rgba(59, 130, 246, 0.2);
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
