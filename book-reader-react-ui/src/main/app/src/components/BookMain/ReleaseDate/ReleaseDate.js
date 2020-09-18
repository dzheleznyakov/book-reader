import React from 'react';
import PropTypes from 'prop-types';

import classes from './ReleaseDate.module.scss';

const ReleaseDate = props => {
    const { children } = props;

    let prefix = null;
    if (children)
        prefix = 'Release Date:';

    return <div className={classes.ReleaseDateWrapper}>{prefix} <span className={classes.ReleaseDate}>{children}</span></div>;
};

ReleaseDate.propTypes = {
    children: PropTypes.string,
};

export default ReleaseDate;
