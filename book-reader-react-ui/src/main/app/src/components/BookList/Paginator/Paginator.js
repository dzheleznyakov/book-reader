import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router';

import { fetchCount, PAGE_SIZE } from '../bookListUtils';
import { useSearch } from '../../../hooks';

import classes from './Paginator.module.scss';

const Paginator = () => {
    const [totalCount, setTotalCount] = useState(0);
    const queryParams = useSearch();
    const history = useHistory();

    useEffect(() => {
        fetchCount().then(count => setTotalCount(count));
    }, []);

    const page = +queryParams.page || 1;
    const numberOfPages = totalCount % PAGE_SIZE === 0 
        ? totalCount / PAGE_SIZE 
        : Math.floor(totalCount / PAGE_SIZE) + 1;
    const hasPrev = page > 1;
    const hasNext = page < numberOfPages;

    const { location } = history;
    const { pathname } = location;
    const toPage = pageToGo => () => {
        const updatedSearch = { ...queryParams, page: pageToGo };
        const prevPath = `${pathname}?` +
            Object.keys(updatedSearch).map(key => `${key}=${updatedSearch[key]}`).join('&');
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

export default Paginator;
