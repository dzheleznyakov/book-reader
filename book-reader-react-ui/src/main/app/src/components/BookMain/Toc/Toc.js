import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import * as actions from '../../../store/actions';

import classes from './Toc.module.scss';

const Toc = () => {
    const dispatch = useDispatch();
    const { id: bookId } = useParams();
    const { toc } = useSelector(state => state.books);

    useEffect(() => {
        dispatch(actions.fetchBookToc(bookId));
    }, [bookId, dispatch]);

    let view = null;
    if (toc && toc.length) {
        view = (
            <React.Fragment>
                <h2 className={classes.Title}>Table of Content</h2>
                <ul className={classes.Toc}>
                    {toc.map(([id, title]) => <li key={id}><Link to={`/books/${bookId}/chapters/${id}`}>{title || id}</Link></li>)}
                </ul>
            </React.Fragment>
        );
    }


    return <div>{view}</div>;
};

export default Toc;
