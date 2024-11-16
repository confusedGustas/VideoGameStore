<template>
  <nav class="navbar">
    <router-link to="/" class="navbar-title" exact-active-class="active-link">
      Video Game Store
    </router-link>
    <div class="navbar-buttons">
      <button v-if="!isAuthenticated" @click="goToLogin">Login</button>
      <button v-if="!isAuthenticated" @click="goToRegister">Register</button>
      <button v-if="isAuthenticated" @click="goToProfile">Profile</button>
      <button v-if="isAuthenticated" @click="logout">Logout</button>
      <button @click="goToCheckout" class="checkout-button">
        <ShoppingCartIcon class="checkout-icon" />
        Checkout
      </button>
    </div>
  </nav>
</template>

<script setup>
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ref } from 'vue'
import { ShoppingCartIcon } from 'lucide-vue-next'

const router = useRouter()
const isAuthenticated = ref(false)

const checkLogin = async () => {
  try {
    const response = await axios.get('/api/users/check', {
      withCredentials: true,
    })
    isAuthenticated.value = response.data.userLoggedIn
  } catch (error) {
    console.error('Error checking login status:', error)
    isAuthenticated.value = false
  }
}

const logout = async () => {
  try {
    await axios.post('/api/users/logout', {}, {withCredentials: true})
    isAuthenticated.value = false
    if (router.currentRoute.value.name === 'profile') {
      await router.push({name: 'home'})
    } else {
      await router.go(0)
    }
  } catch (error) {
    console.error('Error logging out:', error)
  }
}

const goToLogin = () => {
  router.push({name: 'login'})
}

const goToRegister = () => {
  router.push({name: 'register'})
}

const goToProfile = () => {
  router.push({name: 'profile'})
}

const goToCheckout = async () => {
  if (isAuthenticated.value) {
    await router.push({name: 'checkout'})
  } else {
    await router.push({name: 'login', query: {redirect: 'checkout'}})
  }
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
  display: flex;
  align-items: center;
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

.checkout-button {
  display: flex;
  align-items: center;
  background-color: #f0c14b !important;
  color: #111 !important;
}

.checkout-button:hover {
  background-color: #ddb347 !important;
}

.checkout-icon {
  margin-right: 0.5rem;
  width: 1.2rem;
  height: 1.2rem;
}
</style>
