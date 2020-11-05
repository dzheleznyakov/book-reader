import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { NavLink } from 'react-router-dom';

import classes from './ChapterNavigationBar.module.scss';
import homeIcon from '../../../../shared/svg/home.svg';
import bookIcon from '../../../../shared/svg/book.svg';

const ICON_SIZE = "25px";

const ChapterNavigationBar = props => {
    const { nav } = props;

    const { prev, next } = nav;

    const prevLink = prev 
        ? <NavLink key="prev" className={classes.ChapterNavUrl} to={nav.prev}>Prev</NavLink>
        : null;

    const nextLink = next
        ? <NavLink key="next" className={classes.ChapterNavUrl} to={nav.next}>Next</NavLink>
        : null;

    const navigation = [
        prevLink,
        (
            <div key="home-book" className={classes.ChapterNavIconsWrapper}>
                <NavLink to="/">
                    <img src={homeIcon} width={ICON_SIZE} height={ICON_SIZE} alt="Home" />
                </NavLink>
                <NavLink to={nav.book}>
                    <img src={bookIcon} width={ICON_SIZE} height={ICON_SIZE} alt="Book Main" />
                </NavLink>
            </div>
        ),
        nextLink,
    ];

    return <Fragment>{navigation}</Fragment>;
};

ChapterNavigationBar.propTypes = {
    nav: PropTypes.objectOf(PropTypes.string),
};

ChapterNavigationBar.defaultProps = {
    nav: [],
};

export default ChapterNavigationBar;
