import { PrivateAxios } from '../utils/axios'

// Create Admin
export const createAdmin = (employeeDto) => {
    return PrivateAxios.post('/employee/admin', employeeDto).then((response) => response.data)
}

// Create Employee
export const createEmployee = (employeeDto, adminId) => {
    return PrivateAxios.post(`/employee/create/${adminId}`, employeeDto).then((response) => response.data)
}

// Get All Employees
export const getAllEmployees = (pageNumber = 0, pageSize = 10, sortBy = 'id', sortDir = 'asc') => {
    return PrivateAxios.get('/employee', {
        params: { pageNumber, pageSize, sortBy, sortDir }
    }).then((response) => response.data)
}

// Get All Patients
export const getAllPatients = (pageNumber = 0, pageSize = 10, sortBy = 'id', sortDir = 'asc') => {
    return PrivateAxios.get('/patients', {
        params: { pageNumber, pageSize, sortBy, sortDir }
    }).then((response) => response.data)
}

// Remove Employee (Delete)
export const removeEmployee = (employeeId) => {
    return PrivateAxios.delete(`/employee/${employeeId}`).then((response) => response.data)
}

