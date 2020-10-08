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
