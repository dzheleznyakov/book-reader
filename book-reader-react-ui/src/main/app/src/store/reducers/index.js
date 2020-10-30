import  { combineReducers } from 'redux';

import navigationReducer from './navigation';
import searchReducer from './search';
import booksReducer from './books';
import chaptersReducer from './chapters';

export default combineReducers({
    navigation: navigationReducer,
    search: searchReducer,
    books: booksReducer,
    chapters: chaptersReducer,
});
