<template>
  <footer class="footer-modern">
    <div class="container">
      <div class="row">
        <div class="col-md-6 text-center text-md-start mb-3 mb-md-0">
          <h5 class="mb-2">
            <i class="bi bi-hospital me-2"></i>Hospital Management System
          </h5>
          <p class="mb-0 text-white-50">
            Sistem manajemen rumah sakit terintegrasi untuk pelayanan kesehatan yang lebih baik
          </p>
        </div>
        <div class="col-md-6 text-center text-md-end">
          <div class="social-links mb-3">
            <a href="#" class="social-link"><i class="bi bi-facebook"></i></a>
            <a href="#" class="social-link"><i class="bi bi-twitter"></i></a>
            <a href="#" class="social-link"><i class="bi bi-instagram"></i></a>
            <a href="#" class="social-link"><i class="bi bi-linkedin"></i></a>
          </div>
          <p class="mb-0 text-white-50">
            &copy; 2024 HMS. All Rights Reserved | 
            <a href="#" class="text-white-50 text-decoration-none">Terms and Conditions</a>
          </p>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped>
.footer-modern {
  background: linear-gradient(135deg, #2c3e50, #34495e);
  color: #fff;
  padding: 3rem 0 2rem;
  margin-top: 4rem;
}

.footer-modern h5 {
  color: #fff;
  font-weight: 600;
}

.social-links {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

@media (min-width: 768px) {
  .social-links {
    justify-content: flex-end;
  }
}

.social-link {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  color: #fff;
  text-decoration: none;
  transition: all 0.3s ease;
  font-size: 1.2rem;
}

.social-link:hover {
  background: #3498db;
  transform: translateY(-3px);
  color: #fff;
}

.footer-modern a:hover {
  color: #3498db !important;
}
</style>

