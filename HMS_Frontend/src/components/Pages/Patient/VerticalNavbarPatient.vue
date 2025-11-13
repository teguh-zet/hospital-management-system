<template>
  <nav class="navbar-nav bg-dark sidebar" style="min-height: 100vh; width: 250px; padding-top: 20px; display: flex; flex-direction: column;">
    <div class="nav flex-column" style="flex: 1;">
      <router-link to="/user/patient/dashboard" class="nav-link text-white" active-class="active">
        <i class="bi bi-speedometer2 me-2"></i>Dashboard
      </router-link>
      <router-link to="/user/patient/appointment" class="nav-link text-white" active-class="active">
        <i class="bi bi-calendar-plus me-2"></i>Book Appointment
      </router-link>
      <router-link to="/user/patient/appoHistory" class="nav-link text-white" active-class="active">
        <i class="bi bi-clock-history me-2"></i>Appointment History
      </router-link>
      <router-link to="/user/patient/healthHistory" class="nav-link text-white" active-class="active">
        <i class="bi bi-heart-pulse me-2"></i>Health History
      </router-link>
      <router-link to="/user/profile" class="nav-link text-white" active-class="active">
        <i class="bi bi-person-circle me-2"></i>My Profile
      </router-link>
      <div class="nav-link text-white mt-auto" style="margin-top: auto; padding-top: 20px; border-top: 1px solid rgba(255,255,255,0.1);">
        <a href="#" class="text-white text-decoration-none" @click.prevent="handleLogout">
          <i class="bi bi-box-arrow-right me-2"></i>Logout
        </a>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRouter } from 'vue-router'
import { useUserStore } from '../../../stores/user'
import { doLogout } from '../../../utils/auth'
import { useToast } from 'vue-toastification'

export default {
  name: 'VerticalNavbarPatient',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const toast = useToast()

    const handleLogout = () => {
      doLogout(() => {
        userStore.logout()
        toast.success('Logout berhasil')
        router.push('/')
      })
    }

    return {
      handleLogout
    }
  }
}
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
}

.nav-link {
  padding: 12px 20px;
  transition: all 0.3s ease;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.1);
  transform: translateX(5px);
}

.nav-link.active {
  background-color: #3498db;
  font-weight: 600;
}
</style>

