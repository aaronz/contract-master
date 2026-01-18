import request from '@/utils/request'

export default {
  getContracts() {
    return request.get('/contracts')
  }
}
