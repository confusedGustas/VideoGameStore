<template>
  <div>
    <NavbarComponent />
  </div>

  <div class="search-bar">
    <input v-model="searchQuery" @keyup.enter="search" placeholder="Search Games..." />
    <button @click="search">Search</button>
  </div>

  <div class="search-results">
    <h2>Search Results</h2>
    <div class="game-grid">
      <div v-for="game in games" :key="game.id" class="game-card" @click="goToDetails(game.id)">
        <img :src="getImageUrl(game.image)" :alt="game.name" />
        <h3 class="game-title">{{ game.name }}</h3>
        <p class="price">${{ game.price.toFixed(2) }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import NavbarComponent from "@/components/NavbarComponent.vue";

const router = useRouter();
const route = useRoute();
const games = ref([]);
const searchQuery = ref('');

const fetchGames = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/games?search=${searchQuery.value}`);
    games.value = response.data.items;
  } catch (error) {
    console.error('Error fetching games:', error);
  }
};

const search = () => {
  fetchGames();
};

onMounted(() => {
  searchQuery.value = route.query.q || '';
  fetchGames();
});

watch(() => route.query.q, (newQuery) => {
  searchQuery.value = newQuery;
  fetchGames();
});

const goToDetails = (id) => {
  router.push({ name: 'details', params: { id } });
};

const getImageUrl = (imageName) => {
  return `http://localhost:8080/api/images/get/${imageName}`;
};
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
  background-color: #4CAF50;
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
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 2rem;
}

.game-card {
  background-color: white;
  border-radius: 8px;
  padding: 1rem;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
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
  color: #4CAF50;
}
</style>