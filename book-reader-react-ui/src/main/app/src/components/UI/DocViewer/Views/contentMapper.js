import React from 'react';

import Views from './Views';
import types from './types';

const mapper = (type, content) => (type === types.TEXT
    ? content
    : <Views docs={content} />
);

export default mapper;
