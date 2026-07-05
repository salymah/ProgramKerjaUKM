import api from './api'

export default {
  login(username, password) {
    return api.post('/auth/login', { username, password })
  },
  // Fitur "Lupa Password": tidak ada infrastruktur email, jadi alurnya
  // berupa pengajuan permintaan yang nanti ditinjau & diproses WK III.
  lupaPassword(username, alasan) {
    return api.post('/auth/lupa-password', { username, alasan })
  },
  getResetRequests() {
    return api.get('/auth/reset-requests')
  },
  prosesResetPassword(id, passwordBaru) {
    return api.patch(`/auth/reset-requests/${id}`, { passwordBaru })
  },
}
