<template>
  <div class="order-confirmation">
    <h1>Order Confirmation</h1>
    <p>Thank you for your order! Please check your email for further details.</p>
    <router-link to="/" class="home-link">Return to Home</router-link>
  </div>
</template>

<style scoped>
.order-confirmation {
  max-width: 600px;
  margin: 2rem auto;
  padding: 2rem;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
}

h1 {
  color: #4caf50;
  margin-bottom: 1rem;
}

p {
  font-size: 1.1rem;
  margin-bottom: 2rem;
}

.home-link {
  display: inline-block;
  padding: 0.75rem 1.5rem;
  background-color: #4caf50;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.home-link:hover {
  background-color: #45a049;
}
</style>
