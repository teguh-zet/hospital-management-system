<template>
  <div style="padding: 50px; text-align: center; font-family: Arial, sans-serif;">
    <h1 style="color: #3498db;">Hospital Management System</h1>
    <p style="font-size: 18px; color: #27ae60;">✅ Vue.js is working correctly!</p>
    <p>If you can see this, the application is running successfully.</p>
    <div style="margin-top: 30px;">
      <a href="/test" style="padding: 10px 20px; background: #3498db; color: white; text-decoration: none; border-radius: 5px; margin: 5px;">Go to Test Page</a>
      <a href="/home" style="padding: 10px 20px; background: #27ae60; color: white; text-decoration: none; border-radius: 5px; margin: 5px;">Go to Full Home</a>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SimpleHome',
  mounted() {
    console.log('SimpleHome component mounted successfully')
  }
}
</script>


