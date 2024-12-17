<template>
  <div class="overflow-x-auto">
    <table class="w-full table-auto border-collapse">
      <thead>
      <tr class="bg-gray-100 text-center">
        <th class="p-3">Order Date</th>
        <th class="p-3">Buyer Name</th>
        <th class="p-3">Email</th>
        <th class="p-3">Phone</th>
        <th class="p-3">Shipping Address</th>
        <th class="p-3">Payment Method</th>
        <th class="p-3">Total Amount</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(purchase, index) in purchases" :key="index" class="border-b text-center">
        <td class="p-3">{{ formatDate(purchase.orderDate) }}</td>
        <td class="p-3">{{ purchase.buyerName }}</td>
        <td class="p-3">{{ purchase.buyerEmail }}</td>
        <td class="p-3">{{ purchase.buyerPhone }}</td>
        <td class="p-3">{{ purchase.shippingAddress }}</td>
        <td class="p-3">{{ purchase.paymentMethod }}</td>
        <td class="p-3">${{ purchase.totalAmount.toFixed(2) }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
const props = defineProps({
  purchases: {
    type: Array,
    required: true
  }
})

const formatDate = (dateArray) => {
  const [year, month, day, hour, minute] = dateArray
  return new Date(year, month - 1, day, hour, minute).toLocaleString()
}
</script>

