import React from 'react';
import PropTypes from 'prop-types';

import classes from './Title.module.scss';

const Title = props => {
    const title = props.children || ' ';
    return (
        <h1 className={classes.Title}>
            {title}
        </h1>
    );
};

Title.propTypes = {
    children: PropTypes.string,
};

export default Title;
