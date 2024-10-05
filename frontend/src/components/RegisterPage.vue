<template>
  <div class="register-page">
    <h1 class="title">Register</h1>
    <form @submit.prevent="register">
      <input v-model="username" type="text" placeholder="Username" required />
      <input v-model="email" type="email" placeholder="Email" required />
      <input v-model="password" type="password" placeholder="Password" required />
      <button type="submit">Register</button>
    </form>
    <p>Already have an account? <router-link to="/login">Login here</router-link></p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const username = ref('');
const email = ref('');
const password = ref('');

const register = async () => {
  try {
    await axios.post('http://localhost:8080/api/users/register', {
      username: username.value,
      email: email.value,
      password: password.value,
    });
    await router.push({name: 'login'});
  } catch (error) {
    console.error('Registration error:', error);
    alert('Registration failed. Please try again.');
  }
};
</script>

<style scoped>
.title {
  text-align: center;
  margin-bottom: 20px;
}

.register-page {
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
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
</style>
