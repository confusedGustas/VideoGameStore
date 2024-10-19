<template>
  <div>
    <nav class="navbar">
      <div class="navbar-buttons">
        <button @click="goHome">Home</button>
        <button v-if="userLoggedIn" @click="logout" class="logout">
          Logout
        </button>
      </div>
      <h1 class="navbar-title">Profile</h1>
    </nav>

    <div v-if="userDetails">
      <div v-if="games.length > 0">
        <button class="add-game-button" @click="showAddGamePopup = true">
          Add Game
        </button>
        <div class="game-grid">
          <div v-for="game in games" :key="game.id" class="game-card">
            <img :src="getImageUrl(game.image)" :alt="game.name" />
            <h3 class="game-title">{{ game.name }}</h3>
            <div class="game-actions">
              <button @click="goToDetails(game.id)" class="details-button">
                Details
              </button>
              <button @click="deleteGame(game.id)" class="delete-button">
                Delete
              </button>
            </div>
          </div>
        </div>

        <div class="pagination-controls">
          <button @click="prevPage" :disabled="currentPage === 1">
            Previous
          </button>
          <span>Page {{ currentPage }}</span>
          <button @click="nextPage" :disabled="!hasMoreGames">Next</button>
        </div>
      </div>
      <div v-else>
        <p>No listings available.</p>
      </div>
    </div>

    <div v-else>
      <p>Redirecting to login...</p>
    </div>
  </div>

  <transition name="fade">
    <div v-if="showAddGamePopup" class="modal-overlay">
      <div class="modal-content">
        <h2>Add New Game</h2>
        <form @submit.prevent="saveGame">
          <label for="name">Name:</label>
          <input v-model="newGame.name" type="text" id="name" required />

          <label for="price">Price:</label>
          <input
            v-model="newGame.price"
            type="number"
            id="price"
            min="0"
            step="0.01"
            required
          />

          <label for="description">Description:</label>
          <textarea
            v-model="newGame.description"
            id="description"
            required
            maxlength="200"
            rows="4"
            class="fixed-textarea"
          ></textarea>
          <small>{{ newGame.description.length }}/200 characters</small>

          <label for="image">Image:</label>
          <input
            type="file"
            id="image"
            @change="handleImageUpload"
            accept="image/*"
            required
          />

          <label for="releaseYear">Release Year:</label>
          <input
            v-model="newGame.releaseYear"
            type="number"
            id="releaseYear"
            min="1950"
            :max="currentYear"
            required
          />

          <label for="region">Region:</label>
          <input v-model="newGame.region" type="text" id="region" required />

          <label for="platform">Platform:</label>
          <input
            v-model="newGame.platform"
            type="text"
            id="platform"
            required
          />

          <label for="publisher">Publisher:</label>
          <input
            v-model="newGame.publisher"
            type="text"
            id="publisher"
            required
          />

          <label for="genre">Genre:</label>
          <input v-model="newGame.genre" type="text" id="genre" required />

          <label for="stock">Stock:</label>
          <input
            v-model="newGame.stock"
            type="number"
            id="stock"
            min="0"
            required
          />

          <div class="modal-actions">
            <button type="submit">Save</button>
            <button @click="showAddGamePopup = false" type="button">
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const userDetails = ref(null)
const games = ref([])
const currentPage = ref(1)
const itemsPerPage = 10
const hasMoreGames = ref(true)
const userLoggedIn = ref(false)
const showAddGamePopup = ref(false)
const newGame = ref({
  name: '',
  price: null,
  description: '',
  image: null,
  releaseYear: null,
  region: '',
  platform: '',
  publisher: '',
  genre: '',
  stock: null,
  imageFile: null,
})

const currentYear = computed(() => new Date().getFullYear())

const generateUniqueFilename = originalName => {
  return `${Math.random().toString(36).slice(2, 11)}.${originalName.split('.').pop()}`
}

const handleImageUpload = event => {
  const files = event.target.files
  if (files && files.length > 0) {
    const lastFile = files[files.length - 1]
    newGame.value.image = generateUniqueFilename(lastFile.name)
    newGame.value.imageFile = lastFile
  }
}

