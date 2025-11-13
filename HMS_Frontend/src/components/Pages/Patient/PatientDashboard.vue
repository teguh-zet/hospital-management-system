<template>
  <div class="patient-dashboard">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarPatient />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="dashboard-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-person-heart me-2"></i>Patient Dashboard
            </h2>
            <p class="text-muted">Welcome, {{ userStore.currentUser?.firstName || 'Patient' }}</p>
          </div>

          <div class="row mb-4">
            <div class="col-md-4 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">Total Appointments</h6>
                      <h3 class="mb-0">{{ appointmentCount }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-calendar-check"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-4 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">Health Records</h6>
                      <h3 class="mb-0">{{ healthHistoryCount }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-heart-pulse"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-4 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">Status</h6>
                      <h3 class="mb-0">
                        <span :class="patientStatus.class">{{ patientStatus.text }}</span>
                      </h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-info-circle"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">Quick Actions</h5>
            </div>
            <div class="card-body">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <router-link to="/user/patient/appointment" class="btn btn-primary btn-lg w-100">
                    <i class="bi bi-calendar-plus me-2"></i>Book New Appointment
                  </router-link>
                </div>
                <div class="col-md-6 mb-3">
                  <router-link to="/user/patient/appoHistory" class="btn btn-outline-primary btn-lg w-100">
                    <i class="bi bi-clock-history me-2"></i>View Appointment History
                  </router-link>
                </div>
                <div class="col-md-6 mb-3">
                  <router-link to="/user/patient/healthHistory" class="btn btn-outline-info btn-lg w-100">
                    <i class="bi bi-heart-pulse me-2"></i>View Health History
                  </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '../../../stores/user'
import { getPatientById } from '../../../services/patient'
import { getAppointmentHistory, getHealthHistory } from '../../../services/patient'
import VerticalNavbarPatient from './VerticalNavbarPatient.vue'

export default {
  name: 'PatientDashboard',
  components: {
    VerticalNavbarPatient
  },
  setup() {
    const userStore = useUserStore()
    const appointmentCount = ref(0)
    const healthHistoryCount = ref(0)
    const patientData = ref(null)
    const loading = ref(true)

    const patientStatus = computed(() => {
      if (!patientData.value) {
        return { text: 'Active', class: 'text-success' }
      }
      if (patientData.value.admitStatus) {
        return { text: 'Admitted', class: 'text-warning' }
      }
      return { text: 'Active', class: 'text-success' }
    })

    const loadPatientData = async () => {
      try {
        loading.value = true
        const userId = userStore.currentUser?.id
        if (userId) {
          // Get patient data
          const patient = await getPatientById(userId)
          patientData.value = patient

          // Get appointment history
          const appointments = await getAppointmentHistory(userId)
          appointmentCount.value = appointments?.length || 0

          // Get health history
          const healthHistory = await getHealthHistory(userId)
          healthHistoryCount.value = healthHistory?.length || 0
        }
      } catch (error) {
        console.error('Error loading patient data:', error)
      } finally {
        loading.value = false
      }
    }

    onMounted(() => {
      loadPatientData()
    })

    return {
      userStore,
      appointmentCount,
      healthHistoryCount,
      patientStatus,
      loading
    }
  }
}
</script>

<style scoped>
.patient-dashboard {
  min-height: 100vh;
  background: #f8f9fa;
}

.dashboard-header {
  margin-top: 20px;
}

.stat-card {
  border: none;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
}

.stat-icon {
  font-size: 3rem;
  color: #3498db;
  opacity: 0.3;
}

.text-gradient {
  background: linear-gradient(135deg, #3498db, #2980b9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
</style>
