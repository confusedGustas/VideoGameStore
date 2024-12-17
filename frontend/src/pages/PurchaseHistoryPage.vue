<template>
  <div class="container mx-auto px-4 py-8">
    <div class="flex justify-between items-center mb-8">
      <h1 class="text-3xl font-bold">Purchase History</h1>
      <Button @click="goToProfile">Back to Profile</Button>
    </div>
    <div v-if="loading" class="text-center">
      <p>Loading purchase history...</p>
    </div>
    <div v-else-if="error" class="text-center text-red-600">
      <p>{{ error }}</p>
    </div>
    <PurchaseHistoryTable v-else :purchases="purchaseHistory" />
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {useRouter} from 'vue-router'
import axios from 'axios'
import Button from '@/components/Button.vue'
import PurchaseHistoryTable from '@/components/PurchaseHistoryTable.vue'

const router = useRouter()
const purchaseHistory = ref([])
const loading = ref(true)
const error = ref(null)

const fetchPurchaseHistory = async () => {
  try {
    loading.value = true
    const response = await axios.get('/api/checkout/get-purchase-history', {withCredentials: true})
    purchaseHistory.value = response.data
  } catch (error) {
    console.error('Error fetching purchase history:', error)
    error.value = 'Failed to fetch purchase history. Please try again later.'
  } finally {
    loading.value = false
  }
}

const goToProfile = () => {
  router.push('/profile')
}

onMounted(() => {
  fetchPurchaseHistory()
})
</script>

