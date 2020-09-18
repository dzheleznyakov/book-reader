import React from 'react';
import PropTypes from 'prop-types';

import classes from './Authors.module.scss';

const Authors = props => {
    const { children } = props;

    let prefix = null;
    let authors = null;
    if (children && children.length) {
        prefix = 'by';
        authors = <span className={classes.Authors}>{children.join(', ')}</span>;
    }

    return (
        <div>{prefix} {authors}</div>
    );
};

Authors.propTypes = {
    authors: PropTypes.arrayOf(PropTypes.string),
};

Authors.defaultTypes = {
    authors: [],
};

export default Authors;
