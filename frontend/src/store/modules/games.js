import axios from 'axios';

const state = {
    games: [],
    selectedGame: null,
    searchResults: []
};

const mutations = {
    SET_GAMES(state, games) {
        state.games = games;
    },
    SET_SELECTED_GAME(state, game) {
        state.selectedGame = game;
    },
    SET_SEARCH_RESULTS(state, results) {
        state.searchResults = results;
    }
};

const actions = {
    async fetchGames({ commit }) {
        return await fetchData('http://localhost:8080/api/games', commit, 'SET_GAMES');
    },
    async fetchGameById({ commit }, id) {
        return await fetchData(`http://localhost:8080/api/games/get/${id}`, commit, 'SET_SELECTED_GAME');
    },
    async searchGames({ commit }, search) {
        return await fetchData(`http://localhost:8080/api/games?search=${search}`, commit, 'SET_SEARCH_RESULTS');
    }
};

const fetchData = async (url, commit, mutation) => {
    try {
        const { data } = await axios.get(url);
        commit(mutation, data.items || data);
        return data.items || data;
    } catch (error) {
        console.error(`Error fetching data from ${url}:`, error);
        throw error;
    }
};

export default {
    namespaced: true,
    state,
    mutations,
    actions
};
