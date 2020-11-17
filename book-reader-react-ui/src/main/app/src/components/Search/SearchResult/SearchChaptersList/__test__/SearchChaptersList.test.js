import { createMemoryHistory } from 'history';
import { act } from '@testing-library/react';

import SearchChaptersList from '../SearchChaptersList';

describe("<SearchChaptersList />", () => {
    const bookId = 'mock-book-id';

    let wrapper;
    let history;

    beforeEach(() => {
        history = createMemoryHistory({ initialEntries: ['search'] });
    });

    const renderComponent = async (chapterIds, bookId) => {
        const { renderComponent, getWrapper } = renderComponentBuilder(
            SearchChaptersList, { chapterIds, bookId }
        )
            .withHistory(history)
            .get();
        await renderComponent();
        wrapper = getWrapper();
    };

    const toggleExpand = async () => act(
        async () => wrapper.find('ul > div').simulate('click')
    );

    const assertText = expectedTest => {
        expect(wrapper.text()).to.be.equal(expectedTest);
    };

    const assertTextStartsWith = expectedText => {
        const regex = new RegExp(`^${expectedText}`);
        expect(wrapper.text()).to.match(regex);
    };

    test("should expand-shring when toggling the details message", async () => {
        const chapterIds = [['ch1_id', 'Ch 1 Title']];
        
        await renderComponent(chapterIds, bookId);

        assertTextStartsWith('More details');

        await toggleExpand();

        assertTextStartsWith('Less details');

        await toggleExpand();

        assertTextStartsWith('More details');
    });

    test("should be shrinked after rendering", async () => {
        const chapterIds = [['ch1_id', 'Ch 1 Title']];
        
        await renderComponent(chapterIds, bookId);

        assertText('More details');
    });

    test("should show chapter title after expansion", async () => {
        const chapterTitle = 'Ch 1 Title';
        const chapterIds = [["ch1_id", chapterTitle]];
        
        await renderComponent(chapterIds, bookId);
        await toggleExpand();

        assertText(`Less details${chapterTitle}`);
    });

    test("should show chapter id if no title provided", async () => {
        const chapterId = 'ch1_id';
        const chapterIds = [[chapterId, '']];
        
        await renderComponent(chapterIds, bookId);
        await toggleExpand();

        assertText(`Less details${chapterId}`);
    });
});
