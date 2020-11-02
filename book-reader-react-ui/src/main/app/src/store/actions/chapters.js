import * as actionTypes from '../actionTypes/chapters';

export const fetchChapterData = (bookId, chapterId) => ({
    type: actionTypes.FETCH_CHAPTER_DATA,
    bookId,
    chapterId,
});

export const storeChapterData = data => ({
    type: actionTypes.STORE_CHAPTER_DATA,
    data,
});

export const releaseChapterData = () => ({
    type: actionTypes.RELEASE_CHAPTER_DATA,
});
