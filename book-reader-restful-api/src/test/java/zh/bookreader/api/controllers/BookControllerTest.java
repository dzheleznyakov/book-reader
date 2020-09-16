package zh.bookreader.api.controllers;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zh.bookreader.api.ApiTestUtils;
import zh.bookreader.api.converters.BookToBookMainCommandConverter;
import zh.bookreader.api.converters.BookToBookOverviewCommandConverter;
import zh.bookreader.api.converters.EnclosingDocumentToEnclosingDocumentCommandConverter;
import zh.bookreader.api.converters.TextDocumentToTextDocumentCommandConverter;
import zh.bookreader.model.Book;
import zh.bookreader.services.BookService;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String CHARACTER_ENCODING = StandardCharsets.UTF_8.toString();
    private static final int LIBRARY_SIZE = 25;

    @Mock
    private BookService bookService;

    private BookController bookController;

    private List<Book> books;

    private MockMvc mockMvc;

    @BeforeEach
    void setUpBooks() {
        books = IntStream.range(0, LIBRARY_SIZE)
                .mapToObj(i -> "book-id-" + i)
                .map(id -> {
                    Book b = new Book();
                    b.setId(id);
                    return b;
                })
                .collect(ImmutableList.toImmutableList());
    }

    @BeforeEach
    void setUpMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookController)
                .build();
    }

    @BeforeEach
    void setUpController() {
        TextDocumentToTextDocumentCommandConverter textDocConverter = new TextDocumentToTextDocumentCommandConverter();
        EnclosingDocumentToEnclosingDocumentCommandConverter enclosingDocConverter = new EnclosingDocumentToEnclosingDocumentCommandConverter(textDocConverter);
        BookToBookMainCommandConverter bookMainConverter = new BookToBookMainCommandConverter(textDocConverter, enclosingDocConverter);
        bookController = new BookController(
                bookService,
                new BookToBookOverviewCommandConverter(), bookMainConverter);
    }

    @Nested
    @DisplayName("Test getting books overviews (GET /api/books)")
    class TestBookOverview {
        @BeforeEach
        void setUpMocks() {
            when(bookService.findAll()).thenReturn(books);
        }

        @AfterEach
        void verifyMocks() {
            verify(bookService, times(1)).findAll();
        }

        @Test
        @DisplayName("Test call to GET /api/books?offset=5&limit=5")
        void testAccessToBooks() throws Exception {
            int offset = 5;
            int limit = 5;
            ResultActions result = mockMvc.perform(get("/api/books?offset={offset}&limit={limit}", offset, limit))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(CONTENT_TYPE))
                    .andExpect(content().encoding(CHARACTER_ENCODING))
                    .andExpect(jsonArrayOfSize(limit));
            for (int i = 0; i < limit; ++i)
                result.andExpect(jsonPath(getJsonArrayPointer(i, ".id"), is(books.get(i + offset).getId())));
        }

        @Test
        @DisplayName("If offset is not specified, then it defaults to 0")
        void testDefaultOffset() throws Exception {
            int limit = 5;
            int defaultOffset = 0;
            ResultActions result = mockMvc.perform(get("/api/books?limit={limit}", limit))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(CONTENT_TYPE))
                    .andExpect(content().encoding(CHARACTER_ENCODING))
                    .andExpect(jsonArrayOfSize(limit));
            for (int i = defaultOffset; i < defaultOffset + limit; ++i)
                result.andExpect(jsonPath(getJsonArrayPointer(i, ".id"), is(books.get(i).getId())));
        }

        @Test
        @DisplayName("If limit is not specified, then it defaults to 10")
        void testDefaultLimit() throws Exception {
            int defaultLimit = 10;
            int offset = 5;
            mockMvc.perform(get("/api/books?offset={offset}", offset))
                    .andExpect(status().isOk())
                    .andExpect(jsonArrayOfSize(defaultLimit));
        }

        @Test
        @DisplayName("If the offset value is larger than the books size, then an empty list is returned")
        void testOffsetIsLargerThanBooksNumber() throws Exception {
            int offset = LIBRARY_SIZE + 1;
            mockMvc.perform(get("/api/books?offset={offset}", offset))
                    .andExpect(status().isOk())
                    .andExpect(emptyJsonArray());
        }

        @Test
        void testOffsetPlusLimitExceedBooksNumber() throws Exception {
            int offset = 20;
            int limit = 10;
            int expectedSize = offset + limit - LIBRARY_SIZE;
            mockMvc.perform(get("/api/books?offset={offset}&limit={limit}", offset, limit))
                    .andExpect(status().isOk())
                    .andExpect(jsonArrayOfSize(expectedSize));
        }

        @Test
        @DisplayName("If the offset is negative, then an empty list should be return")
        void testNegativeOffset() throws Exception {
            int offset = -1;
            mockMvc.perform(get("/api/books?offset={offset}", offset))
                    .andExpect(status().isOk())
                    .andExpect(emptyJsonArray());
        }

        @Test
        @DisplayName("If the limit is negative, then an empty list should be returned")
        void testNegativeLimit() throws Exception {
            int limit = -1;
            mockMvc.perform(get("/api/books?limit={limit}", limit))
                    .andExpect(status().isOk())
                    .andExpect(emptyJsonArray());
        }

        @Nonnull
        private ResultMatcher emptyJsonArray() {
            return jsonArrayOfSize(0);
        }

        @Nonnull
        private ResultMatcher jsonArrayOfSize(int expectedSize) {
            return jsonPath("$", hasSize(expectedSize));
        }

        @Nonnull
        private String getJsonArrayPointer(int i, String prop) {
            return "$[" + i + "]" + prop;
        }
    }

    @Nested
    @DisplayName("Test getting books count (GET /api/books/count)")
    class TestBookCount {
        private static final int BOOK_COUNT = 42;

        @BeforeEach
        void setUpMocks() {
            when(bookService.count()).thenReturn(BOOK_COUNT);
        }

        @AfterEach
        void verifyMocks() {
            verify(bookService, times(1)).count();
        }

        @Test
        @DisplayName("Test count")
        void testCount() throws Exception {
            mockMvc.perform(get("/api/books/count"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(BOOK_COUNT)));
        }
    }

    @Nested
    @DisplayName("Test getting book main page (GET /api/books/{id})")
    class TestBookMainPage {
        private static final String BOOK_ID = "bookId";
        private final Book book = ApiTestUtils.getBook();

        @BeforeEach
        void setUpBook() {
            book.setId(BOOK_ID);
        }

        @AfterEach
        void verifyMocks() {
            verify(bookService, times(1)).findById(BOOK_ID);
        }

        @Test
        @DisplayName("Test fetching the book that does not exist")
        void testGettingNonExistentBook() throws Exception {
            when(bookService.findById(BOOK_ID))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get("/api/books/{bookId}", BOOK_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").doesNotExist());

        }

        @Test
        @DisplayName("Test fetching the book main page")
        void testGettingBookMainPage() throws Exception {
            when(bookService.findById(BOOK_ID))
                    .thenReturn(Optional.of(book));

            mockMvc.perform(get("/api/books/{bookId}", BOOK_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(equalTo(BOOK_ID))));
        }
    }
}