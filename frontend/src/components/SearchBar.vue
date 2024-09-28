<template>
  <div class="search-bar">
    <input v-model="searchQuery" @input="onSearch" placeholder="Search games...">
    <ul v-if="searchResults.length > 0" class="search-results">
      <li v-for="game in searchResults" :key="game.id" @click="selectGame(game)">
        {{ game.name }}
      </li>
    </ul>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';

export default {
  name: 'SearchBar',
  data() {
    return {
      searchQuery: ''
    }
  },
  computed: {
    ...mapState('games', ['searchResults'])
  },
  methods: {
    ...mapActions('games', ['setSearchQuery', 'searchGames']),
    onSearch() {
      if (this.searchQuery.length > 2) {
        this.searchGames(this.searchQuery);
      }
    },
    selectGame(game) {
      this.$router.push({name: 'GameDetails', params: {id: game.id}});
    }
  }
}
</script>

<style scoped>
.search-bar {
  margin: 20px 0;
  position: relative;
}

input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
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
}

.search-results li {
  padding: 10px;
  cursor: pointer;
}

.search-results li:hover {
  background-color: #f0f0f0;
}
</style>