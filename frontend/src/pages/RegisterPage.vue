<template>
  <div class="container mx-auto px-4 max-w-md">
    <h1 class="text-3xl font-bold text-center mb-8">Register</h1>
    <form @submit.prevent="register" class="space-y-4">
      <input v-model="username" type="text" placeholder="Username" required class="w-full p-2 border border-gray-300 rounded-md" />
      <input v-model="email" type="email" placeholder="Email" required class="w-full p-2 border border-gray-300 rounded-md" />
      <input v-model="password" type="password" placeholder="Password" required class="w-full p-2 border border-gray-300 rounded-md" />
      <Button type="submit" class="w-full">Register</Button>
    </form>
    <p class="mt-4 text-center">
      Already have an account? <router-link to="/login" class="text-blue-600">Login here</router-link>
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
const email = ref('')
const password = ref('')

const register = async () => {
  try {
    await axios.post('/api/users/register', {
      username: username.value,
      email: email.value,
      password: password.value,
    })
    await router.push({ name: 'login' })
  } catch (error) {
    console.error('Registration error:', error)
    alert('Registration failed. Please try again.')
  }
}
</script>
