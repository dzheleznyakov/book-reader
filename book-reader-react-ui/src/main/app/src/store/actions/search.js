import * as actionTypes from '../actionTypes/search';

export const setSearchQuery = (query) => ({
    type: actionTypes.SET_SEARCH_QUERY,
    query,
});

export const search = (query, offset, limit) => ({
    type: actionTypes.SEARCH,
    query,
    offset,
    limit,
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

export const setSearchError = (error) => ({
    type: actionTypes.SET_SEARCH_ERROR,
    error,
});
