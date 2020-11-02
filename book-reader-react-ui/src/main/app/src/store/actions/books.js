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

export const saveBookReadingHistory = (id, chapterId) => ({
    type: actionTypes.SAVE_BOOK_READING_HISTORY,
    id,
    chapterId,
});

export const fetchBookToc = id => ({
    type: actionTypes.FETCH_BOOK_TOC,
    id,
});

export const fetchBookTocStart = () => ({
    type: actionTypes.FETCH_BOOK_TOC_START,
});

export const storeBookToc = toc => ({
    type: actionTypes.STORE_BOOK_TOC,
    toc,
});
