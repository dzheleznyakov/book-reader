import React from 'react';
import PropTypes from 'prop-types';

import classes from './Topics.module.scss';

const Topics = props => {
    const { children } = props;
    const topicsString = children && children.length
        ? `${children.join(', ')}`
        : null;

    return <div className={classes.Topics}>{topicsString}</div>;
};

Topics.propTypes = {
    children: PropTypes.arrayOf(PropTypes.string),
};

export default Topics;