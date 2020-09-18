import React, { useEffect, useState } from 'react';
import { useSearch } from '../../hooks/useSearch';

import { fetchPage } from './bookListUtils';
import BookListItem from './BookListItem/BookListItem';
import Paginator from './Paginator/Paginator';
import Spinner from '../UI/Spinner/Spinner';

import classes from './BookList.module.scss';

const BookList = props => {
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
