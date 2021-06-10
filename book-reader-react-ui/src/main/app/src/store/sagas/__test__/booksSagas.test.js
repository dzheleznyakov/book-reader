import { createSagaTestRunner } from './sagasTestUtils';
import { 
    fetchBookMainPageSaga,
    fetchBookReadingHistorySaga,
    saveBookReadingHistorySaga,
    fetchBookTocSaga,
    fetchBooksCountSaga,
} from '../booksSagas';
import { 
    fetchBookMainPageStart, fetchBookMainPageSuccess, fetchBookMainPageFailure,
    fetchBookReadingHistorySuccess,
    fetchBookTocStart, storeBookToc,
    storeBooksCount 
} from '../../actions';

describe("Books sagas", () => {
    const bookId = 'mock-book-id';

    describe("fetchBookMainPageSaga", () => {
        const bookMainDataUrl = '/books/mock-book-id';

        test("should fetch book's main page data", () => {
            const bookMainPageData = 'DATA';
            createSagaTestRunner(fetchBookMainPageSaga, { id: bookId })
                .dispatchAction(fetchBookMainPageStart())
                .callApi('get', [bookMainDataUrl], { data: bookMainPageData })
                .dispatchAction(fetchBookMainPageSuccess(bookMainPageData))
                .test();
        });

        test("should dispatch error if it is thrown during fetching", () => {
            const err = new Error('Something went terribly wrong');
            createSagaTestRunner(fetchBookMainPageSaga, { id: bookId })
                .dispatchAction(fetchBookMainPageStart())
                .callApi('get', [bookMainDataUrl], null, err)
                .dispatchAction(fetchBookMainPageFailure(err))
                .test();
        });
    });

    describe("fetchBooksCountSaga", () => {
        const booksCountUrl = "/books/count";

        test("should fetch books count", () => {
            const booksCount = 42;
            createSagaTestRunner(fetchBooksCountSaga)
                .callApi('get', [booksCountUrl], { data: booksCount })
                .dispatchAction(storeBooksCount(booksCount))
                .test();
        });

        test("should return 0 if the call fails", () => {
            createSagaTestRunner(fetchBooksCountSaga)
                .callApi('get', [booksCountUrl], null, new Error('Something went terribly wrong'))
                .dispatchAction(storeBooksCount(0))
                .test();
        });
    });

    describe("fetchBookReadingHistorySaga", () => {
        const bookHistoryApiUrl = '/books/mock-book-id/lastChapter';

        test("should fetch book last read chapter",  () => {
            const bookHistory = { bookId, lastChapterIndex: 42 };
            createSagaTestRunner(fetchBookReadingHistorySaga, { id: bookId })
                .callApi('get', [bookHistoryApiUrl], { data: bookHistory })
                .dispatchAction(fetchBookReadingHistorySuccess(bookHistory))
                .test();
        });

        test("should return default history if there is an error while fetching", () => {
            createSagaTestRunner(fetchBookReadingHistorySaga, { id: bookId })
                .callApi('get', [bookHistoryApiUrl], null, new Error('Something went terribly wrong'))
                .dispatchAction(fetchBookReadingHistorySuccess({ bookId, lastChapterIndex: -1 }))
                .test();
        });
    });

    describe("saveBookReadingHistorySaga", () => {
        const bookId = 'mock-book-id';
        const chapterId = 'mock-chapter-id';
        const action = { id: bookId, chapterId };
        const saveBookHistoryApiUrl = '/books/mock-book-id/lastChapter';

        test("should save book reading history", () => {
            createSagaTestRunner(saveBookReadingHistorySaga, action)
                .callApi('put', [saveBookHistoryApiUrl, { data: chapterId }], {})
                .test();
        });

        test("should catch an thrown error", () => {
            createSagaTestRunner(saveBookReadingHistorySaga, action)
                .callApi('put', [saveBookHistoryApiUrl, { data: chapterId }], null, new Error('Something went terrible wrong'))
                .test();
        });
    });

    describe("fetchBookTocSaga", () => {
        const action = { id: bookId };
        const tocApiUrl = '/books/mock-book-id/toc';

        test("should fetch book's ToC", () => {
            const tocFromApi = [1, 2, 3];

            createSagaTestRunner(fetchBookTocSaga, action)
                .dispatchAction(fetchBookTocStart())
                .callApi('get', [tocApiUrl], { data: { toc: tocFromApi } })
                .dispatchAction(storeBookToc(tocFromApi))
                .test();
        });

        test("should return empty toc if API call throws", () => {
            createSagaTestRunner(fetchBookTocSaga, action)
                .dispatchAction(fetchBookTocStart())
                .callApi('get', [tocApiUrl], null, new Error('Something went terribly wrong'))
                .dispatchAction(storeBookToc([]))
                .test();
        });
    });
});
