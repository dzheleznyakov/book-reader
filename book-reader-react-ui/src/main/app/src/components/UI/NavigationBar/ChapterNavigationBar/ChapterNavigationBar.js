import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { NavLink } from 'react-router-dom';

import classes from './ChapterNavigationBar.module.scss';

const ChapterNavigationBar = props => {
    const { nav } = props;
    console.log('!!', props);
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
        return <Fragment>{navigation}</Fragment>;
};

ChapterNavigationBar.propTypes = {
    nav: PropTypes.arrayOf(PropTypes.shape({
        prefix: PropTypes.string,
        url: PropTypes.string.isRequired,
    }))
};

ChapterNavigationBar.defaultProps = {
    nav: [],
};

export default ChapterNavigationBar;
