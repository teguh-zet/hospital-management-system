<template>
  <div class="all-patient-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarAdmin />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-person-heart me-2"></i>All Patients
            </h2>
          </div>

          <div class="card" v-if="!loading && patients.length > 0">
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Admit Status</th>
                      <th>Current Status</th>
                      <th>Doctor</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="patient in patients" :key="patient.id">
                      <td>{{ patient.id }}</td>
                      <td>{{ patient.user?.firstName }} {{ patient.user?.lastName }}</td>
                      <td>{{ patient.user?.email }}</td>
                      <td>
                        <span :class="patient.admitStatus ? 'badge bg-warning' : 'badge bg-secondary'">
                          {{ patient.admitStatus ? 'Admitted' : 'Not Admitted' }}
                        </span>
                      </td>
                      <td>
                        <span :class="patient.currentStatus ? 'badge bg-success' : 'badge bg-secondary'">
                          {{ patient.currentStatus ? 'Active' : 'Inactive' }}
                        </span>
                      </td>
                      <td>
                        {{ patient.doctor?.employee?.user?.firstName || '-' }}
                        {{ patient.doctor?.employee?.user?.lastName || '' }}
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- Pagination -->
              <nav v-if="totalPages > 1">
                <ul class="pagination justify-content-center">
                  <li class="page-item" :class="{ disabled: currentPage === 0 }">
                    <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">Previous</a>
                  </li>
                  <li
                    v-for="page in totalPages"
                    :key="page"
                    class="page-item"
                    :class="{ active: currentPage === page - 1 }"
                  >
                    <a class="page-link" href="#" @click.prevent="changePage(page - 1)">{{ page }}</a>
                  </li>
                  <li class="page-item" :class="{ disabled: currentPage === totalPages - 1 }">
                    <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">Next</a>
                  </li>
                </ul>
              </nav>
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
              <i class="bi bi-person-heart" style="font-size: 3rem; color: #ccc;"></i>
              <p class="text-muted mt-3">No patients found</p>
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
import { getAllPatients } from '../../../services/admin'
import VerticalNavbarAdmin from './VerticalNavbarAdmin.vue'

export default {
  name: 'AllPatient',
  components: {
    VerticalNavbarAdmin
  },
  setup() {
    const toast = useToast()
    const patients = ref([])
    const loading = ref(true)
    const currentPage = ref(0)
    const totalPages = ref(0)
    const pageSize = ref(10)

    const loadPatients = async (page = 0) => {
      try {
        loading.value = true
        const response = await getAllPatients(page, pageSize.value, 'id', 'asc')
        patients.value = response?.content || []
        currentPage.value = response?.pageNumber || 0
        totalPages.value = response?.totalPages || 0
      } catch (error) {
        console.error('Error loading patients:', error)
        toast.error('Gagal memuat daftar pasien')
      } finally {
        loading.value = false
      }
    }

    const changePage = (page) => {
      if (page >= 0 && page < totalPages.value) {
        loadPatients(page)
      }
    }

    onMounted(() => {
      loadPatients()
    })

    return {
      patients,
      loading,
      currentPage,
      totalPages,
      changePage
    }
  }
}
</script>

<style scoped>
.all-patient-page {
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
