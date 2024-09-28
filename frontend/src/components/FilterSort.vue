<template>
  <div class="filter-sort">
    <select v-model="sortColumn" @change="onSortChange">
      <option value="name">Name</option>
      <option value="price">Price</option>
      <option value="releaseYear">Release Year</option>
    </select>
    <select v-model="sortOrder" @change="onSortChange">
      <option value="ASC">Ascending</option>
      <option value="DESC">Descending</option>
    </select>
  </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  name: 'FilterSort',
  data() {
    return {
      sortColumn: 'name',
      sortOrder: 'ASC'
    }
  },
  methods: {
    ...mapActions('games', ['setSortColumn', 'setSortOrder', 'fetchGames']),
    onSortChange() {
      this.setSortColumn(this.sortColumn);
      this.setSortOrder(this.sortOrder);
      this.fetchGames();
    }
  }
}
</script>

<style scoped>
.filter-sort {
  margin: 20px 0;
}

select {
  margin-right: 10px;
  padding: 5px;
}
</style>