import request from '@/utils/request'

export default {
  getContracts(params) {
    return request.get('/contracts', { params })
  },
  
  getContractDetail(id) {
    return request.get(`/contracts/${id}`)
  },

  updateContract(id, data) {
    return request.put(`/contracts/${id}`, data)
  }
}
