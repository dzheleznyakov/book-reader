import React, { Fragment } from 'react';
import PropTypes from 'prop-types';

import viewMapper from './viewFactory';
import contentMapper from './contentMapper';
import docShape from '../docShape';
import classFactory from './classFactory';
import types from './types';

const Views = props => {
    const { docs } = props;

    const views = docs.map((doc, i) => {
        const { 
            id, 
            documentType: type, 
            content: docContent, 
            formatting = [], 
            href, 
            width, 
            height,
            metadata,
        } = doc;
        const View = viewMapper(type, formatting);
        const content = contentMapper(type, docContent);

        let image;
        if (Array.isArray(docContent) && docContent.length && Number.isInteger(docContent[0])) {
            image = docContent;
        }

        const colspan = metadata && metadata['@colspan'];

        const viewProps = { 
            key: i, 
            id: id || undefined,
            href,
            width: width || undefined,
            height: height || undefined,
            colSpan: colspan || undefined,
            image,
            className: classFactory(type, formatting).toString(),
        }
        if (type === types.RAW)
            viewProps.dangerouslySetInnerHTML = { __html: content };
        else
            viewProps.children = content;

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
