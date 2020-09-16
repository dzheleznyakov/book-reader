import React from 'react';
import PropTypes from 'prop-types';
import { useHistory } from 'react-router-dom';
import DotDotDot from 'react-dotdotdot';

import Image from '../Image/Image';

import classes from './BookListItem.module.scss';

const BookListItem = props => {
    const { id, image, title, authors, topics } = props;
    const history = useHistory();
    
    const onTitleClicked = () => history.push(`/books/${id}`);
    
    const titleRef = (
        <button
            className={classes.Url}
            onClick={onTitleClicked}
        >
            {title}
        </button>
    );

    return (
        <div className={classes.BookListItemWrapper}>
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
