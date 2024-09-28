<template>
  <div v-if="game" class="game-details">
    <h1>{{ game.name }}</h1>
    <img :src="game.image" :alt="game.name" class="game-image">
    <p>{{ game.description }}</p>
    <p>Price: ${{ game.price }}</p>
    <p>Release Year: {{ game.releaseYear }}</p>
    <p>Stock: {{ game.stock }}</p>
    <button @click="goBack">Back to Search</button>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';

export default {
  name: 'GameDetails',
  computed: {
    ...mapState('games', ['selectedGame']),
    game() {
      return this.selectedGame;
    }
  },
  methods: {
    ...mapActions('games', ['fetchGameById']),
    goBack() {
      this.$router.go(-1);
    }
  },
  created() {
    const gameId = this.$route.params.id;
    this.fetchGameById(gameId);
  }
}
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
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}
</style>