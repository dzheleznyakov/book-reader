import React from 'react';

import DocViewer from './DocViewer';

const mapper = (type, content) => {
    if (type === 'TEXT') {
        return content;
    }
    return <DocViewer docs={content} />
};

export default mapper;
