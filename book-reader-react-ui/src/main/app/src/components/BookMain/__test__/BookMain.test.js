import React from 'react';
import { Router } from 'react-router-dom';
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import { mount } from 'enzyme';
import { createMemoryHistory } from 'history';
import { act } from '@testing-library/react';

import BookMain from '../BookMain';
import axios from '../../../axios-api';
import reducer from '../../../store/reducers';
import { fetchBookMainPageSuccess } from '../../../store/actions/books';

import bookResponse from './bookResponse.json';

describe("<BookMain />", () => {
    const bookId = bookResponse.id;
    const responseParagraphs = [
        'Paragraph 1 of the description text.',
        'Paragraph 2 of the description text.',
        'Paragraph 3 of the description text.',
    ];

    let reduxStore;
    const defaultBookState = { 
        loading: false, 
        bookInfo: bookResponse,
        historyItem: { bookId, lastChapterIndex: -1 },
    };

    let realCreateObjectURL;
    let mockCreateObjectURL;

    beforeAll(() => {
        realCreateObjectURL = global.URL.createObjectURL;
    });

    afterAll(() => {
        global.URL.createObjectURL = realCreateObjectURL;
        reduxStore = null;
    });

    const sandbox = sinon.createSandbox();

    let axiosGetStub;

    let history;
    let wrapper;

    beforeEach(() => {
        mockCreateObjectURL = sandbox.mock()
                .returns('https://mock-url.net')
        global.URL.createObjectURL = mockCreateObjectURL;

        history = createMemoryHistory({ initialEntries: [`/books/${bookId}`] });
    });

    afterEach(() => {
        sandbox.restore();
    });

    const renderComponent = async (books) => {
        const initialState = { books: { ...defaultBookState, ...books } };
        reduxStore = createStore(reducer, initialState);
        await act(
            async () => {
                wrapper = mount(
                    <Provider store={reduxStore}>
                        <Router history={history}>
                            <BookMain />
                        </Router>
                    </Provider>
                );
            }
        );
    };

    test("test render without errors", async () => {
        await renderComponent();

        const bookMainText = wrapper.find(BookMain).text();

        expect(bookMainText).to.contain(bookResponse.title);
        expect(bookMainText).to.contain(bookResponse.releaseDate);
        bookResponse.authors.forEach(author => expect(bookMainText).to.contain(author));
        bookResponse.topics.forEach(topic => expect(bookMainText).to.contain(topic));
        Object.keys(bookResponse.resources)
            .forEach(resource => {
                expect(bookMainText).to.contain(resource);
                expect(bookMainText).to.contain(bookResponse.resources[resource]);
            })
        responseParagraphs.forEach(par => expect(bookMainText).to.contain(par));
    });

    test("the page shows spinner until the book is loaded", async () => {
        await renderComponent({ loading: true, bookInfo: null });

        const bookMain = wrapper.find(BookMain);
        let renderedBookMain = bookMain.render();

        let bookInfo = renderedBookMain.find('[data-type="book-info"]');
        let spinner = renderedBookMain.find('.Spinner');
        expect(bookInfo).to.have.length(0);
        expect(spinner).to.have.length(1);

        reduxStore.dispatch(fetchBookMainPageSuccess(bookResponse));

        renderedBookMain = bookMain.render();

        bookInfo = renderedBookMain.find('[data-type="book-info"]');
        spinner = renderedBookMain.find('.Spinner');
        expect(bookInfo).to.have.length(1);
        expect(spinner).to.have.length(0);
    });
});