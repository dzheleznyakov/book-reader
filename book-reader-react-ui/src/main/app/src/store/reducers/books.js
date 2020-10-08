import * as actionTypes from '../actionTypes/books';

const initialState = {
    loading: false,
    bookInfo: null,
    error: null,
};

const fetchBookMainStart = (state) => {
    return { 
        ...state, 
        loading: true,
        bookInfo: null,
        error: null,
    };
};

const fetchBookMainPageSuccess = (state, action) => {
    const { bookInfo } = action;
    return {
        ...state,
        loading: false,
        bookInfo,
    };
};

const fetchBookMainPageFailure = (state, action) => {
    const { error } = action;
    return {
        ...state,
        loading: false,
        error: error.message,
    };
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_BOOK_MAIN_PAGE_START: return fetchBookMainStart(state, action);
        case actionTypes.FETCH_BOOK_MAIN_PAGE_SUCCESS: return fetchBookMainPageSuccess(state, action);
        case actionTypes.FETCH_BOOK_MAIN_PAGE_FAILURE: return fetchBookMainPageFailure(state, action);
        default: return state;
    }
};

export default reducer;
