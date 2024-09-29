<template>
  <div class="game-card" v-if="game">
    <img v-if="imageUrl" :src="imageUrl" :alt="game.name" class="game-image" />
    <div v-else class="image-placeholder">No Image Available</div>
    <h3>{{ game.name }}</h3>
    <p class="description">{{ truncatedDescription }}</p>
    <p>Price: ${{ game.price?.toFixed(2) || 'N/A' }}</p>
    <p>Stock: {{ game.stock }}</p>
    <button @click="viewDetails" class="view-details-btn">View Details</button>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { defineProps } from 'vue';

const props = defineProps({
  game: {
    type: Object,
    required: true
  }
});

const router = useRouter();
const imageUrl = ref(null);

const truncatedDescription = computed(() => {
  return props.game?.description?.length > 40
      ? props.game.description.slice(0, 97) + '...'
      : props.game?.description || '';
});

const fetchGameImage = async (name) => {
  if (!name) return;
  try {
    const { data, headers } = await axios.get(`http://localhost:8080/api/images/get/${name}`, {
      responseType: 'arraybuffer'
    });
    const blob = new Blob([data], { type: headers['content-type'] });
    imageUrl.value = URL.createObjectURL(blob);
  } catch (error) {
    console.error('Error fetching image:', error);
    imageUrl.value = '/default-image.jpg';
  }
};

const viewDetails = () => {
  const {id} = props.game || {};
  if (id) {
    router.push({name: 'GameDetails', params: {id}});
  }
};

watch(() => props.game, (newGame) => {
  if (newGame?.image) {
    fetchGameImage(newGame.image);
  }
}, {immediate: true});

onUnmounted(() => {
  if (imageUrl.value?.startsWith('blob:')) {
    URL.revokeObjectURL(imageUrl.value);
  }
});
</script>

<style scoped>
.game-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  margin: 16px;
  width: 250px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.game-card:hover {
  transform: translateY(-5px);
}

.game-image, .image-placeholder {
  width: 100%;
  height: 300px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 12px;
}

.image-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f0f0;
  color: #666;
}

h3 {
  font-size: 1.2em;
  margin-bottom: 10px;
}

p {
  margin: 8px 0;
}

.description {
  font-size: 0.9em;
  color: #666;
}

.view-details-btn {
  background-color: #4a5568;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.view-details-btn:hover {
  background-color: #2d3748;
}
</style>
