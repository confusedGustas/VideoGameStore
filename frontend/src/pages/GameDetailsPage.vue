<template>
  <div class="container mx-auto px-4">
    <NavbarComponent />
    <div v-if="game" class="flex flex-col md:flex-row gap-8 mt-10">
      <div class="md:w-1/2">
        <img :src="getImageUrl(game.id)" :alt="game.name" class="w-full h-auto rounded-lg shadow-md" />
      </div>
      <div class="md:w-1/2 space-y-4">
        <h2 class="text-3xl font-bold">{{ game.name }}</h2>
        <p class="text-2xl font-semibold">${{ game.price.toFixed(2) }}</p>
        <div class="space-y-2">
          <p><strong>Genre:</strong> {{ game.genre.name }}</p>
          <p><strong>Publisher:</strong> {{ game.publisher.publisherName }}</p>
          <p><strong>Platform:</strong> {{ game.activationPlatform.platformName }}</p>
          <p><strong>Region:</strong> {{ game.activationRegion.regionName }}</p>
          <p><strong>Release Year:</strong> {{ game.releaseYear }}</p>
          <p><strong>Stock:</strong> {{ game.stock }} available</p>
        </div>
        <p class="text-gray-700 whitespace-pre-wrap break-words">{{ game.description }}</p>
        <div class="space-x-4">
          <Button @click="goBack" variant="secondary">Back to Games</Button>
          <Button @click="addToCart">Add to Cart</Button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import NavbarComponent from '@/components/NavbarComponent.vue'
import Button from '@/components/Button.vue'

const router = useRouter()
const route = useRoute()
const game = ref(null)

onMounted(async () => {
  try {
    const response = await axios.get(`/api/games/get/${route.params.id}`)
    game.value = response.data
  } catch (error) {
    console.error('Error fetching game details:', error)
  }
})

const goBack = () => {
  router.push({ name: 'home' })
}

const getImageUrl = imageId => {
  return `/api/images/get/${imageId}`
}

const addToCart = () => {
  if (game.value) {
    let cart = JSON.parse(localStorage.getItem('cart')) || []
    const existingItem = cart.find(item => item.id === game.value.id)
    if (existingItem) {
      existingItem.quantity += 1
    } else {
      cart.push({...game.value, quantity: 1})
    }
    localStorage.setItem('cart', JSON.stringify(cart))
  }
}
</script>
