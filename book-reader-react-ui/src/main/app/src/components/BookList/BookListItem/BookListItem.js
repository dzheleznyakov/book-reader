import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import DotDotDot from 'react-dotdotdot';

import Image from '../../UI/Image/Image';

import classes from './BookListItem.module.scss';

const BookListItem = props => {
    const { id, image, title, authors, topics, children } = props;
        
    const titleRef = (
        <Link
            className={classes.Url}
            to={`/books/${id}?title=${title}`}
        >
            {title}
        </Link>
    );

    return (
        <div className={classes.BookListItemWrapper}>
            <div className={classes.BookListCard}>
                <div className={classes.BookListItem}>
                    <Image image={image} />
                    <div className={classes.BookListInfo}>
                        <DotDotDot clamp={4}>
                            <h1 className={classes.Title}>{titleRef}</h1>
                        </DotDotDot>
                        <span data-type="authors">{authors.join(', ')}</span>
                        <span data-type="topics" className={classes.Topics}>{topics.join(', ')}</span>
                    </div>
                </div>
                {children}
            </div>
        </div>
    );
};

BookListItem.propTypes = {
    id: PropTypes.string.isRequired,
    title: PropTypes.string,
    authors: PropTypes.arrayOf(PropTypes.string),
    topics: PropTypes.arrayOf(PropTypes.string),
    image: PropTypes.arrayOf(PropTypes.number),
};

BookListItem.defaultProps = {
    title: "",
    authors: [],
    topics: [],
    image: [],
};

export default BookListItem;
