import request from '@/utils/request'

export default {
  getContracts(params) {
    return request.get('/contracts', { params })
  }
}
