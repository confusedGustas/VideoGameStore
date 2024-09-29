<template>
  <div>
    <div class="search-bar">
      <div class="search-input-wrapper">
        <input
            v-model="searchQuery"
            @keyup.enter="performSearch"
            placeholder="Search games..."
            class="search-input"
        />
        <button @click="performSearch" class="search-button">
          <SearchIcon class="h-5 w-5" />
        </button>
      </div>
      <ul v-if="searchResults.length > 0" class="search-results">
        <li v-for="game in searchResults" :key="game.id" @click="selectGame(game)">
          {{ game.name }}
        </li>
      </ul>
    </div>

    <h1 v-if="searchQuery && searchResults.length > 0">Search Results:</h1>
    <h1 v-else>Game List</h1>

    <div class="game-grid">
      <GameCard v-for="game in (searchQuery && searchResults.length > 0 ? searchResults : games)" :key="game.id" :game="game" />
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref, computed} from 'vue';
import {useStore} from 'vuex';
import GameCard from './GameCard.vue';
import {SearchIcon} from 'lucide-vue-next';
import router from "@/router";

const store = useStore();
const searchQuery = ref('');
const searchResults = computed(() => store.state.games.searchResults);
const games = computed(() => store.state.games.games);

const performSearch = () => {
  if (searchQuery.value.length > 2) {
    store.dispatch('games/searchGames', searchQuery.value);
  }
};

const selectGame = (game) => {
  // Navigate to the selected game details
  router.push({name: 'GameDetails', params: {id: game.id}});
  searchQuery.value = '';
};

const fetchGames = async () => {
  try {
    await store.dispatch('games/fetchGames');
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

.search-bar {
  margin-bottom: 20px;
  position: relative;
}

.search-input-wrapper {
  display: flex;
  align-items: center;
}

.search-input {
  flex-grow: 1;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px 0 0 4px;
}

.search-button {
  padding: 10px 15px;
  background-color: #4a5568;
  color: white;
  border: none;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #2d3748;
}

.search-results {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #ddd;
  border-top: none;
  list-style-type: none;
  padding: 0;
  margin: 0;
  max-height: 200px;
  overflow-y: auto;
  border-radius: 0 0 4px 4px;
}

.search-results li {
  padding: 10px;
  cursor: pointer;
}

.search-results li:hover {
  background-color: #f0f0f0;
}
</style>