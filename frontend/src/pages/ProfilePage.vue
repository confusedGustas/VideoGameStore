<template>
  <div class="container mx-auto px-4 py-4">
    <nav class="flex justify-between items-center mb-8">
      <router-link to="/profile" class="text-2xl font-bold">Profile</router-link>
      <div class="space-x-4">
        <Button @click="goHome">Home</Button>
        <Button @click="goToPurchaseHistory">Purchase History</Button>
        <Button v-if="isAdmin" @click="goToAdminPanel">Admin Panel</Button>
        <Button v-if="userLoggedIn" @click="logout">Logout</Button>
      </div>
    </nav>

    <div class="mb-8 space-x-4">
      <Button @click="showChangeUsernameModal = true">Change Username</Button>
      <Button @click="showChangeEmailModal = true">Change Email</Button>
      <Button @click="showChangePasswordModal = true">Change Password</Button>
    </div>

    <Modal :show="showChangeUsernameModal" title="Change Username" @close="showChangeUsernameModal = false" @confirm="updateUsername">
      <input v-model="newUsername" type="text" placeholder="New Username" class="w-full p-2 border border-gray-300 rounded-md" required />
    </Modal>

    <Modal :show="showChangeEmailModal" title="Change Email" @close="showChangeEmailModal = false" @confirm="updateEmail">
      <input v-model="newEmail" type="email" placeholder="New Email" class="w-full p-2 border border-gray-300 rounded-md" required />
    </Modal>

    <Modal :show="showChangePasswordModal" title="Change Password" @close="showChangePasswordModal = false" @confirm="updatePassword">
      <input v-model="newPassword" type="password" placeholder="New Password" class="w-full p-2 border border-gray-300 rounded-md" required />
    </Modal>

    <div v-if="userDetails">
      <Button @click="showAddGamePopup = true" class="mb-8">Add Game</Button>

      <div v-if="games.length > 0" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <div v-for="game in games" :key="game.id" class="bg-white shadow-md rounded-lg overflow-hidden">
          <img :src="getImageUrl(game.id)" :alt="game.name" class="w-full h-48 object-cover" />
          <div class="p-4">
            <h3 class="text-lg font-semibold mb-2 truncate">{{ game.name }}</h3>
            <div class="space-y-2">
              <Button @click="goToDetails(game.id)" variant="secondary" class="w-full">Details</Button>
              <Button @click="editGame(game.id)" class="w-full">Edit</Button>
              <Button @click="deleteGame(game.id)" variant="danger" class="w-full">Delete</Button>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="text-center text-gray-600">
        <p>No listings available</p>
      </div>

      <div v-if="shouldShowPagination" class="mt-8">
        <Pagination
          :current-page="currentPage"
          :has-more="hasMoreGames"
          @prev="prevPage"
          @next="nextPage"
        />
      </div>
    </div>

    <Modal :show="showAddGamePopup" title="Add New Game" @close="showAddGamePopup = false" @confirm="saveGame">
      <GameForm v-model="newGame" />
    </Modal>

    <Modal :show="showEditGamePopup" title="Edit Game" @close="showEditGamePopup = false" @confirm="updateGame">
      <GameForm v-model="editingGame" ref="gameFormRef" />
    </Modal>
  </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue'
import {useRouter} from 'vue-router'
import axios from 'axios'
import Button from '@/components/Button.vue'
import Modal from '@/components/Modal.vue'
import Pagination from '@/components/Pagination.vue'
import GameForm from '@/components/GameForm.vue'

const router = useRouter()
const userDetails = ref(null)
const games = ref([])
const currentPage = ref(1)
const itemsPerPage = 10
const hasMoreGames = ref(true)
const userLoggedIn = ref(false)
const showAddGamePopup = ref(false)
const showEditGamePopup = ref(false)
const newGame = ref({
  name: '',
  price: null,
  description: '',
  releaseYear: null,
  region: '',
  platform: '',
  publisher: '',
  genre: '',
  stock: null,
  imageFile: null,
})
const editingGame = ref({})
const showChangeUsernameModal = ref(false)
const showChangeEmailModal = ref(false)
const showChangePasswordModal = ref(false)
const newUsername = ref('')
const newEmail = ref('')
const newPassword = ref('')
const isAdmin = ref(false)
const totalItems = ref(0)
const gameFormRef = ref(null)

const fetchUserDetails = async () => {
  try {
    const response = await axios.get('/api/users/get-details', {withCredentials: true})
    userDetails.value = response.data
  } catch (error) {
    console.error('Error fetching user details:', error)
  }
}

