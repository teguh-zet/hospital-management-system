<template>
  <div class="login-page">
    <Base>
      <div class="container">
        <div class="row justify-content-center align-items-center min-vh-100 py-5">
          <div class="col-md-6 col-lg-5">
            <div class="card login-card fade-in">
              <div class="card-header text-center">
                <h3 class="mb-0">
                  <i class="bi bi-box-arrow-in-right me-2"></i>
                  Login Here
                </h3>
                <p class="text-white-50 mb-0 mt-2">Welcome back! Please login to your account</p>
              </div>
              <div class="card-body p-4">
                <form @submit.prevent="handleFormSubmit">
                  <div class="mb-4">
                    <label for="email" class="form-label">
                      <i class="bi bi-envelope me-2"></i>Email Address
                    </label>
                    <input
                      type="email"
                      class="form-control"
                      id="email"
                      placeholder="Enter your email"
                      autocomplete="email"
                      v-model="loginDetail.username"
                      required
                    />
                  </div>
                  <div class="mb-4">
                    <label for="password" class="form-label">
                      <i class="bi bi-lock me-2"></i>Password
                    </label>
                    <input
                      type="password"
                      class="form-control"
                      id="password"
                      placeholder="Enter your password"
                      v-model="loginDetail.password"
                      required
                    />
                  </div>
                  <div class="d-grid gap-2 mb-3">
                    <button 
                      type="submit" 
                      class="btn btn-primary btn-lg"
                      :disabled="isLoading"
                    >
                      <span v-if="!isLoading">
                        <i class="bi bi-box-arrow-in-right me-2"></i>Login
                      </span>
                      <span v-else>
                        <span class="spinner-border spinner-border-sm me-2" role="status"></span>
                        Logging in...
                      </span>
                    </button>
                    <button
                      type="button"
                      class="btn btn-outline-secondary"
                      @click="handleReset"
                      :disabled="isLoading"
                    >
                      <i class="bi bi-arrow-clockwise me-2"></i>Clear
                    </button>
                  </div>
                  <div class="text-center">
                    <router-link 
                      to="/forgot" 
                      class="text-decoration-none text-primary"
                    >
                      <i class="bi bi-question-circle me-1"></i>Forgot Password?
                    </router-link>
                  </div>
                  <div class="text-center mt-3">
                    <small class="text-muted">
                      Don't have an account? 
                      <router-link to="/signUp" class="text-primary text-decoration-none">
                        Sign Up
                      </router-link>
                    </small>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Base>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { useUserStore } from '../../../stores/user'
import { loginUser } from '../../../services/user'
import { doLogin } from '../../../utils/auth'
import Base from '../../Base/Base.vue'

export default {
  name: 'Login',
  components: {
    Base
  },
  setup() {
    const router = useRouter()
    const toast = useToast()
    const userStore = useUserStore()
    
    const loginDetail = ref({
      username: '',
      password: ''
    })

    const isLoading = ref(false)

    const handleFormSubmit = async () => {
      if (
        loginDetail.value.username.trim() === '' ||
        loginDetail.value.password.trim() === ''
      ) {
        toast.error("Email dan Password wajib diisi!")
        return
      }

      isLoading.value = true
      
      try {
        const data = await loginUser(loginDetail.value)
        
        // Validate response data
        if (!data || !data.user) {
          toast.error("Response tidak valid dari server")
          return
        }
        
        // Check if user has roles
        if (!data.user.roles || data.user.roles.length === 0) {
          toast.error("User tidak memiliki role. Silakan hubungi admin.")
          return
        }
        
        toast.success("Login berhasil! Selamat datang kembali.")
        
        const roleId = data.user.roles[0].id
        let redirectPath = '/'
        
        if (roleId === 500) {
          redirectPath = '/user/admin/dashboard'
        } else if (roleId === 501) {
          redirectPath = '/user/doctor/dashboard'
        } else if (roleId === 502) {
          redirectPath = '/user/patient/dashboard'
        } else if (roleId === 503) {
          redirectPath = '/user/receptionist/dashboard'
        } else if (roleId === 504) {
          redirectPath = '/user/accountant/dashboard'
        }

        doLogin(data, () => {
          userStore.setUser(data)
          router.push(redirectPath)
        })
      } catch (error) {
        console.error('Login error:', error)
        console.error('Error details:', {
          message: error.message,
          response: error.response,
          status: error.response?.status,
          data: error.response?.data
        })
        
        // Handle different error types
        if (error.response) {
          // Server responded with error status
          const status = error.response.status
          const errorData = error.response.data
          
          if (status === 401 || status === 403) {
            toast.error(errorData?.message || "Email atau password salah")
          } else if (status === 400) {
            toast.error(errorData?.message || "Request tidak valid")
          } else if (status === 404) {
            toast.error("Endpoint tidak ditemukan. Pastikan backend berjalan.")
          } else if (status === 500) {
            toast.error("Server error. Silakan coba lagi nanti.")
          } else {
            toast.error(errorData?.message || `Error ${status}: Terjadi kesalahan`)
          }
        } else if (error.request) {
          // Request was made but no response received
          toast.error("Tidak dapat terhubung ke server. Pastikan backend berjalan di http://localhost:9090")
        } else {
          // Error setting up request
          toast.error("Terjadi kesalahan saat mengirim request: " + error.message)
        }
      } finally {
        isLoading.value = false
      }
    }

    const handleReset = () => {
      loginDetail.value = {
        username: '',
        password: ''
      }
    }

    return {
      loginDetail,
      isLoading,
      handleFormSubmit,
      handleReset
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 2rem 0;
}

.login-card {
  border: none;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
}

.login-card .card-header {
  background: linear-gradient(135deg, #3498db, #2980b9);
  padding: 2rem;
}

.login-card .card-body {
  background: #fff;
}

.form-control:focus {
  border-color: #3498db;
  box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
}

.btn-primary {
  background: linear-gradient(135deg, #3498db, #2980b9);
  border: none;
  padding: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(52, 152, 219, 0.3);
}

.btn-primary:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.spinner-border-sm {
  width: 1rem;
  height: 1rem;
}

@media (max-width: 768px) {
  .login-page {
    padding: 1rem;
  }
  
  .login-card .card-header {
    padding: 1.5rem;
  }
  
  .login-card .card-body {
    padding: 1.5rem;
  }
}
</style>

