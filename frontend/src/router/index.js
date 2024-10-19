import { createRouter, createWebHistory } from 'vue-router'
import LandingPage from '@/components/LandingPage.vue'
import SearchResults from '@/components/SearchResults.vue'
import GameDetails from '@/components/GameDetails.vue'
import LoginPage from '@/components/LoginPage.vue'
import RegisterPage from '@/components/RegisterPage.vue'
import ProfilePage from '@/components/ProfilePage.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: LandingPage,
  },
  {
    path: '/search',
    name: 'search',
    component: SearchResults,
  },
  {
    path: '/game/:id',
    name: 'details',
    component: GameDetails,
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
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
