import React from 'react';
import PropTypes from 'prop-types';
import { useHistory, useParams } from 'react-router';
import { useSelector } from 'react-redux';

import classes from './ReadButton.module.scss';

const ReadButton = props => {
    const { historyItem } = useSelector(state => state.books);
    const { toc } = props;
    const { id } = useParams();
    const history = useHistory();

    const { bookId, lastChapterIndex } = historyItem;

    const nextChapter = bookId === id && lastChapterIndex >= 0 ? toc[lastChapterIndex] : toc[0];
    const clicked = toc.length
        ? ()  => history.push(`/books/${id}/chapters/${nextChapter}`)
        : () => {};

    let buttonText = "Start Reading";
    if (bookId === id && lastChapterIndex >= 0)
        buttonText = "Continue";

return <button className={classes.ReadButton} onClick={clicked}>{buttonText}</button>;
};

ReadButton.propTypes = {
    toc: PropTypes.arrayOf(PropTypes.string),
};

ReadButton.defaultProps = {
    toc: [],
};

export default ReadButton;
