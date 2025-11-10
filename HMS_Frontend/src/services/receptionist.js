import { PrivateAxios } from '../utils/axios'

// Get All Patients for Receptionist
export const getAllPatientsForReceptionist = (pageNumber = 0, pageSize = 10, sortBy = 'id', sortDir = 'asc') => {
    return PrivateAxios.get('/receptionist/patients', {
        params: { pageNumber, pageSize, sortBy, sortDir }
    }).then((response) => response.data)
}

// Get All Patients for Admit
export const getAllPatientsForAdmit = (pageNumber = 0, pageSize = 10, sortBy = 'id', sortDir = 'asc') => {
    return PrivateAxios.get('/receptionist/patients/admit', {
        params: { pageNumber, pageSize, sortBy, sortDir }
    }).then((response) => response.data)
}

// Get All Patients for Discharge
export const getAllPatientsForDischarge = (pageNumber = 0, pageSize = 10, sortBy = 'id', sortDir = 'asc') => {
    return PrivateAxios.get('/receptionist/patients/discharge', {
        params: { pageNumber, pageSize, sortBy, sortDir }
    }).then((response) => response.data)
}

// Appoint Doctor to Patient
export const appointDoctorToPatient = (patientId, doctorId) => {
    return PrivateAxios.put(`/patients/${patientId}/doctor/${doctorId}`).then((response) => response.data)
}

// Admit Patient (Allocate Ward)
export const admitPatient = (healthHistoryDto, wardId) => {
    return PrivateAxios.put(`/healthhistory/ward/${wardId}`, healthHistoryDto).then((response) => response.data)
}

// Discharge Patient
export const dischargePatient = (healthHistoryId) => {
    return PrivateAxios.put(`/healthhistory/${healthHistoryId}/discharge`).then((response) => response.data)
}

// Get All Wards
export const getAllWards = () => {
    return PrivateAxios.get('/wards/').then((response) => response.data)
}

// Get Health History by Payment Status
export const getHealthHistoryByPaymentStatus = (patientId) => {
    return PrivateAxios.get(`/patient/${patientId}/healthhistory/paymentstatus`).then((response) => response.data)
}

