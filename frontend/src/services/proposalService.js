import api from './api'

export default {
  getByProgramKerja(programKerjaId) {
    return api.get(`/proposal/program-kerja/${programKerjaId}`)
  },
  upload(programKerjaId, file) {
    const formData = new FormData()
    formData.append('file', file)
    return api.post(`/proposal/program-kerja/${programKerjaId}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
  download(id) {
    return api.get(`/proposal/${id}/download`, { responseType: 'blob' })
  },
  delete(id) {
    return api.delete(`/proposal/${id}`)
  },
}
