import React from 'react';
import PropTypes from 'prop-types';

import classes from './Resources.module.scss';

const Resources = props => {
    const { children } = props;

    const renderResource = (descr, url) => (
        <li key={descr}>
            <strong>{descr}:</strong>{' '}
            <a href={url} className={classes.Url}>
                {url}
            </a>
        </li>
    );

    const resourcesArePresent = children && Object.keys(children).length;
    const resources = resourcesArePresent ? (
        <div className={classes.ResourcesWrapper}>
            <h2 className={classes.Title}>Resources:</h2>
            <ul className={classes.Resources}>
                {Object.keys(children).map(key => renderResource(key, children[key]))}
            </ul>
        </div>
    ) : null;

    return resources;
};

Resources.propTypes = {
    children: PropTypes.objectOf(PropTypes.string),
};

export default Resources;
