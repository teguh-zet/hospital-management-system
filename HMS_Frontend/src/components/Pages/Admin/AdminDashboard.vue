<template>
  <div class="admin-dashboard">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarAdmin />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="dashboard-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-shield-check me-2"></i>Admin Dashboard
            </h2>
            <p class="text-muted">Welcome, {{ userStore.currentUser?.firstName || 'Admin' }}</p>
          </div>

          <div class="row mb-4">
            <div class="col-md-4 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">Total Employees</h6>
                      <h3 class="mb-0">{{ employeeCount }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-people"></i>
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
            <div class="col-md-4 mb-3">
              <div class="card stat-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <h6 class="text-muted mb-2">Resources</h6>
                      <h3 class="mb-0">{{ resourceCount }}</h3>
                    </div>
                    <div class="stat-icon">
                      <i class="bi bi-box-seam"></i>
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
                  <router-link to="/user/admin/allEmployee" class="btn btn-primary btn-lg w-100">
                    <i class="bi bi-people me-2"></i>Manage Employees
                  </router-link>
                </div>
                <div class="col-md-6 mb-3">
                  <router-link to="/user/admin/allPatient" class="btn btn-outline-primary btn-lg w-100">
                    <i class="bi bi-person-heart me-2"></i>View All Patients
                  </router-link>
                </div>
                <div class="col-md-6 mb-3">
                  <router-link to="/user/admin/addEmp" class="btn btn-success btn-lg w-100">
                    <i class="bi bi-person-plus me-2"></i>Add Employee
                  </router-link>
                </div>
                <div class="col-md-6 mb-3">
                  <router-link to="/user/admin/resources" class="btn btn-outline-info btn-lg w-100">
                    <i class="bi bi-box-seam me-2"></i>Manage Resources
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
import { getAllEmployees } from '../../../services/admin'
import { getAllPatients } from '../../../services/admin'
import { getAllResources } from '../../../services/resources'
import VerticalNavbarAdmin from './VerticalNavbarAdmin.vue'

export default {
  name: 'AdminDashboard',
  components: {
    VerticalNavbarAdmin
  },
  setup() {
    const userStore = useUserStore()
    const employeeCount = ref(0)
    const patientCount = ref(0)
    const resourceCount = ref(0)
    const loading = ref(true)

    const loadDashboardData = async () => {
      try {
        loading.value = true
        const [employees, patients, resources] = await Promise.all([
          getAllEmployees(0, 1),
          getAllPatients(0, 1),
          getAllResources(0, 1)
        ])
        employeeCount.value = employees?.totalElements || 0
        patientCount.value = patients?.totalElements || 0
        resourceCount.value = resources?.totalElements || 0
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
      employeeCount,
      patientCount,
      resourceCount,
      loading
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
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
