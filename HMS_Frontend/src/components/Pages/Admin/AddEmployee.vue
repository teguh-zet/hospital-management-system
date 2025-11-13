<template>
  <div class="add-employee-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarAdmin />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-person-plus me-2"></i>Add Employee
            </h2>
          </div>

          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">Employee Information</h5>
            </div>
            <div class="card-body">
              <form @submit.prevent="handleSubmit">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label for="firstName" class="form-label">First Name</label>
                    <input
                      type="text"
                      class="form-control"
                      id="firstName"
                      v-model="formData.firstName"
                      required
                    />
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="lastName" class="form-label">Last Name</label>
                    <input
                      type="text"
                      class="form-control"
                      id="lastName"
                      v-model="formData.lastName"
                      required
                    />
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input
                      type="email"
                      class="form-control"
                      id="email"
                      v-model="formData.email"
                      required
                    />
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input
                      type="password"
                      class="form-control"
                      id="password"
                      v-model="formData.password"
                      required
                    />
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="role" class="form-label">Role</label>
                    <select class="form-control" id="role" v-model="formData.role" required>
                      <option value="">Select Role</option>
                      <option value="ROLE_DOCTOR">Doctor</option>
                      <option value="ROLE_RECEPTIONIST">Receptionist</option>
                      <option value="ROLE_ACCOUNTANT">Accountant</option>
                    </select>
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="department" class="form-label">Department</label>
                    <input
                      type="text"
                      class="form-control"
                      id="department"
                      v-model="formData.department"
                      placeholder="e.g., Cardiology, Pediatrics"
                    />
                  </div>
                  <div class="col-md-6 mb-3" v-if="formData.role === 'ROLE_DOCTOR'">
                    <label for="specialization" class="form-label">Specialization</label>
                    <input
                      type="text"
                      class="form-control"
                      id="specialization"
                      v-model="formData.specialization"
                      placeholder="e.g., Cardiologist, Pediatrician"
                    />
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="gender" class="form-label">Gender</label>
                    <select class="form-control" id="gender" v-model="formData.gender" required>
                      <option value="">Select Gender</option>
                      <option value="Male">Male</option>
                      <option value="Female">Female</option>
                      <option value="Other">Other</option>
                    </select>
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="mobileNo" class="form-label">Mobile Number</label>
                    <input
                      type="text"
                      class="form-control"
                      id="mobileNo"
                      v-model="formData.mobileNo"
                      placeholder="e.g., 081234567890"
                      required
                    />
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="securityQue" class="form-label">Security Question</label>
                    <input
                      type="text"
                      class="form-control"
                      id="securityQue"
                      v-model="formData.securityQue"
                      placeholder="e.g., What is your mother's maiden name?"
                      required
                    />
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="securityAns" class="form-label">Security Answer</label>
                    <input
                      type="text"
                      class="form-control"
                      id="securityAns"
                      v-model="formData.securityAns"
                      placeholder="Your answer to security question"
                      required
                    />
                  </div>
                  <div class="col-md-12 mb-3">
                    <label for="photo" class="form-label">Profile Photo (Optional)</label>
                    <input
                      type="file"
                      class="form-control"
                      id="photo"
                      accept="image/*"
                      @change="handlePhotoChange"
                    />
                    <small class="text-muted">Max size: 5MB. Supported formats: JPG, PNG, GIF</small>
                    <div v-if="photoPreview" class="mt-2">
                      <img :src="photoPreview" alt="Preview" style="max-width: 200px; max-height: 200px; border-radius: 8px; border: 2px solid #ddd;" />
                      <button type="button" class="btn btn-sm btn-danger ms-2" @click="removePhotoPreview">
                        <i class="bi bi-x"></i> Remove
                      </button>
                    </div>
                  </div>
                </div>
                <div class="text-end">
                  <button type="button" class="btn btn-outline-secondary me-2" @click="handleReset">
                    Reset
                  </button>
                  <button type="submit" class="btn btn-primary" :disabled="isLoading">
                    <span v-if="!isLoading">
                      <i class="bi bi-check-circle me-2"></i>Add Employee
                    </span>
                    <span v-else>
                      <span class="spinner-border spinner-border-sm me-2"></span>Adding...
                    </span>
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { useUserStore } from '../../../stores/user'
import { createEmployee } from '../../../services/admin'
import { uploadPhoto } from '../../../services/user'
import VerticalNavbarAdmin from './VerticalNavbarAdmin.vue'

