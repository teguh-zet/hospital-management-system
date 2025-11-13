<template>
  <div class="health-history-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarPatient />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-heart-pulse me-2"></i>Health History
            </h2>
          </div>

          <div class="card" v-if="!loading && healthHistory.length > 0">
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>Date</th>
                      <th>Symptoms</th>
                      <th>Diagnosis</th>
                      <th>Treatment</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="history in healthHistory" :key="history.id">
                      <td>{{ formatDate(history.appointmentDate) }}</td>
                      <td>{{ history.symptoms || '-' }}</td>
                      <td>{{ history.diagnosis || '-' }}</td>
                      <td>{{ history.treatment || '-' }}</td>
                      <td>
                        <span :class="getStatusClass(history)">
                          {{ getStatusText(history) }}
                        </span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <div class="card" v-else-if="loading">
            <div class="card-body text-center">
              <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
            </div>
          </div>

          <div class="card" v-else>
            <div class="card-body text-center">
              <i class="bi bi-heart-pulse" style="font-size: 3rem; color: #ccc;"></i>
              <p class="text-muted mt-3">No health history found</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import { useUserStore } from '../../../stores/user'
import { getHealthHistory } from '../../../services/patient'
import VerticalNavbarPatient from './VerticalNavbarPatient.vue'

export default {
  name: 'HealthHistory',
  components: {
    VerticalNavbarPatient
  },
  setup() {
    const toast = useToast()
    const userStore = useUserStore()
    const healthHistory = ref([])
    const loading = ref(true)

    const loadHealthHistory = async () => {
      try {
        loading.value = true
        const patientId = userStore.currentUser?.patient?.id || userStore.currentUser?.id
        if (patientId) {
          const data = await getHealthHistory(patientId)
          healthHistory.value = data || []
        }
      } catch (error) {
        console.error('Error loading health history:', error)
        toast.error('Gagal memuat riwayat kesehatan')
      } finally {
        loading.value = false
      }
    }

    const formatDate = (dateString) => {
      if (!dateString) return '-'
      const date = new Date(dateString)
      return date.toLocaleDateString('id-ID', { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
      })
    }

    const getStatusText = (history) => {
      if (history.admitStatus) return 'Admitted'
      if (history.currentStatus) return 'Active'
      return 'Completed'
    }

    const getStatusClass = (history) => {
      if (history.admitStatus) return 'badge bg-warning'
      if (history.currentStatus) return 'badge bg-success'
      return 'badge bg-secondary'
    }

    onMounted(() => {
      loadHealthHistory()
    })

    return {
      healthHistory,
      loading,
      formatDate,
      getStatusText,
      getStatusClass
    }
  }
}
</script>

<style scoped>
.health-history-page {
  min-height: 100vh;
  background: #f8f9fa;
}

.page-header {
  margin-top: 20px;
}

.text-gradient {
  background: linear-gradient(135deg, #3498db, #2980b9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.table {
  background: white;
}
</style>
