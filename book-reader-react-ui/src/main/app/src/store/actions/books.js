import * as actionTypes from '../actionTypes/books';

export const fetchBookMainPage = id => ({
    type: actionTypes.FETCH_BOOK_MAIN_PAGE,
    id,
});

export const fetchBookMainPageStart = () => ({
    type: actionTypes.FETCH_BOOK_MAIN_PAGE_START,
});

export const fetchBookMainPageSuccess = bookInfo => ({
    type: actionTypes.FETCH_BOOK_MAIN_PAGE_SUCCESS,
    bookInfo,
});

export const fetchBookMainPageFailure = error => ({
    type: actionTypes.FETCH_BOOK_MAIN_PAGE_FAILURE,
    error,
});

export const fetchBookReadingHistory = id => ({
    type: actionTypes.FETCH_BOOK_READING_HISTORY,
    id,
});

export const fetchBookReadingHistorySuccess = historyItem => ({
    type: actionTypes.FETCH_BOOK_READING_HISTORY_SUCCESS,
    historyItem,
});
