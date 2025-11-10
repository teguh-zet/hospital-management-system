<template>
  <div>
    <Base>
      <br />
      <br />
      <div class="container">
        <div class="row mt-3">
          <div class="col-sm-6 offset-sm-3">
            <div class="card border-dark">
              <div class="card-header">
                <h3>Login Here</h3>
              </div>
              <div class="card-body">
                <form @submit.prevent="handleFormSubmit">
                  <div class="mb-3">
                    <label for="email" class="form-label">Enter Email</label>
                    <input
                      type="text"
                      class="form-control"
                      id="email"
                      autocomplete="current-password"
                      v-model="loginDetail.username"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="password" class="form-label">Enter Password</label>
                    <input
                      type="password"
                      class="form-control"
                      id="password"
                      v-model="loginDetail.password"
                    />
                  </div>
                  <div class="text-center">
                    <button type="submit" class="btn btn-outline-primary">
                      Login
                    </button>
                    <button
                      type="button"
                      class="btn btn-outline-secondary ms-2"
                      @click="handleReset"
                    >
                      Clear
                    </button>
                    <nav class="mt-2">
                      <router-link to="/forgot">Forgot Password ?</router-link>
                    </nav>
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

    const handleChange = (field, value) => {
      loginDetail.value[field] = value
    }

    const handleFormSubmit = async () => {
      console.log("Before sending data")
      console.log(loginDetail.value)

      if (
        loginDetail.value.username.trim() === '' ||
        loginDetail.value.password.trim() === ''
      ) {
        toast.error("Credentials is Required !!!!")
      } else {
        try {
          const data = await loginUser(loginDetail.value)
          console.log("User Logged in : ")
          console.log(data)
          toast.success("Logged In")

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
            console.log("login details saved to localstorage .. Here in Login")
            userStore.setUser(data)
            router.push(redirectPath)
          })
        } catch (error) {
          console.log(error)
          if (error.response && (error.response.status === 400 || error.response.status === 404)) {
            toast.error(error.response.data.message)
          } else {
            toast.error("Invalid Username or Password")
          }
        }
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
      handleChange,
      handleFormSubmit,
      handleReset
    }
  }
}
</script>

