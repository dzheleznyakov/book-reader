import { call, put } from 'redux-saga/effects';

import * as actions from '../actions';
import axios from '../../axios-api';
import navigationModes from '../../components/UI/NavigationBar/navigationModes';

export function* fetchChapterNavigationSaga(action) {
    const { bookId, chapterId } = action;

    const url = `/books/${bookId}/chapters/${chapterId}/navigation`;

    const { data } = yield call(axios.get, url);
    const { prev, next } = data;

    const nav = [{
        prefix: prev ? 'Prev' : '',
        url: prev || '#',
    }, {
        prefix: next ? 'Next' : '',
        url: next || '#',
    }];

    yield put(actions.setNavigation(navigationModes.CHAPTER, nav));
}