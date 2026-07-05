import api from './api'

export default {
  getByProgramKerja(programKerjaId) {
    return api.get(`/catatan/program-kerja/${programKerjaId}`)
  },
  create(programKerjaId, data) {
    return api.post(`/catatan/program-kerja/${programKerjaId}`, data)
  },
  delete(id) {
    return api.delete(`/catatan/${id}`)
  },
}
