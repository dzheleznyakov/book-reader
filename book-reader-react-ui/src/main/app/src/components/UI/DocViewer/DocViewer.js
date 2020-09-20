import React from 'react';
import PropTypes from 'prop-types';

import Views from './Views/Views';
import docShape from './docShape';

import classes from './DocViewer.module.scss';

const DocViewer = props => {
    const { docs } = props;

    return (
        <div className={classes.DocViewer}>
            <Views docs={docs} />
        </div>
    );
};

DocViewer.propTypes = {
    docs: PropTypes.arrayOf(PropTypes.shape(docShape)),
};

export default DocViewer;