const saveGame = async () => {
  try {
    const formData = new FormData()
    formData.append(
      'image',
      new File([newGame.value.imageFile], newGame.value.image),
    )
    await axios.post('/api/images/save', formData, {
      withCredentials: true,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    const gameData = { ...newGame.value }
    delete gameData.imageFile

    await axios.post('/api/games/save', gameData, {
      withCredentials: true,
    })

    showAddGamePopup.value = false
    await fetchGames(currentPage.value)
  } catch (error) {
    console.error('Error saving the game:', error)
  }
}

const checkUserLoggedIn = async () => {
  try {
    const response = await axios.get('/api/users/check', {
      withCredentials: true,
    })
    userLoggedIn.value = response.data.userLoggedIn
    return response.data.userLoggedIn
  } catch (error) {
    console.error('Error checking user login status:', error)
    return false
  }
}

const fetchUserDetails = async () => {
  try {
    const response = await axios.get('/api/users/get-details', {
      withCredentials: true,
    })
    userDetails.value = response.data
  } catch (error) {
    console.error('Error fetching user details:', error)
  }
}

const fetchGames = async (page = 1) => {
  try {
    const offset = page - 1
    const response = await axios.get('/api/users/get-games', {
      params: { offset },
      withCredentials: true,
    })
    games.value = response.data.items
    const totalGames = response.data.totalItems
    hasMoreGames.value =
      response.data.items.length === itemsPerPage &&
      currentPage.value * itemsPerPage < totalGames
  } catch (error) {
    console.error('Error fetching games:', error)
  }
}

const goToDetails = id => {
  router.push({ name: 'details', params: { id } })
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

const getImageUrl = imageName => {
  return `/api/images/get/${imageName}`
}

const goHome = () => {
  router.push({ name: 'home' })
}

const logout = async () => {
  try {
    await axios.post('/api/users/logout', {}, { withCredentials: true })
    userLoggedIn.value = false
    await router.push({ name: 'home' })
  } catch (error) {
    console.error('Error logging out:', error)
  }
}

const deleteGame = async gameId => {
  try {
    await axios.delete(`/api/games/delete`, {
      params: { gameId },
      withCredentials: true,
      headers: {},
    })
    await fetchGames(currentPage.value)
  } catch (error) {
    console.error('Error deleting game:', error)
  }
}

watch(showAddGamePopup, newVal => {
  if (newVal) {
    document.body.classList.add('no-scroll')
  } else {
    document.body.classList.remove('no-scroll')
  }
})

onMounted(async () => {
  const loggedIn = await checkUserLoggedIn()
  if (!loggedIn) {
    await router.push({ name: 'login' })
  } else {
    await fetchUserDetails()
    await fetchGames(currentPage.value)
  }
})
</script>

<style>
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

.game-title {
  font-size: 1.2rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

.navbar-title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  color: #333;
  font-size: 2.5rem;
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

.game-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 2rem;
}

.game-card {
  max-width: 240px;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: white;
  border-radius: 8px;
  padding: 1rem;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.game-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.game-card img {
  width: 100%;
  height: 300px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.game-card {
  font-weight: bold;
}

.game-actions {
  margin-top: auto;
  display: flex;
  justify-content: space-between;
}

.details-button,
.delete-button {
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.details-button {
  background-color: #2196f3;
  color: white;
}

.details-button:hover {
  background-color: #1976d2;
}

.delete-button {
  background-color: #f44336;
  color: white;
}

.delete-button:hover {
  background-color: #d32f2f;
}

.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2rem;
}

.fixed-textarea {
  width: 382px;
  height: 100px;
  resize: none;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-family: inherit;
  font-size: inherit;
}

.modal-content small {
  display: block;
  margin-bottom: 0.5rem;
  color: #666;
}

.pagination-controls button {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  margin: 0 1rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pagination-controls button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.pagination-controls span {
  font-size: 1.25rem;
  color: #333;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.modal-content::-webkit-scrollbar {
  display: none;
}

.modal-content h2 {
  margin-bottom: 1rem;
}

.modal-content form {
  display: flex;
  flex-direction: column;
}

.modal-content label {
  margin-top: 0.5rem;
}

.modal-content input,
.modal-content textarea {
  margin-bottom: 0.5rem;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
}

.modal-actions button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.modal-actions button[type='submit'] {
  background-color: #4caf50;
  color: white;
}

.modal-actions button[type='button'] {
  background-color: #f44336;
  color: white;
}

.add-game-button {
  padding: 0.5rem 1rem;
  margin-bottom: 1rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
.no-scroll {
  overflow: hidden;
}
</style>
