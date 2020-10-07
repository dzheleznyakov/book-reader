import React from 'react';
import { useDispatch } from 'react-redux';

import * as actions from '../../../../store/actions';

import classes from './MainNavigationBar.module.scss';

const ENTER_KEY = 'Enter';

const MainNavigationBar = () => {
    const dispatch = useDispatch();

    const cb = (event) => {
        const { key } = event;
        if (key === ENTER_KEY && event.target.value && event.target.value.trim())
            dispatch(actions.search(event.target.value));
    };

    return (
        <input 
            type="text" 
            className={classes.SearchInput}
            placeholder="Search..."
            onKeyUp={cb}
        />
    );
};

export default MainNavigationBar;
