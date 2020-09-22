import { takeLatest } from 'redux-saga/effects';

import * as actionTypes from '../actionTypes';
import * as navigationSagas from './navigationSagas';

export function* watchNavigation() {
    yield takeLatest(actionTypes.FETCH_CHAPTER_NAVIGATION, navigationSagas.fetchChapterNavigationSaga);
}
