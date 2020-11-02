import React from 'react';
import { mount } from 'enzyme';

import Topics from '../Topics';

describe("<Topics />", () => {
    let component;

    afterEach(() => {
        component = null;
    });

    const renderComponent = topics => {
        component = mount(<Topics>{topics}</Topics>).find(Topics);
    };

    test("test 'topics' are undefined", () => {
        renderComponent();

        expect(component.text()).to.be.equal('');
    });

    test("test 'topics' are null", () => {
        renderComponent(null);

        expect(component.text()).to.be.equal('');
    });

    test("test 'topics' are empty", () => {
        renderComponent([]);

        expect(component.text()).to.be.equal('')
    });

    test("test with one topic", () => {
        renderComponent(['Topic One']);

        expect(component.text()).to.be.equal('Topic One');
    });

    test("test with more than one topic", () => {
        renderComponent(['Topic One', 'Topic Two']);

        expect(component.text()).to.be.equal('Topic One, Topic Two');
    });
});
