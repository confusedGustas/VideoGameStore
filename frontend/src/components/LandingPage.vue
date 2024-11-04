<template>
  <div>
    <NavbarComponent />
  </div>

  <div class="landing-page">
    <div v-if="Games.length > 0">
      <div class="search-bar">
        <input
          v-model="searchQuery"
          @keyup.enter="search"
          placeholder="Search Games..."
        />
        <button @click="search">Search</button>
      </div>

      <div class="filter-sort">
        <select v-model="selectedSortColumn" @change="fetchGames(currentPage)">
          <option value="">Sort By</option>
          <option value="name">Name</option>
          <option value="price">Price</option>
        </select>

        <select v-model="selectedSortOrder" @change="fetchGames(currentPage)">
          <option value="">Order</option>
          <option value="ASC">Ascending</option>
          <option value="DESC">Descending</option>
        </select>
      </div>

      <div class="game-grid">
        <div
          v-for="game in Games"
          :key="game.id"
          class="game-card"
          @click="goToDetails(game.id)"
        >
          <img :src="getImageUrl(game.id)" :alt="game.name" />
          <h3 class="game-title">{{ game.name }}</h3>
          <p class="price">{{ formatPrice(game.price) }}</p>
        </div>
      </div>

      <div class="pagination-controls">
        <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
        <span>Page {{ currentPage }}</span>
        <button @click="nextPage" :disabled="!hasMorePages">Next</button>
      </div>
    </div>

    <div v-else class="no-listings-message">
      <p>No listings available</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import NavbarComponent from '@/components/NavbarComponent.vue'

const router = useRouter()
const Games = ref([])
const searchQuery = ref('')
const currentPage = ref(1)
const hasMorePages = ref(true)
const itemsPerPage = 10
const selectedSortColumn = ref('')
const selectedSortOrder = ref('')

const fetchGames = async (page = 1) => {
  try {
    const offset = page - 1

    const params = {
      offset,
      ...(selectedSortColumn.value && { sortColumn: selectedSortColumn.value }),
      ...(selectedSortOrder.value && { sortOrder: selectedSortOrder.value }),
    }

    const response = await axios.get(`/api/games`, { params })

    if (response.data && response.data.items) {
      Games.value = response.data.items

      const totalItems = response.data.totalItems || 0
      hasMorePages.value =
        response.data.items.length === itemsPerPage &&
        currentPage.value * itemsPerPage < totalItems
    } else {
      Games.value = []
      hasMorePages.value = false
    }
  } catch (err) {
    Games.value = []
    hasMorePages.value = false
  }
}

onMounted(() => {
  fetchGames(currentPage.value)
})

const search = () => {
  if (searchQuery.value) {
    router.push({ name: 'search', query: { q: searchQuery.value } })
  }
}

const goToDetails = id => {
  router.push({ name: 'details', params: { id } })
}

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

const getImageUrl = imageId => {
  return `/api/images/get/${imageId}`
}

const formatPrice = price => {
  return price !== undefined ? `$${price.toFixed(2)}` : 'Price not available'
}
</script>

<style scoped>
.search-bar {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
  margin-top: 2rem;
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

.search-bar input {
  width: 500px;
  padding: 0.75rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px 0 0 4px;
  outline: none;
}

.search-bar input:focus {
  border-color: #4CAF50;
}

.game-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  justify-content: flex-start;
  margin-left: 0;
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

.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2rem;
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

.filter-sort {
  display: flex;
  justify-content: center;
  margin: 1rem 0;
  padding-left: 2rem;
}

.filter-sort select {
  padding: 0.75rem 1.75rem 0.75rem 1rem;
  font-size: 1rem;
  margin: 0 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: white;
  background-image: url('data:image/svg+xml;charset=UTF-8,%3Csvg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none" stroke="gray" stroke-width="2"%3E%3Cpath d="M5 8l5 5 5-5" stroke-linecap="round" stroke-linejoin="round"/%3E%3C/svg%3E');
  background-repeat: no-repeat;
  background-position: right 0.5rem top 50%;
  background-size: 10px;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}

.filter-sort select:focus {
  border-color: #4caf50;
  outline: none;
}

.landing-page p {
  font-size: 1rem;
  color: #555;
}

.no-listings-message {
  text-align: center;
  font-size: 1rem;
  color: #555;
}
</style>
