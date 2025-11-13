<template>
  <nav aria-label="breadcrumb" class="mb-3">
    <ol class="breadcrumb">
      <li class="breadcrumb-item">
        <router-link to="/">
          <i class="bi bi-house-door me-1"></i>Home
        </router-link>
      </li>
      <li v-for="(item, index) in items" :key="index" class="breadcrumb-item" :class="{ active: index === items.length - 1 }">
        <router-link v-if="index !== items.length - 1 && item.path" :to="item.path">
          {{ item.label }}
        </router-link>
        <span v-else>{{ item.label }}</span>
      </li>
    </ol>
  </nav>
</template>

<script>
export default {
  name: 'Breadcrumbs',
  props: {
    items: {
      type: Array,
      required: true,
      validator: (items) => items.every(item => item.label)
    }
  }
}
</script>

<style scoped>
.breadcrumb {
  background: transparent;
  padding: 0;
  margin: 0;
}

.breadcrumb-item a {
  color: #3498db;
  text-decoration: none;
}

.breadcrumb-item a:hover {
  text-decoration: underline;
}

.breadcrumb-item.active {
  color: #6c757d;
}
</style>