const fetchGames = async (page = 1) => {
  try {
    const offset = (page - 1)
    const response = await axios.get('/api/users/get-games', {
      params: {offset},
      withCredentials: true,
    })
    games.value = response.data.items
    totalItems.value = response.data.totalItems
    hasMoreGames.value = games.value.length === itemsPerPage && currentPage.value * itemsPerPage < totalItems.value
  } catch (error) {
    console.error('Error fetching games:', error)
  }
}

const shouldShowPagination = computed(() => totalItems.value > itemsPerPage)

const goToDetails = (id) => {
  router.push({name: 'details', params: {id}})
}

const nextPage = () => {
  if (hasMoreGames.value) {
    currentPage.value++
    fetchGames(currentPage.value)
  }
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    fetchGames(currentPage.value)
  }
}

const getImageUrl = (imageId) => `/api/images/get/${imageId}`

const goHome = () => router.push({name: 'home'})

const goToAdminPanel = () => router.push({name: 'admin'})

const goToPurchaseHistory = () => router.push({ name: 'purchaseHistory' })

const logout = async () => {
  try {
    await axios.post('/api/users/logout', {}, {withCredentials: true})
    userLoggedIn.value = false
    await router.push({name: 'home'})
  } catch (error) {
    console.error('Error logging out:', error)
  }
}

const deleteGame = async (gameId) => {
  try {
    await axios.delete(`/api/games/delete`, {
      params: {gameId},
      withCredentials: true,
    })
    await fetchGames(currentPage.value)
  } catch (error) {
    console.error('Error deleting game:', error)
  }
}

const editGame = async (gameId) => {
  try {
    editingGame.value = { id: gameId }
    showEditGamePopup.value = true
  } catch (error) {
    console.error('Error fetching game details:', error)
  }
}

const saveGame = async () => {
  try {
    const formData = new FormData()
    Object.keys(newGame.value).forEach(key => {
      if (key === 'imageFile' && newGame.value[key]) {
        formData.append('image', newGame.value[key])
      } else {
        formData.append(key, newGame.value[key])
      }
    })

    await axios.post('/api/games/save', formData, {
      withCredentials: true,
      headers: {'Content-Type': 'multipart/form-data'},
    })

    newGame.value = {
      name: '',
      price: null,
      description: '',
      releaseYear: null,
      region: '',
      platform: '',
      publisher: '',
      genre: '',
      stock: null,
      imageFile: null,
    }

    showAddGamePopup.value = false
    window.location.reload()
  } catch (error) {
    console.error('Error saving the game:', error)
  }
}

const updateGame = async () => {
  try {
    const formData = new FormData()
    Object.keys(editingGame.value).forEach(key => {
      if (key !== 'imageFile') {
        formData.append(key, editingGame.value[key])
      }
    })

    if (editingGame.value.imageFile) {
      formData.append('image', editingGame.value.imageFile)
    } else if (gameFormRef.value && gameFormRef.value.currentImageBlob) {
      formData.append('image', gameFormRef.value.currentImageBlob, 'image.jpg')
    }

    await axios.post(`/api/games/update-game?gameId=${editingGame.value.id}`, formData, {
      withCredentials: true,
      headers: { 'Content-Type': 'multipart/form-data' },
    })

    showEditGamePopup.value = false
    window.location.reload()
  } catch (error) {
    console.error('Error updating the game:', error)
  }
}

const updateUsername = async () => {
  try {
    await axios.post('/api/users/update-username', {username: newUsername.value})
    showChangeUsernameModal.value = false
    newUsername.value = ''
    await fetchUserDetails()
    window.location.reload()
  } catch (error) {
    console.error('Error updating username:', error)
  }
}

const updateEmail = async () => {
  try {
    await axios.post('/api/users/update-email', {email: newEmail.value})
    showChangeEmailModal.value = false
    newEmail.value = ''
    await fetchUserDetails()
    window.location.reload()
  } catch (error) {
    console.error('Error updating email:', error)
  }
}

const updatePassword = async () => {
  try {
    await axios.post('/api/users/update-password', {password: newPassword.value})
    showChangePasswordModal.value = false
    newPassword.value = ''
    window.location.reload()
  } catch (error) {
    console.error('Error updating password:', error)
  }
}

const checkUserLoggedIn = async () => {
  try {
    const response = await axios.get('/api/users/check', {withCredentials: true})
    userLoggedIn.value = response.data.userLoggedIn
    isAdmin.value = response.data.isAdmin
    return response.data.userLoggedIn
  } catch (error) {
    console.error('Error checking user login status:', error)
    return false
  }
}

onMounted(async () => {
  const isLoggedIn = await checkUserLoggedIn()
  if (isLoggedIn) {
    await fetchUserDetails()
    await fetchGames(currentPage.value)
  } else {
    await router.push({name: 'login'})
  }
})
</script>

