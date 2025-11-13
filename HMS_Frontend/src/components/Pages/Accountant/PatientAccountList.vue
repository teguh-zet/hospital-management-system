<template>
  <div class="patient-account-list-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarAccountant />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-receipt me-2"></i>Patient Account List
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
                      <th>Amount</th>
                      <th>Payment Status</th>
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
                          Rp {{ formatPrice(patient.health_history[0].amount || 0) }}
                        </span>
                        <span v-else>-</span>
                      </td>
                      <td>
                        <span v-if="patient.health_history && patient.health_history.length > 0">
                          <span :class="getPaymentStatusClass(patient.health_history[0])">
                            {{ getPaymentStatusText(patient.health_history[0]) }}
                          </span>
                        </span>
                        <span v-else>-</span>
                      </td>
                      <td>
                        <button
                          v-if="patient.health_history && patient.health_history.length > 0 && 
                                !patient.health_history[0].paymentStatus"
                          class="btn btn-sm btn-success"
                          @click="showPaymentModal(patient.health_history[0])"
                        >
                          <i class="bi bi-cash me-1"></i>Update Payment
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
              <i class="bi bi-receipt" style="font-size: 3rem; color: #ccc;"></i>
              <p class="text-muted mt-3">No patient accounts found</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Payment Modal -->
    <div v-if="showModal" class="modal fade show" style="display: block;" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Update Payment</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label for="amount" class="form-label">Payment Amount</label>
              <input
                type="number"
                class="form-control"
                id="amount"
                v-model.number="paymentAmount"
                min="0"
                step="0.01"
                required
              />
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeModal">Cancel</button>
            <button type="button" class="btn btn-primary" @click="handleUpdatePayment" :disabled="!paymentAmount || paymentAmount <= 0">
              Update Payment
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
import { getAllPatientsAfterAppointment } from '../../../services/accountant'
import { updatePayment } from '../../../services/accountant'
import VerticalNavbarAccountant from './VerticalNavbarAccountant.vue'

export default {
  name: 'PatientAccountList',
  components: {
    VerticalNavbarAccountant
  },
  setup() {
    const toast = useToast()
    const patients = ref([])
    const loading = ref(true)
    const currentPage = ref(0)
    const totalPages = ref(0)
    const pageSize = ref(10)
    const showModal = ref(false)
    const selectedHealthHistory = ref(null)
    const paymentAmount = ref(0)

    const loadData = async (page = 0) => {
      try {
        loading.value = true
        const response = await getAllPatientsAfterAppointment(page, pageSize.value, 'id', 'asc')
        patients.value = response?.content || []
        currentPage.value = response?.pageNumber || 0
        totalPages.value = response?.totalPages || 0
      } catch (error) {
        console.error('Error loading data:', error)
        toast.error('Gagal memuat daftar akun pasien')
      } finally {
        loading.value = false
      }
    }

    const changePage = (page) => {
      if (page >= 0 && page < totalPages.value) {
        loadData(page)
      }
    }

    const showPaymentModal = (healthHistory) => {
      selectedHealthHistory.value = healthHistory
      paymentAmount.value = healthHistory.amount || 0
      showModal.value = true
    }

    const closeModal = () => {
      showModal.value = false
      selectedHealthHistory.value = null
      paymentAmount.value = 0
    }

    const handleUpdatePayment = async () => {
      if (!selectedHealthHistory.value || !paymentAmount.value) return

      try {
        await updatePayment(selectedHealthHistory.value.id, paymentAmount.value)
        toast.success('Payment berhasil diupdate')
        closeModal()
        loadData(currentPage.value)
      } catch (error) {
        console.error('Error updating payment:', error)
        toast.error('Gagal mengupdate payment')
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

    const formatPrice = (price) => {
      return new Intl.NumberFormat('id-ID').format(price)
    }

    const getPaymentStatusText = (history) => {
      return history.paymentStatus ? 'Paid' : 'Pending'
    }

    const getPaymentStatusClass = (history) => {
      return history.paymentStatus ? 'badge bg-success' : 'badge bg-warning'
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
      showModal,
      paymentAmount,
      showPaymentModal,
      closeModal,
      handleUpdatePayment,
      formatDate,
      formatPrice,
      getPaymentStatusText,
      getPaymentStatusClass
    }
  }
}
</script>

<style scoped>
.patient-account-list-page {
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
