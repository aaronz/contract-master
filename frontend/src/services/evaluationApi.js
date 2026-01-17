// frontend/src/services/evaluationApi.js

import axios from 'axios';

const API_BASE_URL = '/api'; // Adjust if your API is on a different base path

export default {
  /**
   * Triggers a manual rule evaluation.
   * @param {Array<string>} ruleIds - Array of rule IDs.
   * @param {Array<string>} contractIds - Array of contract IDs.
   * @returns {Promise<Object>} - The response containing the job ID.
   */
  triggerEvaluation(ruleIds, contractIds) {
    return axios.post(`${API_BASE_URL}/evaluations`, {
      rule_ids: ruleIds,
      contract_ids: contractIds,
    });
  },

  /**
   * Fetches a paginated list of evaluation jobs for the Problem Center.
   * @param {number} page - Page number (0-indexed).
   * @param {number} size - Number of items per page.
   * @returns {Promise<Array<Object>>} - Array of evaluation jobs.
   */
  getEvaluationJobs(page = 0, size = 20) {
    return axios.get(`${API_BASE_URL}/problem-center/evaluation-jobs`, {
      params: { page, size },
    });
  },

  /**
   * Fetches detailed results for a specific evaluation job.
   * @param {string} jobId - The ID of the evaluation job.
   * @returns {Promise<Array<Object>>} - Array of evaluation results.
   */
  getEvaluationResults(jobId) {
    return axios.get(`${API_BASE_URL}/problem-center/evaluation-jobs/${jobId}/results`);
  },

  /**
   * Fetches a list of available contracts.
   * @returns {Promise<Array<Object>>} - Array of contracts.
   */
  getContracts() {
    return axios.get(`${API_BASE_URL}/contracts`);
  },

  /**
   * Fetches automatic rule trigger scenarios.
   * @returns {Promise<Object>} - Object containing trigger scenarios.
   */
  getTriggerScenarios() {
    return axios.get(`${API_BASE_URL}/rules/trigger-scenarios`);
  },
};
