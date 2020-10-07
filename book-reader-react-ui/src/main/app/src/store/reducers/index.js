import  { combineReducers } from 'redux';

import navigationReducer from './navigation';
import searchReducer from './search';

export default combineReducers({
    navigation: navigationReducer,
    search: searchReducer,
});
