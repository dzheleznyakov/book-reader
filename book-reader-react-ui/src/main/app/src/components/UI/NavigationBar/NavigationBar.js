import React from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';
import { NavLink } from 'react-router-dom';

import classes from './NavigationBar.module.scss';

const NavigationBar = props => {
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

NavigationBar.propTypes = {};

NavigationBar.defaultProps = {};

export default NavigationBar;
