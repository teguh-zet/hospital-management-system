<template>
  <div class="admit-patient-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarReceptionist />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-hospital me-2"></i>Admit Patient
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
                      <th>Doctor</th>
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
                        {{ patient.doctor?.employee?.user?.firstName || '-' }}
                        {{ patient.doctor?.employee?.user?.lastName || '' }}
                      </td>
                      <td>
                        <button
                          v-if="patient.health_history && patient.health_history.length > 0 && 
                                patient.health_history[0].currentStatus && !patient.health_history[0].admitStatus"
                          class="btn btn-sm btn-success"
                          @click="showAdmitModal(patient)"
                        >
                          <i class="bi bi-hospital me-1"></i>Admit
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
              <i class="bi bi-hospital" style="font-size: 3rem; color: #ccc;"></i>
              <p class="text-muted mt-3">No patients available for admission</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Admit Modal -->
    <div v-if="showModal" class="modal fade show" style="display: block;" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Admit Patient</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label for="wardId" class="form-label">Select Ward</label>
              <select class="form-control" id="wardId" v-model="selectedWardId" required>
                <option value="">Select a ward</option>
                <option v-for="ward in wards" :key="ward.id" :value="ward.id">
                  {{ ward.wardName }} - {{ ward.wardType }}
                </option>
              </select>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeModal">Cancel</button>
            <button type="button" class="btn btn-primary" @click="handleAdmit" :disabled="!selectedWardId">
              Admit Patient
            </button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="showModal" class="modal-backdrop fade show"></div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import { getAllPatientsForAdmit } from '../../../services/receptionist'
import { getAllWards, admitPatient } from '../../../services/receptionist'
import VerticalNavbarReceptionist from './VerticalNavbarReceptionist.vue'

export default {
  name: 'AdmitPatient',
  components: {
    VerticalNavbarReceptionist
  },
  setup() {
    const toast = useToast()
    const patients = ref([])
    const wards = ref([])
    const loading = ref(true)
    const currentPage = ref(0)
    const totalPages = ref(0)
    const pageSize = ref(10)
    const showModal = ref(false)
    const selectedPatient = ref(null)
    const selectedWardId = ref('')

    const loadData = async (page = 0) => {
      try {
        loading.value = true
        const [patientsData, wardsData] = await Promise.all([
          getAllPatientsForAdmit(page, pageSize.value, 'id', 'asc'),
          getAllWards()
        ])
        patients.value = patientsData?.content || []
        currentPage.value = patientsData?.pageNumber || 0
        totalPages.value = patientsData?.totalPages || 0
        wards.value = wardsData || []
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

    const showAdmitModal = (patient) => {
      selectedPatient.value = patient
      selectedWardId.value = ''
      showModal.value = true
    }

    const closeModal = () => {
      showModal.value = false
      selectedPatient.value = null
      selectedWardId.value = ''
    }

    const handleAdmit = async () => {
      if (!selectedWardId.value || !selectedPatient.value) return

      try {
        const healthHistory = selectedPatient.value.health_history?.[0]
        if (!healthHistory) {
          toast.error('Health history tidak ditemukan')
          return
        }

        await admitPatient(healthHistory, parseInt(selectedWardId.value))
        toast.success('Patient berhasil di-admit')
        closeModal()
        loadData(currentPage.value)
      } catch (error) {
        console.error('Error admitting patient:', error)
        toast.error('Gagal meng-admit patient')
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
      wards,
      loading,
      currentPage,
      totalPages,
      changePage,
      showModal,
      selectedWardId,
      showAdmitModal,
      closeModal,
      handleAdmit,
      formatDate
    }
  }
}
</script>

<style scoped>
.admit-patient-page {
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
