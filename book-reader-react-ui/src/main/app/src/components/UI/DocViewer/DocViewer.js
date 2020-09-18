import React from 'react';

import Views from './Views/Views';

import classes from './DocViewer.module.scss';

const DocViewer = props => {
    const { docs } = props;

    return (
        <div className={classes.DocViewer}>
            <h2 className={classes.Title}>Description:</h2>
            <Views docs={docs} />
        </div>
    );
};

export default DocViewer;