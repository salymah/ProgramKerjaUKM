import { defineStore } from 'pinia'
import informasiService from '@/services/informasiService'

export const useInformasiStore = defineStore('informasi', {
  state: () => ({
    list: [],
    loading: false,
  }),
  actions: {
    async fetchAll() {
      this.loading = true
      try {
        const res = await informasiService.getAll()
        this.list = res.data.data
      } finally {
        this.loading = false
      }
    },
  },
})
