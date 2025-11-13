<template>
  <div class="profile-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <component :is="navbarComponent" />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-person-circle me-2"></i>My Profile
            </h2>
          </div>

          <div class="row">
            <div class="col-md-4 mb-4">
              <div class="card profile-card">
                <div class="card-body text-center">
                  <div class="profile-avatar mb-3 position-relative">
                    <img 
                      v-if="userStore.currentUser?.photo" 
                      :src="getPhotoUrl(userStore.currentUser.photo)" 
                      alt="Profile Photo"
                      class="profile-photo"
                      @error="handleImageError"
                    />
                    <i v-else class="bi bi-person-circle profile-icon"></i>
                    <div v-if="isUploadingPhoto" class="photo-upload-overlay">
                      <div class="spinner-border text-light" role="status">
                        <span class="visually-hidden">Uploading...</span>
                      </div>
                    </div>
                  </div>
                  <h4>{{ userStore.currentUser?.firstName }} {{ userStore.currentUser?.lastName }}</h4>
                  <p class="text-muted">{{ userStore.currentUser?.email }}</p>
                  <span class="badge bg-primary mb-3">
                    {{ getRoleName(userStore.currentUser?.roles) }}
                  </span>
                  <div class="mt-3">
                    <label for="photoUpload" class="btn btn-sm btn-outline-primary me-2" :disabled="isUploadingPhoto">
                      <i class="bi bi-camera me-1"></i>{{ userStore.currentUser?.photo ? 'Change Photo' : 'Upload Photo' }}
                    </label>
                    <input 
                      type="file" 
                      id="photoUpload" 
                      accept="image/*" 
                      style="display: none;"
                      @change="handlePhotoUpload"
                      :disabled="isUploadingPhoto"
                    />
                    <button 
                      v-if="userStore.currentUser?.photo" 
                      class="btn btn-sm btn-outline-danger"
                      @click="handleRemovePhoto"
                      :disabled="isUploadingPhoto"
                    >
                      <i class="bi bi-trash me-1"></i>Remove
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-md-8">
              <div class="card">
                <div class="card-header">
                  <h5 class="mb-0">Personal Information</h5>
                </div>
                <div class="card-body">
                  <div class="row mb-3">
                    <div class="col-md-6">
                      <strong>First Name:</strong>
                      <p>{{ userStore.currentUser?.firstName || '-' }}</p>
                    </div>
                    <div class="col-md-6">
                      <strong>Last Name:</strong>
                      <p>{{ userStore.currentUser?.lastName || '-' }}</p>
                    </div>
                  </div>
                  <div class="row mb-3">
                    <div class="col-md-6">
                      <strong>Email:</strong>
                      <p>{{ userStore.currentUser?.email || '-' }}</p>
                    </div>
                    <div class="col-md-6">
                      <strong>Mobile Number:</strong>
                      <p>{{ userStore.currentUser?.mobileNo || '-' }}</p>
                    </div>
                  </div>
                  <div class="row mb-3">
                    <div class="col-md-6">
                      <strong>Gender:</strong>
                      <p>{{ userStore.currentUser?.gender || '-' }}</p>
                    </div>
                    <div class="col-md-6">
                      <strong>Blood Group:</strong>
                      <p>{{ userStore.currentUser?.bloodGroup || '-' }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card mt-3">
                <div class="card-header">
                  <h5 class="mb-0">Change Password</h5>
                </div>
                <div class="card-body">
                  <form @submit.prevent="handleChangePassword">
                    <div class="mb-3">
                      <label for="currentPassword" class="form-label">Current Password</label>
                      <input
                        type="password"
                        class="form-control"
                        id="currentPassword"
                        v-model="passwordForm.currentPassword"
                        required
                      />
                    </div>
                    <div class="mb-3">
                      <label for="newPassword" class="form-label">New Password</label>
                      <input
                        type="password"
                        class="form-control"
                        id="newPassword"
                        v-model="passwordForm.newPassword"
                        required
                      />
                    </div>
                    <div class="mb-3">
                      <label for="confirmPassword" class="form-label">Confirm New Password</label>
                      <input
                        type="password"
                        class="form-control"
                        id="confirmPassword"
                        v-model="passwordForm.confirmPassword"
                        required
                      />
                    </div>
                    <div class="mb-3">
                      <label for="securityAnswer" class="form-label">Security Answer</label>
                      <input
                        type="text"
                        class="form-control"
                        id="securityAnswer"
                        v-model="passwordForm.securityAnswer"
                        :placeholder="`Answer: ${userStore.currentUser?.securityQue || 'Security Question'}`"
                        required
                      />
                      <small class="text-muted">Masukkan jawaban dari security question Anda</small>
                    </div>
                    <button type="submit" class="btn btn-primary" :disabled="isChangingPassword">
                      <span v-if="!isChangingPassword">
                        <i class="bi bi-key me-2"></i>Change Password
                      </span>
                      <span v-else>
                        <span class="spinner-border spinner-border-sm me-2"></span>Changing...
                      </span>
                    </button>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { useUserStore } from '../../../stores/user'
import { doLogout, getToken } from '../../../utils/auth'
import { changePassword, uploadPhoto, removePhoto, getUserById } from '../../../services/user'
import { PrivateAxios } from '../../../utils/axios'
import VerticalNavbarAdmin from '../Admin/VerticalNavbarAdmin.vue'
import VerticalNavbarPatient from '../Patient/VerticalNavbarPatient.vue'
import VerticalNavbarDoctor from '../Doctor/VerticalNavbarDoctor.vue'
import VerticalNavbarReceptionist from '../Receptionist/VerticalNavbarReceptionist.vue'
import VerticalNavbarAccountant from '../Accountant/VerticalNavbarAccountant.vue'

export default {
  name: 'Profile',
  components: {
    VerticalNavbarAdmin,
    VerticalNavbarPatient,
    VerticalNavbarDoctor,
    VerticalNavbarReceptionist,
    VerticalNavbarAccountant
  },
  setup() {
    const router = useRouter()
    const toast = useToast()
    const userStore = useUserStore()
    
    const passwordForm = ref({
      currentPassword: '',
      newPassword: '',
      confirmPassword: '',
      securityAnswer: ''
    })
    const isChangingPassword = ref(false)
    const isUploadingPhoto = ref(false)

    const getRoleName = (roles) => {
      if (!roles || roles.length === 0) return 'N/A'
      return roles[0]?.name?.replace('ROLE_', '') || 'N/A'
    }

    const navbarComponent = computed(() => {
      const role = getRoleName(userStore.currentUser?.roles)
      if (role === 'ADMIN') return 'VerticalNavbarAdmin'
      if (role === 'PATIENT') return 'VerticalNavbarPatient'
      if (role === 'DOCTOR') return 'VerticalNavbarDoctor'
      if (role === 'RECEPTIONIST') return 'VerticalNavbarReceptionist'
      if (role === 'ACCOUNTANT') return 'VerticalNavbarAccountant'
      return 'VerticalNavbarAdmin'
    })

    const getPhotoUrl = (photoPath) => {
      if (!photoPath) return ''
      // If photo path starts with /uploads/, it's a relative path
      if (photoPath.startsWith('/uploads/')) {
        // Get base URL from axios config and remove /api/ suffix
        let baseUrl = PrivateAxios.defaults.baseURL || 'http://localhost:9090/api'
        // Remove /api/ or /api suffix to get server base URL
        if (baseUrl.endsWith('/api/')) {
          baseUrl = baseUrl.replace('/api/', '')
        } else if (baseUrl.endsWith('/api')) {
          baseUrl = baseUrl.replace('/api', '')
        }
        // Construct full URL: http://localhost:9090/uploads/filename.jpg
        return `${baseUrl}${photoPath}`
      }
      return photoPath
    }

    const handlePhotoUpload = async (event) => {
      const file = event.target.files[0]
      if (!file) return

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

      isUploadingPhoto.value = true
      try {
        const userId = userStore.currentUser?.id
        if (!userId) {
          toast.error('User ID tidak ditemukan')
          return
        }

        const formData = new FormData()
        formData.append('file', file)

        const updatedUser = await uploadPhoto(userId, formData)
        toast.success('Foto berhasil diupload!')
        
        // Reload user data from API to get latest photo
        try {
          const freshUserData = await getUserById(userId)
          if (freshUserData) {
            const currentData = JSON.parse(localStorage.getItem('data') || '{}')
            currentData.user = freshUserData
            localStorage.setItem('data', JSON.stringify(currentData))
            userStore.initUser()
          }
        } catch (refreshError) {
          console.error('Error refreshing user data:', refreshError)
          // Fallback: update from response
          if (updatedUser) {
            const currentData = JSON.parse(localStorage.getItem('data') || '{}')
            currentData.user = updatedUser
            localStorage.setItem('data', JSON.stringify(currentData))
            userStore.initUser()
          }
        }
        
        // Reset file input
        event.target.value = ''
      } catch (error) {
        console.error('Error uploading photo:', error)
        let errorMessage = 'Gagal mengupload foto'
        
        if (error.response?.data) {
          if (error.response.data.message) {
            errorMessage = error.response.data.message
          } else if (typeof error.response.data === 'object') {
            const firstError = Object.values(error.response.data)[0]
            if (firstError) {
              errorMessage = Array.isArray(firstError) ? firstError[0] : firstError
            }
          }
        }
        
        toast.error(errorMessage)
      } finally {
        isUploadingPhoto.value = false
      }
    }

    const handleRemovePhoto = async () => {
      if (!confirm('Apakah Anda yakin ingin menghapus foto profil?')) {
        return
      }

      isUploadingPhoto.value = true
      try {
        const userId = userStore.currentUser?.id
        if (!userId) {
          toast.error('User ID tidak ditemukan')
          return
        }

        const updatedUser = await removePhoto(userId)
        toast.success('Foto berhasil dihapus!')
        
        // Reload user data from API to get latest photo (null)
        try {
          const freshUserData = await getUserById(userId)
          if (freshUserData) {
            const currentData = JSON.parse(localStorage.getItem('data') || '{}')
            currentData.user = freshUserData
            localStorage.setItem('data', JSON.stringify(currentData))
            userStore.initUser()
          }
        } catch (refreshError) {
          console.error('Error refreshing user data:', refreshError)
          // Fallback: update from response
          if (updatedUser) {
            const currentData = JSON.parse(localStorage.getItem('data') || '{}')
            currentData.user = updatedUser
            localStorage.setItem('data', JSON.stringify(currentData))
            userStore.initUser()
          }
        }
      } catch (error) {
        console.error('Error removing photo:', error)
        let errorMessage = 'Gagal menghapus foto'
        
        if (error.response?.data) {
          if (error.response.data.message) {
            errorMessage = error.response.data.message
          }
        }
        
        toast.error(errorMessage)
      } finally {
        isUploadingPhoto.value = false
      }
    }

    const handleImageError = (event) => {
      // If image fails to load, hide it and show icon instead
      event.target.style.display = 'none'
    }

    const getToken = () => {
      const data = localStorage.getItem('data')
      if (data) {
        try {
          return JSON.parse(data).token
        } catch (e) {
          return null
        }
      }
      return null
    }

    const handleChangePassword = async () => {
      // Validation
      if (!passwordForm.value.currentPassword || !passwordForm.value.newPassword || !passwordForm.value.confirmPassword || !passwordForm.value.securityAnswer) {
        toast.error('Harap lengkapi semua field')
        return
      }

      if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
        toast.error('New password dan confirm password tidak sama')
        return
      }

      if (passwordForm.value.newPassword.length < 4) {
        toast.error('Password minimal 4 karakter')
        return
      }

      // Check password pattern (uppercase, lowercase, number, special char)
      const passwordPattern = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\S+$).{4,15}$/
      if (!passwordPattern.test(passwordForm.value.newPassword)) {
        toast.error('Password harus mengandung minimal 1 huruf besar, 1 huruf kecil, 1 angka, dan 1 karakter khusus')
        return
      }

      isChangingPassword.value = true
      try {
        const userId = userStore.currentUser?.id
        if (!userId) {
          toast.error('User ID tidak ditemukan. Silakan login kembali.')
          return
        }

        const changePasswordDto = {
          currentPassword: passwordForm.value.currentPassword,
          newPassword: passwordForm.value.newPassword,
          securityAnswer: passwordForm.value.securityAnswer
        }

        await changePassword(userId, changePasswordDto)
        toast.success('Password berhasil diubah! Silakan login kembali.')
        
        // Reset form
        passwordForm.value = {
          currentPassword: '',
          newPassword: '',
          confirmPassword: '',
          securityAnswer: ''
        }

        // Logout after 2 seconds
        setTimeout(() => {
          doLogout(() => {
            userStore.logout()
            router.push('/login')
          })
        }, 2000)
      } catch (error) {
        console.error('Error changing password:', error)
        // Handle different error response formats
        let errorMessage = 'Gagal mengubah password'
        
        if (error.response?.data) {
          // ApiResponse format: { message: "...", success: true/false }
          if (error.response.data.message) {
            errorMessage = error.response.data.message
          } 
          // Validation error format: { fieldName: "error message" }
          else if (typeof error.response.data === 'object') {
            const firstError = Object.values(error.response.data)[0]
            if (firstError) {
              errorMessage = Array.isArray(firstError) ? firstError[0] : firstError
            }
          }
        }
        
        toast.error(errorMessage)
      } finally {
        isChangingPassword.value = false
      }
    }

    // Initialize user data on mount
    onMounted(() => {
      userStore.initUser()
      // Debug: log photo path
      if (userStore.currentUser?.photo) {
        console.log('User photo path:', userStore.currentUser.photo)
        console.log('Photo URL:', getPhotoUrl(userStore.currentUser.photo))
      }
    })

    return {
      userStore,
      passwordForm,
      isChangingPassword,
      isUploadingPhoto,
      getRoleName,
      navbarComponent,
      getPhotoUrl,
      handlePhotoUpload,
      handleRemovePhoto,
      handleImageError,
      handleChangePassword
    }
  }
}
</script>

<style scoped>
.profile-page {
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

.profile-avatar {
  margin: 0 auto;
  position: relative;
  display: inline-block;
}

.profile-photo {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #3498db;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.profile-icon {
  font-size: 5rem;
  color: #3498db;
}

.photo-upload-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.profile-card {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: none;
}

.profile-card .card-body {
  padding: 2rem;
}
</style>

