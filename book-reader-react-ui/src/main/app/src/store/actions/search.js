import * as actionTypes from '../actionTypes/search';

export const search = query => ({
    type: actionTypes.SEARCH,
    query,
});

export const setSearchStart = query => ({
    type: actionTypes.SET_SEARCH_START,
    query,
});

export const setSearchSuccess = ({ totalCount, results }) => ({
    type: actionTypes.SET_SEARCH_SUCCESS,
    totalCount,
    results,
});

export const setSearchError = (err) => ({
    type: actionTypes.SET_SEARCH_ERROR,
    err,
});
