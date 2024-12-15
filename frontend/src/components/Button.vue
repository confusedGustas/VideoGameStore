<template>
  <button
    :class="[
      'px-4 py-2 rounded-md transition-colors',
      variant === 'primary' ? 'bg-black text-white hover:bg-gray-800' : 'bg-gray-200 text-black hover:bg-gray-300'
    ]"
    :disabled="disabled"
  >
    <slot></slot>
  </button>
</template>

<script setup>
defineProps({
  variant: {
    type: String,
    default: 'primary'
  },
  disabled: {
    type: Boolean,
    default: false
  }
})
</script>
