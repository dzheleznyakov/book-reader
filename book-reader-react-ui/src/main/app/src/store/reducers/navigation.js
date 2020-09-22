import * as actionTypes from '../actionTypes/navigation';
import modes from '../../components/UI/NavigationBar/navigationModes';

const initialState = {
    mode: null,
    nav: [],
};

const setNavigation = (state, action) => ({ 
    ...state, 
    mode: action.mode, 
    nav: action.nav
});

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SET_NAVIGATION: return setNavigation(state, action);
        default: return state;
    }
};

export default reducer;