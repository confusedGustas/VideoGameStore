<template>
  <div>
    <h1>Game List</h1>
    <div class="game-grid">
      <GameCard v-for="game in games" :key="game.id" :game="game" />
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {useStore} from 'vuex';
import GameCard from './GameCard.vue';

const store = useStore();
const games = ref([]);

const fetchGames = async () => {
  try {
    games.value = await store.dispatch('games/fetchGames');
  } catch (error) {
    console.error('Error fetching games:', error);
  }
};

onMounted(() => {
  fetchGames();
});
</script>

<style scoped>
h1 {
  text-align: center;
  margin-bottom: 20px;
}

.game-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}
</style>