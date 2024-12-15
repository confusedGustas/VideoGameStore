<template>
  <transition
    enter-active-class="transition-opacity duration-300"
    leave-active-class="transition-opacity duration-300"
    enter-from-class="opacity-0"
    leave-to-class="opacity-0"
  >
    <div v-if="show" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-8 max-w-md w-full max-h-[90vh] overflow-y-auto no-scrollbar">
        <h2 class="text-2xl font-bold mb-4">{{ title }}</h2>
        <slot></slot>
        <div class="flex justify-end mt-6 space-x-4">
          <Button @click="$emit('close')" variant="secondary">Cancel</Button>
          <Button @click="$emit('confirm')" variant="primary">Confirm</Button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import Button from './Button.vue'

defineProps({
  show: Boolean,
  title: String
})

defineEmits(['close', 'confirm'])
</script>

<style scoped>
.no-scrollbar {
  scrollbar-width: none;
  -ms-overflow-style: none;
}
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>
