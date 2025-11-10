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
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.carousel-item img {
  transition: transform 0.5s ease;
}

.carousel-item:hover img {
  transform: scale(1.05);
}

.carousel-control-prev,
.carousel-control-next {
  width: 50px;
  height: 50px;
  background: rgba(52, 152, 219, 0.8);
  border-radius: 50%;
  top: 50%;
  transform: translateY(-50%);
  opacity: 0.8;
  transition: all 0.3s ease;
}

.carousel-control-prev:hover,
.carousel-control-next:hover {
  opacity: 1;
  background: rgba(52, 152, 219, 1);
}

.carousel-control-prev {
  left: 20px;
}

.carousel-control-next {
  right: 20px;
}
</style>

