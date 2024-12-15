<template>
  <div class="flex justify-center items-center space-x-4 mt-8">
    <Button
      v-if="currentPage > 1"
      @click="$emit('prev')"
      variant="secondary"
    >
      Previous
    </Button>
    <span class="text-gray-700">Page {{ currentPage }}</span>
    <Button
      v-if="hasMore"
      @click="$emit('next')"
      variant="secondary"
    >
      Next
    </Button>
  </div>
</template>

<script setup>
import Button from './Button.vue'

defineProps({
  currentPage: {
    type: Number,
    required: true
  },
  hasMore: {
    type: Boolean,
    required: true
  }
})

defineEmits(['prev', 'next'])
</script>
