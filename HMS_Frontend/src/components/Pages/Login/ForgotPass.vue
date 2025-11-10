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
                <h3>Forgot Password</h3>
              </div>
              <div class="card-body">
                <form>
                  <div class="mb-3">
                    <label for="email" class="form-label">Enter Email</label>
                    <input
                      type="text"
                      class="form-control"
                      id="email"
                      v-model="email"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="firstName" class="form-label">User Name</label>
                    <input id="firstName" class="form-control" :value="user.firstName" disabled />
                  </div>
                  <div class="mb-3">
                    <label for="securityQue" class="form-label">Security Question</label>
                    <input
                      type="text"
                      class="form-control"
                      id="securityQue"
                      :value="user.securityQue"
                      disabled
                    />
                  </div>
                  <div class="mb-3">
                    <label for="key" class="form-label">
                      Enter Correct Security Answer
                    </label>
                    <input
                      type="text"
                      class="form-control"
                      id="key"
                      v-model="key"
                    />
                  </div>
                  <div v-if="key === user.securityAns" class="mb-3">
                    <label for="password" class="form-label">Enter New Password</label>
                    <input
                      type="password"
                      class="form-control"
                      id="password"
                      v-model="password"
                    />
                  </div>
                  <div class="text-center">
                    <button
                      type="button"
                      class="btn btn-outline-primary"
                      :disabled="!email"
                      @click="getTheUserServer"
                    >
                      Get User Details
                    </button>
                    <button
                      v-if="key === user.securityAns"
                      type="button"
                      class="btn btn-outline-danger ms-2"
                      @click="forgotp"
                    >
                      Forgot
                    </button>
                    <button
                      type="button"
                      class="btn btn-outline-secondary ms-2"
                      @click="resetData"
                    >
                      Clear
                    </button>
                    <nav class="mt-2">
                      <router-link to="/login">Login Now</router-link>
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
import { useToast } from 'vue-toastification'
import { getTheUser, setNewPassword } from '../../../services/user'
import Base from '../../Base/Base.vue'

export default {
  name: 'ForgotPass',
  components: {
    Base
  },
  setup() {
    const toast = useToast()
    
    const email = ref("")
    const key = ref("")
    const password = ref("")
    const user = ref({
      email: "",
      firstName: "",
      securityQue: "",
      securityAns: "",
      password: ""
    })

    const resetData = () => {
      user.value = {
        email: "",
        firstName: "",
        securityQue: "",
        securityAns: "",
        password: ""
      }
      email.value = ""
      key.value = ""
      password.value = ""
    }

    const getTheUserServer = async (event) => {
      event.preventDefault()
      try {
        const data = await getTheUser(email.value)
        user.value = {
          email: data.email,
          firstName: data.firstName,
          securityQue: data.securityQue,
          securityAns: data.securityAns,
        }
      } catch (error) {
        toast.error("User not found")
      }
    }

    const forgotp = async (event) => {
      event.preventDefault()
      try {
        await setNewPassword(email.value, password.value)
        resetData()
        toast.success("Password Reset Successfully")
      } catch (error) {
        toast.error("Failed to reset password")
      }
    }

    return {
      email,
      key,
      password,
      user,
      resetData,
      getTheUserServer,
      forgotp
    }
  }
}
</script>

