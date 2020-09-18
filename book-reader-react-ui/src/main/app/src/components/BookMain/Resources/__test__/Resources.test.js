import React from 'react';
import { mount } from 'enzyme';

import Resources from '../Resources';

describe("<Resources />", () => {
    let component;

    beforeEach(() => {
        component = null;
    });

    const renderComponent = resources => {
        component = mount(<Resources>{resources}</Resources>).find(Resources);
    };

    test("test nothing is rendered when 'resources' undefined",  () => {
        renderComponent();

        expect(component.text()).to.be.equal('');
    });

    test("test nothing is rendered when 'resources' is null",  () => {
        renderComponent(null);

        expect(component.text()).to.be.equal('');
    });

    test("test nothing is rendered when 'resources' is an empty object",  () => {
        renderComponent({});

        expect(component.text()).to.be.equal('');
    });

    test("test rendering with 'resources' not empty",  () => {
        const key1 = 'Resource 1';
        const value1 = 'https://value-1.net';
        const key2 = 'Resource 2';
        const value2 = 'https://value-2.net';
        renderComponent({ [key1]: value1, [key2]: value2 });

        expect(component.text()).to.be.equal(`Resources:${key1}: ${value1}${key2}: ${value2}`);
    });
});
