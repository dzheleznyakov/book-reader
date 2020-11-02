import * as actionTypes from '../actionTypes/chapters';

const initalState = {
    content: null,
    index: null,
};

const storeChapterData = (state, action) => {
    const { data } = action;
    const { content, index } = data;
    return {
        ...state,
        content,
        index,
    };
};

const releaseChapterData = (state) => ({
    ...state,
    content: null,
    index: null,
});

const reducer = (state = initalState, action) => {
    switch (action.type) {
        case actionTypes.STORE_CHAPTER_DATA: return storeChapterData(state, action);
        case actionTypes.RELEASE_CHAPTER_DATA: return releaseChapterData(state, action);
        default: return state;
    }
}

export default reducer;
