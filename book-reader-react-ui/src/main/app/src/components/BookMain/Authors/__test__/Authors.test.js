import React from 'react';
import { mount } from 'enzyme';

import Authors from '../Authors';
import { expect } from 'chai';

describe("<Authors />", () => {
    let component;

    const renderComponent = (authors) => {
        component = mount(<Authors>{authors}</Authors>).find(Authors);
    };

    test("when 'authors' is undefined, the white space is rendered", () => {
        renderComponent();

        expect(component.text()).to.be.equal(' ');
    });

    test("when 'authors' is null, the white space is rendered", () => {
        renderComponent(null);

        expect(component.text()).to.be.equal(' ');
    });

    test("when 'authors' is an empty array, the white space is rendered", () => {
        renderComponent([]);

        expect(component.text()).to.be.equal(' ');
    });

    test("test rendering one author", () => {
        renderComponent(['Author One']);

        expect(component.text()).to.be.equal('by Author One');
    });

    test("test rendering multiple authors", () => {
        renderComponent(['Author One', 'Author Two']);

        expect(component.text()).to.be.equal('by Author One, Author Two');
    })
});
