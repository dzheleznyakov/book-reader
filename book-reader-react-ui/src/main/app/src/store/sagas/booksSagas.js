import { put, call } from 'redux-saga/effects';

import axios from '../../axios-api';
import * as actions from '../actions';

export function* fetchBookMainPageSaga(action) {
    const { id } = action;
    yield put(actions.fetchBookMainPageStart());

    try {
        const res = yield call(axios.get, `/books/${id}`);
        yield put(actions.fetchBookMainPageSuccess(res.data));
    } catch (error) {
        yield put(actions.fetchBookMainPageFailure(error));
    }
}

export function* fetchBookReadingHistorySaga(action) {
    const { id } = action;

    try {
        const { data } = yield call(axios.get, `/books/${id}/lastChapter`);
        yield put(actions.fetchBookReadingHistorySuccess(data));
    } catch (error) {
        console.error("Error while fetching history for book [%s]", id, error);
        yield put(actions.fetchBookReadingHistorySuccess({
            bookId: id,
            lastChapterIndex: -1,
        }))
    }
}

export function* saveBookReadingHistorySaga(action) {
    const { id, chapterId } = action;

    try {
        yield call(axios.put, `/books/${id}/lastChapter`, { data: chapterId });
    } catch(error) {
        console.error('Error while saving book reading history [%s/%s]', id, chapterId, error);
    }
};
