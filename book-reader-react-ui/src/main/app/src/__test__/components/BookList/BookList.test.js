import React from 'react';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';
import { mount } from 'enzyme';
import { act } from '@testing-library/react';

import BookList from '../../../components/BookList/BookList';
import BookListItem from '../../../components/BookList/BookListItem/BookListItem';
import axios from '../../../axios-api';

describe("<BookList />", () => {
    let history;
    let wrapper;
    let bookList;

    let realCreateObjectURL;    

    const numOfBooks = 9;
    let booksResponse;

    const sandbox = sinon.createSandbox();
    let axiosGetStub;

    beforeAll(() => {
        realCreateObjectURL = global.URL.createObjectURL;
        global.URL.createObjectURL = () => {};
    });

    beforeEach(() => {
        history = createMemoryHistory({ initialEntries: ['/books'] })
        axiosGetStub = sandbox.stub(axios, 'get');
        
        booksResponse = [];
        for (let i = 0; i < numOfBooks; ++i)
            booksResponse.push({
                id: `id-${i}`,
                image: [10 * i, 10 * i + 1, 10 * i + 2],
                title: `Book Title ${i}`,
                authors: [`Author ${i}`],
                topics: [`Topic ${i}`],
            })
    });

    afterEach(() => {
        history = null;
        sandbox.restore();
    });

    afterAll(() => {
        global.URL.createObjectURL = realCreateObjectURL;
    });

    const renderComponent = async () => {
        await act(
            async () => {
                wrapper = mount(<Router history={history}><BookList /></Router>);
                bookList = wrapper.find(BookList);
            }
        );
    };

    test("render with no errors", async () => {
        axiosGetStub.resolves({ data: booksResponse });
        await renderComponent();

        const renderedBookList = bookList.render();

        const itemsList = renderedBookList.find('.BookListItem');
        expect(itemsList).to.have.length(numOfBooks);

        const paginator = renderedBookList.find('.Paginator');
        expect(paginator).to.have.length(1);
    });

    test("render even if the api has error", async () => {
        axiosGetStub.rejects({ status: 503 });
        await renderComponent();

        const renderedBookList = bookList.render();

        const itemsList = renderedBookList.find('.BookListItem');
        expect(itemsList).to.have.length(0);
    });
});