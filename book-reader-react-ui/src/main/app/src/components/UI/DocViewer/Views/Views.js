import React, { Fragment } from 'react';
import PropTypes from 'prop-types';

import viewMapper from './viewFactory';
import contentMapper from './contentMapper';

const Views = props => {
    const { docs } = props;

    const views = docs.map((doc, i) => {
        const { documentType: type, content: docContent, formatting = [] } = doc;
        const View = viewMapper(type, formatting);
        const content = contentMapper(type, formatting, docContent);
        return View({ key: i, children: content });
    })

    return (
        <Fragment>
            {views}
        </Fragment>
    );
};

Views.propTypes = {
    docs: PropTypes.arrayOf(PropTypes.shape({})),
};

Views.defaultProps = {
    docs: [],
};

export default Views;
