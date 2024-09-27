<template>
  <div class="container mx-auto p-4">
    <h1 class="text-4xl font-bold mb-6 text-center text-gray-800">Video Game Store</h1>

    <div class="mb-6">
      <input
          v-model="searchQuery"
          @input="searchGames"
          class="w-full p-2 border rounded"
          placeholder="Search games..."
      />
    </div>

    <div class="mb-6 flex justify-between items-center">
      <div>
        <label class="mr-2">Sort by:</label>
        <select v-model="sortBy" @change="fetchGames" class="p-2 border rounded">
          <option value="name">Name</option>
          <option value="price">Price</option>
          <option value="releaseYear">Release Year</option>
        </select>
      </div>
      <div>
        <label class="mr-2">Order:</label>
        <select v-model="sortOrder" @change="fetchGames" class="p-2 border rounded">
          <option value="asc">Ascending</option>
          <option value="desc">Descending</option>
        </select>
      </div>
    </div>

    <GameList
        :games="games"
        @delete-game="deleteGame"
    />

    <div class="mt-8">
      <h2 class="text-2xl font-bold mb-4">Add New Game</h2>
      <form @submit.prevent="addGame" class="space-y-4">
        <input v-model="newGame.name" placeholder="Name" class="w-full p-2 border rounded" required />
        <input v-model="newGame.price" type="number" step="0.01" placeholder="Price" class="w-full p-2 border rounded" required />
        <input v-model="newGame.description" placeholder="Description" class="w-full p-2 border rounded" required />
        <input v-model="newGame.image" placeholder="Image URL" class="w-full p-2 border rounded" required />
        <input v-model="newGame.releaseYear" type="number" placeholder="Release Year" class="w-full p-2 border rounded" required />
        <input v-model="newGame.stock" type="number" placeholder="Stock" class="w-full p-2 border rounded" required />
        <button type="submit" class="w-full bg-blue-500 text-white p-2 rounded hover:bg-blue-600">Add Game</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import GameList from './components/GameList.vue'

const games = ref([])
const searchQuery = ref('')
const sortBy = ref('name')
const sortOrder = ref('asc')

const newGame = ref({
  name: '',
  price: '',
  description: '',
  image: '',
  releaseYear: '',
  stock: ''
})

const fetchGames = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/games?sortBy=${sortBy.value}&sortOrder=${sortOrder.value}`)
    if (!response.ok) {
      throw new Error('Network response was not ok')
    }
    games.value = await response.json()
  } catch (error) {
    console.error('Error fetching games:', error)
  }
}

const searchGames = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/games/search?query=${searchQuery.value}`)
    if (!response.ok) {
      throw new Error('Network response was not ok')
    }
    games.value = await response.json()
  } catch (error) {
    console.error('Error searching games:', error)
  }
}

const addGame = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/games', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newGame.value),
    })
    if (!response.ok) {
      throw new Error('Network response was not ok')
    }
    const addedGame = await response.json()
    games.value.push(addedGame)
    newGame.value = { name: '', price: '', description: '', image: '', releaseYear: '', stock: '' }
  } catch (error) {
    console.error('Error adding game:', error)
  }
}

const deleteGame = async (id) => {
  try {
    const response = await fetch(`http://localhost:8080/api/games/${id}`, {
      method: 'DELETE',
    })
    if (!response.ok) {
      throw new Error('Network response was not ok')
    }
    games.value = games.value.filter(game => game.id !== id)
  } catch (error) {
    console.error('Error deleting game:', error)
  }
}

onMounted(fetchGames)
</script>