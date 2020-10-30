import { call, put } from 'redux-saga/effects';

import axios from '../../axios-api';
import * as actions from '../actions';

export function* fetchChapterDataSaga(action) {
    const { bookId, chapterId } = action;

    try {
        const { data } = yield call(axios.get, `/books/${bookId}/chapters/${chapterId}`);
        yield put(actions.storeChapterData(data));
    } catch (error) {
        console.error('Error while fetching chapter data [%s/%s]', bookId, chapterId, error);
    }
}