<template>
  <div>
    <Base>
      <br />
      <div class="container">
        <div class="row mt-5 mb-5">
          <div class="col-sm-6 offset-sm-3">
            <div class="card border-dark">
              <div class="card-header">
                <h1>Welcome Patient</h1>
                <h3>Fill Info to register</h3>
              </div>
              <div class="card-body">
                <form @submit.prevent="submitForm">
                  <div class="mb-3">
                    <label for="firstName" class="form-label">Enter Your First Name</label>
                    <input
                      type="text"
                      class="form-control"
                      placeholder="Enter Here"
                      id="firstName"
                      v-model="user.firstName"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="lastName" class="form-label">Enter Your Last Name</label>
                    <input
                      type="text"
                      class="form-control"
                      placeholder="Enter Here"
                      id="lastName"
                      v-model="user.lastName"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="email" class="form-label">Enter Your E-mail</label>
                    <input
                      type="email"
                      class="form-control"
                      placeholder="Enter Here"
                      id="email"
                      v-model="user.email"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="password" class="form-label">Enter Your Password</label>
                    <input
                      type="password"
                      class="form-control"
                      placeholder="Enter Here"
                      id="password"
                      v-model="user.password"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="gender" class="form-label">Gender</label>
                    <select class="form-control" id="gender" v-model="user.gender">
                      <option disabled value="">Select</option>
                      <option>Male</option>
                      <option>Female</option>
                      <option>Other</option>
                    </select>
                  </div>
                  <div class="mb-3">
                    <label for="securityQue" class="form-label">Enter Your Security Question</label>
                    <input
                      id="securityQue"
                      class="form-control"
                      placeholder="Enter Here"
                      type="text"
                      v-model="user.securityQue"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="securityAns" class="form-label">Enter Your Security Answer</label>
                    <input
                      id="securityAns"
                      class="form-control"
                      placeholder="Enter Here"
                      type="password"
                      v-model="user.securityAns"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="mobileNo" class="form-label">Enter Your Mobile Number</label>
                    <input
                      id="mobileNo"
                      class="form-control"
                      placeholder="Enter Here"
                      type="number"
                      v-model="user.mobileNo"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="bloodGroup" class="form-label">Enter Your Blood Group</label>
                    <input
                      id="bloodGroup"
                      class="form-control"
                      placeholder="Enter Here"
                      type="text"
                      v-model="user.bloodGroup"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="dob" class="form-label">Enter Your DOB</label>
                    <input
                      id="dob"
                      class="form-control"
                      placeholder="Enter Here"
                      type="date"
                      v-model="user.dob"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="plotNo" class="form-label">plotNo</label>
                    <input
                      id="plotNo"
                      class="form-control"
                      placeholder="Enter Here"
                      type="text"
                      v-model="address.plotNo"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="buildingName" class="form-label">buildingName</label>
                    <input
                      id="buildingName"
                      class="form-control"
                      placeholder="Enter Here"
                      type="text"
                      v-model="address.buildingName"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="areaName" class="form-label">areaName</label>
                    <input
                      id="areaName"
                      class="form-control"
                      placeholder="Enter Here"
                      type="text"
                      v-model="address.areaName"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="city" class="form-label">city</label>
                    <input
                      id="city"
                      class="form-control"
                      placeholder="Enter Here"
                      type="text"
                      v-model="address.city"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="state" class="form-label">state</label>
                    <input
                      id="state"
                      class="form-control"
                      placeholder="Enter Here"
                      type="text"
                      v-model="address.state"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="country" class="form-label">country</label>
                    <input
                      id="country"
                      class="form-control"
                      placeholder="Enter Here"
                      type="text"
                      v-model="address.country"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="pincode" class="form-label">pincode</label>
                    <input
                      id="pincode"
                      class="form-control"
                      placeholder="Enter Here"
                      type="number"
                      v-model="address.pincode"
                    />
                  </div>
                  <div class="text-center">
                    <button type="submit" class="btn btn-outline-primary">
                      Register
                    </button>
                    <button
                      type="button"
                      @click="resetData"
                      class="btn btn-outline-secondary ms-3"
                    >
                      Reset
                    </button>
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
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { singup } from '../../../services/user'
import Base from '../../Base/Base.vue'

export default {
  name: 'Signup',
  components: {
    Base
  },
  setup() {
    const router = useRouter()
    const toast = useToast()
    
    const address = ref({
      plotNo: "",
      buildingName: "",
      areaName: "",
      city: "",
      state: "",
      country: "",
      pincode: ""
    })

    const user = ref({
      firstName: "",
      lastName: "",
      email: "",
      password: "",
      gender: "",
      securityQue: "",
      securityAns: "",
      mobileNo: "",
      bloodGroup: "",
      dob: "",
      address: {},
      roles: []
    })

    const data = ref({
      admitStatus: false,
      currentStatus: false,
      user: {}
    })

    watch([address, user], () => {
      user.value.address = address.value
      data.value.user = user.value
    }, { deep: true })

    const resetData = () => {
      address.value = {
        plotNo: "",
        buildingName: "",
        areaName: "",
        city: "",
        state: "",
        country: "",
        pincode: ""
      }
      user.value = {
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        gender: "",
        securityQue: "",
        securityAns: "",
        mobileNo: "",
        bloodGroup: "",
        dob: "",
        address: {},
        roles: []
      }
      data.value = {
        admitStatus: false,
        currentStatus: false,
        user: {}
      }
    }

    const submitForm = async () => {
      console.log("Before send to Server")
      try {
        const response = await singup(data.value)
        console.log(response)
        console.log("Success LOG")
        toast.success("User Registred as " + response.id)
        router.push('/home')
        resetData()
      } catch (error) {
        console.log(error)
        console.log("error log")
        toast.error("Registration failed")
      }
    }

    return {
      address,
      user,
      data,
      resetData,
      submitForm
    }
  }
}
</script>

