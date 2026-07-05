import api from './api'

export default {
  getAll() {
    return api.get('/tempat')
  },
  getById(id) {
    return api.get(`/tempat/${id}`)
  },
  create(data) {
    return api.post('/tempat', data)
  },
  update(id, data) {
    return api.put(`/tempat/${id}`, data)
  },
  delete(id) {
    return api.delete(`/tempat/${id}`)
  },
}
