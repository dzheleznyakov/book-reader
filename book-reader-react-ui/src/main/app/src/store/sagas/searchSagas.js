import { put, call } from 'redux-saga/effects';

import axios from '../../axios-api';
import * as actions from '../actions';

export function* searchSaga(action) {
    const { query, offset = 0, limit = 10 } = action;

    yield put(actions.setSearchStart(query));
    const params = {
        q: query,
        offset,
        limit,
    };

    try {
        const res = yield call(axios.get, 'search', { params });
        yield put(actions.setSearchSuccess(res.data));
    } catch (err) {
        yield put(actions.setSearchError(err))
    }
}
