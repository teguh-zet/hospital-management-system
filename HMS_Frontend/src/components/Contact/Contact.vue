<template>
  <BaseCart>
    <div class="my-5">
      <h1 class="text-center">Contact Us</h1>
    </div>
    <div class="container contact_div">
      <div class="row">
        <div class="col-md-6 col-10 mx-auto">
          <form @submit.prevent="formSubmit">
            <div class="mb-3">
              <label for="fullname" class="form-label">Full Name</label>
              <input
                type="text"
                class="form-control"
                id="fullname"
                name="fullname"
                v-model="data.fullname"
                placeholder="Enter Your Name"
              />
            </div>
            <div class="mb-3">
              <label for="phone" class="form-label">Phone Number</label>
              <input
                type="number"
                class="form-control"
                id="phone"
                name="phone"
                v-model="data.phone"
                placeholder="Enter Your Phone Number"
              />
            </div>
            <div class="mb-3">
              <label for="email" class="form-label">Email address</label>
              <input
                type="email"
                class="form-control"
                id="email"
                name="email"
                v-model="data.email"
                placeholder="name@example.com"
              />
            </div>
            <div class="mb-3">
              <label for="msg" class="form-label">Message</label>
              <textarea
                class="form-control"
                id="msg"
                rows="3"
                name="msg"
                v-model="data.msg"
              ></textarea>
            </div>
            <div class="col-12">
              <button class="btn btn-outline-primary" type="submit">
                Submit form
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </BaseCart>
</template>

<script>
import { ref } from 'vue'
import BaseCart from '../Base/BaseCart.vue'

export default {
  name: 'Contact',
  components: {
    BaseCart
  },
  setup() {
    const data = ref({
      fullname: '',
      phone: '',
      email: '',
      msg: ''
    })

    const formSubmit = (e) => {
      e.preventDefault()
      console.log(data.value)
    }

    return {
      data,
      formSubmit
    }
  }
}
</script>

