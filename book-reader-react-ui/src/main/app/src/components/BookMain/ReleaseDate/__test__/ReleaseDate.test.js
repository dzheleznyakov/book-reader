import React from 'react';
import { mount } from 'enzyme';

import ReleaseDate from '../ReleaseDate';

describe("<ReleaseDate />", () => {
    let component;

    beforeEach(() => {
        component = null;
    });

    const renderComponent = releaseDate => {
        component = mount(<ReleaseDate>{releaseDate}</ReleaseDate>);
    };

    test("test with 'releaseDate' undefined", () => {
        renderComponent();

        expect(component.text()).to.be.equal(' ');
    });

    test("test with 'releaseDate' null", () => {
        renderComponent(null);

        expect(component.text()).to.be.equal(' ');
    });

    test("test with 'releaseDate' an empty string", () => {
        renderComponent('');

        expect(component.text()).to.be.equal(' ');
    });

    test("test with normal 'releaseDate'", () => {
        renderComponent('January 1970');

        expect(component.text()).to.be.equal('Release Date: January 1970');
    });
});
