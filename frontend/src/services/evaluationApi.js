// frontend/src/services/evaluationApi.js

import request from '@/utils/request';

const API_BASE_URL = '';

export default {
  triggerEvaluation(contractId, ruleIds) { // Changed parameter order and single contractId
    return request.post(`/evaluations`, {
      contractIds: [contractId], // Wrap single contractId in a list
      ruleIds: ruleIds,
    });
  },

  getEvaluationJobs(page = 0, size = 20) {
    return request.get(`/problem-center/evaluation-jobs`, {
      params: { page, size },
    });
  },

  getEvaluationResults(jobId) {
    return request.get(`/problem-center/evaluation-jobs/${jobId}/results`);
  },

  getContracts() {
    return request.get(`/contracts`);
  },

  getTriggerScenarios() {
    return request.get(`/rules/trigger-scenarios`);
  },

  getRules() {
    return request.get(`/rules`);
  },

  getRule(id) {
    return request.get(`/rules/${id}`);
  },

  createRule(rule) {
    return request.post(`/rules`, rule);
  },

  updateRule(id, rule) {
    return request.put(`/rules/${id}`, rule);
  },

  batchUpdateRules(rules) {
    return request.put(`/rules/batch`, rules);
  },
};
