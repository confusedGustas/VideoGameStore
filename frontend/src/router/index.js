import { createRouter, createWebHistory } from 'vue-router';
import LandingPage from "@/components/LandingPage.vue";
import SearchResults from "@/components/SearchResults.vue";
import GameDetails from "@/components/GameDetails.vue";

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
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;