import React from 'react';
import { mount } from 'enzyme';

import Title from '../Title';
import { expect } from 'chai';

describe("<Title />", () => {
    test("shows passed string", () => {
        const title = "Mock Title";

        const wrapper = mount(<Title>{title}</Title>);
        const component = wrapper.find(Title);
        
        expect(component.text()).to.be.equal(title);
    });

    test("shows whitespace if nothing is passed", () => {
        const wrapper = mount(<Title>{null}</Title>);
        const component = wrapper.find(Title);

        expect(component.text()).to.be.equal(' ');
    });

    test("shows whitespace if an empty string is passed", () => {
        const wrapper = mount(<Title>{''}</Title>);
        const component = wrapper.find(Title);

        expect(component.text()).to.be.equal(' ');
    });
});