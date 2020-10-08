import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import Spinner from '../UI/Spinner/Spinner';
import Paginator from '../UI/Paginator/Paginator';
import SearchHeader from './SearchHeader/SearchHeader';
import SearchResult from './SearchResult/SearchResult';
import { useSearch } from '../../hooks';
import * as actions from '../../store/actions';
import localStorageKeys from '../../shared/keys/localStorageKeys';

import classes from './Search.module.scss';

const PAGE_SIZE = 10;

const Search = () => {
    let { query, fetching, totalCount, results } = useSelector(state => state.search);
    const dispatch = useDispatch();

    if (!query)
        query = localStorage.getItem(localStorageKeys.query) || '';

    const { page = 1 } = useSearch();

    useEffect(() => {
        const limit = PAGE_SIZE;
        const offset = limit * (page - 1);
        dispatch(actions.search(query, offset, limit));
    }, [dispatch, page, query]);

    const view = fetching ? <Spinner /> : <SearchResult items={results} />;

    return (
        <div className={classes.SearchWrapper}>
            <SearchHeader />
            {view}
            <Paginator totalCount={totalCount} pageSize={PAGE_SIZE} />
        </div>
    );
};

export default Search;
