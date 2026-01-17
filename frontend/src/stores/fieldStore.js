import { defineStore } from 'pinia'
import request from '@/utils/request'

export const useFieldStore = defineStore('fieldStore', {
  state: () => ({
    fields: [],
    loading: false
  }),

  getters: {
    isFieldVisible: (state) => (fieldCode) => {
      const field = state.fields.find(f => f.fieldCode === fieldCode)
      return field ? field.isVisible !== false : true // Default to visible
    },
    getFieldByCode: (state) => (fieldCode) => {
      return state.fields.find(f => f.fieldCode === fieldCode)
    }
  },

  actions: {
    async fetchFieldConfigs() {
      this.loading = true
      try {
        const response = await request.get('/metadata/contract-fields')
        this.fields = response.data.data || []
      } catch (error) {
        console.error('Failed to fetch field metadata', error)
      } finally {
        this.loading = false
      }
    }
  }
})
