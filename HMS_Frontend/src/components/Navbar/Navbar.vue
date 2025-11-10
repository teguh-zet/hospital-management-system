<template>
  <div class="container-fluid nav_bg">
    <div class="row">
      <div class="col-10 mx-auto">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
          <div class="container-fluid">
            <router-link class="navbar-brand" to="/">Hospital Management System</router-link>
            <button 
              class="navbar-toggler" 
              type="button" 
              data-bs-toggle="collapse" 
              data-bs-target="#navbarSupportedContent" 
              aria-controls="navbarSupportedContent" 
              aria-expanded="false" 
              aria-label="Toggle navigation"
            >
              <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <li class="nav-item">
                  <router-link 
                    :class="['nav-link', { active: $route.path === '/' }]"
                    to="/"
                  >
                    Home
                  </router-link>
                </li>
                <li class="nav-item">
                  <router-link 
                    :class="['nav-link', { active: $route.path === '/services' }]"
                    to="/services"
                  >
                    Services
                  </router-link>
                </li>
                <li class="nav-item">
                  <router-link 
                    :class="['nav-link', { active: $route.path === '/about' }]"
                    to="/about"
                  >
                    About
                  </router-link>
                </li>
                <li class="nav-item">
                  <router-link 
                    :class="['nav-link', { active: $route.path === '/contact' }]"
                    to="/contact"
                  >
                    Contact
                  </router-link>
                </li>
              </ul>
            </div>
          </div>
        </nav>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Navbar'
}
</script>

<style scoped>
.menu_active {
  font-weight: bold;
  border-bottom: 1px solid #565387;
}
</style>

