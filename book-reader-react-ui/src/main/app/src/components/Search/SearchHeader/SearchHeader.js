import React from 'react';
import { useSelector } from 'react-redux';

import classes from './SearchHeader.module.scss';

const SearchHeader = props => {
    const { query = '', fetching, totalCount = 0, error } = useSelector(state => state.search);

    let header = null;
    if (error)
        header = <div className={[classes.Header, classes.Error].join(' ')}>Error while searching for "{query}": {error.message}</div>;
    else if (fetching)
        header = <div className={classes.Header}>Searching for "{query}"</div>;
    else
        header = <div className={classes.Header}>Results for "{query}" ({totalCount} in total)</div>;

    return header;
};

export default SearchHeader;
