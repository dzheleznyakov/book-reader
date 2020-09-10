import React, { useEffect, useState } from 'react';

import axios from '../../axios-api';
import BookListItem from './BookListItem/BookListItem';

import classes from './BookList.module.scss';

const BookList = props => {
    const [bookList, setBookList] = useState([]);

    useEffect(() => {
        axios.get('/books')
            .then(res => res.data)
            .then(list => setBookList(list));
    }, []);

    const list = bookList.map(entry => <BookListItem key={entry.id} {...entry} />);

    return <div className={classes.BookList}>{list}</div>;
};

export default BookList;
