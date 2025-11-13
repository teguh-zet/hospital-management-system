<template>
  <div class="all-employee-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <VerticalNavbarAdmin />
        </div>
        <div class="col-md-9" style="margin-left: 250px; padding: 20px;">
          <div class="page-header mb-4">
            <h2 class="text-gradient">
              <i class="bi bi-people me-2"></i>All Employees
            </h2>
          </div>

          <SearchFilter
            placeholder="Search by name, email, or role..."
            @search="handleSearch"
            @export="handleExport"
            @print="handlePrint"
          />

          <div class="card" v-if="!loading && filteredEmployees.length > 0">
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Role</th>
                      <th>Department</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="employee in filteredEmployees" :key="employee.id">
                      <td>{{ employee.id }}</td>
                      <td>{{ employee.user?.firstName }} {{ employee.user?.lastName }}</td>
                      <td>{{ employee.user?.email }}</td>
                      <td>
                        <span class="badge bg-primary">
                          {{ getRoleName(employee.user?.roles) }}
                        </span>
                      </td>
                      <td>{{ employee.department || '-' }}</td>
                      <td>
                        <router-link
                          :to="`/user/admin/rmEmp?id=${employee.id}`"
                          class="btn btn-sm btn-danger"
                        >
                          <i class="bi bi-trash me-1"></i>Remove
                        </router-link>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- Pagination -->
              <nav v-if="totalPages > 1">
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

          <div class="card" v-else-if="!loading && employees.length > 0 && filteredEmployees.length === 0">
            <div class="card-body text-center">
              <i class="bi bi-search" style="font-size: 3rem; color: #ccc;"></i>
              <p class="text-muted mt-3">No employees match your search</p>
            </div>
          </div>

          <div class="card" v-else>
            <div class="card-body text-center">
              <i class="bi bi-people" style="font-size: 3rem; color: #ccc;"></i>
              <p class="text-muted mt-3">No employees found</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useToast } from 'vue-toastification'
import { getAllEmployees } from '../../../services/admin'
import { exportToPDF, exportToCSV, printTable } from '../../../utils/export'
import VerticalNavbarAdmin from './VerticalNavbarAdmin.vue'
import SearchFilter from '../../Common/SearchFilter.vue'

export default {
  name: 'AllEmployee',
  components: {
    VerticalNavbarAdmin,
    SearchFilter
  },
  setup() {
    const toast = useToast()
    const employees = ref([])
    const loading = ref(true)
    const currentPage = ref(0)
    const totalPages = ref(0)
    const pageSize = ref(10)
    const searchQuery = ref('')

    const loadEmployees = async (page = 0) => {
      try {
        loading.value = true
        const response = await getAllEmployees(page, pageSize.value, 'id', 'asc')
        employees.value = response?.content || []
        currentPage.value = response?.pageNumber || 0
        totalPages.value = response?.totalPages || 0
      } catch (error) {
        console.error('Error loading employees:', error)
        toast.error('Gagal memuat daftar employee')
      } finally {
        loading.value = false
      }
    }

    const changePage = (page) => {
      if (page >= 0 && page < totalPages.value) {
        loadEmployees(page)
      }
    }

    const getRoleName = (roles) => {
      if (!roles || roles.length === 0) return 'N/A'
      return roles[0]?.name || 'N/A'
    }

    const filteredEmployees = computed(() => {
      if (!searchQuery.value.trim()) {
        return employees.value
      }
      const query = searchQuery.value.toLowerCase()
      return employees.value.filter(emp => {
        const name = `${emp.user?.firstName || ''} ${emp.user?.lastName || ''}`.toLowerCase()
        const email = (emp.user?.email || '').toLowerCase()
        const role = getRoleName(emp.user?.roles).toLowerCase()
        const dept = (emp.department || '').toLowerCase()
        return name.includes(query) || email.includes(query) || role.includes(query) || dept.includes(query)
      })
    })

    const handleSearch = (query) => {
      searchQuery.value = query
    }

    const handleExport = () => {
      const columns = ['ID', 'Name', 'Email', 'Role', 'Department']
      const data = filteredEmployees.value.map(emp => [
        emp.id,
        `${emp.user?.firstName || ''} ${emp.user?.lastName || ''}`,
        emp.user?.email || '-',
        getRoleName(emp.user?.roles),
        emp.department || '-'
      ])
      exportToPDF('All Employees Report', columns, data, 'employees')
      toast.success('Report exported successfully!')
    }

    const handlePrint = () => {
      const columns = ['ID', 'Name', 'Email', 'Role', 'Department']
      const data = filteredEmployees.value.map(emp => [
        emp.id,
        `${emp.user?.firstName || ''} ${emp.user?.lastName || ''}`,
        emp.user?.email || '-',
        getRoleName(emp.user?.roles),
        emp.department || '-'
      ])
      printTable('All Employees Report', columns, data)
    }

    onMounted(() => {
      loadEmployees()
    })

    return {
      employees,
      filteredEmployees,
      loading,
      currentPage,
      totalPages,
      changePage,
      getRoleName,
      handleSearch,
      handleExport,
      handlePrint
    }
  }
}
</script>

<style scoped>
.all-employee-page {
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
