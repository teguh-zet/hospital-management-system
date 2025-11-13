<template>
  <div class="appointment-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarPatient />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-calendar-plus me-2"></i>Book Appointment
            </h2>
          </div>

          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">Appointment Form</h5>
            </div>
            <div class="card-body">
              <form @submit.prevent="handleSubmit">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label for="appointmentDate" class="form-label">Appointment Date</label>
                    <input
                      type="date"
                      class="form-control"
                      id="appointmentDate"
                      v-model="formData.appointmentDate"
                      :min="minDate"
                      required
                    />
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="appointmentTime" class="form-label">Appointment Time</label>
                    <input
                      type="time"
                      class="form-control"
                      id="appointmentTime"
                      v-model="formData.appointmentTime"
                      required
                    />
                  </div>
                  <div class="col-md-12 mb-3">
                    <label for="symptoms" class="form-label">Symptoms / Reason</label>
                    <textarea
                      class="form-control"
                      id="symptoms"
                      rows="4"
                      v-model="formData.symptoms"
                      placeholder="Describe your symptoms or reason for appointment"
                      required
                    ></textarea>
                  </div>
                  <div class="col-md-12 mb-3">
                    <label for="doctorId" class="form-label">Select Doctor</label>
                    <select class="form-control" id="doctorId" v-model="formData.doctorId" required>
                      <option value="">Select a doctor</option>
                      <option v-for="doctor in doctors" :key="doctor.id" :value="doctor.id">
                        {{ doctor.employee?.user?.firstName }} {{ doctor.employee?.user?.lastName }}
                      </option>
                    </select>
                  </div>
                </div>
                <div class="text-end">
                  <button type="button" class="btn btn-outline-secondary me-2" @click="handleReset">
                    Reset
                  </button>
                  <button type="submit" class="btn btn-primary" :disabled="isLoading">
                    <span v-if="!isLoading">
                      <i class="bi bi-check-circle me-2"></i>Book Appointment
                    </span>
                    <span v-else>
                      <span class="spinner-border spinner-border-sm me-2"></span>Booking...
                    </span>
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { useUserStore } from '../../../stores/user'
import { addAppointment } from '../../../services/patient'
import { getAllDoctors } from '../../../services/doctor'
import VerticalNavbarPatient from './VerticalNavbarPatient.vue'

export default {
  name: 'Appointment',
  components: {
    VerticalNavbarPatient
  },
  setup() {
    const router = useRouter()
    const toast = useToast()
    const userStore = useUserStore()
    
    const formData = ref({
      appointmentDate: '',
      appointmentTime: '',
      symptoms: '',
      doctorId: ''
    })
    
    const doctors = ref([])
    const isLoading = ref(false)

    const minDate = computed(() => {
      const today = new Date()
      return today.toISOString().split('T')[0]
    })

    const loadDoctors = async () => {
      try {
        const data = await getAllDoctors()
        doctors.value = data || []
      } catch (error) {
        console.error('Error loading doctors:', error)
        if (error.response?.status === 403) {
          toast.error('Anda tidak memiliki izin untuk mengakses daftar dokter. Silakan hubungi admin.')
        } else if (error.response?.status === 401) {
          toast.error('Session expired. Silakan login kembali.')
        } else {
          toast.error('Gagal memuat daftar dokter: ' + (error.response?.data?.message || error.message))
        }
      }
    }

    const handleSubmit = async () => {
      if (!formData.value.appointmentDate || !formData.value.appointmentTime || !formData.value.symptoms || !formData.value.doctorId) {
        toast.error('Harap lengkapi semua field')
        return
      }

      isLoading.value = true
      try {
        const patientId = userStore.currentUser?.patient?.id || userStore.currentUser?.id
        const healthHistoryDto = {
          appointmentDate: formData.value.appointmentDate,
          appointmentTime: formData.value.appointmentTime,
          symptoms: formData.value.symptoms,
          admitStatus: false,
          currentStatus: true
        }
        
        await addAppointment(healthHistoryDto, patientId)
        toast.success('Appointment berhasil dibuat!')
        router.push('/user/patient/appoHistory')
      } catch (error) {
        console.error('Error creating appointment:', error)
        toast.error(error.response?.data?.message || 'Gagal membuat appointment')
      } finally {
        isLoading.value = false
      }
    }

    const handleReset = () => {
      formData.value = {
        appointmentDate: '',
        appointmentTime: '',
        symptoms: '',
        doctorId: ''
      }
    }

    onMounted(() => {
      loadDoctors()
    })

    return {
      formData,
      doctors,
      isLoading,
      minDate,
      handleSubmit,
      handleReset
    }
  }
}
</script>

<style scoped>
.appointment-page {
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
</style>
