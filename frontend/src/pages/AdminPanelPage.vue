<template>
  <div class="container mx-auto px-4 py-4">
    <nav class="flex justify-between items-center mb-8">
      <router-link to="/admin" class="text-2xl font-bold">Admin Panel</router-link>
      <div class="space-x-4">
        <Button @click="goHome">Home</Button>
        <Button v-if="userLoggedIn" @click="logout">Logout</Button>
      </div>
    </nav>

    <div class="mb-6 flex space-x-4">
      <input
        type="text"
        v-model="search"
        placeholder="Search users"
        class="flex-grow p-2 border border-gray-300 rounded-md"
      />
      <Button @click="fetchUsers">Search</Button>
    </div>

    <div class="overflow-x-auto">
      <table class="w-full table-auto border-collapse">
        <thead>
        <tr class="bg-gray-100 text-center">
          <th class="p-3">Username</th>
          <th class="p-3">Email</th>
          <th class="p-3">Role</th>
          <th class="p-3">Status</th>
          <th class="p-3">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr
          v-for="user in users"
          :key="user.id"
          class="border-b text-center"
        >
          <td class="p-3">{{ user.username }}</td>
          <td class="p-3">{{ user.email }}</td>
          <td class="p-3">{{ user.role }}</td>
          <td class="p-3">
            <span class="inline-block w-20">{{ user.enabled ? 'Enabled' : 'Disabled' }}</span>
          </td>
          <td class="p-3">
            <div class="flex justify-center">
              <Button
                @click="toggleUserStatus(user)"
                :class="['w-24', user.enabled ? 'bg-red-500' : 'bg-green-500']"
              >
                {{ user.enabled ? 'Disable' : 'Enable' }}
              </Button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div v-if="shouldShowPagination" class="mt-6">
      <Pagination
        :current-page="currentPage"
        :has-more="hasMoreUsers"
        @prev="prevPage"
        @next="nextPage"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import Button from '@/components/Button.vue'
import Pagination from '@/components/Pagination.vue'

const router = useRouter()
const users = ref([])
const offset = ref(0)
const limit = ref(20)
const search = ref('')
const currentPage = ref(1)
const hasMoreUsers = ref(true)
const userLoggedIn = ref(false)
const isAdmin = ref(false)
const totalUsers = ref(0)

const fetchUsers = async () => {
  try {
    const response = await axios.get('/api/admin/get-users', {
      params: {
        limit: limit.value,
        offset: offset.value,
        search: search.value,
      },
    })

    users.value = response.data.users
    totalUsers.value = response.data.totalUsers
    hasMoreUsers.value = users.value.length === limit.value
  } catch (error) {
    console.error('Error fetching users:', error)
  }
}

const nextPage = () => {
  offset.value += 1
  currentPage.value++
  fetchUsers()
}

const prevPage = () => {
  if (offset.value >= 1) {
    offset.value -= 1
    currentPage.value--
    fetchUsers()
  }
}

const toggleUserStatus = async (user) => {
  try {
    const userId = user.id
    const state = !user.enabled
    const url = `/api/admin/change-user-status?userId=${userId}&state=${state}`
    await axios.post(url)
    user.enabled = state
  } catch (error) {
    console.error('Error toggling user status:', error)
  }
}

const goHome = () => {
  router.push({ name: 'home' })
}

const logout = async () => {
  try {
    await axios.post('/api/users/logout', {}, { withCredentials: true })
    await router.push({name: 'home'})
  } catch (error) {
    console.error('Error logging out:', error)
  }
}

const checkUserLoggedIn = async () => {
  try {
    const response = await axios.get('/api/users/check', { withCredentials: true })
    userLoggedIn.value = response.data.userLoggedIn
    isAdmin.value = response.data.admin

    if (!response.data.userLoggedIn) {
      await router.push({ name: 'login' })
    }

    return response.data.userLoggedIn
  } catch (error) {
    console.error('Error checking user login status:', error)
    await router.push({ name: 'login' })
    return false
  }
}

const shouldShowPagination = computed(() => totalUsers.value > limit.value)

onMounted(async () => {
  const loggedIn = await checkUserLoggedIn()
  if (loggedIn) {
    await fetchUsers()
  }
})
</script>
