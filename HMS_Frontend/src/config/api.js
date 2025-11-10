// API Configuration
export const API_CONFIG = {
  BASE_URL: 'http://localhost:9090/api/',
  RESOURCE_URL: 'http://localhost:9090/api/resources',
  TIMEOUT: 10000, // 10 seconds
}

// Role IDs
export const ROLES = {
  ADMIN: 500,
  DOCTOR: 501,
  PATIENT: 502,
  RECEPTIONIST: 503,
  ACCOUNTANT: 504
}

// Pagination defaults
export const PAGINATION = {
  PAGE_NUMBER: 0,
  PAGE_SIZE: 10,
  SORT_BY: 'id',
  SORT_DIR: 'asc'
}

