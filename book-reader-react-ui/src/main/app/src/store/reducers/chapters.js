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

const reducer = (state = initalState, action) => {
    switch (action.type) {
        case actionTypes.STORE_CHAPTER_DATA: return storeChapterData(state, action);
        default: return state;
    }
}

export default reducer;
