import React from 'react';
import { mount } from 'enzyme';

import BookListItem from '../../../../components/BookList/BookListItem/BookListItem';
import Image from '../../../../components/BookList/Image/Image';

import classes from '../../../../components/BookList/BookListItem/BookListItem.module.scss';
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

    beforeAll(() => {
        realCreateObjectURL = global.URL.createObjectURL;
        global.URL.createObjectURL = () => {};
    });

    beforeEach(() => {
        wrapper = mount(<BookListItem {...props} />);
    });

    afterAll(() => {
        global.URL.createObjectURL = realCreateObjectURL;
    });

    const getTitle = () => wrapper.find('h1').text();
    const getAuthors = () => wrapper.find('span[data-type="authors"]').text();
    const getTopics = () => wrapper.find('span[data-type="topics"]').text();
    const getImageBytes = () => wrapper.find(Image).prop('image');

    test("render without errors", () => {    
        expect(getTitle()).to.be.equal(props.title);
        expect(getAuthors()).to.be.equal(props.authors.join(', '));
        expect(getTopics()).to.be.equal(props.topics.join(', '));
        expect(getImageBytes()).to.be.deep.equal(props.image);
    });
});