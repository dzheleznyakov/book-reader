import React from 'react';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';
import { mount } from 'enzyme';
import { act } from '@testing-library/react';

import Paginator from '../Paginator';
import { PAGE_SIZE } from '../../../BookList/bookListUtils';

describe("<Paginator />", () => {
    let wrapper;
    let paginator;
    let prevButton;
    let nextButton;
    let history;

    afterEach(() => {
        history = null;
    });

    
    const renderComponent = async (page, totalCount = 1000) => {
        if (page) {
            history = createMemoryHistory({ initialEntries: [`/test/path?page=${page}`] });
        } else {
            history = createMemoryHistory({ initialEntries: ['/test/path'] });
        }

        await act(
            async () => {
                wrapper = mount(
                    <Router history={history}>
                        <Paginator totalCount={totalCount} pageSize={PAGE_SIZE} />
                    </Router>
                );
                paginator = wrapper.find(Paginator)
                prevButton = paginator.find('button').at(0);
                nextButton = paginator.find('button').at(1);
            }
        );
    };

    const assertRenderedTextMatches = (regex) => {
        expect(paginator.text()).to.match(regex);
    };

    const assertButtonsDisabled = (prevDisabled, nextDisabled) => {
        const prevButtonAttribs = prevButton.render()[0].attribs;
        expect(prevButtonAttribs.disabled === '').to.be.equal(prevDisabled)
        const nextButtonAttribs = nextButton.render()[0].attribs;
        expect(nextButtonAttribs.disabled === '').to.be.equal(nextDisabled)
    };

    const clickButton = async (button) => {
        await act(
            async () => button.simulate('click')
        );
    };

    const assertPage = pageNumber => {
        expect(history.location.search).to.contain('page=' + pageNumber);
    };

    test("if page query param is not specified, then it defaults to 1", async () => {
        await renderComponent(null, 1000);
        assertRenderedTextMatches(/Prev1 \/ \d+Next/);
    })
    
    test("if page query param is specified, then the corresponding page is shown", async () => {
        await renderComponent(42, 1000);
        assertRenderedTextMatches(/Prev42 \/ \d+Next/);
    });

    test("calculated number of pages if the total number is a divisor of 10", async () => {
        await renderComponent(null, 1000);
        assertRenderedTextMatches(/Prev\d+ \/ 100Next/);
    });

    test("calculated number of pages if the total number is not a divisor of 10", async () => {
        await renderComponent(null, 1001);
        assertRenderedTextMatches(/Prev\d+ \/ 101Next/);
    });

    test("the Prev button should be disabled on the first page", async () => {
        await renderComponent(1, 1000);
        assertButtonsDisabled(true, false);
    });

    test("the Next button should be disabled on the last page", async () => {
        await renderComponent(100, 1000);
        assertButtonsDisabled(false, true);
    });

    test("the Prev and Next buttons should be disabled if the page is the first and last one", async () => {
        await renderComponent(1, 5);
        assertButtonsDisabled(true, true);
    });

    test("the Prev and Next buttons should be enabled if the page is neither first nor last one", async () => {
        await renderComponent(10, 1000);
        assertButtonsDisabled(false, false);
    });

    test("when clicking the Prev button, the previous page is loaded", async () => {
        await renderComponent(10);

        await clickButton(prevButton);

        assertPage(9);
    });

    test("when clicking the Next button, the next page is loaded", async () => {
        await renderComponent(10);

        await clickButton(nextButton);

        assertPage(11);
    });
});