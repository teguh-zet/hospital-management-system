<template>
  <div class="remove-employee-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarAdmin />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-person-x me-2"></i>Remove Employee
            </h2>
          </div>

          <div class="card">
            <div class="card-body">
              <div v-if="employeeId && employee" class="mb-4">
                <h5>Confirm Removal</h5>
                <div class="alert alert-warning">
                  <strong>Warning:</strong> This action cannot be undone. Are you sure you want to remove this employee?
                </div>
                <div class="card">
                  <div class="card-body">
                    <p><strong>ID:</strong> {{ employee.id }}</p>
                    <p><strong>Name:</strong> {{ employee.user?.firstName }} {{ employee.user?.lastName }}</p>
                    <p><strong>Email:</strong> {{ employee.user?.email }}</p>
                    <p><strong>Department:</strong> {{ employee.department || '-' }}</p>
                  </div>
                </div>
                <div class="mt-3">
                  <button class="btn btn-danger me-2" @click="showConfirmModal = true">
                    <i class="bi bi-trash me-2"></i>Confirm Remove
                  </button>
                  <router-link to="/user/admin/allEmployee" class="btn btn-outline-secondary">
                    Cancel
                  </router-link>
                </div>
              </div>
              <div v-else class="text-center">
                <i class="bi bi-exclamation-triangle" style="font-size: 3rem; color: #ccc;"></i>
                <p class="text-muted mt-3">No employee ID provided</p>
                <router-link to="/user/admin/allEmployee" class="btn btn-primary">
                  Back to Employee List
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <ConfirmModal
      :show="showConfirmModal"
      title="Confirm Removal"
      :message="`Apakah Anda yakin ingin menghapus employee ${employee?.user?.firstName} ${employee?.user?.lastName}? Tindakan ini tidak dapat dibatalkan.`"
      type="danger"
      confirm-text="Delete Employee"
      @confirm="handleRemove"
      @cancel="showConfirmModal = false"
    />
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { removeEmployee, getAllEmployees } from '../../../services/admin'
import ConfirmModal from '../../Common/ConfirmModal.vue'
import VerticalNavbarAdmin from './VerticalNavbarAdmin.vue'

export default {
  name: 'RemoveEmployee',
  components: {
    VerticalNavbarAdmin,
    ConfirmModal
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const toast = useToast()
    
    const employeeId = ref(null)
    const employee = ref(null)
    const isLoading = ref(false)
    const showConfirmModal = ref(false)

    const loadEmployee = async () => {
      const id = route.query.id || route.params.id
      if (!id) return

      employeeId.value = parseInt(id)
      try {
        const response = await getAllEmployees(0, 1000)
        employee.value = response?.content?.find(emp => emp.id === employeeId.value)
        if (!employee.value) {
          toast.error('Employee tidak ditemukan')
        }
      } catch (error) {
        console.error('Error loading employee:', error)
        toast.error('Gagal memuat data employee')
      }
    }

    const handleRemove = async () => {
      if (!employeeId.value) return

      showConfirmModal.value = false
      isLoading.value = true
      try {
        await removeEmployee(employeeId.value)
        toast.success('Employee berhasil dihapus!')
        router.push('/user/admin/allEmployee')
      } catch (error) {
        console.error('Error removing employee:', error)
        toast.error(error.response?.data?.message || 'Gagal menghapus employee')
      } finally {
        isLoading.value = false
      }
    }

    onMounted(() => {
      loadEmployee()
    })

    return {
      employeeId,
      employee,
      isLoading,
      showConfirmModal,
      handleRemove
    }
  }
}
</script>

<style scoped>
.remove-employee-page {
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
