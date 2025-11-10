<template>
  <section id="header" class="d-flex align-items-center">
    <div class="container-fluid">
      <div class="row">
        <div class="col-10 mx-auto">
          <div class="row">
            <div class="col-md-6 pt-5 pt-lg-0 order-2 order-lg-1 d-flex justify-content-center">
              <h1>
                Welcome to <strong class="brand-name">HMS</strong> {{ name }}
              </h1>
              <h2 class="my-3">
                We are the team of talented health care Professionals
              </h2>
              <div class="mt-3">
                <router-link :to="visit" class="btn-get-started">
                  {{ btname }}
                </router-link>
              </div>
            </div>
            <div class="col-lg-6 order-1 order-lg-2 header-img"></div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'Common',
  props: {
    name: String,
    visit: String,
    btname: String
  }
}
</script>

<style scoped>
.brand-name {
  color: #3498db;
}
.btn-get-started {
  padding: 10px 30px;
  background-color: #3498db;
  color: white;
  text-decoration: none;
  border-radius: 5px;
}
.btn-get-started:hover {
  background-color: #2980b9;
  color: white;
}
</style>

