<template>
  <BaseCart>
    <div class="home-page">
      <CreateCarousel />
      <Common
        name="Home Page"
        :imgsrc="web"
        visit="/login"
        btname="Get Started"
      />
      <div class="container my-5">
        <div class="row justify-content-center">
          <div class="col-lg-10">
            <div class="card mission-card fade-in">
              <div class="card-header">
                <h2 class="mb-0">
                  <i class="bi bi-heart-pulse me-2"></i>Our Mission
                </h2>
              </div>
              <div class="card-body">
                <div class="mission-content">
                  <p>
                    Kami berkomitmen untuk mempertahankan standar perawatan tertinggi dan
                    merespons kebutuhan komunitas dengan penuh kasih sayang. Menyediakan
                    layanan kesehatan yang canggih, berkualitas tinggi, dan terjangkau
                    serta informasi terbaru untuk meningkatkan dan mempertahankan kesehatan
                    demi kesejahteraan komunitas.
                  </p>
                  <p class="mt-3">
                    Kami secara konsisten mengejar penciptaan nilai bagi pelanggan, karyawan,
                    dan masyarakat luas. Membina hubungan terapeutik berdasarkan kasih sayang
                    yang dirasakan, kualitas yang terukur, dan biaya yang terjangkau. Menjadi
                    mitra dalam promosi kesehatan dengan setiap bagian masyarakat.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Features Section -->
        <div class="row mt-5 mb-5">
          <div class="col-md-4 mb-4">
            <div class="feature-card fade-in">
              <div class="feature-icon">
                <i class="bi bi-hospital"></i>
              </div>
              <h4>Fasilitas Modern</h4>
              <p>Dilengkapi dengan peralatan medis terbaru dan teknologi canggih</p>
            </div>
          </div>
          <div class="col-md-4 mb-4">
            <div class="feature-card fade-in">
              <div class="feature-icon">
                <i class="bi bi-people"></i>
              </div>
              <h4>Tim Profesional</h4>
              <p>Dokter dan tenaga medis berpengalaman dan terpercaya</p>
            </div>
          </div>
          <div class="col-md-4 mb-4">
            <div class="feature-card fade-in">
              <div class="feature-icon">
                <i class="bi bi-clock-history"></i>
              </div>
              <h4>Pelayanan 24/7</h4>
              <p>Siap melayani Anda kapan saja, siang maupun malam</p>
            </div>
          </div>
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

<style scoped>
.home-page {
  background: #f8f9fa;
}

.mission-card {
  margin-top: 2rem;
  border: none;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.mission-card .card-header {
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  padding: 1.5rem 2rem;
  border-radius: 12px 12px 0 0;
}

.mission-card .card-body {
  padding: 2.5rem;
}

.mission-content p {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #555;
  text-align: justify;
}

.feature-card {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  height: 100%;
  border: 1px solid #e9ecef;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
}

.feature-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 1.5rem;
  background: linear-gradient(135deg, #3498db, #2980b9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  color: white;
}

.feature-card h4 {
  color: #2c3e50;
  font-weight: 600;
  margin-bottom: 1rem;
}

.feature-card p {
  color: #7f8c8d;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .mission-card .card-body {
    padding: 1.5rem;
  }
  
  .mission-content p {
    font-size: 1rem;
  }
  
  .feature-card {
    padding: 1.5rem;
  }
}
</style>

