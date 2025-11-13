<template>
  <div class="discharge-patient-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarReceptionist />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-box-arrow-right me-2"></i>Discharge Patient
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
                      <th>Ward</th>
                      <th>Admit Date</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="patient in patients" :key="patient.id">
                      <td>{{ patient.user?.firstName }} {{ patient.user?.lastName }}</td>
                      <td>{{ patient.user?.email }}</td>
                      <td>
                        {{ patient.ward?.wardName || '-' }}
                        <span v-if="patient.ward">({{ patient.ward.wardType }})</span>
                      </td>
                      <td>
                        <span v-if="patient.health_history && patient.health_history.length > 0">
                          {{ formatDate(patient.health_history[0].appointmentDate) }}
                        </span>
                        <span v-else>-</span>
                      </td>
                      <td>
                        <button
                          v-if="patient.health_history && patient.health_history.length > 0 && 
                                patient.health_history[0].admitStatus"
                          class="btn btn-sm btn-warning"
                          @click="handleDischarge(patient.health_history[0].id)"
                        >
                          <i class="bi bi-box-arrow-right me-1"></i>Discharge
                        </button>
                        <span v-else class="text-muted">-</span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- Pagination -->
              <nav v-if="totalPages > 1" class="mt-3">
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
              <i class="bi bi-box-arrow-right" style="font-size: 3rem; color: #ccc;"></i>
              <p class="text-muted mt-3">No patients available for discharge</p>
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
import { getAllPatientsForDischarge, dischargePatient } from '../../../services/receptionist'
import VerticalNavbarReceptionist from './VerticalNavbarReceptionist.vue'

export default {
  name: 'DischargePatient',
  components: {
    VerticalNavbarReceptionist
  },
  setup() {
    const toast = useToast()
    const patients = ref([])
    const loading = ref(true)
    const currentPage = ref(0)
    const totalPages = ref(0)
    const pageSize = ref(10)

    const loadData = async (page = 0) => {
      try {
        loading.value = true
        const response = await getAllPatientsForDischarge(page, pageSize.value, 'id', 'asc')
        patients.value = response?.content || []
        currentPage.value = response?.pageNumber || 0
        totalPages.value = response?.totalPages || 0
      } catch (error) {
        console.error('Error loading data:', error)
        toast.error('Gagal memuat data')
      } finally {
        loading.value = false
      }
    }

    const changePage = (page) => {
      if (page >= 0 && page < totalPages.value) {
        loadData(page)
      }
    }

    const handleDischarge = async (healthHistoryId) => {
      if (!confirm('Apakah Anda yakin ingin discharge patient ini?')) {
        return
      }

      try {
        await dischargePatient(healthHistoryId)
        toast.success('Patient berhasil di-discharge')
        loadData(currentPage.value)
      } catch (error) {
        console.error('Error discharging patient:', error)
        toast.error('Gagal discharge patient')
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

    onMounted(() => {
      loadData()
    })

    return {
      patients,
      loading,
      currentPage,
      totalPages,
      changePage,
      handleDischarge,
      formatDate
    }
  }
}
</script>

<style scoped>
.discharge-patient-page {
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
