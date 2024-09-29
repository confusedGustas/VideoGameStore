<template>
  <div v-if="game" class="game-details">
    <h1>{{ game.name }}</h1>
    <img :src="imageUrl" :alt="game.name" class="game-image" />
    <p class="description">{{ game.description }}</p>
    <div class="game-info">
      <p><strong>Price:</strong> ${{ game.price?.toFixed(2) }}</p>
      <p><strong>Release Year:</strong> {{ game.releaseYear }}</p>
      <p><strong>Stock:</strong> {{ game.stock }}</p>
    </div>
    <button @click="goBack" class="back-btn">Back to Search</button>
  </div>
  <div v-else class="loading">Loading game details...</div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useStore } from 'vuex';
import axios from 'axios';

const route = useRoute();
const router = useRouter();
const store = useStore();

const game = ref(null);
const imageUrl = ref('/default-image.jpg'); // Set default image

const fetchGameDetails = async (id) => {
  try {
    game.value = await store.dispatch('games/fetchGameById', id);
    await fetchGameImage(game.value.image);
  } catch (error) {
    console.error('Error fetching game details:', error);
  }
};

const fetchGameImage = async (name) => {
  if (!name) return; // Early return if no name provided
  try {
    const { data, headers } = await axios.get(`http://localhost:8080/api/images/get/${name}`, {
      responseType: 'arraybuffer'
    });
    const blob = new Blob([data], { type: headers['content-type'] });
    imageUrl.value = URL.createObjectURL(blob);
  } catch (error) {
    console.error('Error fetching image:', error);
  }
};

const goBack = () => router.go(-1);

onMounted(() => fetchGameDetails(route.params.id));

onUnmounted(() => {
  if (imageUrl.value.startsWith('blob:')) {
    URL.revokeObjectURL(imageUrl.value);
  }
});
</script>

<style scoped>
.game-details {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
.game-image {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
.description {
  font-size: 1.1em;
  line-height: 1.6;
  margin: 20px 0;
}
.game-info {
  background-color: #f0f0f0;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
}
.back-btn {
  background-color: #4a5568;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}
.back-btn:hover {
  background-color: #2d3748;
}
.loading {
  text-align: center;
  font-size: 1.2em;
  margin-top: 50px;
}
</style>