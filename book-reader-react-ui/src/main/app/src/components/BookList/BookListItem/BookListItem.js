import React from 'react';
import PropTypes from 'prop-types';
import DotDotDot from 'react-dotdotdot';

import Image from '../Image/Image';

import classes from './BookListItem.module.scss';

const BookListItem = props => {
    const { id, image = [], title, authors = [], topics = [] } = props;
        return (
            <div className={classes.BookListItemWrapper}>
                <div className={classes.BookListItem}>
                    <Image image={image} />
                    <div className={classes.BookListInfo}>
                        <DotDotDot clamp={4}>
                            <h1 className={classes.Title}><a href={`/books/${id}`}>{title}</a></h1>
                        </DotDotDot>
                        <span>{authors.join(', ')}</span>
                        <span className={classes.Topics}>{topics.join(', ')}</span>
                    </div>
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
