<template>
  <div class="container-fluid p-0 m-0">
    <Navbar />
    <slot />
    <Footer />
  </div>
</template>

<script>
import Footer from '../Footer/Footer.vue'
import Navbar from '../Navbar/Navbar.vue'

export default {
  name: 'BaseCart',
  components: {
    Footer,
    Navbar
  }
}
</script>

