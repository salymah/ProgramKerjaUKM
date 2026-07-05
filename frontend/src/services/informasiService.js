import api from './api'

export default {
  getAll() {
    return api.get('/informasi')
  },
  create(data) {
    return api.post('/informasi', data)
  },
  update(id, data) {
    return api.put(`/informasi/${id}`, data)
  },
  delete(id) {
    return api.delete(`/informasi/${id}`)
  },
}
