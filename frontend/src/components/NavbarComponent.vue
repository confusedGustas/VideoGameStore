<template>
  <nav class="navbar">
    <router-link to="/" class="navbar-title" exact-active-class="active-link"
      >Video Game Store</router-link
    >
    <div class="navbar-buttons">
      <button v-if="!isAuthenticated" @click="goToLogin">Login</button>
      <button v-if="!isAuthenticated" @click="goToRegister">Register</button>
      <button v-if="isAuthenticated" @click="goToProfile">Profile</button>
      <button v-if="isAuthenticated" @click="logout">Logout</button>
    </div>
  </nav>
</template>

<script setup>
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ref } from 'vue'

const router = useRouter()
const isAuthenticated = ref(false)

const checkLogin = async () => {
  const response = await axios.get('/api/users/check', {
    withCredentials: true,
  })
  isAuthenticated.value = response.data.userLoggedIn
}

const logout = async () => {
  await axios.post('/api/users/logout', {}, { withCredentials: true })

  if (window.location.pathname === '/profile') {
    await router.push({ name: 'home' })
  } else window.location.reload()
}

const goToLogin = () => {
  router.push({ name: 'login' })
}

const goToRegister = () => {
  router.push({ name: 'register' })
}

const goToProfile = () => {
  router.push({ name: 'profile' })
}

checkLogin()
</script>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  padding: 1.5rem;
  background-color: white;
  border-radius: 6px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: relative;
  margin-bottom: 2rem;
}

.navbar-title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  color: #333;
  font-size: 2.5rem;
  text-decoration: none;
  font-weight: bold;
}

.navbar-title:hover {
  color: #4caf50;
}

.navbar-buttons {
  margin-left: auto;
}

.navbar-buttons button {
  margin-left: 10px;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.navbar-buttons button:hover {
  background-color: #45a049;
}
</style>
