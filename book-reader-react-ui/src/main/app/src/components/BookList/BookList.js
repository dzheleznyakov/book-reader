import React, { useEffect, useState } from 'react';
import { useSearch } from '../../hooks/useSearch';

import { fetchPage } from './bookListUtils';
import BookListItem from './BookListItem/BookListItem';
import Paginator from './Paginator/Paginator';

import classes from './BookList.module.scss';

const BookList = props => {
    const { page = 1 } = useSearch();
    const [bookList, setBookList] = useState([]);

    useEffect(() => {
        fetchPage(page)
            .then(bookList => {
                setBookList(bookList);
            });
    }, [page]);

    const list = bookList.map(entry => <BookListItem key={entry.id} {...entry} />);

    return (
        <div className={classes.BookList}>
            {list}
            <Paginator />
        </div>
    );
};

export default BookList;
