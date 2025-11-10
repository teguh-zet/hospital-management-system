<template>
  <BaseCa>
    <div class="col-md-4 col-10 mx-auto">
      <div class="card">
        <img
          :src="imgsrc"
          class="card-img-top"
          :alt="imgsrc"
        />
        <div class="card-body">
          <h5 class="card-title font-weight-bold">{{ title }}</h5>
          <h5 class="card-title font-weight-bold">{{ desc }}</h5>
          <p class="card-text"></p>
          <router-link to="/login" class="btn btn-primary">
            Book Appointment
          </router-link>
        </div>
      </div>
    </div>
  </BaseCa>
</template>

<script>
import BaseCa from '../Base/BaseCa.vue'

export default {
  name: 'Card',
  components: {
    BaseCa
  },
  props: {
    imgsrc: String,
    title: String,
    desc: String
  }
}
</script>

