import  { combineReducers } from 'redux';

import navigationReducer from './navigation';
import searchReducer from './search';
import booksReducer from './books';

export default combineReducers({
    navigation: navigationReducer,
    search: searchReducer,
    books: booksReducer,
});
