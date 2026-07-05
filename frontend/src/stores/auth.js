import { defineStore } from 'pinia'
import authService from '@/services/authService'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    user: JSON.parse(localStorage.getItem('user') || 'null'),
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    role: (state) => state.user?.role || null,
    isWkIII: (state) => state.user?.role === 'WKIII',
    isPembina: (state) => state.user?.role === 'PEMBINA',
    isUkm: (state) => state.user?.role === 'UKM',
  },
  actions: {
    async login(username, password) {
      const res = await authService.login(username, password)
      const { token, user } = res.data.data
      this.token = token
      this.user = user
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(user))
      return user
    },
    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },
  },
})
