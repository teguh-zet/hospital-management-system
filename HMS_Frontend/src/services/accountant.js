import { PrivateAxios } from '../utils/axios'

// Get All Patients After Appointment
export const getAllPatientsAfterAppointment = (pageNumber = 0, pageSize = 10, sortBy = 'id', sortDir = 'asc') => {
    return PrivateAxios.get('/appointment/patients', {
        params: { pageNumber, pageSize, sortBy, sortDir }
    }).then((response) => response.data)
}

// Get Patient by ID
export const getPatientById = (patientId) => {
    return PrivateAxios.get(`/patients/${patientId}`).then((response) => response.data)
}

// Update Payment
export const updatePayment = (healthHistoryId, amount) => {
    return PrivateAxios.put(`/healthhistory/${healthHistoryId}/amount/${amount}`).then((response) => response.data)
}

// Get Health History by Payment Status
export const getHealthHistoryByPaymentStatus = (patientId) => {
    return PrivateAxios.get(`/patient/${patientId}/healthhistory/paymentstatus`).then((response) => response.data)
}

