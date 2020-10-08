import React from 'react';
import { useSelector } from 'react-redux';

import ChapterNavigationBar from './ChapterNavigationBar/ChapterNavigationBar';
import MainNavigationBar from './MainNavigationBar/MainNavigationBar';
import modes from './navigationModes';

import classes from './NavigationBar.module.scss';

const NavigationBar = () => {
    const { nav, mode } = useSelector(state => state.navigation);

    let navigation;
    switch (mode) {
        case modes.CHAPTER: navigation = <ChapterNavigationBar nav={nav} />; break;
        case modes.MAIN: navigation = <MainNavigationBar />; break;
        default: navigation = null;
    }

    return (
        <div className={classes.NavigationWrapper} >
            {navigation}
        </div>
    );
};

export default NavigationBar;
