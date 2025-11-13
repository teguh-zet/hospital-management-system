<template>
  <div class="schedule-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarDoctor />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-calendar-event me-2"></i>Select Schedule
            </h2>
          </div>

          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">Set Your Weekly Schedule</h5>
            </div>
            <div class="card-body">
              <form @submit.prevent="handleSubmit">
                <div class="row">
                  <div class="col-md-12 mb-3">
                    <label class="form-label">Select Available Days</label>
                    <div class="d-flex flex-wrap gap-3">
                      <div v-for="day in days" :key="day.value" class="form-check">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          :id="day.value"
                          :value="day.value"
                          v-model="selectedDays"
                        />
                        <label class="form-check-label" :for="day.value">
                          {{ day.label }}
                        </label>
                      </div>
                    </div>
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="startTime" class="form-label">Start Time</label>
                    <input
                      type="time"
                      class="form-control"
                      id="startTime"
                      v-model="scheduleData.startTime"
                      required
                    />
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="endTime" class="form-label">End Time</label>
                    <input
                      type="time"
                      class="form-control"
                      id="endTime"
                      v-model="scheduleData.endTime"
                      required
                    />
                  </div>
                </div>
                <div class="text-end">
                  <button type="submit" class="btn btn-primary" :disabled="isLoading || selectedDays.length === 0">
                    <span v-if="!isLoading">
                      <i class="bi bi-check-circle me-2"></i>Save Schedule
                    </span>
                    <span v-else>
                      <span class="spinner-border spinner-border-sm me-2"></span>Saving...
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
import { ref } from 'vue'
import { useToast } from 'vue-toastification'
import { useUserStore } from '../../../stores/user'
import { selectSchedule } from '../../../services/doctor'
import VerticalNavbarDoctor from './VerticalNavbarDoctor.vue'

export default {
  name: 'Schedule',
  components: {
    VerticalNavbarDoctor
  },
  setup() {
    const toast = useToast()
    const userStore = useUserStore()
    
    const days = [
      { value: 'MONDAY', label: 'Monday' },
      { value: 'TUESDAY', label: 'Tuesday' },
      { value: 'WEDNESDAY', label: 'Wednesday' },
      { value: 'THURSDAY', label: 'Thursday' },
      { value: 'FRIDAY', label: 'Friday' },
      { value: 'SATURDAY', label: 'Saturday' },
      { value: 'SUNDAY', label: 'Sunday' }
    ]
    
    const selectedDays = ref([])
    const scheduleData = ref({
      startTime: '',
      endTime: ''
    })
    
    const isLoading = ref(false)

    const handleSubmit = async () => {
      if (selectedDays.value.length === 0) {
        toast.error('Pilih minimal satu hari')
        return
      }

      if (!scheduleData.value.startTime || !scheduleData.value.endTime) {
        toast.error('Harap lengkapi waktu kerja')
        return
      }

      isLoading.value = true
      try {
        const doctorId = userStore.currentUser?.employee?.doctor?.id
        if (!doctorId) {
          toast.error('Doctor ID tidak ditemukan')
          return
        }

        const doctorDto = {
          startTime: scheduleData.value.startTime,
          endTime: scheduleData.value.endTime
        }

        // Save schedule for each selected day
        for (const day of selectedDays.value) {
          await selectSchedule(doctorDto, doctorId, day)
        }
        
        toast.success('Schedule berhasil disimpan!')
        selectedDays.value = []
        scheduleData.value = { startTime: '', endTime: '' }
      } catch (error) {
        console.error('Error saving schedule:', error)
        toast.error(error.response?.data?.message || 'Gagal menyimpan schedule')
      } finally {
        isLoading.value = false
      }
    }

    return {
      days,
      selectedDays,
      scheduleData,
      isLoading,
      handleSubmit
    }
  }
}
</script>

<style scoped>
.schedule-page {
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

.form-check {
  min-width: 120px;
}
</style>
