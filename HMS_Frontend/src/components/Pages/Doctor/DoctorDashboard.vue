<template>
  <div class="doctor-dashboard">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarDoctor />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="dashboard-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-heart-pulse me-2"></i>Doctor Dashboard
            </h2>
            <p class="text-muted">Welcome, Dr. {{ userStore.currentUser?.firstName || 'Doctor' }}</p>
          </div>

          <div class="row mb-4">
            <div class="col-md-6 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">Today's Appointments</h6>
                      <h3 class="mb-0">{{ appointmentCount }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-calendar-check"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-6 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">Total Patients</h6>
                      <h3 class="mb-0">{{ patientCount }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-person-heart"></i>
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
                  <router-link to="/user/doctor/appointmentList" class="btn btn-primary btn-lg w-100">
                    <i class="bi bi-calendar-check me-2"></i>View Appointments
                  </router-link>
                </div>
                <div class="col-md-6 mb-3">
                  <router-link to="/user/doctor/selectSchedule" class="btn btn-outline-primary btn-lg w-100">
                    <i class="bi bi-calendar-event me-2"></i>Set Schedule
                  </router-link>
                </div>
                <div class="col-md-6 mb-3">
                  <router-link to="/user/doctor/resources" class="btn btn-outline-info btn-lg w-100">
                    <i class="bi bi-box-seam me-2"></i>View Resources
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
import { ref, onMounted } from 'vue'
import { useUserStore } from '../../../stores/user'
import { getPatientsByDoctor } from '../../../services/doctor'
import VerticalNavbarDoctor from './VerticalNavbarDoctor.vue'

export default {
  name: 'DoctorDashboard',
  components: {
    VerticalNavbarDoctor
  },
  setup() {
    const userStore = useUserStore()
    const appointmentCount = ref(0)
    const patientCount = ref(0)
    const loading = ref(true)

    const loadDashboardData = async () => {
      try {
        loading.value = true
        // Get doctor ID from user data
        const doctorId = userStore.currentUser?.employee?.doctor?.id
        if (doctorId) {
          const patients = await getPatientsByDoctor(doctorId)
          patientCount.value = patients?.length || 0
          // Count appointments from patients
          appointmentCount.value = patients?.filter(p => p.currentStatus)?.length || 0
        }
      } catch (error) {
        console.error('Error loading dashboard data:', error)
      } finally {
        loading.value = false
      }
    }

    onMounted(() => {
      loadDashboardData()
    })

    return {
      userStore,
      appointmentCount,
      patientCount,
      loading
    }
  }
}
</script>

<style scoped>
.doctor-dashboard {
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
