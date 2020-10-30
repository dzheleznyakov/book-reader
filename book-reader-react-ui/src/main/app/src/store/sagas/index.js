import { takeLatest } from 'redux-saga/effects';

import * as actionTypes from '../actionTypes';
import * as navigationSagas from './navigationSagas';
import * as searchSagas from './searchSagas';
import * as booksSagas from './booksSagas';
import * as chaptersSagas from './chaptersSagas';

export function* watchNavigation() {
    yield takeLatest(actionTypes.FETCH_CHAPTER_NAVIGATION, navigationSagas.fetchChapterNavigationSaga);
}

export function* watchSearch() {
    yield takeLatest(actionTypes.SEARCH, searchSagas.searchSaga);
}

export function* watchBooks() {
    yield takeLatest(actionTypes.FETCH_BOOK_MAIN_PAGE, booksSagas.fetchBookMainPageSaga);
    yield takeLatest(actionTypes.FETCH_BOOK_READING_HISTORY, booksSagas.fetchBookReadingHistorySaga);
    yield takeLatest(actionTypes.SAVE_BOOK_READING_HISTORY, booksSagas.saveBookReadingHistorySaga);
}

export function* watchChapters() {
    yield takeLatest(actionTypes.FETCH_CHAPTER_DATA, chaptersSagas.fetchChapterDataSaga);
}
