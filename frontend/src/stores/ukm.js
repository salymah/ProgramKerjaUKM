import { defineStore } from 'pinia'
import ukmService from '@/services/ukmService'

export const useUkmStore = defineStore('ukm', {
  state: () => ({
    list: [],
    loading: false,
  }),
  actions: {
    async fetchAll() {
      this.loading = true
      try {
        const res = await ukmService.getAll()
        this.list = res.data.data
      } finally {
        this.loading = false
      }
    },
  },
})
