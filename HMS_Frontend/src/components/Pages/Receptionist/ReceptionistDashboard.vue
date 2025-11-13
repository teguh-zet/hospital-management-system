<template>
  <div class="receptionist-dashboard">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarReceptionist />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="dashboard-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-reception-4 me-2"></i>Receptionist Dashboard
            </h2>
            <p class="text-muted">Welcome, {{ userStore.currentUser?.firstName || 'Receptionist' }}</p>
          </div>

          <div class="row mb-4">
            <div class="col-md-3 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">Total Patients</h6>
                      <h3 class="mb-0">{{ totalPatients }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-people"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-3 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">Appointments</h6>
                      <h3 class="mb-0">{{ appointmentCount }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-calendar-check"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-3 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">To Admit</h6>
                      <h3 class="mb-0">{{ admitCount }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-hospital"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-3 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">To Discharge</h6>
                      <h3 class="mb-0">{{ dischargeCount }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-box-arrow-right"></i>
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
                  <router-link to="/user/receptionist/appointmentList" class="btn btn-primary btn-lg w-100">
                    <i class="bi bi-calendar-check me-2"></i>View Appointments
                  </router-link>
                </div>
                <div class="col-md-6 mb-3">
                  <router-link to="/user/receptionist/admitList" class="btn btn-success btn-lg w-100">
                    <i class="bi bi-hospital me-2"></i>Admit Patient
                  </router-link>
                </div>
                <div class="col-md-6 mb-3">
                  <router-link to="/user/receptionist/discharge" class="btn btn-warning btn-lg w-100">
                    <i class="bi bi-box-arrow-right me-2"></i>Discharge Patient
                  </router-link>
                </div>
                <div class="col-md-6 mb-3">
                  <router-link to="/user/receptionist/resource" class="btn btn-outline-info btn-lg w-100">
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
import { getAllPatientsForReceptionist } from '../../../services/receptionist'
import { getAllPatientsForAdmit } from '../../../services/receptionist'
import { getAllPatientsForDischarge } from '../../../services/receptionist'
import VerticalNavbarReceptionist from './VerticalNavbarReceptionist.vue'

export default {
  name: 'ReceptionistDashboard',
  components: {
    VerticalNavbarReceptionist
  },
  setup() {
    const userStore = useUserStore()
    const totalPatients = ref(0)
    const appointmentCount = ref(0)
    const admitCount = ref(0)
    const dischargeCount = ref(0)
    const loading = ref(true)

    const loadDashboardData = async () => {
      try {
        loading.value = true
        const [patients, admitPatients, dischargePatients] = await Promise.all([
          getAllPatientsForReceptionist(0, 1),
          getAllPatientsForAdmit(0, 1),
          getAllPatientsForDischarge(0, 1)
        ])
        totalPatients.value = patients?.totalElements || 0
        appointmentCount.value = patients?.content?.filter(p => p.currentStatus)?.length || 0
        admitCount.value = admitPatients?.totalElements || 0
        dischargeCount.value = dischargePatients?.totalElements || 0
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
      totalPatients,
      appointmentCount,
      admitCount,
      dischargeCount,
      loading
    }
  }
}
</script>

<style scoped>
.receptionist-dashboard {
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
