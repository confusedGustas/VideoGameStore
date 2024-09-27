<template>
  <div v-if="games.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
    <div v-for="game in games" :key="game.id" class="border rounded-lg overflow-hidden shadow-lg">
      <img :src="game.image" :alt="game.name" class="w-full h-48 object-cover" />
      <div class="p-4">
        <h2 class="text-xl font-bold mb-2">{{ game.name }}</h2>
        <p class="text-gray-700 mb-2">{{ game.description }}</p>
        <p class="text-lg font-bold mb-2">${{ game.price.toFixed(2) }}</p>
        <p class="mb-2">Release Year: {{ game.releaseYear }}</p>
        <p class="mb-4">In Stock: {{ game.stock }}</p>
        <button @click="$emit('delete-game', game.id)" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
          Delete
        </button>
      </div>
    </div>
  </div>
  <div v-else class="text-center text-gray-600">
    No games found. Try adjusting your search or filters.
  </div>
</template>

<script setup>
defineProps({
  games: {
    type: Array,
    required: true
  }
})

defineEmits(['delete-game'])
</script>