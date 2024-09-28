import api from '@/services/api';

const state = {
    games: [],
    totalPages: 0,
    currentPage: 1,
    searchQuery: '',
    sortOrder: 'ASC',
    sortColumn: 'name'
};

const mutations = {
    SET_GAMES(state, games) {
        state.games = games;
    },
    SET_TOTAL_PAGES(state, totalPages) {
        state.totalPages = totalPages;
    },
    SET_CURRENT_PAGE(state, currentPage) {
        state.currentPage = currentPage;
    },
    SET_SEARCH_QUERY(state, query) {
        state.searchQuery = query;
    },
    SET_SORT_ORDER(state, order) {
        state.sortOrder = order;
    },
    SET_SORT_COLUMN(state, column) {
        state.sortColumn = column;
    }
};

const actions = {
    async fetchGames({ commit, state }) {
        try {
            const response = await api.get('/api/games', {
                params: {
                    limit: 10,
                    offset: (state.currentPage - 1) * 10,
                    sortOrder: state.sortOrder,
                    sortColumn: state.sortColumn,
                    search: state.searchQuery
                }
            });
            commit('SET_GAMES', response.data.items);
            commit('SET_TOTAL_PAGES', response.data.totalPages);
        } catch (error) {
            console.error('Error fetching games:', error);
        }
    },
    setSearchQuery({ commit }, query) {
        commit('SET_SEARCH_QUERY', query);
    },
    setSortOrder({ commit }, order) {
        commit('SET_SORT_ORDER', order);
    },
    setSortColumn({ commit }, column) {
        commit('SET_SORT_COLUMN', column);
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