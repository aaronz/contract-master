<template>
  <div class="problem-center-page">
    <h1>Problem Center</h1>
    <p>Review manual rule evaluation jobs and their results.</p>

    <el-table :data="evaluationJobs" style="width: 100%" @row-click="viewJobDetails">
      <el-table-column prop="id" label="Job ID" width="180" />
      <el-table-column prop="status" label="Status" width="120">
        <template #default="{ row }">
          <el-tag :type="getStatusTagType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="triggerType" label="Trigger Type" width="120" />
      <el-table-column prop="createdAt" label="Created At" width="180">
        <template #default="{ row }">
          {{ new Date(row.createdAt).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column prop="completedAt" label="Completed At" width="180">
        <template #default="{ row }">
          {{ row.completedAt ? new Date(row.completedAt).toLocaleString() : 'N/A' }}
        </template>
      </el-table-column>
      <el-table-column prop="triggeredBy" label="Triggered By" />
      <el-table-column label="Actions" width="100">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="viewJobDetails(scope.row)">View Details</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="prev, pager, next"
      :total="totalJobs"
      :page-size="pageSize"
      @current-change="handlePageChange"
      class="pagination-container"
    />

    <el-dialog
      v-model="isJobDetailsVisible"
      :title="`Details for Job ID: ${selectedJob?.id}`"
      width="70%"
      destroy-on-close
    >
      <div v-if="selectedJob">
        <h3>Job Summary</h3>
        <p><strong>Status:</strong> <el-tag :type="getStatusTagType(selectedJob.status)">{{ selectedJob.status }}</el-tag></p>
        <p><strong>Trigger Type:</strong> {{ selectedJob.triggerType }}</p>
        <p><strong>Created At:</strong> {{ new Date(selectedJob.createdAt).toLocaleString() }}</p>
        <p><strong>Completed At:</strong> {{ selectedJob.completedAt ? new Date(selectedJob.completedAt).toLocaleString() : 'N/A' }}</p>
        <p><strong>Triggered By:</strong> {{ selectedJob.triggeredBy }}</p>

        <h3>Evaluation Results</h3>
        <el-table :data="selectedJobResults" style="width: 100%">
          <el-table-column prop="ruleId" label="Rule ID" />
          <el-table-column prop="contractId" label="Contract ID" />
          <el-table-column prop="status" label="Result Status">
            <template #default="{ row }">
              <el-tag :type="getResultStatusTagType(row.status)">{{ row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="details" label="Details" />
          <el-table-column prop="timestamp" label="Timestamp">
            <template #default="{ row }">
              {{ new Date(row.timestamp).toLocaleString() }}
            </template>
          </el-table-column>
        </el-table>
        <p v-if="selectedJobResults.length === 0">No results available for this job yet.</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import evaluationApi from '../services/evaluationApi';
import { ElMessage } from 'element-plus';

const evaluationJobs = ref([]);
const totalJobs = ref(0);
const currentPage = ref(0);
const pageSize = ref(20);

const isJobDetailsVisible = ref(false);
const selectedJob = ref(null);
const selectedJobResults = ref([]);

onMounted(() => {
  fetchEvaluationJobs();
});

const fetchEvaluationJobs = async () => {
  try {
    const response = await evaluationApi.getEvaluationJobs(currentPage.value, pageSize.value);
    evaluationJobs.value = response.data;
    // Assuming API returns total count in headers or a different structure
    // For now, assuming data.length is total, need to adjust based on backend API
    totalJobs.value = response.data.length;
  } catch (error) {
    ElMessage.error('Failed to fetch evaluation jobs.');
    console.error('Error fetching evaluation jobs:', error);
  }
};

const handlePageChange = (page) => {
  currentPage.value = page - 1; // El-pagination is 1-indexed
  fetchEvaluationJobs();
};

const viewJobDetails = async (job) => {
  selectedJob.value = job;
  isJobDetailsVisible.value = true;
  try {
    const response = await evaluationApi.getEvaluationResults(job.id);
    selectedJobResults.value = response.data;
  } catch (error) {
    ElMessage.error(`Failed to fetch results for job ${job.id}.`);
    console.error(`Error fetching results for job ${job.id}:`, error);
  }
};

const getStatusTagType = (status) => {
  switch (status) {
    case 'COMPLETED': return 'success';
    case 'IN_PROGRESS': return 'info';
    case 'PENDING': return 'warning';
    case 'FAILED': return 'danger';
    default: return '';
  }
};

const getResultStatusTagType = (status) => {
  switch (status) {
    case 'PASS': return 'success';
    case 'FAIL': return 'danger';
    case 'ERROR': return 'danger';
    default: return '';
  }
};
</script>

<style scoped>
.problem-center-page {
  padding: 20px;
}
.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>