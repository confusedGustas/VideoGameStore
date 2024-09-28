import { createStore } from 'vuex';
import games from './modules/games';

export default createStore({
    modules: {
        games
    }
});