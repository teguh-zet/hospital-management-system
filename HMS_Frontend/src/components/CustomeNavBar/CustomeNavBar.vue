<template>
  <div>
    <nav class="navbar navbar-expand-sm navbar-dark bg-dark fixed-top px-4 mb">
      <router-link class="navbar-brand" to="/">Hospital Management System</router-link>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
        @click="toggle"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div :class="['collapse navbar-collapse', { show: isOpen }]" id="navbarNav">
        <ul class="navbar-nav me-auto"></ul>
        <ul class="navbar-nav">
          <li v-if="!login" class="nav-item">
            <router-link class="nav-link" to="/login">Login</router-link>
          </li>
          <li v-if="!login" class="nav-item">
            <router-link class="nav-link" to="/signUp">SignUp</router-link>
          </li>
          <li v-if="login" class="nav-item">
            <a class="nav-link" href="#" @click.prevent="handleLogout">Logout</a>
          </li>
        </ul>
      </div>
    </nav>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { doLogout, isLoggedIn, getCurrentUserDetail } from '../../utils/auth'

export default {
  name: 'CustomeNavBar',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const isOpen = ref(false)
    const login = ref(false)
    const user = ref(null)

    const initAuth = () => {
      login.value = isLoggedIn()
      user.value = getCurrentUserDetail()
    }

    onMounted(() => {
      initAuth()
      userStore.initUser()
    })

    watch(() => userStore.login, (newVal) => {
      login.value = newVal
      if (newVal) {
        user.value = userStore.currentUser
      }
    })

    const toggle = () => {
      isOpen.value = !isOpen.value
    }

    const handleLogout = () => {
      doLogout(() => {
        login.value = false
        user.value = null
        userStore.logout()
        router.push('/')
      })
    }

    return {
      isOpen,
      login,
      user,
      toggle,
      handleLogout
    }
  }
}
</script>

<style scoped>
.navbar {
  margin-bottom: 20px;
}
</style>

