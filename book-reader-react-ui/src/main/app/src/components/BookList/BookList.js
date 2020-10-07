import React, { useEffect, useState } from 'react';
import { useSearch } from '../../hooks/useSearch';
import { useDispatch } from 'react-redux';

import * as actions from '../../store/actions';
import navModes from '../UI/NavigationBar/navigationModes';
import { fetchPage } from './bookListUtils';
import BookListItem from './BookListItem/BookListItem';
import Paginator from './Paginator/Paginator';
import Spinner from '../UI/Spinner/Spinner';

import classes from './BookList.module.scss';

const BookList = () => {
    const dispatch = useDispatch();
    const { page = 1 } = useSearch();
    const [bookList, setBookList] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchPage(page)
            .then(bookList => {
                setLoading(false);
                setBookList(bookList);
            });
    }, [page]);

    useEffect(() => {
        dispatch(actions.setNavigation(navModes.MAIN));
    }, [dispatch]);

    const spinner = loading ? <Spinner /> : null;
    const list = bookList.map(entry => <BookListItem key={entry.id} {...entry} />);

    return (
        <div className={classes.BookList}>
            {spinner}
            {list}
            <Paginator />
        </div>
    );
};

export default BookList;
