<template>
  <div class="container mx-auto px-4">
    <NavbarComponent />
    <div class="mb-8 flex items-center gap-2 mt-4">
      <input
        v-model="searchQuery"
        @keyup.enter="search"
        placeholder="Search Games..."
        class="flex-1 p-2 border border-gray-300 rounded-md"
      />
      <Button @click="search" class="whitespace-nowrap">Search</Button>
    </div>
    <div class="flex justify-center space-x-4 mb-8">
      <select
        v-model="selectedSortColumn"
        @change="fetchGames(currentPage)"
        class="p-2 border border-gray-300 rounded-md"
      >
        <option value="">Sort By</option>
        <option value="name">Name</option>
        <option value="price">Price</option>
      </select>
      <select
        v-model="selectedSortOrder"
        @change="fetchGames(currentPage)"
        class="p-2 border border-gray-300 rounded-md"
      >
        <option value="">Order</option>
        <option value="ASC">Ascending</option>
        <option value="DESC">Descending</option>
      </select>
      <select
        v-model="selectedRating"
        @change="fetchGames(currentPage)"
        class="p-2 border border-gray-300 rounded-md"
      >
        <option value="">Rating</option>
        <option v-for="n in 5" :key="n" :value="n">
          {{ n }} <span class="text-yellow-500">{{ '★'.repeat(n) }}</span>
        </option>
      </select>
    </div>
    <div v-if="games.length > 0" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
      <GameCard
        v-for="game in games"
        :key="game.id"
        :game="game"
        @details="goToDetails"
      />
    </div>
    <div v-else class="text-center text-gray-700">
      No results found
    </div>
    <div v-if="shouldShowPagination" class="mt-6">
      <Pagination
        :current-page="currentPage"
        :has-more="hasMorePages"
        @prev="prevPage"
        @next="nextPage"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import NavbarComponent from '@/components/NavbarComponent.vue'
import GameCard from '@/components/GameCard.vue'
import Pagination from '@/components/Pagination.vue'
import Button from '@/components/Button.vue'

const router = useRouter()
const route = useRoute()
const games = ref([])
const searchQuery = ref('')
const currentPage = ref(1)
const hasMorePages = ref(true)
const itemsPerPage = 10
const selectedSortColumn = ref('')
const selectedSortOrder = ref('')
const selectedRating = ref('')
const totalItems = ref(0)

const fetchGames = async (page = 1) => {
  try {
    const offset = (page - 1)
    const params = {
      search: searchQuery.value,
      offset,
      limit: itemsPerPage,
      ...(selectedSortColumn.value && { sortColumn: selectedSortColumn.value }),
      ...(selectedSortOrder.value && { sortOrder: selectedSortOrder.value }),
      ...(selectedRating.value && { rating: selectedRating.value }),
    }
    const response = await axios.get(`/api/games`, { params })
    if (response.data && response.data.items) {
      games.value = response.data.items
      totalItems.value = response.data.totalItems || 0
      hasMorePages.value = games.value.length === itemsPerPage && currentPage.value * itemsPerPage < totalItems.value
    } else {
      games.value = []
      hasMorePages.value = false
    }
  } catch (error) {
    console.error('Error fetching games:', error)
    games.value = []
    hasMorePages.value = false
  }
}

const shouldShowPagination = computed(() => totalItems.value > itemsPerPage)

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
  router.push({name: 'details', params: {id}})
}
</script>
