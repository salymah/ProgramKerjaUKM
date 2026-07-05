import { defineStore } from 'pinia'
import programKerjaService from '@/services/programKerjaService'

export const useProgramKerjaStore = defineStore('programKerja', {
  state: () => ({
    list: [],
    statistik: { total: 0, diajukan: 0, disetujui: 0, selesai: 0 },
    loading: false,
  }),
  actions: {
    async fetchAll() {
      this.loading = true
      try {
        const res = await programKerjaService.getAll()
        this.list = res.data.data
      } finally {
        this.loading = false
      }
    },
    async fetchStatistik() {
      const res = await programKerjaService.getStatistik()
      this.statistik = res.data.data
    },
  },
})
