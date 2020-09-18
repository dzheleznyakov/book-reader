import React from 'react';

import viewMapper from './viewMapper';
import contentMapper from './contentMapper';

const DocViewer = props => {
    const { docs } = props;

    const views = docs.map((doc, i) => {
        const type = doc.documentType;
        const docContent = doc.content;
        const View = viewMapper(type);
        const content = contentMapper(type, docContent);
        return View({ key: i, children: content });
    })

    return <div>{views}</div>;
};

export default DocViewer;