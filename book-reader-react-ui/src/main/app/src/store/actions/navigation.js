import * as actionTypes from '../actionTypes/navigation';

export const fetchChapterNavigation = (bookId, chapterId) => ({
    type: actionTypes.FETCH_CHAPTER_NAVIGATION,
    bookId,
    chapterId,
});

export const setNavigation = (mode, nav = []) => ({
    type: actionTypes.SET_NAVIGATION,
    mode,
    nav,
});
