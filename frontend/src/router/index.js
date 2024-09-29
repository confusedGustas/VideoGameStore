import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import GameList from '../components/GameList.vue'
import GameDetails from '../components/GameDetails.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/games',
        name: 'GameList',
        component: GameList
    },
    {
        path: '/game/:id',
        name: 'GameDetails',
        component: GameDetails
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router