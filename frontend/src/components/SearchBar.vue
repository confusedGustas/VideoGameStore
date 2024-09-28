<template>
  <div class="search-bar">
    <input v-model="searchQuery" @input="onSearch" placeholder="Search games...">
  </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  name: 'SearchBar',
  data() {
    return {
      searchQuery: ''
    }
  },
  methods: {
    ...mapActions('games', ['setSearchQuery', 'fetchGames']),
    onSearch() {
      this.setSearchQuery(this.searchQuery);
      this.fetchGames();
    }
  }
}
</script>

<style scoped>
.search-bar {
  margin: 20px 0;
}

input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
}
</style>