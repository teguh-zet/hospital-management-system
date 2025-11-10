import axios from "axios"
import { getToken, doLogout } from "./auth"

export const BASE_URL = 'http://localhost:9090/api/'

export const MYAXIOIS = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

export const PrivateAxios = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Request Interceptor
PrivateAxios.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// Response Interceptor
PrivateAxios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response) {
      // Handle 401 Unauthorized
      if (error.response.status === 401) {
        doLogout(() => {
          // Redirect to login if not already there
          if (window.location.pathname !== '/login') {
            window.location.href = '/login'
          }
        })
      }
      // Handle 403 Forbidden
      if (error.response.status === 403) {
        console.error('Access denied: You do not have permission to access this resource')
      }
    }
    return Promise.reject(error)
  }
)

