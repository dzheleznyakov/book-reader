import React from 'react';

import Views from './Views';
import types from './types';

const mapper = (type, content) => {
    switch (type) {
        case types.TEXT: case types.RAW: return content;
        case types.IMAGE: return null;
        default: return <Views docs={content} />;
    }
};

export default mapper;
