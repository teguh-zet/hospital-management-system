import { defineStore } from 'pinia'
import { getCurrentUserDetail, isLoggedIn } from '../utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    data: {},
    login: false
  }),
  
  actions: {
    initUser() {
      this.data = getCurrentUserDetail() || {}
      this.login = isLoggedIn()
    },
    
    setUser(userData) {
      this.data = userData.user || {}
      this.login = true
    },
    
    logout() {
      this.data = {}
      this.login = false
    }
  },
  
  getters: {
    isAuthenticated: (state) => state.login,
    currentUser: (state) => state.data
  }
})

