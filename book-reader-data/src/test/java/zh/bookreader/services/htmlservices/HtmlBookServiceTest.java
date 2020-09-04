package zh.bookreader.services.htmlservices;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import zh.bookreader.model.Chapter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static zh.bookreader.testutils.TestUtils.box;
import static zh.bookreader.testutils.hamcrest.ZhMatchers.isEmpty;
import static zh.bookreader.testutils.hamcrest.ZhMatchers.isEqualTo;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test HtmlBookService functionality")
class HtmlBookServiceTest {
    private static final String BOOK_TEST_LIBRARY_PATH = "library";
    private static final String EMPTY_LIBRARY_PATH = "emptyLibrary";
    private static final String BOOK_TITLE_1 = "Book Title One";
    private static final String BOOK_TITLE_2 = "Book Title Two";
    private static final List<String> AUTHORS_1 = ImmutableList.of("Author 1.1", "Author 1.2");
    private static final List<String> AUTHORS_2 = ImmutableList.of("Author 2.1", "Author 2.2");
    private static final String BOOK_ID_1 = "book-one";
    private static final String BOOK_ID_2 = "book-two";
    private static final String RELEASE_DATE_1 = "January 1970";
    private static final String RELEASE_DATE_2 = "February 1970";
    private static final List<String> TOPICS_1 = ImmutableList.of("Test 1", "Test 2");
    private static final List<String> TOPICS_2 = ImmutableList.of("Test 3");
    private static final Byte[] IMAGE_1;
    private static final Byte[] IMAGE_2 = new Byte[0];
    private static final Map<String, String> RESOURCES_1 = ImmutableMap.of(
            "Mock resource 1", "42",
            "Mock resource 2", "forty two");
    private static final Map<String, String> RESOURCES_2 = ImmutableMap.of();
    private static final List<String> CHAPTERS_NAMES_1 = ImmutableList.of(
            "preface.html", "ch01.html", "ch02.html", "app.html");
    private static final List<String> CHAPTERS_NAMES_2 = ImmutableList.of(
            "intro.html", "p01.html", "p02.html");

    static {
        String image1Path = "library/book-one/media/square.jpg";
        var image1Url = HtmlBookService.class.getClassLoader().getResource(image1Path);
        if (image1Url == null)
            throw new IllegalStateException("Cannot find file " + image1Path);
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(image1Url.toURI()));
            IMAGE_1 = box(imageBytes);
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Exception when reading file " + image1Path);
        }
    }

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
        Function<String, Chapter> getChapter = name -> {
            Chapter ch = new Chapter();
            ch.setName(name);
            return ch;
        };
        var chapters1 = CHAPTERS_NAMES_1.stream()
                .map(getChapter)
                .collect(ImmutableList.toImmutableList());
        var chapters2 = CHAPTERS_NAMES_2.stream()
                .map(getChapter)
                .collect(ImmutableList.toImmutableList());

        book1 = new Book();
        book1.setTitle(BOOK_TITLE_1);
        book1.setAuthors(AUTHORS_1);
        book1.setId(BOOK_ID_1);
        book1.setReleaseDate(RELEASE_DATE_1);
        book1.setTopics(TOPICS_1);
        book1.setResources(RESOURCES_1);
        book1.setImage(IMAGE_1);
        book1.setChapters(chapters1);

        book2 = new Book();
        book2.setTitle(BOOK_TITLE_2);
        book2.setAuthors(AUTHORS_2);
        book2.setId(BOOK_ID_2);
        book2.setReleaseDate(RELEASE_DATE_2);
        book2.setTopics(TOPICS_2);
        book2.setResources(RESOURCES_2);
        book2.setImage(IMAGE_2);
        book2.setChapters(chapters2);
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
    class HtmlBookServiceTest_FindAll {
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
    class HtmlBookServiceTest_FindById {
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
    class HtmlBookServiceTest_FindByTitle {
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
        void testIgnoreNonAlphaNumericCharacters() {
            List<Book> books = bookService.findByTitle("Book# One!");

            assertThat(books, hasSize(1));
            assertThat(books.get(0), isEqualTo(book1));
        }
    }
}