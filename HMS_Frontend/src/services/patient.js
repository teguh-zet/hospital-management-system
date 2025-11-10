import { PrivateAxios } from '../utils/axios'

// Add Appointment (Create Health History)
export const addAppointment = (healthHistoryDto, patientId) => {
    return PrivateAxios.post(`/patients/${patientId}/healthHistory`, healthHistoryDto).then((response) => response.data)
}

// Get Appointment History by Patient
export const getAppointmentHistory = (patientId) => {
    return PrivateAxios.get(`/patient/${patientId}/appointmenthistory`).then((response) => response.data)
}

// Get Health History by Patient
export const getHealthHistory = (patientId) => {
    return PrivateAxios.get(`/patient/${patientId}/healthhistory`).then((response) => response.data)
}

// Delete Health History (Cancel Appointment)
export const cancelAppointment = (healthHistoryId) => {
    return PrivateAxios.delete(`/healthhistory/${healthHistoryId}`).then((response) => response.data)
}

// Get Patient by ID
export const getPatientById = (patientId) => {
    return PrivateAxios.get(`/patients/${patientId}`).then((response) => response.data)
}

