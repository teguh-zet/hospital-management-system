<template>
  <div class="appointment-list-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarReceptionist />
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
                          <span :class="getStatusClass(patient.health_history[0])">
                            {{ getStatusText(patient.health_history[0]) }}
                          </span>
                        </span>
                        <span v-else>-</span>
                      </td>
                      <td>
                        <button
                          v-if="patient.health_history && patient.health_history.length > 0 && 
                                patient.health_history[0].currentStatus && !patient.doctor"
                          class="btn btn-sm btn-primary"
                          @click="assignDoctor(patient)"
                        >
                          <i class="bi bi-person-plus me-1"></i>Assign Doctor
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
import { getAllPatientsForReceptionist } from '../../../services/receptionist'
import { getAllDoctors } from '../../../services/doctor'
import { appointDoctorToPatient } from '../../../services/receptionist'
import VerticalNavbarReceptionist from './VerticalNavbarReceptionist.vue'

export default {
  name: 'AppointmentList',
  components: {
    VerticalNavbarReceptionist
  },
  setup() {
    const toast = useToast()
    const patients = ref([])
    const doctors = ref([])
    const loading = ref(true)
    const currentPage = ref(0)
    const totalPages = ref(0)
    const pageSize = ref(10)

    const loadData = async (page = 0) => {
      try {
        loading.value = true
        const [patientsData, doctorsData] = await Promise.all([
          getAllPatientsForReceptionist(page, pageSize.value, 'id', 'asc'),
          getAllDoctors()
        ])
        patients.value = patientsData?.content || []
        currentPage.value = patientsData?.pageNumber || 0
        totalPages.value = patientsData?.totalPages || 0
        doctors.value = doctorsData || []
      } catch (error) {
        console.error('Error loading data:', error)
        if (error.response?.status === 403) {
          toast.error('Anda tidak memiliki izin untuk mengakses data ini')
        } else if (error.response?.status === 401) {
          toast.error('Session expired. Silakan login kembali.')
        } else {
          toast.error('Gagal memuat daftar appointment: ' + (error.response?.data?.message || error.message))
        }
      } finally {
        loading.value = false
      }
    }

    const changePage = (page) => {
      if (page >= 0 && page < totalPages.value) {
        loadData(page)
      }
    }

    const assignDoctor = async (patient) => {
      const doctorId = prompt('Masukkan ID Doctor:')
      if (!doctorId) return

      try {
        await appointDoctorToPatient(patient.id, parseInt(doctorId))
        toast.success('Doctor berhasil ditugaskan')
        loadData(currentPage.value)
      } catch (error) {
        console.error('Error assigning doctor:', error)
        toast.error('Gagal menugaskan doctor')
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
      if (history.currentStatus) return 'Active'
      return 'Completed'
    }

    const getStatusClass = (history) => {
      if (history.admitStatus) return 'badge bg-warning'
      if (history.currentStatus) return 'badge bg-success'
      return 'badge bg-secondary'
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
      assignDoctor,
      formatDate,
      getStatusText,
      getStatusClass
    }
  }
}
</script>

<style scoped>
.appointment-list-page {
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
