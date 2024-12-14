<template>
  <div class="login-page">
    <h1 class="title">Login</h1>
    <form @submit.prevent="login">
      <input v-model="username" type="text" placeholder="Username" required />
      <input
        v-model="password"
        type="password"
        placeholder="Password"
        required
      />
      <button type="submit">Login</button>
    </form>
    <p>
      Don't have an account?
      <router-link to="/register">Register here</router-link>
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

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

<style scoped>
.title {
  text-align: center;
  margin-bottom: 20px;
}

.login-page {
  max-width: 400px;
  margin: auto;
  padding: 1rem;
  background: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

form {
  display: flex;
  flex-direction: column;
}

input {
  margin-bottom: 1rem;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 0.75rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
</style>
