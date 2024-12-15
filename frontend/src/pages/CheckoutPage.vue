<template>
  <div class="container mx-auto px-4">
    <NavbarComponent />

    <form @submit.prevent="submitCheckout" class="max-w-2xl mx-auto mt-10 px-4" novalidate>
      <div class="flex flex-col items-center space-y-8">
        <div class="w-full max-w-md space-y-4">
          <div>
            <label for="shippingAddress" class="block text-sm font-medium text-gray-700">Shipping Address</label>
            <input
              id="shippingAddress"
              v-model="checkoutData.shippingAddress"
              type="text"
              required
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 focus:outline-none"
            />
          </div>

          <div>
            <label for="buyerName" class="block text-sm font-medium text-gray-700">Buyer's Name</label>
            <input
              id="buyerName"
              v-model="checkoutData.buyerName"
              type="text"
              required
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 focus:outline-none"
            />
          </div>

          <div>
            <label for="buyerEmail" class="block text-sm font-medium text-gray-700">Email</label>
            <input
              id="buyerEmail"
              v-model="checkoutData.buyerEmail"
              type="email"
              required
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 focus:outline-none"
            />
          </div>

          <div>
            <label for="buyerPhone" class="block text-sm font-medium text-gray-700">Phone Number</label>
            <input
              id="buyerPhone"
              v-model="checkoutData.buyerPhone"
              type="tel"
              required
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 focus:outline-none"
            />
          </div>

          <div>
            <label for="paymentMethod" class="block text-sm font-medium text-gray-700">Payment Method</label>
            <select
              id="paymentMethod"
              v-model="checkoutData.paymentMethod"
              required
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 focus:outline-none"
            >
              <option value="CREDIT_CARD">Credit Card</option>
              <option value="PAYPAL">PayPal</option>
              <option value="CASH">Cash</option>
            </select>
          </div>
        </div>

        <div class="bg-gray-100 p-6 rounded-lg w-full max-w-md">
          <h2 class="text-xl font-semibold mb-4">Order Summary</h2>
          <ul v-if="items.length > 0" class="space-y-4">
            <li v-for="item in items" :key="item.id" class="flex justify-between items-center">
              <div>
                <span class="font-medium">{{ truncateText(item.name, 10) }}</span>
                <div class="text-sm text-gray-600">
                  Quantity:
                  <button @click.prevent="decreaseQuantity(item)" class="px-2 py-1 bg-gray-200 rounded">-</button>
                  {{ item.quantity }}
                  <button @click.prevent="increaseQuantity(item)" class="px-2 py-1 bg-gray-200 rounded">+</button>
                </div>
              </div>
              <div class="mt-6">
                <span class="font-medium">${{ (item.price * item.quantity).toFixed(2) }}</span>
                <button @click="removeItem(item)" class="ml-2 text-red-600 hover:text-red-800">Remove</button>
              </div>
            </li>
          </ul>
          <p v-else class="text-gray-600">Your cart is empty.</p>
          <div class="mt-4 text-xl font-semibold">Total: ${{ totalPrice.toFixed(2) }}</div>
          <button v-if="items.length > 0" @click="clearCart" class="mt-4 w-full bg-red-600 text-white py-2 rounded hover:bg-red-700">
            Clear Cart
          </button>
        </div>
      </div>

      <div class="mt-8 flex justify-center">
        <Button type="submit" :disabled="items.length === 0" class="w-full max-w-md">
          Place Order
        </Button>
      </div>
    </form>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue'
import {useRouter} from 'vue-router'
import axios from 'axios'
import NavbarComponent from '@/components/NavbarComponent.vue'
import Button from '@/components/Button.vue'

const router = useRouter()
const items = ref([])

const checkoutData = ref({
  shippingAddress: '',
  buyerName: '',
  buyerEmail: '',
  buyerPhone: '',
  paymentMethod: '',
  items: [],
})

const totalPrice = computed(() => {
  return items.value.reduce((total, item) => total + item.price * item.quantity, 0)
})

onMounted(() => {
  loadCartItems()
})

const loadCartItems = () => {
  items.value = JSON.parse(localStorage.getItem('cart')) || []
  updateCheckoutItems()
}

const updateCheckoutItems = () => {
  checkoutData.value.items = items.value.map(item => ({
    id: item.id,
    quantity: item.quantity,
  }))
}

const saveCartItems = () => {
  localStorage.setItem('cart', JSON.stringify(items.value))
  updateCheckoutItems()
}

const increaseQuantity = (item) => {
  item.quantity++
  saveCartItems()
}

const decreaseQuantity = (item) => {
  if (item.quantity > 1) {
    item.quantity--
    saveCartItems()
  }
}

const removeItem = (itemToRemove) => {
  items.value = items.value.filter(item => item.id !== itemToRemove.id)
  saveCartItems()
}

const clearCart = () => {
  items.value = []
  saveCartItems()
}

const submitCheckout = async () => {
  try {
    const payload = {
      shippingAddress: checkoutData.value.shippingAddress,
      buyerName: checkoutData.value.buyerName,
      buyerEmail: checkoutData.value.buyerEmail,
      buyerPhone: checkoutData.value.buyerPhone,
      paymentMethod: checkoutData.value.paymentMethod,
      items: items.value.map(item => ({
        gameId: item.id,
        quantity: item.quantity,
      })),
    }

    const response = await axios.post('/api/checkout/save', payload, { withCredentials: true })
    clearCart()

    await router.push({ name: 'orderConfirmation' })
  } catch (error) {
    console.error('Checkout failed:', error)
    alert('Checkout failed. Please check the quantity of items in your cart, or the details you entered.')
  }
}

const truncateText = (text, maxLength) => {
  if (text.length > maxLength) {
    return text.substring(0, maxLength) + '...'
  }
  return text
}
</script>
