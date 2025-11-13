<template>
  <div class="appoint-patient-list-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarDoctor />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-calendar-check me-2"></i>Appointment List
            </h2>
          </div>

          <div class="card" v-if="!loading && patients.length > 0">
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>Patient Name</th>
                      <th>Email</th>
                      <th>Appointment Date</th>
                      <th>Symptoms</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="patient in patients" :key="patient.id">
                      <td>{{ patient.user?.firstName }} {{ patient.user?.lastName }}</td>
                      <td>{{ patient.user?.email }}</td>
                      <td>
                        <span v-if="patient.health_history && patient.health_history.length > 0">
                          {{ formatDate(patient.health_history[0].appointmentDate) }}
                        </span>
                        <span v-else>-</span>
                      </td>
                      <td>
                        <span v-if="patient.health_history && patient.health_history.length > 0">
                          {{ patient.health_history[0].symptoms || '-' }}
                        </span>
                        <span v-else>-</span>
                      </td>
                      <td>
                        <span v-if="patient.health_history && patient.health_history.length > 0">
                          <span :class="getStatusClass(patient.health_history[0])">
                            {{ getStatusText(patient.health_history[0]) }}
                          </span>
                        </span>
                        <span v-else>-</span>
                      </td>
                      <td>
                        <button
                          v-if="patient.health_history && patient.health_history.length > 0 && 
                                patient.health_history[0].currentStatus && !patient.health_history[0].admitStatus"
                          class="btn btn-sm btn-success me-2"
                          @click="approveAppointment(patient.health_history[0])"
                        >
                          <i class="bi bi-check-circle me-1"></i>Approve
                        </button>
                        <button
                          v-if="patient.health_history && patient.health_history.length > 0 && 
                                patient.health_history[0].currentStatus && !patient.health_history[0].admitStatus"
                          class="btn btn-sm btn-danger"
                          @click="rejectAppointment(patient.health_history[0])"
                        >
                          <i class="bi bi-x-circle me-1"></i>Reject
                        </button>
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
              <p class="text-muted mt-3">No appointments found</p>
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
import { getPatientsByDoctor, updateHealthHistory } from '../../../services/doctor'
import VerticalNavbarDoctor from './VerticalNavbarDoctor.vue'

export default {
  name: 'AppointPatientList',
  components: {
    VerticalNavbarDoctor
  },
  setup() {
    const toast = useToast()
    const userStore = useUserStore()
    const patients = ref([])
    const loading = ref(true)

    const loadPatients = async () => {
      try {
        loading.value = true
        const doctorId = userStore.currentUser?.employee?.doctor?.id
        if (doctorId) {
          const data = await getPatientsByDoctor(doctorId)
          patients.value = data || []
        }
      } catch (error) {
        console.error('Error loading patients:', error)
        toast.error('Gagal memuat daftar appointment')
      } finally {
        loading.value = false
      }
    }

    const approveAppointment = async (healthHistory) => {
      try {
        await updateHealthHistory(healthHistory, true)
        toast.success('Appointment berhasil disetujui')
        loadPatients()
      } catch (error) {
        console.error('Error approving appointment:', error)
        toast.error('Gagal menyetujui appointment')
      }
    }

    const rejectAppointment = async (healthHistory) => {
      if (!confirm('Apakah Anda yakin ingin menolak appointment ini?')) {
        return
      }
      try {
        await updateHealthHistory(healthHistory, false)
        toast.success('Appointment ditolak')
        loadPatients()
      } catch (error) {
        console.error('Error rejecting appointment:', error)
        toast.error('Gagal menolak appointment')
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
      if (history.currentStatus) return 'Pending'
      return 'Completed'
    }

    const getStatusClass = (history) => {
      if (history.admitStatus) return 'badge bg-warning'
      if (history.currentStatus) return 'badge bg-info'
      return 'badge bg-secondary'
    }

    onMounted(() => {
      loadPatients()
    })

    return {
      patients,
      loading,
      approveAppointment,
      rejectAppointment,
      formatDate,
      getStatusText,
      getStatusClass
    }
  }
}
</script>

<style scoped>
.appoint-patient-list-page {
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
