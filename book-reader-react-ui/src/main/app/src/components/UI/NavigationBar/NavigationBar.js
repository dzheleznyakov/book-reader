import React from 'react';
import { useSelector } from 'react-redux';
import { NavLink } from 'react-router-dom';

import classes from './NavigationBar.module.scss';

const NavigationBar = () => {
    const nav = useSelector(state => state.navigation.nav);
    const navigation = nav.length 
        ? nav
            .filter(n => n.url)
            .map((n, i) => (
                <NavLink 
                    key={i} 
                    className={classes.ChapterNavUrl} 
                    to={n.url}
                >
                    {n.prefix}
                </NavLink>
            ))
        : null;
    return (
        <div className={classes.NavigationWrapper} >
            {navigation}
        </div>
    );
};

export default NavigationBar;
