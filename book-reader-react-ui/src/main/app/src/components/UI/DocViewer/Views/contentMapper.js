import React from 'react';

import Views from './Views';

const mapper = (type, formatting, content) => {
    if (type === 'TEXT') {
        return content;
    }
    return <Views docs={content} />
};

export default mapper;
