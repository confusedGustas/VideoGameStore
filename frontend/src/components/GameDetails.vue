<template>
  <div class="game-details">
    <h1 class="title">Video Game Store</h1>
    <div v-if="game" class="game-info">
      <div class="image-container">
        <img :src="getImageUrl(game.image)" :alt="game.name" />
      </div>
      <div class="info-container">
        <h2>{{ game.name }}</h2>
        <p class="price">${{ game.price.toFixed(2) }}</p>
        <div class="details">
          <p><strong>Genre:</strong> {{ game.genre.name }}</p>
          <p><strong>Publisher:</strong> {{ game.publisher.publisherName }}</p>
          <p><strong>Platform:</strong> {{ game.activationPlatform.platformName }}</p>
          <p><strong>Region:</strong> {{ game.activationRegion.regionName }}</p>
          <p><strong>Release Year:</strong> {{ game.releaseYear }}</p>
          <p><strong>Stock:</strong> {{ game.stock }} available</p>
        </div>
        <button @click="goBack" class="back-button">Back to Games</button>
      </div>
    </div>
    <div class="description-box" v-if="game">
      <p class="description">{{ game.description }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const route = useRoute();
const game = ref(null);

onMounted(async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/games/get/${route.params.id}`);
    game.value = response.data;
  } catch (error) {
    console.error('Error fetching game details:', error);
  }
});

const goBack = () => {
  router.push({ name: 'home' });
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
  margin-bottom: 2rem;
}

.game-info {
  display: flex;
  background-color: white;
  border-radius: 8px;
  padding: 2rem;
  max-width: 900px;
  margin: 0 auto;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.image-container {
  flex: 1;
  margin-right: 2rem;
}

.image-container img {
  width: 100%;
  height: auto;
  max-height: 400px;
  object-fit: cover;
  border-radius: 8px;
}

.info-container {
  flex: 2;
  display: flex;
  flex-direction: column;
  white-space: normal;
  overflow-wrap: break-word;
  word-wrap: break-word;
  overflow: hidden;
  text-overflow: ellipsis;
}

.info-container h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
  color: #333;
}

.price {
  font-size: 1.5rem;
  font-weight: bold;
  color: #4CAF50;
  margin-bottom: 1rem;
}

.details {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 1.5rem;
}

.details p {
  margin: 0.5rem 0;
  font-size: 0.9rem;
  color: #333;
}

.back-button {
  display: inline-block;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.back-button:hover {
  background-color: #45a049;
}

.description-box {
  background-color: #ffffff;
  border-radius: 8px;
  padding: 1.5rem;
  max-width: 900px;
  margin: 2rem auto 0 auto;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.description {
  font-size: 1rem;
  line-height: 1.6;
  color: #666;
  word-wrap: break-word;
  overflow-wrap: break-word;
  max-height: 150px;
  overflow-y: auto;
}
</style>