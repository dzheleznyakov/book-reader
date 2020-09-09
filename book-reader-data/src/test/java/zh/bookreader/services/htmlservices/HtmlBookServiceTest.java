package zh.bookreader.services.htmlservices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.bookreader.model.Book;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static zh.bookreader.services.htmlservices.TestBookConstants.BOOK_ID_1;
import static zh.bookreader.services.htmlservices.TestBookConstants.BOOK_TEST_LIBRARY_PATH;
import static zh.bookreader.services.htmlservices.TestBookConstants.BOOK_TITLE_1;
import static zh.bookreader.services.htmlservices.TestBookConstants.EMPTY_LIBRARY_PATH;
import static zh.bookreader.testutils.hamcrest.ZhMatchers.isEmpty;
import static zh.bookreader.testutils.hamcrest.ZhMatchers.isEqualTo;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test HtmlBookService functionality")
class HtmlBookServiceTest {
    private HtmlBookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUpService() throws URISyntaxException {
        URI pathToLibrary = getPathToLibrary(BOOK_TEST_LIBRARY_PATH);
        bookService = new HtmlBookService(pathToLibrary);
    }

    @BeforeEach
    void setUpCovers() {
        book1 = TestBookConstants.getBook1();
        book2 = TestBookConstants.getBook2();
    }

    @Nonnull
    private URI getPathToLibrary(String path) throws URISyntaxException {
        return this.getClass()
                .getClassLoader()
                .getResource(path)
                .toURI();
    }

    @Nested
    @DisplayName("Test HtmlBookService.findAll()")
    class FindAll {
        @Test
        @DisplayName("Test parsing library: happy path")
        void testFindAll() {
            List<Book> books = bookService.findAll();

            assertThat(books, hasSize(2));
            assertThat(books.get(0), isEqualTo(book1));
            assertThat(books.get(1), isEqualTo(book2));
        }

        @Test
        @DisplayName("Test parsing empty library")
        void testFindAll_NoBooksInLibrary() throws URISyntaxException {
            bookService = new HtmlBookService(getPathToLibrary(EMPTY_LIBRARY_PATH));

            List<Book> books = bookService.findAll();
            assertThat(books, empty());
        }
    }

    @Nested
    @DisplayName("Test HtmlBookService.findById()")
    class FindById {
        @Test
        @DisplayName("Find the book by id: existing book")
        void findExistingBookById() {
            Optional<Book> bookOptional = bookService.findById(BOOK_ID_1);

            assertThat(bookOptional.get(), isEqualTo(book1));
        }

        @Test
        @DisplayName("Find the book by id: the book is missing")
        void tryToFindMissingBook() {
            Optional<Book> bookOptional = bookService.findById("mock-book-id");

            assertThat(bookOptional, isEmpty());
        }
    }

    @Nested
    @DisplayName("Test HtmlBookService.findByTitle()")
    class FindByTitle {
        @Test
        @DisplayName("Test search by title with no match")
        void testNoMatch() {
            List<Book> books = bookService.findByTitle("Mock Name");

            assertThat(books, empty());
        }

        @ParameterizedTest(name = "Search by \"{0}\" should return empty list")
        @DisplayName("Test search by too short title")
        @ValueSource(strings = {"", "B", "Bo"})
        void testEmptyTitle(String query) {
            List<Book> books = bookService.findByTitle(query);

            assertThat(books, empty());
        }

        @Test
        @DisplayName("Find the book by the full title")
        void testFullTitle() {
            List<Book> books = bookService.findByTitle(BOOK_TITLE_1);

            assertThat(books, hasSize(1));
        }

        @Test
        @DisplayName("Find the books by title matching several ones")
        void testPartialMatch_ManyBooks() {
            List<Book> books = bookService.findByTitle("Book");

            assertThat(books, hasSize(2));
            assertThat(books.get(0), isEqualTo(book1));
            assertThat(books.get(1), isEqualTo(book2));
        }

        @Test
        @DisplayName("Find the book by the query matching one book")
        void testPartialMatch_OneBook() {
            List<Book> books = bookService.findByTitle("One");

            assertThat(books, hasSize(1));
            assertThat(books.get(0), isEqualTo(book1));
        }

        @Test
        @DisplayName("Find the book by the query with several tokens")
        void testPartialMatch_SeveralTokens() {
            List<Book> books = bookService.findByTitle("Boo One");

            assertThat(books, hasSize(1));
        }

        @ParameterizedTest(name = "Search by \"{0}\"")
        @DisplayName("Search should ignore case")
        @CsvSource({"book title one, 1", "book, 2", "one, 1", "boo one, 1"})
        void testMatchingIgnoreCase(String title, int matchNumber) {
            List<Book> books = bookService.findByTitle(title);

            assertThat(books, hasSize(matchNumber));
            assertThat(books.get(0), isEqualTo(book1));
            if (matchNumber > 1)
                assertThat(books.get(1), isEqualTo(book2));
        }

