import { call, put } from 'redux-saga/effects';

import * as actions from '../actions';
import axios from '../../axios-api';
import navigationModes from '../../components/UI/NavigationBar/navigationModes';

export function* fetchChapterNavigationSaga(action) {
    const { bookId, chapterId } = action;

    const url = `/books/${bookId}/chapters/${chapterId}/navigation`;

    let nav = {
        prev: null,
        next: null,
        book: `/books/${bookId}`,
    };

    try {
        const { data } = yield call(axios.get, url);
        const { prev, next } = data;

        nav = { ...nav, 
            prev: prev || null,
            next: next || null,
        };
    } catch (err) {
        console.error(err);
    }

    yield put(actions.setNavigation(navigationModes.CHAPTER, nav));
}