export default {
  name: 'AddEmployee',
  components: {
    VerticalNavbarAdmin
  },
  setup() {
    const router = useRouter()
    const toast = useToast()
    const userStore = useUserStore()
    
        const formData = ref({
          firstName: '',
          lastName: '',
          email: '',
          password: '',
          role: '',
          department: '',
          specialization: '',
          gender: '',
          mobileNo: '',
          securityQue: '',
          securityAns: ''
        })
        
        const photoFile = ref(null)
        const photoPreview = ref(null)
        const isLoading = ref(false)

        const handlePhotoChange = (event) => {
          const file = event.target.files[0]
          if (!file) {
            photoFile.value = null
            photoPreview.value = null
            return
          }

          // Validate file type
          if (!file.type.startsWith('image/')) {
            toast.error('File harus berupa gambar')
            event.target.value = ''
            return
          }

          // Validate file size (max 5MB)
          if (file.size > 5 * 1024 * 1024) {
            toast.error('Ukuran file maksimal 5MB')
            event.target.value = ''
            return
          }

          photoFile.value = file
          
          // Create preview
          const reader = new FileReader()
          reader.onload = (e) => {
            photoPreview.value = e.target.result
          }
          reader.readAsDataURL(file)
        }

        const removePhotoPreview = () => {
          photoFile.value = null
          photoPreview.value = null
          const fileInput = document.getElementById('photo')
          if (fileInput) {
            fileInput.value = ''
          }
        }

    const handleSubmit = async () => {
      if (!formData.value.firstName || !formData.value.lastName || !formData.value.email || 
          !formData.value.password || !formData.value.role || !formData.value.gender ||
          !formData.value.mobileNo || !formData.value.securityQue || !formData.value.securityAns) {
        toast.error('Harap lengkapi semua field yang wajib')
        return
      }

      isLoading.value = true
      try {
        // Map role name to role ID
        // Based on AppConstants: ROLE_ADMIN=500, ROLE_DOCTOR=501, ROLE_PATIENT=502, ROLE_RECEPTIONIST=503, ROLE_ACCOUNTANT=504
        const roleIdMap = {
          'ROLE_DOCTOR': 501,
          'ROLE_RECEPTIONIST': 503,
          'ROLE_ACCOUNTANT': 504
        }
        
        const roleId = roleIdMap[formData.value.role]
        if (!roleId) {
          toast.error('Role tidak valid')
          return
        }

        const employeeDto = {
          user: {
            firstName: formData.value.firstName,
            lastName: formData.value.lastName,
            email: formData.value.email,
            password: formData.value.password,
            gender: formData.value.gender,
            mobileNo: formData.value.mobileNo,
            securityQue: formData.value.securityQue,
            securityAns: formData.value.securityAns,
            address: {
              street: '',
              city: '',
              state: '',
              zipCode: '',
              country: ''
            }
          },
          doctor: formData.value.role === 'ROLE_DOCTOR' && formData.value.specialization ? {
            specialization: formData.value.specialization
          } : null
        }
        
        // Remove null doctor if not doctor role
        if (formData.value.role !== 'ROLE_DOCTOR') {
          delete employeeDto.doctor
        }
        
        console.log('Sending employeeDto:', employeeDto)
        console.log('Role ID:', roleId)
        
        const createdEmployee = await createEmployee(employeeDto, roleId)
        
        // Upload photo if provided
        if (photoFile.value && createdEmployee?.user?.id) {
          try {
            const formDataPhoto = new FormData()
            formDataPhoto.append('file', photoFile.value)
            await uploadPhoto(createdEmployee.user.id, formDataPhoto)
            toast.success('Foto berhasil diupload!')
          } catch (photoError) {
            console.error('Error uploading photo:', photoError)
            // Don't fail the whole operation if photo upload fails
            toast.warning('Employee berhasil dibuat, tetapi upload foto gagal')
          }
        }
        
        toast.success('Employee berhasil ditambahkan!')
        router.push('/user/admin/allEmployee')
      } catch (error) {
        console.error('Error creating employee:', error)
        console.error('Error response:', error.response?.data)
        toast.error(error.response?.data?.message || error.response?.data?.error || 'Gagal menambahkan employee')
      } finally {
        isLoading.value = false
      }
    }

        const handleReset = () => {
          formData.value = {
            firstName: '',
            lastName: '',
            email: '',
            password: '',
            role: '',
            department: '',
            specialization: '',
            gender: '',
            mobileNo: '',
            securityQue: '',
            securityAns: ''
          }
          removePhotoPreview()
        }

        return {
          formData,
          photoFile,
          photoPreview,
          isLoading,
          handleSubmit,
          handleReset,
          handlePhotoChange,
          removePhotoPreview
        }
  }
}
</script>

<style scoped>
.add-employee-page {
  min-height: 100vh;
  background: #f8f9fa;
}

.page-header {
  margin-top: 20px;
}

.text-gradient {
  background: linear-gradient(135deg, #3498db, #2980b9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
</style>
