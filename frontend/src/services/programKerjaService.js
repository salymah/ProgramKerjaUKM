import api from './api'

export default {
  getAll() {
    return api.get('/program-kerja')
  },
  getById(id) {
    return api.get(`/program-kerja/${id}`)
  },
  create(data) {
    return api.post('/program-kerja', data)
  },
  update(id, data) {
    return api.put(`/program-kerja/${id}`, data)
  },
  verifikasi(id, data) {
    // data: { status: 'DISETUJUI' | 'REVISI', keterangan }
    return api.patch(`/program-kerja/${id}/verifikasi`, data)
  },
  delete(id) {
    return api.delete(`/program-kerja/${id}`)
  },
  getStatistik() {
    return api.get('/program-kerja/statistik')
  },
}
