<template>
  <div class="search-results">
    <h1 class="title">Video Game Store</h1>
    <h2>Search Results for "{{ searchQuery }}"</h2>
    <div class="game-grid">
      <div v-for="game in games" :key="game.id" class="game-card" @click="goToDetails(game.id)">
        <img :src="getImageUrl(game.image)" :alt="game.name" />
        <h3>{{ game.name }}</h3>
        <p class="price">${{ game.price.toFixed(2) }}</p>
        <p class="genre">{{ game.genre.name }}</p>
        <p class="publisher">{{ game.publisher.publisherName }}</p>
        <p class="platform">{{ game.activationPlatform.platformName }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

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

onMounted(() => {
  searchQuery.value = route.query.q;
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
.title {
  text-align: center;
  color: #333;
  font-size: 2.5rem;
  margin-bottom: 1rem;
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
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.game-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.game-card img {
  width: 100%;
  height: 300px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 1rem;
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

.game-card .genre,
.game-card .publisher,
.game-card .platform {
  color: #666;
}
</style>