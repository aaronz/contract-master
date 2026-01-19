import request from '@/utils/request'

export default {
  getProblems(params) {
    return request.get('/problems', { params })
  },

  getProblem(id) {
    return request.get(`/problems/${id}`)
  },

  updateProblem(id, data) {
    return request.put(`/problems/${id}`, data)
  },

  triggerEvaluation(contractId) {
    return request.post('/evaluations', { contractIds: [contractId] })
  },

  getRules() {
    return request.get('/rules')
  },

  createRule(data) {
    return request.post('/rules', data)
  },

  updateRule(id, data) {
    return request.put(`/rules/${id}`, data)
  }
}
