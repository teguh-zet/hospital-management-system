<template>
  <div class="add-resources-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarAdmin />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-plus-circle me-2"></i>Add Resources
            </h2>
          </div>

          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">Resource Information</h5>
            </div>
            <div class="card-body">
              <form @submit.prevent="handleSubmit">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label for="resource_name" class="form-label">Resource Name</label>
                    <input
                      type="text"
                      class="form-control"
                      id="resource_name"
                      v-model="formData.resource_name"
                      placeholder="e.g., Bed, Wheelchair, Oxygen Tank"
                      required
                    />
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="total_quantity" class="form-label">Total Quantity</label>
                    <input
                      type="number"
                      class="form-control"
                      id="total_quantity"
                      v-model.number="formData.total_quantity"
                      min="1"
                      required
                    />
                    <small class="text-muted">Jumlah total resource yang tersedia</small>
                  </div>
                </div>
                <div class="text-end">
                  <button type="button" class="btn btn-outline-secondary me-2" @click="handleReset">
                    Reset
                  </button>
                  <button type="submit" class="btn btn-primary" :disabled="isLoading">
                    <span v-if="!isLoading">
                      <i class="bi bi-check-circle me-2"></i>Add Resource
                    </span>
                    <span v-else>
                      <span class="spinner-border spinner-border-sm me-2"></span>Adding...
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
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { createResource } from '../../../services/resources'
import VerticalNavbarAdmin from '../Admin/VerticalNavbarAdmin.vue'

export default {
  name: 'AddResources',
  components: {
    VerticalNavbarAdmin
  },
  setup() {
    const router = useRouter()
    const toast = useToast()
    
    const formData = ref({
      resource_name: '',
      total_quantity: 1
    })
    
    const isLoading = ref(false)

    const handleSubmit = async () => {
      if (!formData.value.resource_name || !formData.value.total_quantity || formData.value.total_quantity < 1) {
        toast.error('Harap lengkapi semua field yang wajib')
        return
      }

      isLoading.value = true
      try {
        const resourceDto = {
          resource_name: formData.value.resource_name,
          total_quantity: formData.value.total_quantity,
          occupy_quantity: 0,
          remaining_quantity: formData.value.total_quantity
        }
        
        await createResource(resourceDto)
        toast.success('Resource berhasil ditambahkan!')
        router.push('/user/admin/resources')
      } catch (error) {
        console.error('Error creating resource:', error)
        toast.error(error.response?.data?.message || 'Gagal menambahkan resource')
      } finally {
        isLoading.value = false
      }
    }

    const handleReset = () => {
      formData.value = {
        resource_name: '',
        total_quantity: 1
      }
    }

    return {
      formData,
      isLoading,
      handleSubmit,
      handleReset
    }
  }
}
</script>

<style scoped>
.add-resources-page {
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
