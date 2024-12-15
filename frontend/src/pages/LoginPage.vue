<template>
  <div class="container mx-auto px-4 max-w-md">
    <h1 class="text-3xl font-bold text-center mb-8">Login</h1>
    <form @submit.prevent="login" class="space-y-4">
      <input v-model="username" type="text" placeholder="Username" required class="w-full p-2 border border-gray-300 rounded-md" />
      <input v-model="password" type="password" placeholder="Password" required class="w-full p-2 border border-gray-300 rounded-md" />
      <Button type="submit" class="w-full">Login</Button>
    </form>
    <p class="mt-4 text-center">
      Don't have an account? <router-link to="/register" class="text-blue-600">Register here</router-link>
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import Button from '@/components/Button.vue'

const router = useRouter()
const username = ref('')
const password = ref('')

const login = async () => {
  try {
    await axios.post(
      '/api/users/login',
      { username: username.value, password: password.value },
      { withCredentials: true },
    )
    await router.push({ name: 'home' })
  } catch (error) {
    alert('Login failed. Please check your credentials and try again.')
    console.error('Login error:', error)
  }
}
</script>
