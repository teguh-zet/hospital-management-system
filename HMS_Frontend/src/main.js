import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle'
import 'bootstrap-icons/font/bootstrap-icons.css'
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'

try {
  const app = createApp(App)
  const pinia = createPinia()

  app.use(pinia)
  app.use(router)
  app.use(Toast, {
    position: 'bottom-center',
    timeout: 5000,
    closeOnClick: true,
    pauseOnFocusLoss: true,
    pauseOnHover: true,
    draggable: true,
    draggablePercent: 0.6,
    showCloseButtonOnHover: false,
    hideProgressBar: true,
    closeButton: 'button',
    icon: true,
    rtl: false
  })

  app.mount('#app')
  console.log('Vue app mounted successfully')
} catch (error) {
  console.error('Error mounting Vue app:', error)
  document.getElementById('app').innerHTML = `
    <div style="padding: 20px; text-align: center;">
      <h1>Error Loading Application</h1>
      <p>${error.message}</p>
      <p>Please check the console for more details.</p>
    </div>
  `
}

