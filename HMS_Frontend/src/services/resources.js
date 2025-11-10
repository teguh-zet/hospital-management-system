import { PrivateAxios } from '../utils/axios'

// Create Resource
export const createResource = (resourceDto) => {
    return PrivateAxios.post('/resources/add', resourceDto).then((response) => response.data)
}

// Update Resource
export const updateResource = (resourceDto, resourceId) => {
    return PrivateAxios.put(`/resources/${resourceId}`, resourceDto).then((response) => response.data)
}

// Delete Resource
export const deleteResource = (resourceId) => {
    return PrivateAxios.delete(`/resources/${resourceId}`).then((response) => response.data)
}

// Get Resource by ID
export const getResourceById = (resourceId) => {
    return PrivateAxios.get(`/resources/${resourceId}`).then((response) => response.data)
}

// Get All Resources
export const getAllResources = (pageNumber = 0, pageSize = 10, sortBy = 'id', sortDir = 'asc') => {
    return PrivateAxios.get('/resources', {
        params: { pageNumber, pageSize, sortBy, sortDir }
    }).then((response) => response.data)
}

