<template>
  <div class="bg-white shadow-md rounded-lg p-4 transition-transform hover:-translate-y-1">
    <img :src="getImageUrl(game.id)" :alt="game.name" class="w-full h-48 object-cover rounded-md mb-4" />
    <h3 class="text-lg font-semibold mb-2 truncate">{{ game.name }}</h3>
    <p class="text-gray-700 mb-4">${{ game.price.toFixed(2) }}</p>
    <div class="flex justify-between">
      <Button @click="$emit('details', game.id)" variant="secondary">Details</Button>
      <Button @click="$emit('addToCart', game)" variant="primary">Add to Cart</Button>
    </div>
  </div>
</template>

<script setup>
import Button from './Button.vue'

defineProps({
  game: {
    type: Object,
    required: true
  }
})

defineEmits(['details', 'addToCart'])

const getImageUrl = (imageId) => `/api/images/get/${imageId}`
</script>
