import React from 'react';
import { mount } from 'enzyme';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';

import BookListItem from '../BookListItem';
import Image from '../../Image/Image';
import { expect } from 'chai';

describe("<BookListItem />", () => {
    const props = {
        id: 'mock-id',
        image: [1, 2, 3],
        title: 'Mock Title',
        authors: ['Author One', 'Author Two'],
        topics: ['Topic One', 'Topic Two'],
    };
    let wrapper;
    let realCreateObjectURL;
    let history;

    beforeAll(() => {
        realCreateObjectURL = global.URL.createObjectURL;
        global.URL.createObjectURL = () => {};
    });

    beforeEach(() => {
        history = createMemoryHistory({ initialEntries: ['/books'] })
        wrapper = mount(<Router history={history}><BookListItem {...props} /></Router>);
    });

    afterAll(() => {
        global.URL.createObjectURL = realCreateObjectURL;
    });

    const getTitle = () => wrapper.find('h1').text();
    const getAuthors = () => wrapper.find('span[data-type="authors"]').text();
    const getTopics = () => wrapper.find('span[data-type="topics"]').text();
    const getImageBytes = () => wrapper.find(Image).prop('image');
    const clickTitleUrl = () => wrapper.find('.Url').simulate('click');

    test("render without errors", () => {    
        expect(getTitle()).to.be.equal(props.title);
        expect(getAuthors()).to.be.equal(props.authors.join(', '));
        expect(getTopics()).to.be.equal(props.topics.join(', '));
        expect(getImageBytes()).to.be.deep.equal(props.image);
        
        const expectedPathname = `/books/${props.id}`;
        expect(history.location.pathname).to.be.not.equal(expectedPathname);
        clickTitleUrl();
        expect(history.location.pathname).to.be.equal(expectedPathname);
    });
});