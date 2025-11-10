<template>
  <div class="container-fluid p-0 m-0">
    <CustomeNavBar />
    <br/><br/>
    <slot />
    <br/><br/><br/>
    <Foot />
  </div>
</template>

<script>
import CustomeNavBar from '../CustomeNavBar/CustomeNavBar.vue'
import Foot from '../Home/Footer.vue'

export default {
  name: 'Base',
  components: {
    CustomeNavBar,
    Foot
  }
}
</script>

