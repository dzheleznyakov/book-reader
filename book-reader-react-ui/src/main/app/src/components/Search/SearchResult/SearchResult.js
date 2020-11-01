import React from 'react';
import PropTypes from 'prop-types';

import BookListItem from '../../BookList/BookListItem/BookListItem';
import SearchChaptersList from './SearchChaptersList/SearchChaptersList';

import classes from './SearchResult.module.scss';

const SearchResult = props => {
    const { items } = props;

    const list = items
        .map(item => {
            const { title, authors, bookId, chapterIds, image, topics } = item;
            return (
                <li key={bookId}>
                    <BookListItem 
                        id={bookId}
                        image={image}
                        title={title}
                        authors={authors}
                        topics={topics}
                    >
                        <SearchChaptersList chapterIds={chapterIds} bookId={bookId}/>
                    </BookListItem>
                </li>
            );
        });

    return <ul className={classes.SearchResult}>{list}</ul>;
};

SearchResult.propTypes = {
    items: PropTypes.arrayOf(PropTypes.shape({
        title: PropTypes.string,
        authors: PropTypes.arrayOf(PropTypes.string),
        bookId: PropTypes.string,
        chapterIds: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.string)),
        image: PropTypes.arrayOf(PropTypes.number),
        topics: PropTypes.arrayOf(PropTypes.string),
    })),
};

SearchResult.defaultProps = {
    item: [],
};

export default SearchResult;
