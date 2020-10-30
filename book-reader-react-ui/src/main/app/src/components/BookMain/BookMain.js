import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import Spinner from '../UI/Spinner/Spinner';
import Title from './Title/Title';
import Authors from './Authors/Authors';
import ReleaseDate from './ReleaseDate/ReleaseDate';
import Topics from './Topics/Topics';
import Resources from './Resources/Resources';
import Image from '../UI/Image/Image';
import DocViewer from '../UI/DocViewer/DocViewer';
import ReadButton from './ReadButton/ReadButton';
import navModes from '../UI/NavigationBar/navigationModes';
import * as actions from '../../store/actions';
import { useSearch } from '../../hooks';

import classes from './BookMain.module.scss';

const BookMain = () => {
    const dispatch = useDispatch();
    const { loading, bookInfo, error } = useSelector(state => state.books);
    const { id } = useParams();
    const { title } = useSearch();

    useEffect(() => {
        dispatch(actions.fetchBookMainPage(id));
        dispatch(actions.fetchBookReadingHistory(id));
    }, [id, dispatch]);

    useEffect(() => {
        dispatch(actions.setNavigation(navModes.MAIN));
    }, [dispatch])

    const spinner = loading ? <Spinner /> : null;

    const titleClean = decodeURIComponent(title);
    const errorView = error 
        ? <div className={classes.Error}>Error while fetching "{titleClean}" main page: {error}</div>
        : null;

    const bookInfoComp = bookInfo ? (
        <div data-type="book-info">
            <Image 
                image={bookInfo.image} 
                imageWrapperClass={classes.ImageWrapper}
                imageClass={classes.Image}
            />
            <div className={classes.MetaInfo}>
                <Title>{bookInfo.title}</Title>
                <Authors>{bookInfo.authors}</Authors>
                <ReleaseDate>{bookInfo.releaseDate}</ReleaseDate>
                <Topics>{bookInfo.topics}</Topics>
                <ReadButton toc={bookInfo.toc} />
                <h2 className={classes.Title}>Description:</h2>
                <DocViewer docs={bookInfo.description} />
                <Resources>{bookInfo.resources}</Resources>
            </div>
        </div>
    ) : null;
    
    return (
        <div className={classes.BookMainWrapper}>
            {errorView}
            {spinner}
            {bookInfoComp}
        </div>
    );
};

export default BookMain;
