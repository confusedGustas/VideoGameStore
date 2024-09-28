<template>
  <div class="game-card">
    <img :src="imageUrl" :alt="game.name" class="game-image" v-if="imageUrl">
    <h3>{{ game.name }}</h3>
    <p>{{ game.description }}</p>
    <p>Price: ${{ game.price }}</p>
    <p>Release Year: {{ game.releaseYear }}</p>
    <p>Stock: {{ game.stock }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'GameCard',
  props: {
    game: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      imageUrl: null
    };
  },
  watch: {
    game: {
      handler(newGame) {
        if (newGame) {
          this.fetchGameImage(newGame.image);
        }
      },
      immediate: true
    }
  },
  methods: {
    async fetchGameImage(name) {
      try {
        const response = await axios.get(`http://localhost:8080/api/images/get/${name}`, {
          responseType: 'arraybuffer'
        });
        const blob = new Blob([response.data], {type: response.headers['content-type']});
        this.imageUrl = URL.createObjectURL(blob);
      } catch (error) {
        console.error('Error fetching image:', error);
        this.imageUrl = 'default-image.jpg';
      }
    }
  },
  beforeUnmount() {
    // Clean up the blob URL when the component is destroyed
    if (this.imageUrl && this.imageUrl.startsWith('blob:')) {
      URL.revokeObjectURL(this.imageUrl);
    }
  }
};
</script>

<style scoped>
.game-card {
  border: 1px solid #ddd;
  padding: 10px;
  margin: 10px;
  width: 200px;
}

.game-image {
  width: 100%;
  height: auto;
}
</style>