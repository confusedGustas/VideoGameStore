<template>
  <nav class="navbar">
    <router-link to="/" class="navbar-title" exact-active-class="active-link">
      Video Game Store
    </router-link>
    <div class="navbar-buttons">
      <button @click="goToProfile">Profile</button>
      <button @click="logout">Logout</button>
    </div>
  </nav>

  <div class="checkout-page">
    <h1>Checkout</h1>
    <form @submit.prevent="submitCheckout" class="checkout-form">
      <div class="form-group">
        <label for="shippingAddress">Shipping Address</label>
        <input
          id="shippingAddress"
          v-model="checkoutData.shippingAddress"
          type="text"
          required
        />
      </div>

      <div class="form-group">
        <label for="buyerName">Buyer's Name</label>
        <input
          id="buyerName"
          v-model="checkoutData.buyerName"
          type="text"
          required
        />
      </div>

      <div class="form-group">
        <label for="buyerEmail">Email</label>
        <input
          id="buyerEmail"
          v-model="checkoutData.buyerEmail"
          type="email"
        />
      </div>

      <div class="form-group">
        <label for="buyerPhone">Phone Number</label>
        <input
          id="buyerPhone"
          v-model="checkoutData.buyerPhone"
          type="tel"
          required
        />
      </div>

      <div class="form-group">
        <label for="paymentMethod">Payment Method</label>
        <select
          id="paymentMethod"
          v-model="checkoutData.paymentMethod"
          required
        >
          <option value="CREDIT_CARD">Credit Card</option>
          <option value="PAYPAL">PayPal</option>
          <option value="CASH">Cash</option>
        </select>
      </div>

      <div class="items-list">
        <h2>Items</h2>
        <ul v-if="items.length > 0">
          <li v-for="item in items" :key="item.id" class="cart-item">
            <div class="item-details">
              <span :title="item.name">{{ item.name.length > 20 ? item.name.substring(0, 20) + '...' : item.name }}</span>
              <div class="quantity-controls">
                <button @click.prevent="decreaseQuantity(item)" class="quantity-btn">-</button>
                <span>{{ item.quantity }}</span>
                <button @click.prevent="increaseQuantity(item)" class="quantity-btn">+</button>
              </div>
              <span>Price: ${{ (item.price * item.quantity).toFixed(2) }}</span>
              <button @click.prevent="removeItem(item)" class="remove-btn">Remove</button>
            </div>
          </li>
        </ul>
        <p v-else>Your cart is empty.</p>
        <p><strong>Total: ${{ totalPrice.toFixed(2) }}</strong></p>
        <button v-if="items.length > 0" @click.prevent="clearCart" class="clear-cart-btn">Clear Cart</button>
      </div>

      <button type="submit" class="submit-button" :disabled="items.length === 0">Place Order</button>
    </form>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

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
  const cartItems = JSON.parse(localStorage.getItem('cart')) || []
  items.value = cartItems
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


const goToProfile = () => {
  router.push({ name: 'profile' })
}

const logout = async () => {
  try {
    await axios.post('/api/users/logout', {}, { withCredentials: true })
    await router.push({ name: 'home' })
  } catch (error) {
    console.error('Error logging out:', error)
  }
}
</script>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  padding: 1.5rem;
  background-color: white;
  border-radius: 6px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: relative;
  margin-bottom: 2rem;
}

.navbar-title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  color: #333;
  font-size: 2.5rem;
  text-decoration: none;
  font-weight: bold;
}

.navbar-title:hover {
  color: #4caf50;
}

.navbar-buttons {
  margin-left: auto;
  display: flex;
  align-items: center;
}

.navbar-buttons button {
  margin-left: 10px;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.navbar-buttons button:hover {
  background-color: #45a049;
}

.checkout-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.checkout-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 5px;
  font-weight: bold;
}

input, select {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.items-list {
  background-color: #f9f9f9;
  padding: 15px;
  border-radius: 4px;
}

.items-list ul {
  list-style-type: none;
  padding: 0;
}

.cart-item {
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.item-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.quantity-btn, .remove-btn {
  padding: 5px 10px;
  background-color: #f0f0f0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.quantity-btn:hover, .remove-btn:hover {
  background-color: #e0e0e0;
}

.remove-btn {
  background-color: #ff6b6b;
  color: white;
}

.remove-btn:hover {
  background-color: #ff5252;
}

.clear-cart-btn {
  margin-top: 10px;
  padding: 10px;
  background-color: #ff6b6b;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.clear-cart-btn:hover {
  background-color: #ff5252;
}

.submit-button {
  background-color: #4caf50;
  color: white;
  padding: 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.submit-button:hover {
  background-color: #45a049;
}

.submit-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>
