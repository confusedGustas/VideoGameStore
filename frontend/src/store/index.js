import { createStore } from 'vuex'
import gamesModule from './modules/games'

export default createStore({
    modules: {
        games: gamesModule
    }
})