        @Test
        @DisplayName("Special characters in the query should be ignored")
        void testIgnoreNonAlphaNumericCharacters() {
            List<Book> books = bookService.findByTitle("Book# One!");

            assertThat(books, hasSize(1));
            assertThat(books.get(0), isEqualTo(book1));
        }
    }

    @Nested
    @DisplayName("Test HtmlBookService.findByAuthor()")
    class FindByAuthor {
        @Test
        @DisplayName("Test search by name with no match")
        void testFindByName_NameDoesNotExist() {
            List<Book> books = bookService.findByAuthor("Fake Name");

            assertThat(books, is(empty()));
        }

        @Test
        @DisplayName("Test searching book by the author's full name")
        void testFindByFullName() {
            List<Book> books = bookService.findByAuthor("Author One-One");

            assertThat(books.get(0), isEqualTo(book1));
        }

        @Test
        @DisplayName("Test search by full author name")
        void testFindByPartialName() {
            List<Book> books = bookService.findByAuthor("Author One");

            assertThat(books.get(0), isEqualTo(book1));
        }

        @Test
        void testFindMultipleBooks() {
            List<Book> books = bookService.findByAuthor("Author Both");

            assertThat(books.get(0), isEqualTo(book1));
            assertThat(books.get(1), isEqualTo(book2));
        }

        @ParameterizedTest(name = "Search by \"{0}\" should return empty list")
        @DisplayName("Test search by too short name")
        @ValueSource(strings = {"", "A", "Au"})
        void testSearchByTooShortName(String name) {
            List<Book> books = bookService.findByAuthor(name);

            assertThat(books, is(empty()));
        }

        @Test
        @DisplayName("Author search should ignore case")
        void testAuthorSearchIgnoresCase() {
            List<Book> books = bookService.findByAuthor("author one");

            assertThat(books.get(0), isEqualTo(book1));
        }

        @Test
        @DisplayName("Author search should ignore special characters")
        void testAuthorSearchIgnoresSpecialCharacters() {
            List<Book> books = bookService.findByAuthor("author & both*");

            assertThat(books, hasSize(2));
            assertThat(books.get(0), isEqualTo(book1));
            assertThat(books.get(1), isEqualTo(book2));
        }

        @Test
        void testAuthorSearch_QueryWithSeveralTokesn() {
            List<Book> books = bookService.findByAuthor("Auth One-One");

            assertThat(books, hasSize(1));
            assertThat(books.get(0), isEqualTo(book1));
        }
    }

    @Nested
    @DisplayName("Test HtmlBookService.findByTopic()")
    class FindByTopic {
        @Test
        @DisplayName("Test topic search with no match")
        void testFindByNonExistentTopic() {
            List<Book> books = bookService.findByTopic("Blah");

            assertThat(books, is(empty()));
        }

        @Test
        @DisplayName("Test search by full topic name")
        void testFindByFullTopicName() {
            List<Book> books = bookService.findByTopic("Topic Two");

            assertThat(books, hasSize(1));
            assertThat(books.get(0), isEqualTo(book1));
        }

        @ParameterizedTest(name = "Search by \"{0}\" should return empty list")
        @DisplayName("Test search by too short topic")
        @ValueSource(strings = {"", "T", "To"})
        void testTooShortQuery(String query) {
            List<Book> books = bookService.findByTopic(query);

            assertThat(books, is(empty()));
        }

        @Test
        void testSpecialCharactersAreIgnored() {
            List<Book> books = bookService.findByTopic("Topic*% Two$£");

            assertThat(books, hasSize(1));
            assertThat(books.get(0), isEqualTo(book1));
        }

        @Test
        @DisplayName("Find the book by topic matching several ones")
        void testTopicMatchesSeveralBooks() {
            List<Book> books = bookService.findByTopic("Topic One");

            assertThat(books, hasSize(2));
            assertThat(books.get(0), isEqualTo(book1));
            assertThat(books.get(1), isEqualTo(book2));
        }

        @Test
        @DisplayName("Find the book with two partially matched topics")
        void testWithSeveralPartialTokens() {
            List<Book> books = bookService.findByTopic("Top Thr");

            assertThat(books, hasSize(1));
            assertThat(books.get(0), isEqualTo(book2));
        }

        @Test
        @DisplayName("Find the book by the query matching exactly one")
        void testQueryMatchingOneBook() {
            List<Book> books = bookService.findByTopic("Two");

            assertThat(books, hasSize(1));
            assertThat(books.get(0), isEqualTo(book1));
        }

        @Test
        @DisplayName("Test that the search is case ignorant")
        void testSearchIsCaseIgnorant() {
            List<Book> books = bookService.findByTopic("TOPiC twO");

            assertThat(books, hasSize(1));
            assertThat(books.get(0), isEqualTo(book1));
        }
    }
}