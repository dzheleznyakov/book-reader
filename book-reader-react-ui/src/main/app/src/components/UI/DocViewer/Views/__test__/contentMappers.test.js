import React from 'react';

import contentMapper from '../contentMapper';
import Views from '../Views';
import types from '../types';
import { expect } from 'chai';

describe("contentMapper()", () => {
    test("map content for text doc", () => {
        const content = 'some text';

        const mappedContent = contentMapper(types.TEXT, content);

        expect(mappedContent).to.be.equal(content);
    });

    test("map content for enclosing doc", () => {
        const content = [{
            documentType: types.TEXT,
            content: 'internal text',
        }]

        const mappedContent = contentMapper(types.PARAGRAPH, content);

        expect(mappedContent.type).to.be.equal(Views);
        expect(mappedContent.props.docs).to.be.deep.equal(content);
    });
});
