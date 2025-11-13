<template>
  <div class="search-filter mb-3">
    <div class="row">
      <div class="col-md-6">
        <div class="input-group">
          <span class="input-group-text">
            <i class="bi bi-search"></i>
          </span>
          <input
            type="text"
            class="form-control"
            :placeholder="placeholder"
            v-model="searchQuery"
            @input="handleSearch"
          />
        </div>
      </div>
      <div class="col-md-6 text-end" v-if="showExport">
        <button class="btn btn-outline-primary me-2" @click="handleExport">
          <i class="bi bi-download me-1"></i>Export
        </button>
        <button class="btn btn-outline-secondary" @click="handlePrint">
          <i class="bi bi-printer me-1"></i>Print
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, watch } from 'vue'

export default {
  name: 'SearchFilter',
  props: {
    placeholder: {
      type: String,
      default: 'Search...'
    },
    showExport: {
      type: Boolean,
      default: true
    }
  },
  emits: ['search', 'export', 'print'],
  setup(props, { emit }) {
    const searchQuery = ref('')

    const handleSearch = () => {
      emit('search', searchQuery.value)
    }

    const handleExport = () => {
      emit('export')
    }

    const handlePrint = () => {
      emit('print')
    }

    return {
      searchQuery,
      handleSearch,
      handleExport,
      handlePrint
    }
  }
}
</script>

<style scoped>
.search-filter {
  padding: 15px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>

