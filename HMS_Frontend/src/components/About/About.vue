<template>
  <BaseCart>
    <Common name="About Page" visit="/contact" btname="Contact Now" />
  </BaseCart>
</template>

<script>
import BaseCart from '../Base/BaseCart.vue'
import Common from '../Common/Common.vue'

export default {
  name: 'About',
  components: {
    BaseCart,
    Common
  }
}
</script>

