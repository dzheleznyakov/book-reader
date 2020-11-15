import { createSagaTestRunner } from './sagasTestUtils';
import { fetchChapterNavigationSaga } from '../navigationSagas';
import navModes from '../../../components/UI/NavigationBar/navigationModes';
import { setNavigation } from '../../actions';

describe('Navigation sagas', () => {
    describe('fetchChapterNavigationSaga()', () => {
        const bookId = 'mock-book-id';
        const chapterId = 'mock-chapter-id';

        const expectedApiUrl = '/books/mock-book-id/chapters/mock-chapter-id/navigation';

        test("should save prev and next links if they both returned by the API", () => {
            const navApiResponse = { data: {
                prev: '/prev/link',
                next: '/next/link',
            } };
            const navBarPayload = {
                prev: '/prev/link',
                next: '/next/link',
                book: '/books/mock-book-id',
            };

            createSagaTestRunner(fetchChapterNavigationSaga, { bookId, chapterId })
                .callApi('get', [expectedApiUrl], navApiResponse)
                .dispatchAction(setNavigation(navModes.CHAPTER, navBarPayload))
                .test();
        });

        test("should return only prev link if the next link is not returned by API", () => {
            [undefined, null, ''].forEach(next => {

            const navApiResponse = { data: {
                prev: '/prev/link',
                next,
            } };
            const navBarPayload = {
                prev: '/prev/link',
                next: null,
                book: '/books/mock-book-id',
            }

            createSagaTestRunner(fetchChapterNavigationSaga, { bookId, chapterId })
                .callApi('get', [expectedApiUrl], navApiResponse)
                .dispatchAction(setNavigation(navModes.CHAPTER, navBarPayload))
                .test();
            });
        });

        test("should return only next link if the prev link is not returned by API", () => {
            [undefined, null, ''].forEach(prev => {

            const navApiResponse = { data: {
                prev,
                next: '/next/link',
            } };
            const navBarPayload = {
                prev: null,
                next: '/next/link',
                book: '/books/mock-book-id',
            }

            createSagaTestRunner(fetchChapterNavigationSaga, { bookId, chapterId })
                .callApi('get', [expectedApiUrl], navApiResponse)
                .dispatchAction(setNavigation(navModes.CHAPTER, navBarPayload))
                .test();
            });
        });
    
        test("should return neither next nor prev links if the api call fails", () => {
            const navApiResponse = { data: {
                prev: '/prev/link',
                next: '/next/link',
            } };
            const navBarPayload = {
                prev: null,
                next: null,
                book: '/books/mock-book-id',
            };
    
            createSagaTestRunner(fetchChapterNavigationSaga, { bookId, chapterId })
                .callApi('get', [expectedApiUrl], null, new Error("Something went terribly wrong"))
                .dispatchAction(setNavigation(navModes.CHAPTER, navBarPayload))
                .test();
        });
    });
});