<template>
  <div class="appointment-history-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarPatient />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-clock-history me-2"></i>Appointment History
            </h2>
          </div>

          <div class="card" v-if="!loading && appointments.length > 0">
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>Date</th>
                      <th>Time</th>
                      <th>Symptoms</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="appointment in appointments" :key="appointment.id">
                      <td>{{ formatDate(appointment.appointmentDate) }}</td>
                      <td>{{ appointment.appointmentTime }}</td>
                      <td>{{ appointment.symptoms }}</td>
                      <td>
                        <span :class="getStatusClass(appointment)">
                          {{ getStatusText(appointment) }}
                        </span>
                      </td>
                      <td>
                        <button
                          v-if="appointment.currentStatus && !appointment.admitStatus"
                          class="btn btn-sm btn-danger"
                          @click="cancelAppointment(appointment.id)"
                        >
                          <i class="bi bi-x-circle me-1"></i>Cancel
                        </button>
                        <span v-else class="text-muted">-</span>
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
              <i class="bi bi-calendar-x" style="font-size: 3rem; color: #ccc;"></i>
              <p class="text-muted mt-3">No appointment history found</p>
              <router-link to="/user/patient/appointment" class="btn btn-primary">
                <i class="bi bi-calendar-plus me-2"></i>Book New Appointment
              </router-link>
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
import { getAppointmentHistory, cancelAppointment as cancelAppt } from '../../../services/patient'
import VerticalNavbarPatient from './VerticalNavbarPatient.vue'

export default {
  name: 'AppointmentHistory',
  components: {
    VerticalNavbarPatient
  },
  setup() {
    const toast = useToast()
    const userStore = useUserStore()
    const appointments = ref([])
    const loading = ref(true)

    const loadAppointments = async () => {
      try {
        loading.value = true
        const patientId = userStore.currentUser?.patient?.id || userStore.currentUser?.id
        if (patientId) {
          const data = await getAppointmentHistory(patientId)
          appointments.value = data || []
        }
      } catch (error) {
        console.error('Error loading appointments:', error)
        toast.error('Gagal memuat riwayat appointment')
      } finally {
        loading.value = false
      }
    }

    const cancelAppointment = async (appointmentId) => {
      if (!confirm('Apakah Anda yakin ingin membatalkan appointment ini?')) {
        return
      }

      try {
        await cancelAppt(appointmentId)
        toast.success('Appointment berhasil dibatalkan')
        loadAppointments()
      } catch (error) {
        console.error('Error canceling appointment:', error)
        toast.error('Gagal membatalkan appointment')
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

    const getStatusText = (appointment) => {
      if (appointment.admitStatus) return 'Admitted'
      if (appointment.currentStatus) return 'Active'
      return 'Completed'
    }

    const getStatusClass = (appointment) => {
      if (appointment.admitStatus) return 'badge bg-warning'
      if (appointment.currentStatus) return 'badge bg-success'
      return 'badge bg-secondary'
    }

    onMounted(() => {
      loadAppointments()
    })

    return {
      appointments,
      loading,
      cancelAppointment,
      formatDate,
      getStatusText,
      getStatusClass
    }
  }
}
</script>

<style scoped>
.appointment-history-page {
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
