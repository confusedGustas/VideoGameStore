<template>
  <div class="container mx-auto px-4">
    <NavbarComponent />
    <div v-if="game" class="flex flex-col md:flex-row gap-8 mt-10">
      <div class="md:w-1/2">
        <img :src="getImageUrl(game.id)" :alt="game.name" class="w-full h-auto rounded-lg shadow-md" />
      </div>
      <div class="md:w-1/2 space-y-4">
        <h2 class="text-3xl font-bold truncate w-[30ch]">{{ game.name }}</h2>
        <p class="text-2xl font-semibold">${{ game.price.toFixed(2) }}</p>
        <div class="space-y-2">
          <p class="truncate w-[40ch]"><strong>Genre:</strong> {{ game.genre.name }}</p>
          <p class="truncate w-[40ch]"><strong>Publisher:</strong> {{ game.publisher.publisherName }}</p>
          <p class="truncate w-[40ch]"><strong>Platform:</strong> {{ game.activationPlatform.platformName }}</p>
          <p class="truncate w-[40ch]"><strong>Region:</strong> {{ game.activationRegion.regionName }}</p>
          <p class="truncate w-[40ch]"><strong>Release Year:</strong> {{ game.releaseYear }}</p>
          <p class="truncate w-[40ch]"><strong>Stock:</strong> {{ game.stock }} available</p>
        </div>
        <p class="whitespace-pre-wrap break-words"><strong>Description:</strong> {{ game.description }}</p>

        <div class="flex items-center space-x-2">
          <span class="font-semibold">Rating:</span>
          <div class="flex">
            <template v-for="i in 5" :key="i">
              <svg
                v-if="i <= game.averageRating"
                xmlns="http://www.w3.org/2000/svg"
                class="w-6 h-6 text-yellow-500"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path d="M10 15.27L16.18 18l-1.64-7.03L18 7.24l-7.19-.61L10 0 9.19 6.63 2 7.24l5.46 3.73L3.82 18z"/>
              </svg>
              <svg
                v-else
                xmlns="http://www.w3.org/2000/svg"
                class="w-6 h-6 text-gray-400"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path d="M10 15.27L16.18 18l-1.64-7.03L18 7.24l-7.19-.61L10 0 9.19 6.63 2 7.24l5.46 3.73L3.82 18z"/>
              </svg>
            </template>
          </div>
        </div>

        <div v-if="isLoggedIn" class="space-y-2 mt-4">
          <p class="font-semibold">Submit your rating:</p>
          <div class="flex space-x-2">
            <button
              v-for="star in [1, 2, 3, 4, 5]"
              :key="star"
              :class="{'text-yellow-500': star <= userRating, 'text-gray-400': star > userRating}"
              @click="submitRating(star)"
              class="w-8 h-8 text-center text-xl cursor-pointer"
            >
              ★
            </button>
          </div>
        </div>

        <div class="space-x-4">
          <Button @click="goBack" variant="secondary">Back to Games</Button>
          <Button @click="addToCart">Add to Cart</Button>
        </div>
      </div>
    </div>
    <Comments :gameId="Number(route.params.id)" class="mt-8" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import NavbarComponent from '@/components/NavbarComponent.vue'
import Button from '@/components/Button.vue'
import Comments from '@/components/Comments.vue'

const router = useRouter()
const route = useRoute()
const game = ref(null)
const userRating = ref(0)
const isLoggedIn = ref(false)

const fetchUserDetails = async () => {
  try {
    const response = await axios.get('/api/users/get-details')
    if (response.data && response.data.username) {
      isLoggedIn.value = true
    }
  } catch (error) {
    console.error('Error fetching user details:', error)
    isLoggedIn.value = false
  }
}

onMounted(async () => {
  try {
    const response = await axios.get(`/api/games/get/${route.params.id}`)
    game.value = response.data
    userRating.value = 0
    await fetchUserDetails()
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

const submitRating = async (rating) => {
  try {
    const response = await axios.post('/api/ratings/add-rating', {
      gameId: game.value.id,
      value: rating
    })
    userRating.value = rating
    console.log('Rating submitted:', response.data)

    window.location.reload()
  } catch (error) {
    console.error('Error submitting rating:', error)
  }
}
</script>
