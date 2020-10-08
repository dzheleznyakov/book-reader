import { takeLatest } from 'redux-saga/effects';

import * as actionTypes from '../actionTypes';
import * as navigationSagas from './navigationSagas';
import * as searchSagas from './searchSagas';
import * as booksSagas from './booksSagas';

export function* watchNavigation() {
    yield takeLatest(actionTypes.FETCH_CHAPTER_NAVIGATION, navigationSagas.fetchChapterNavigationSaga);
}

export function* watchSearch() {
    yield takeLatest(actionTypes.SEARCH, searchSagas.searchSaga);
}

export function* watchBooks() {
    yield takeLatest(actionTypes.FETCH_BOOK_MAIN_PAGE, booksSagas.fetchBookMainPageSaga);
}
