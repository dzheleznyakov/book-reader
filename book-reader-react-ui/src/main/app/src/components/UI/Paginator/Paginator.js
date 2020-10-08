import React from 'react';
import PropTypes from 'prop-types';
import { useHistory } from 'react-router';

import { useSearch } from '../../../hooks';

import classes from './Paginator.module.scss';

const Paginator = props => {
    const { totalCount, pageSize } = props;
    const queryParams = useSearch();
    const history = useHistory();

    const page = +queryParams.page || 1;
    const numberOfPages = totalCount % pageSize === 0 
        ? totalCount / pageSize 
        : Math.floor(totalCount / pageSize) + 1;
    const hasPrev = page > 1;
    const hasNext = page < numberOfPages;

    const { location } = history;
    const { pathname } = location;
    const toPage = pageToGo => () => {
        const updatedParams = { ...queryParams, page: pageToGo };
        const prevPath = `${pathname}?` +
            Object.keys(updatedParams).map(key => `${key}=${updatedParams[key]}`).join('&');
        history.push(prevPath);
    };
    const toPrevPage = toPage(page - 1);
    const toNextPage = toPage(page + 1);

    return (
        <div className={classes.Paginator}>
            <button disabled={!hasPrev} onClick={toPrevPage}>Prev</button>
            {page} / {numberOfPages}
            <button disabled={!hasNext} onClick={toNextPage}>Next</button>
        </div>
    );
};

Paginator.propTypes = {
    totalCount: PropTypes.number,
    pageSize: PropTypes.number,
};

Paginator.defaultProps = {
    totalCount: 0,
    pageSize: 10,
};

export default Paginator;
