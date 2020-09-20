import React, { Fragment } from 'react';
import PropTypes from 'prop-types';

import viewMapper from './viewFactory';
import contentMapper from './contentMapper';
import docShape from '../docShape';

const Views = props => {
    const { docs } = props;

    const views = docs.map((doc, i) => {
        const { id, documentType: type, content: docContent, formatting = [] } = doc;
        const View = viewMapper(type, formatting);
        const content = contentMapper(type, docContent);
        const viewProps = { 
            key: i, 
            children: content, 
            id: id || undefined
        }
        // console.log(JSON.stringify(docContent));
        return View(viewProps);
    })

    return (
        <Fragment>
            {views}
        </Fragment>
    );
};

Views.propTypes = {
    docs: PropTypes.arrayOf(PropTypes.shape(docShape)),
};

Views.defaultProps = {
    docs: [],
};

export default Views;
