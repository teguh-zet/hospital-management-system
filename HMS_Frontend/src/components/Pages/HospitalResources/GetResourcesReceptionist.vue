<template>
  <div class="get-resources-receptionist-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarReceptionist />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-box-seam me-2"></i>Resources
            </h2>
          </div>

          <div class="card" v-if="!loading && resources.length > 0">
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Type</th>
                      <th>Quantity</th>
                      <th>Status</th>
                      <th>Info</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="resource in resources" :key="resource.id">
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
              <i class="bi bi-box-seam" style="font-size: 3rem; color: #ccc;"></i>
              <p class="text-muted mt-3">No resources found</p>
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
import { getAllResources } from '../../../services/resources'
import VerticalNavbarReceptionist from '../Receptionist/VerticalNavbarReceptionist.vue'

export default {
  name: 'GetResourcesReceptionist',
  components: {
    VerticalNavbarReceptionist
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
        resources.value = response?.content || []
        currentPage.value = response?.pageNumber ?? 0
        totalPages.value = response?.totalPages ?? 0
      } catch (error) {
        console.error('Error loading resources:', error)
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

    const formatPrice = (price) => {
      return new Intl.NumberFormat('id-ID').format(price)
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
      formatPrice
    }
  }
}
</script>

<style scoped>
.get-resources-receptionist-page {
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
