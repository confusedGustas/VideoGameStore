<template>
  <div>
    <NavbarComponent />
  </div>

  <div class="search-bar">
    <input
      v-model="searchQuery"
      @keyup.enter="search"
      placeholder="Search Games..."
    />
    <button @click="search">Search</button>
  </div>

  <div class="search-results">
    <h2>Search Results</h2>
    <div v-if="games.length > 0">
      <div class="game-grid">
        <div
          v-for="game in games"
          :key="game.id"
          class="game-card"
          @click="goToDetails(game.id)"
        >
          <img :src="getImageUrl(game.id)" :alt="game.name" />
          <h3 class="game-title">{{ game.name }}</h3>
          <p class="price">${{ game.price.toFixed(2) }}</p>
        </div>
      </div>

      <div class="pagination-controls">
        <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
        <span>Page {{ currentPage }}</span>
        <button @click="nextPage" :disabled="!hasMorePages">Next</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import NavbarComponent from '@/components/NavbarComponent.vue'

const router = useRouter()
const route = useRoute()
const games = ref([])
const searchQuery = ref('')
const currentPage = ref(1)
const hasMorePages = ref(true)
const itemsPerPage = 10

const fetchGames = async (page = 1) => {
  try {
    const offset = (page - 1)
    const response = await axios.get(`/api/games`, {
      params: {
        search: searchQuery.value,
        offset,
        limit: itemsPerPage
      }
    })

    games.value = response.data.items || []

    const totalItems = response.data.totalItems || 0
    hasMorePages.value = games.value.length === itemsPerPage && currentPage.value * itemsPerPage < totalItems
  } catch (error) {
    console.error('Error fetching games:', error)
    games.value = []
    hasMorePages.value = false
  }
}

const search = () => {
  currentPage.value = 1
  fetchGames(currentPage.value)
}

onMounted(() => {
  searchQuery.value = route.query.q || ''
  fetchGames(currentPage.value)
})

watch(
  () => route.query.q,
  (newQuery) => {
    searchQuery.value = newQuery
    fetchGames(currentPage.value)
  }
)

const nextPage = () => {
  if (hasMorePages.value) {
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

const goToDetails = (id) => {
  router.push({ name: 'details', params: { id } })
}

const getImageUrl = (imageId) => {
  return `/api/images/get/${imageId}`
}
</script>

<style scoped>
.search-bar {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
  margin-top: 2rem;
}

.search-bar input {
  width: 500px;
  padding: 0.75rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px 0 0 4px;
}

.search-bar button {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-bar button:hover {
  background-color: #45a049;
}

h2 {
  text-align: center;
  color: #666;
  margin-bottom: 46px;
}

.game-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  justify-content: flex-start;
  margin-left: 0;
  margin-top: -0.88rem;
}

.game-card {
  background-color: white;
  border-radius: 8px;
  padding: 1rem;
  cursor: pointer;
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

.game-title {
  font-size: 1.2rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

.game-card h3 {
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
}

.game-card p {
  margin: 0.25rem 0;
  font-size: 0.9rem;
}

.game-card .price {
  font-weight: bold;
  color: #4caf50;
}
</style>
