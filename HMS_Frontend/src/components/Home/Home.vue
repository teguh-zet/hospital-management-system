<template>
  <BaseCart>
    <div class="container-fluid">
      <CreateCarousel />
      <Common
        name="Home Page"
        :imgsrc="web"
        visit="/login"
        btname="Get Started"
      />
      <div class="card px-5">
        <div class="card-header">
          <h2>Mission</h2>
        </div>
        <div class="card-body">
          <b>
            <p class="px-5">
              We are committed to maintain the highest standard of care and
              respond to the needs of the community in a compassionate
              manner.To provide state-of-the-art, high quality and
              cost-effective healthcare services and latest information to
              improve and maintain health for the well-being of the
              community. To unrelentingly pursue the creation of value for
              our customers, employees and society at large.To foster a
              therapeutic relationship based on compassion that is felt,
              quality that is measurable and cost that is affordable. To
              become partners in health promotion with every section of
              society.
            </p>
          </b>
        </div>
      </div>
    </div>
  </BaseCart>
</template>

<script>
import BaseCart from '../Base/BaseCart.vue'
import Common from '../Common/Common.vue'
import CreateCarousel from '../Carousel.vue'
import web from '../../images/nh.jpg'

export default {
  name: 'Home',
  components: {
    BaseCart,
    Common,
    CreateCarousel
  },
  setup() {
    return {
      web
    }
  }
}
</script>

