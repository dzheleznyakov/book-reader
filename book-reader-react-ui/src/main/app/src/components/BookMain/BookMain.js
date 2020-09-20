import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

import Spinner from '../UI/Spinner/Spinner';
import Title from './Title/Title';
import Authors from './Authors/Authors';
import ReleaseDate from './ReleaseDate/ReleaseDate';
import Topics from './Topics/Topics';
import Resources from './Resources/Resources';
import Image from '../UI/Image/Image';
import DocViewer from '../UI/DocViewer/DocViewer';
import { fetchBookMainPage } from './bookMainUtils';

import classes from './BookMain.module.scss';

const BookMain = props => {
    const params = useParams();
    const { id } = params;

    const [bookInfo, setBookInfo] = useState();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchBookMainPage(id)
            .then(info => {
                setLoading(false);
                setBookInfo(info);
            });
    }, [id]);

    const spinner = loading ? <Spinner /> : null;
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
                <h2 className={classes.Title}>Description:</h2>
                <DocViewer docs={bookInfo.description} />
                <Resources>{bookInfo.resources}</Resources>
            </div>
        </div>
    ) : null;
    
    return (
        <div className={classes.BookMainWrapper}>
            {spinner}
            {bookInfoComp}
        </div>
    );
};

export default BookMain;
