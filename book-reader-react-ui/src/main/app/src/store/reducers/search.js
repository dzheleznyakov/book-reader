import * as actionTypes from '../actionTypes/search';

const initialState = {
    query: '',
    fetching: false,
    totalCount: null,
    results: [],
    error: null,
};

const setSearchStart = (state, action) => {
    return { 
        ...state, 
        query: action.query ,
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
        case actionTypes.SET_SEARCH_START: return setSearchStart(state, action);
        case actionTypes.SET_SEARCH_SUCCESS: return setSearchSuccess(state, action);
        case actionTypes.SET_SEARCH_ERROR: return setSearchError(state, action);
        default: return state;
    }
};

export default reducer;
