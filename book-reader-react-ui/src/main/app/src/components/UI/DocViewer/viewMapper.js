import React from 'react';

const mapper = type => {
    if (type === 'TEXT') {
        return props => props.children;
    }
    return props => <div key={props.key}>{props.children}</div>;
};

export default mapper;
