import React from 'react';
import PropTypes from 'prop-types';
import { useHistory, useParams } from 'react-router';

import classes from './ReadButton.module.scss';

const ReadButton = props => {
    const { toc } = props;
    const { id } = useParams();
    const history = useHistory();

    const clicked = toc.length
        ? ()  => history.push(`/books/${id}/chapters/${toc[0]}`)
        : () => {};

    return <button className={classes.ReadButton} onClick={clicked}>Start Reading</button>;
};

ReadButton.propTypes = {
    toc: PropTypes.arrayOf(PropTypes.string),
};

ReadButton.defaultProps = {
    toc: [],
};

export default ReadButton;
