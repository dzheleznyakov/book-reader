import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

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

    useEffect(() => {
        fetchBookMainPage(id)
            .then(info => setBookInfo(info));
    }, [id]);

    const bookInfoComp = bookInfo ? (
        <div>
            <Image 
                image={bookInfo.image} 
                imageWrapperClass={classes.ImageWrapper}
                imageClass={classes.Image}
            />
            <div>
                <Title>{bookInfo.title}</Title>
                <Authors>{bookInfo.authors}</Authors>
                <ReleaseDate>{bookInfo.releaseDate}</ReleaseDate>
                <Topics>{bookInfo.topics}</Topics>
                <DocViewer docs={bookInfo.description} />
                <Resources>{bookInfo.resources}</Resources>
            </div>
        </div>
    ) : null;
    
    return (
        <div className={classes.BookMainWrapper}>
            {bookInfoComp}
        </div>
    );
};

export default BookMain;
