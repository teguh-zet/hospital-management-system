<template>
  <div class="admin-get-resources-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarAdmin />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-box-seam me-2"></i>Resources Management
            </h2>
            <router-link to="/user/admin/addResources" class="btn btn-primary">
              <i class="bi bi-plus-circle me-2"></i>Add Resource
            </router-link>
          </div>

          <div class="card" v-if="!loading && resources && resources.length > 0">
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Type</th>
                      <th>Quantity</th>
                      <th>Status</th>
                      <th>Info</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="resource in resources" :key="resource.id">
                      <td>{{ resource.id || '-' }}</td>
                      <td>{{ resource.resource_name || resource.resourceName || '-' }}</td>
                      <td>
                        <span class="badge bg-secondary">Resource</span>
                      </td>
                      <td>
                        <div>
                          <small class="text-muted">Total: {{ resource.total_quantity || resource.totalQuantity || 0 }}</small><br>
                          <small class="text-success">Available: {{ resource.remaining_quantity || resource.remainingQuantity || 0 }}</small><br>
                          <small class="text-warning">Occupied: {{ resource.occupy_quantity || resource.occupyQuantity || 0 }}</small>
                        </div>
                      </td>
                      <td class="text-muted">-</td>
                      <td class="text-muted">-</td>
                      <td>
                        <button
                          class="btn btn-sm btn-danger"
                          @click="handleDelete(resource.id)"
                        >
                          <i class="bi bi-trash me-1"></i>Delete
                        </button>
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

          <div class="card" v-else-if="!loading">
            <div class="card-body text-center">
              <i class="bi bi-box-seam" style="font-size: 3rem; color: #ccc;"></i>
              <p class="text-muted mt-3">No resources found</p>
              <p class="text-muted small">Total: {{ resources?.length || 0 }} resources</p>
              <router-link to="/user/admin/addResources" class="btn btn-primary mt-2">
                <i class="bi bi-plus-circle me-2"></i>Add First Resource
              </router-link>
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
import { getAllResources, deleteResource } from '../../../services/resources'
import VerticalNavbarAdmin from '../Admin/VerticalNavbarAdmin.vue'

export default {
  name: 'AdminGetResources',
  components: {
    VerticalNavbarAdmin
  },
  setup() {
    const toast = useToast()
    const resources = ref([])
    const loading = ref(true)
    const currentPage = ref(0)
    const totalPages = ref(0)
    const pageSize = ref(10)

    const loadResources = async (page = 0) => {
      try {
        loading.value = true
        const response = await getAllResources(page, pageSize.value, 'id', 'asc')
        console.log('Resources response:', response) // Debug log
        console.log('Resources content:', response?.content) // Debug log content
        
        resources.value = response?.content || []
        
        // Debug: Log setiap resource untuk melihat struktur
        if (resources.value.length > 0) {
          console.log('First resource structure:', resources.value[0])
          console.log('All resource keys:', resources.value.map(r => Object.keys(r)))
        }
        
        currentPage.value = response?.pageNumber ?? 0
        totalPages.value = response?.totalPages ?? 0
        
        // Debug: Log jika data kosong
        if (resources.value.length === 0 && response) {
          console.warn('Resources array is empty but response exists:', response)
        }
      } catch (error) {
        console.error('Error loading resources:', error)
        console.error('Error details:', {
          status: error.response?.status,
          statusText: error.response?.statusText,
          data: error.response?.data,
          message: error.message
        })
        if (error.response?.status === 403) {
          toast.error('Anda tidak memiliki izin untuk mengakses resources')
        } else if (error.response?.status === 401) {
          toast.error('Session expired. Silakan login kembali.')
        } else {
          toast.error('Gagal memuat daftar resources: ' + (error.response?.data?.message || error.message))
        }
        resources.value = []
      } finally {
        loading.value = false
      }
    }

    const changePage = (page) => {
      if (page >= 0 && page < totalPages.value) {
        loadResources(page)
      }
    }

    const handleDelete = async (resourceId) => {
      if (!confirm('Apakah Anda yakin ingin menghapus resource ini?')) {
        return
      }

      try {
        await deleteResource(resourceId)
        toast.success('Resource berhasil dihapus')
        loadResources(currentPage.value)
      } catch (error) {
        console.error('Error deleting resource:', error)
        toast.error('Gagal menghapus resource')
      }
    }

    const formatPrice = (price) => {
      if (price === null || price === undefined || isNaN(price)) {
        return '0'
      }
      return new Intl.NumberFormat('id-ID').format(Number(price))
    }

    onMounted(() => {
      loadResources()
    })

    return {
      resources,
      loading,
      currentPage,
      totalPages,
      changePage,
      handleDelete,
      formatPrice
    }
  }
}
</script>

<style scoped>
.admin-get-resources-page {
  min-height: 100vh;
  background: #f8f9fa;
}

.page-header {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
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
