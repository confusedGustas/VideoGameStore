<template>
  <div class="game-list">
    <game-card v-for="game in games" :key="game.id" :game="game" />
  </div>
</template>

<script>
import { mapState } from 'vuex';
import GameCard from './GameCard.vue';

export default {
  name: 'GameList',
  components: {
    GameCard
  },
  computed: {
    ...mapState('games', ['games'])
  }
}
</script>

<style scoped>
.game-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
</style>