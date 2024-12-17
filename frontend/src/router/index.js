import { createRouter, createWebHistory } from 'vue-router'
import LandingPage from '@/pages/LandingPage.vue'
import LoginPage from '@/pages/LoginPage.vue'
import RegisterPage from '@/pages/RegisterPage.vue'
import ProfilePage from '@/pages/ProfilePage.vue'
import CheckoutPage from "@/pages/CheckoutPage.vue";
import SearchResultsPage from "@/pages/SearchResultsPage.vue";
import GameDetailsPage from "@/pages/GameDetailsPage.vue";
import OrderConfirmationPage from "@/pages/OrderConfirmationPage.vue";
import AdminPanelPage from "@/pages/AdminPanelPage.vue";
import PurchaseHistoryPage from "@/pages/PurchaseHistoryPage.vue";

const routes = [
  {
    path: '/',
    name: 'home',
    component: LandingPage,
  },
  {
    path: '/search',
    name: 'search',
    component: SearchResultsPage,
  },
  {
    path: '/game/:id',
    name: 'details',
    component: GameDetailsPage,
  },
  {
    path: '/login',
    name: 'login',
    component: LoginPage,
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterPage,
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfilePage,
  },
  {
    path: '/checkout',
    name: 'checkout',
    component: CheckoutPage,
  },
  {
    path: '/order-confirmation',
    name: 'orderConfirmation',
    component: OrderConfirmationPage
  },
  {
    path: '/admin',
    name: 'admin',
    component: AdminPanelPage
  },
  {
    path: '/purchase-history',
    name: 'purchaseHistory',
    component: PurchaseHistoryPage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
