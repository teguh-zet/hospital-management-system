import { MYAXIOIS, PrivateAxios } from '../utils/axios'

export const singup = (user) => {
    return MYAXIOIS.post('/v1/auth/register', user).then((response) => response.data)
}

export const loginUser = (loginDetails) => {
    return MYAXIOIS.post('/v1/auth/login', loginDetails).then((response) => response.data)
}

export const getTheUser = (email) => {
    return MYAXIOIS.get('/users/email/' + email).then((response) => response.data)
}

export const setNewPassword = (email, password) => {
    return MYAXIOIS.put('/v1/auth/email/' + email + '/forgot/' + password).then((response) => response.data)
}

export const changePassword = (userId, changePasswordDto) => {
    return PrivateAxios.put(`/users/${userId}/change-password`, changePasswordDto).then((response) => response.data)
}

export const uploadPhoto = (userId, formData) => {
    return PrivateAxios.post(`/users/${userId}/upload-photo`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then((response) => response.data)
}

export const removePhoto = (userId) => {
    return PrivateAxios.delete(`/users/${userId}/photo`).then((response) => response.data)
}

export const getUserById = (userId) => {
    return PrivateAxios.get(`/users/${userId}`).then((response) => response.data)
}

