import * as actionTypes from '../actionTypes/search';
import localStorageKeys from '../../shared/keys/localStorageKeys';

const initialState = {
    query: '',
    fetching: false,
    totalCount: null,
    results: [],
    error: null,
};

const cacheQuery = query => {
    localStorage.setItem(localStorageKeys.query, query);
};

const setSearchQuery = (state, action) => {
    const { query } = action;
    cacheQuery(query);
    return {
        ...state,
        query,
    };
};

const setSearchStart = (state, action) => {
    const { query } = action;
    cacheQuery(query);
    return { 
        ...state, 
        query,
        results: [],
        totalCount: null,
        fetching: true,
        error: null,
    };
};

const setSearchSuccess = (state, action) => {
    return {
        ...state,
        fetching: false,
        results: action.results,
        totalCount: action.totalCount,
    };
};

const setSearchError = (state, action) => {
    return {
        ...state,
        fetching: false,
        error: action.error,
        results: [],
        totalCount: null,
    };
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SET_SEARCH_QUERY: return setSearchQuery(state, action);
        case actionTypes.SET_SEARCH_START: return setSearchStart(state, action);
        case actionTypes.SET_SEARCH_SUCCESS: return setSearchSuccess(state, action);
        case actionTypes.SET_SEARCH_ERROR: return setSearchError(state, action);
        default: return state;
    }
};

export default reducer;
