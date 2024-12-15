<template>
  <div>
    <form @submit.prevent="submitForm">
      <div class="mb-4">
        <label for="name" class="block text-sm font-medium text-gray-700">Name</label>
        <input :value="game.name" @input="updateField('name', $event.target.value)" type="text" id="name" class="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none" required />
      </div>
      <div class="mb-4">
        <label for="price" class="block text-sm font-medium text-gray-700">Price</label>
        <input :value="game.price" @input="updateField('price', $event.target.value)" type="number" id="price" class="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none" required />
      </div>
      <div class="mb-4">
        <label for="description" class="block text-sm font-medium text-gray-700">Description</label>
        <textarea :value="game.description" @input="updateField('description', $event.target.value)" id="description" class="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none h-32 resize-none overflow-hidden no-scrollbar" required></textarea>
      </div>
      <div class="mb-4">
        <label for="releaseYear" class="block text-sm font-medium text-gray-700">Release Year</label>
        <input :value="game.releaseYear" @input="updateField('releaseYear', $event.target.value)" type="number" id="releaseYear" class="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none" required />
      </div>
      <div class="mb-4">
        <label for="region" class="block text-sm font-medium text-gray-700">Region</label>
        <input :value="game.region" @input="updateField('region', $event.target.value)" type="text" id="region" class="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none" required />
      </div>
      <div class="mb-4">
        <label for="platform" class="block text-sm font-medium text-gray-700">Platform</label>
        <input :value="game.platform" @input="updateField('platform', $event.target.value)" type="text" id="platform" class="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none" required />
      </div>
      <div class="mb-4">
        <label for="publisher" class="block text-sm font-medium text-gray-700">Publisher</label>
        <input :value="game.publisher" @input="updateField('publisher', $event.target.value)" type="text" id="publisher" class="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none" required />
      </div>
      <div class="mb-4">
        <label for="genre" class="block text-sm font-medium text-gray-700">Genre</label>
        <input :value="game.genre" @input="updateField('genre', $event.target.value)" type="text" id="genre" class="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none" required />
      </div>
      <div class="mb-4">
        <label for="stock" class="block text-sm font-medium text-gray-700">Stock</label>
        <input :value="game.stock" @input="updateField('stock', $event.target.value)" type="number" id="stock" class="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none" required />
      </div>
      <div class="mb-4">
        <label for="imageFile" class="block text-sm font-medium text-gray-700">Image</label>
        <div class="flex items-center mt-1">
          <input
            @change="handleFileUpload"
            type="file"
            id="imageFile"
            class="hidden"
            ref="fileInput"
            accept="image/*"
          />
          <label
            for="imageFile"
            class="cursor-pointer bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Choose file
          </label>
          <span
            class="ml-3 text-sm text-gray-500 truncate"
            style="max-width: 200px; display: inline-block; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;"
          >
      {{ fileName || 'No file chosen' }}
    </span>
        </div>

        <div class="mt-2 w-48 h-48 border border-gray-300 rounded-md overflow-hidden">
          <img
            v-if="imagePreview"
            :src="imagePreview"
            alt="Image preview"
            class="w-full h-full object-cover"
          />
        </div>
      </div>

    </form>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from 'vue'
import axios from 'axios'

const props = defineProps({
  modelValue: Object,
})

const emit = defineEmits(['update:modelValue'])

const game = ref({ ...props.modelValue })
const fileInput = ref(null)
const imagePreview = ref(null)
const currentImageBlob = ref(null)

const fileName = computed(() => {
  if (game.value.imageFile) {
    return game.value.imageFile.name
  } else if (currentImageBlob.value) {
    return 'Current image'
  }
  return ''
})

watch(() => props.modelValue, (newValue) => {
  game.value = { ...newValue }
  if (newValue.id && !imagePreview.value) {
    loadGameData(newValue.id)
  }
}, { deep: true })

const handleFileUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    game.value.imageFile = file
    const reader = new FileReader()
    reader.onload = (e) => {
      imagePreview.value = e.target.result
    }
    reader.readAsDataURL(file)
    currentImageBlob.value = null
  } else {
    game.value.imageFile = null
    imagePreview.value = null
  }
  emit('update:modelValue', { ...game.value })
}

const updateField = (field, value) => {
  game.value[field] = value
  emit('update:modelValue', { ...game.value })
}

const loadGameData = async (gameId) => {
  try {
    const response = await axios.get(`/api/games/get/${gameId}`, { withCredentials: true })
    const data = response.data

    game.value = {
      id: data.id,
      name: data.name,
      price: data.price,
      description: data.description,
      releaseYear: data.releaseYear,
      stock: data.stock,
      region: data.activationRegion?.regionName || '',
      platform: data.activationPlatform?.platformName || '',
      publisher: data.publisher?.publisherName || '',
      genre: data.genre?.name || '',
      imageFile: null,
    }

    emit('update:modelValue', { ...game.value })

    const imageResponse = await axios.get(`/api/images/get/${gameId}`, {
      withCredentials: true,
      responseType: 'blob'
    })
    currentImageBlob.value = imageResponse.data
    imagePreview.value = URL.createObjectURL(currentImageBlob.value)
  } catch (error) {
    console.error('Error loading game data:', error)
  }
}

onMounted(() => {
  if (props.modelValue.id) {
    loadGameData(props.modelValue.id)
  }
})

defineExpose({ currentImageBlob })
</script>
