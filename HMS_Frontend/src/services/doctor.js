import { PrivateAxios } from '../utils/axios'

// Get All Doctors
export const getAllDoctors = () => {
    return PrivateAxios.get('/doctors').then((response) => response.data)
}

// Get Patients by Doctor
export const getPatientsByDoctor = (doctorId) => {
    return PrivateAxios.get(`/doctor/${doctorId}/patients`).then((response) => response.data)
}

// Select Schedule
export const selectSchedule = (doctorDto, docId, days) => {
    return PrivateAxios.put(`/doctor/${docId}/schedule/${days}`, doctorDto).then((response) => response.data)
}

// Update Health History (Approve/Reject Appointment)
export const updateHealthHistory = (healthDto, admitStatus) => {
    return PrivateAxios.put(`/healthhistory/${admitStatus}`, healthDto).then((response) => response.data)
}

