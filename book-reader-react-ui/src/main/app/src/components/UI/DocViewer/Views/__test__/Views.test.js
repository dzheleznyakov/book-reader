import React from 'react';
import { shallow } from 'enzyme';

import Views from '../Views';
import types from '../types';

describe("<Views />", () => {
    let wrapper;
    const id = 'mock-id';
    const docText = 'Mock content.';
    const doc = {
        documentType: types.BLOCK,
        formatting: [],
        id,
        content: [{
            documentType: types.TEXT,
            content: docText,
        }],
    };

    afterEach(() => {
        wrapper = null;
    });

    const renderComponent = () => {
        wrapper = shallow(<Views docs={[doc]} />)
    }

    test("If document has an id, then the rendered element has the same id",  () => {
        renderComponent();

        expect(wrapper.html()).to.be.equal(`<div id="${id}">${docText}</div>`);
    });
});
