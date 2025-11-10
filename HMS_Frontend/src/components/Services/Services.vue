<template>
  <BaseCart>
    <div class="my-5">
      <h1 class="text-center">Our Services</h1>
    </div>
    <div class="container-fluid mb-5">
      <div class="row">
        <div class="col-10 mx-auto">
          <div class="row gy-4">
            <Card
              v-for="(val, ind) in Sdata"
              :key="ind"
              :imgsrc="val.imgsrc"
              :title="val.title"
              :desc="val.desc"
            />
          </div>
        </div>
      </div>
    </div>
  </BaseCart>
</template>

<script>
import BaseCart from '../Base/BaseCart.vue'
import Card from '../Card/Card.vue'
import Sdata from '../Sdata/Sdata.js'

export default {
  name: 'Services',
  components: {
    BaseCart,
    Card
  },
  setup() {
    return {
      Sdata
    }
  }
}
</script>

