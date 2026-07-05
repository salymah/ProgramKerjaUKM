import { defineStore } from 'pinia'
import tempatService from '@/services/tempatService'

export const useTempatStore = defineStore('tempat', {
  state: () => ({
    list: [],
    loading: false,
  }),
  actions: {
    async fetchAll() {
      this.loading = true
      try {
        const res = await tempatService.getAll()
        this.list = res.data.data
      } finally {
        this.loading = false
      }
    },
  },
})
