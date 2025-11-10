<template>
  <div class="container">
    <div id="carouselExample" class="carousel slide" data-bs-ride="carousel">
      <div class="carousel-inner">
        <div 
          v-for="(image, index) in images" 
          :key="index"
          :class="['carousel-item', { active: index === currentIndex }]"
        >
          <img
            :src="image"
            class="d-block w-100"
            :alt="`Slide ${index + 1}`"
            style="
              border-radius: 20px;
              border-width: 5px;
              height: 500px;
              width: 1500px;
              object-fit: cover;
            "
          />
        </div>
      </div>
      <button
        class="carousel-control-prev"
        type="button"
        data-bs-target="#carouselExample"
        data-bs-slide="prev"
      >
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button
        class="carousel-control-next"
        type="button"
        data-bs-target="#carouselExample"
        data-bs-slide="next"
      >
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import img1 from '../images/ak.jpg'
import img2 from '../images/dj.jpg'
import img3 from '../images/nh.jpg'

export default {
  name: 'Carousel',
  setup() {
    const currentIndex = ref(0)
    const images = [img1, img3, img2]

    onMounted(() => {
      // Initialize Bootstrap carousel
      const carouselElement = document.querySelector('#carouselExample')
      if (carouselElement) {
        // Bootstrap will handle the carousel automatically
      }
    })

    return {
      currentIndex,
      images
    }
  }
}
</script>

<style scoped>
.carousel {
  margin-bottom: 20px;
}
</style>

