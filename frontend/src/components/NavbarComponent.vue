<template>
  <nav class="bg-white shadow-md p-4">
    <div class="container mx-auto flex justify-between items-center">
      <router-link to="/" class="text-2xl font-bold text-black">
        Video Game Store
      </router-link>
      <div class="flex space-x-4">
        <Button v-if="!isLandingPage" @click="goToHome">Home</Button>
        <Button v-if="!isAuthenticated" @click="goToLogin">Login</Button>
        <Button v-if="!isAuthenticated" @click="goToRegister">Register</Button>
        <Button v-if="isAuthenticated" @click="goToProfile">Profile</Button>
        <Button v-if="isAuthenticated" @click="logout">Logout</Button>
        <Button @click="goToCheckout" class="flex items">
          <ShoppingCartIcon class="w-5 h-5 mr-2"/>
          Checkout
        </Button>
      </div>
    </div>
  </nav>
</template>

<script setup>
import {ref, computed} from 'vue'
import {useRouter, useRoute} from 'vue-router'
import axios from 'axios'
import {ShoppingCartIcon} from 'lucide-vue-next'
import Button from './Button.vue'

const router = useRouter()
const route = useRoute()
const isAuthenticated = ref(false)

const checkLogin = async () => {
  try {
    const response = await axios.get('/api/users/check', {withCredentials: true})
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

const goToLogin = () => router.push({name: 'login'})
const goToRegister = () => router.push({name: 'register'})
const goToProfile = () => router.push({name: 'profile'})
const goToHome = () => router.push({name: 'home'})
const goToCheckout = async () => {
  if (isAuthenticated.value) {
    await router.push({name: 'checkout'})
  } else {
    await router.push({name: 'login', query: {redirect: 'checkout'}})
  }
}

const isLandingPage = computed(() => route.name === 'home')

checkLogin()
</script>
