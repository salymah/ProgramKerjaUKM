import api from './api'

export default {
  getAll() {
    return api.get('/ukm')
  },
  getById(id) {
    return api.get(`/ukm/${id}`)
  },
  create(data) {
    return api.post('/ukm', data)
  },
  update(id, data) {
    return api.put(`/ukm/${id}`, data)
  },
  delete(id) {
    return api.delete(`/ukm/${id}`)
  },
}
