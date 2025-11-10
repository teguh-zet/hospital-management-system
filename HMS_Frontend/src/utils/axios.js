import axios from "axios"
import { getToken } from "./auth"

export const BASE_URL = 'http://localhost:9090/api/'

export const MYAXIOIS = axios.create({
  baseURL: BASE_URL
})

export const PrivateAxios = axios.create({
  baseURL: BASE_URL,
})

PrivateAxios.interceptors.request.use(config => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
    return config
  }
  return config
}, (error) => {
  return Promise.reject(error)
})

