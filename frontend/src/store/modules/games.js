import axios from 'axios';

const state = {
    games: [],
    searchResults: [],
    selectedGame: null,
    currentPage: 1,
    totalPages: 1,
    pageSize: 10
};

const mutations = {
    SET_GAMES(state, games) {
        state.games = games;
    },
    SET_SEARCH_RESULTS(state, results) {
        state.searchResults = results;
    },
    SET_SELECTED_GAME(state, game) {
        state.selectedGame = game;
    },
    SET_CURRENT_PAGE(state, page) {
        state.currentPage = page;
    },
    SET_TOTAL_PAGES(state, total) {
        state.totalPages = total;
    }
};

const actions = {
    async fetchGames({ commit, state }) {
        try {
            const response = await axios.get('http://localhost:8080/api/games', {
                params: {
                    page: state.currentPage - 1,
                    size: state.pageSize
                }
            });
            commit('SET_GAMES', response.data.content);
            commit('SET_TOTAL_PAGES', response.data.totalPages);
        } catch (error) {
            console.error('Error fetching games:', error);
        }
    },
    async searchGames({ commit }, query) {
        try {
            const response = await axios.get(`http://localhost:8080/api/games`, {
                params: { search: query }
            });
            commit('SET_SEARCH_RESULTS', response.data);
        } catch (error) {
            console.error('Error searching games:', error);
        }
    },
    async fetchGameById({ commit }, id) {
        try {
            const response = await axios.get(`http://localhost:8080/api/games/${id}`);
            commit('SET_SELECTED_GAME', response.data);
        } catch (error) {
            console.error('Error fetching game details:', error);
        }
    },
    setCurrentPage({ commit }, page) {
        commit('SET_CURRENT_PAGE', page);
    }
};

export default {
    namespaced: true,
    state,
    mutations,
    actions
};