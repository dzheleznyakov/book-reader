import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';

import classes from './SearchChaptersList.module.scss';
import arrow from '../../../../shared/svg/triangle-right.svg';

const SearchChaptersList = props => {
    const { chapterIds, bookId} = props;
    const [expanded, setExpanded] = useState(null);

    const detailsCssClasses = [classes.DetailsControl];
    if (expanded === true)
        detailsCssClasses.push(classes.Expanded);
    else if (expanded === false)
        detailsCssClasses.push(classes.Shrinked)

    const expand = () => setExpanded(!expanded);

    const detailsMessage = expanded ? 'Less details' : 'More details';
    
    const chapterLinks = expanded ? chapterIds
        .map(([chId, title]) => (
            <li key={bookId + chId}>
                <Link 
                    to={`/books/${bookId}/chapters/${chId}`}
                    className={classes.Url}
                >
                    {title}
                </Link>
            </li>
        )) : null;

    return (
        <ul className={classes.Wrapper}>
            <div 
                className={detailsCssClasses.join(' ')}
                onClick={expand}
            >
                <img src={arrow} width="10px" height="10px" alt=">" />
                {detailsMessage}
            </div>
            {chapterLinks}
        </ul>
    );
};

SearchChaptersList.propTypes = {
    chapterIds: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.string)),
    bookId: PropTypes.string,
};

SearchChaptersList.defaultProps = {};

export default SearchChaptersList;
