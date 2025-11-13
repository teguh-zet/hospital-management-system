<template>
  <div class="accountant-dashboard">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarAccountant />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="dashboard-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-calculator me-2"></i>Accountant Dashboard
            </h2>
            <p class="text-muted">Welcome, {{ userStore.currentUser?.firstName || 'Accountant' }}</p>
          </div>

          <div class="row mb-4">
            <div class="col-md-6 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">Pending Payments</h6>
                      <h3 class="mb-0">{{ pendingPayments }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-currency-dollar"></i>
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
                <div class="col-md-12 mb-3">
                  <router-link to="/user/accountant/PatientAccountList" class="btn btn-primary btn-lg w-100">
                    <i class="bi bi-receipt me-2"></i>View Patient Accounts
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
import { getAllPatientsAfterAppointment } from '../../../services/accountant'
import VerticalNavbarAccountant from './VerticalNavbarAccountant.vue'

export default {
  name: 'AccountantDashboard',
  components: {
    VerticalNavbarAccountant
  },
  setup() {
    const userStore = useUserStore()
    const pendingPayments = ref(0)
    const patientCount = ref(0)
    const loading = ref(true)

    const loadDashboardData = async () => {
      try {
        loading.value = true
        const patients = await getAllPatientsAfterAppointment(0, 1)
        patientCount.value = patients?.totalElements || 0
        // Count patients with pending payments
        pendingPayments.value = patients?.content?.filter(p => !p.paymentStatus)?.length || 0
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
      pendingPayments,
      patientCount,
      loading
    }
  }
}
</script>

<style scoped>
.accountant-dashboard {
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
