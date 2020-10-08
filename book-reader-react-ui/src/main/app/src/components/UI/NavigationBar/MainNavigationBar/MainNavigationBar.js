import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';

import * as actions from '../../../../store/actions';
import localStorageKeys from '../../../../shared/keys/localStorageKeys';

import classes from './MainNavigationBar.module.scss';

const ENTER_KEY = 'Enter';

const MainNavigationBar = () => {
    const dispatch = useDispatch();
    const history = useHistory();
    let query = useSelector(state => state.search.query);
    if (!query)
        query = localStorage.getItem(localStorageKeys.query) || null;

    const onEnter = (event) => {
        const { key, target } = event;
        if (key === ENTER_KEY && target.value && target.value.trim()) {
            dispatch(actions.setSearchQuery(target.value));
            history.push('/search');
        }
    };

    return (
        <input 
            type="text" 
            className={classes.SearchInput}
            placeholder="Search..."
            defaultValue={query}
            onKeyUp={onEnter}
        />
    );
};

export default MainNavigationBar;
