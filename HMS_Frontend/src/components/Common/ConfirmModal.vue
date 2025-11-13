<template>
  <div v-if="show" class="modal fade show" style="display: block;" tabindex="-1" @click.self="handleCancel">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header" :class="headerClass">
          <h5 class="modal-title">
            <i :class="iconClass + ' me-2'"></i>{{ title }}
          </h5>
          <button type="button" class="btn-close" @click="handleCancel"></button>
        </div>
        <div class="modal-body">
          <p>{{ message }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="handleCancel">
            <i class="bi bi-x-circle me-1"></i>Cancel
          </button>
          <button type="button" class="btn" :class="confirmButtonClass" @click="handleConfirm">
            <i :class="confirmIcon + ' me-1'"></i>{{ confirmText }}
          </button>
        </div>
      </div>
    </div>
    <div class="modal-backdrop fade show" @click="handleCancel"></div>
  </div>
</template>

<script>
export default {
  name: 'ConfirmModal',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: 'Confirm Action'
    },
    message: {
      type: String,
      required: true
    },
    type: {
      type: String,
      default: 'warning', // warning, danger, info, success
      validator: (value) => ['warning', 'danger', 'info', 'success'].includes(value)
    },
    confirmText: {
      type: String,
      default: 'Confirm'
    }
  },
  emits: ['confirm', 'cancel'],
  computed: {
    headerClass() {
      const classes = {
        warning: 'bg-warning text-dark',
        danger: 'bg-danger text-white',
        info: 'bg-info text-white',
        success: 'bg-success text-white'
      }
      return classes[this.type] || classes.warning
    },
    confirmButtonClass() {
      const classes = {
        warning: 'btn-warning',
        danger: 'btn-danger',
        info: 'btn-info',
        success: 'btn-success'
      }
      return classes[this.type] || 'btn-warning'
    },
    iconClass() {
      const icons = {
        warning: 'bi bi-exclamation-triangle',
        danger: 'bi bi-exclamation-circle',
        info: 'bi bi-info-circle',
        success: 'bi bi-check-circle'
      }
      return icons[this.type] || icons.warning
    },
    confirmIcon() {
      const icons = {
        warning: 'bi bi-check-circle',
        danger: 'bi bi-trash',
        info: 'bi bi-check',
        success: 'bi bi-check-circle'
      }
      return icons[this.type] || icons.warning
    }
  },
  methods: {
    handleConfirm() {
      this.$emit('confirm')
    },
    handleCancel() {
      this.$emit('cancel')
    }
  }
}
</script>

<style scoped>
.modal.show {
  display: block !important;
}
</style>

