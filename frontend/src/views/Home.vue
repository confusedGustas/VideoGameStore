<template>
  <div class="home">
    <h1>Video Game Store</h1>
    <div class="auth-buttons">
      <button @click="login">Login</button>
      <button @click="register">Register</button>
    </div>
    <search-bar />
    <filter-sort />
    <game-list />
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex';
import SearchBar from '@/components/SearchBar.vue';
import FilterSort from '@/components/FilterSort.vue';
import GameList from '@/components/GameList.vue';

export default {
  name: 'HomeComponent',
  components: {
    SearchBar,
    FilterSort,
    GameList
  },
  computed: {
    ...mapState('games', ['currentPage', 'totalPages'])
  },
  methods: {
    ...mapActions('games', ['fetchGames', 'setCurrentPage']),
    prevPage() {
      if (this.currentPage > 1) {
        this.setCurrentPage(this.currentPage - 1);
        this.fetchGames();
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.setCurrentPage(this.currentPage + 1);
        this.fetchGames();
      }
    },
    login() {
      // Implement login functionality
      console.log('Login clicked');
    },
    register() {
      // Implement registration functionality
      console.log('Register clicked');
    }
  },
  mounted() {
    this.fetchGames();
  }
}
</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.auth-buttons {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.auth-buttons button {
  margin-left: 10px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.pagination button {
  margin: 0 10px;
}
</style>