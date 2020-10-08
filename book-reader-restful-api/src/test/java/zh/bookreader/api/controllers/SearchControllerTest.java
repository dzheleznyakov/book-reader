package zh.bookreader.api.controllers;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zh.bookreader.api.converters.SearchHitToSearchHitCommandConverter;
import zh.bookreader.model.Book;
import zh.bookreader.services.BookService;
import zh.bookreader.services.SearchService;
import zh.bookreader.services.util.SearchHit;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test SearchController")
class SearchControllerTest {
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String CHARACTER_ENCODING = StandardCharsets.UTF_8.toString();

    @Mock
    private SearchService searchService;

    @Mock
    private BookService bookService;

    private SearchController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUpController() {
        SearchHitToSearchHitCommandConverter searchHitConverter = new SearchHitToSearchHitCommandConverter(bookService);
        controller = new SearchController(searchService, searchHitConverter);
    }

    @BeforeEach
    void setUpMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Nested
    @DisplayName("Test search API (GET /api/search)")
    class TestSearch {
        private static final String URL_PATTERN = "/api/search?q={query}&offset={offset}&limit={limit}";
        private static final String ID_0 = "id-0";
        private static final String ID_1 = "id-1";
        private static final String ID_2 = "id-2";
        private static final int DEFAULT_QUERY_LENGTH = 50;

        @Test
        @DisplayName("Test happy path")
        void happyPath() throws Exception {
            SearchHit hit1 = getSearchHit(ID_0);
            SearchHit hit2 = getSearchHit(ID_1);
            SearchHit hit3 = getSearchHit(ID_2);

            when(searchService.find(anyList()))
                    .thenReturn(ImmutableList.of(hit1, hit2, hit3));

            when(bookService.findById(ID_0))
                    .thenReturn(Optional.of(getBook(ID_0)));
            when(bookService.findById(ID_1))
                    .thenReturn(Optional.of(getBook(ID_1)));
            when(bookService.findById(ID_2))
                    .thenReturn(Optional.of(getBook(ID_2)));

            mockMvc.perform(get(URL_PATTERN, "foo bar", 0, 10))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(CONTENT_TYPE))
                    .andExpect(content().encoding(CHARACTER_ENCODING))
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(resultHasSize(3))
                    .andExpect(nThElementHasId(0, ID_0))
                    .andExpect(nThElementHasId(1, ID_1))
                    .andExpect(nThElementHasId(2, ID_2));

            verify(searchService, times(1)).find(anyList());
        }

        @Test
        @DisplayName("Returned result are offset and limited")
        void testOffsetAndLimit() throws Exception {
            int offset = 3;
            int limit = 8;
            stubSearchService(50);
            stubBookService(offset, limit);

            mockMvc.perform(get(URL_PATTERN, "foo bar", offset, limit))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(resultHasSize(limit))
                    .andExpect(nThElementHasId(0, "3"))
                    .andExpect(nThElementHasId(4, String.valueOf(offset + 4)))
                    .andExpect(nThElementHasId(limit - 1, "10"));
        }

        @Test
        @DisplayName("Default offset is 0")
        void testDefaultOffset() throws Exception {
            int limit = 3;
            int expectedOffset = 0;
            stubSearchService(50);
            stubBookService(expectedOffset, limit);

            mockMvc.perform(get("/api/search?q={query}&limit={limit}", "foo bar", limit))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(nThElementHasId(0, String.valueOf(expectedOffset)));
        }

        @Test
        @DisplayName("Default limit is 10")
        void testDefaultLimit() throws Exception {
            int offset = 0;
            int expectedLimit = 10;
            stubSearchService(50);
            stubBookService(offset, expectedLimit);

            mockMvc.perform(get("/api/search?q={query}&offset={offset}", "foo bar", offset))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(resultHasSize(expectedLimit));
        }

        @Test
        @DisplayName("When the number of remaining books is less then limit, return remaining ones")
        void testReturnRemaining() throws Exception {
            int offset = 10;
            int limit = 10;
            int expectedSize = 5;
            stubSearchService(15);
            stubBookService(offset, expectedSize);

            mockMvc.perform(get(URL_PATTERN, "foo bar", offset, limit))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(resultHasSize(expectedSize));

            verify(bookService, times(expectedSize)).findById(any());
        }

        @Test
        @DisplayName("When offset is greater than result size, return empty result")
        void testOffsetGreaterThenResultSize() throws Exception {
            int offset = 20;
            stubSearchService(19);

            mockMvc.perform(get(URL_PATTERN, "foo bar", offset, 10))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(resultIsEmpty());

            verify(bookService, times(0)).findById(any());
        }

        @Test
        @DisplayName("When offset is equal to result size, return empty list")
        void testOffsetEqualToResultSize() throws Exception {
            int offset = 20;
            stubSearchService(20);

            mockMvc.perform(get(URL_PATTERN, "foo bar", offset, 10))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(resultIsEmpty());

            verify(bookService, times(0)).findById(any());
        }

        @Test
        @DisplayName("Max incoming query size is set to 50")
        void testMaxQuerySize() throws Exception {
            String query = IntStream.range(0, 99)
                    .mapToObj(i -> "a")
                    .collect(Collectors.joining());

            mockMvc.perform(get(URL_PATTERN, query, 0, 10));

            ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);
            verify(searchService).find(captor.capture());

            Integer length = captor.getValue().stream()
                    .map(String::length)
                    .reduce(0, Integer::sum);
            assertThat(length, is(DEFAULT_QUERY_LENGTH));
        }

        @Test
        @DisplayName("Result shows total search count")
        void testTotalCount() throws Exception {
            int offset = 0;
            int limit = 10;
            int totalCount = 50;
            stubSearchService(totalCount);
            stubBookService(offset, offset + limit);

            mockMvc.perform(get(URL_PATTERN, "foo bar", offset, limit))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.totalCount", is(totalCount)));
        }

        private void stubSearchService(int n) {
            when(searchService.find(anyList()))
                    .thenReturn(getSearchHitList(n));
        }

        private List<SearchHit> getSearchHitList(int n) {
            return IntStream.range(0, n)
                    .mapToObj(i -> getSearchHit(String.valueOf(i)))
                    .collect(ImmutableList.toImmutableList());
        }

        @Nonnull
        private SearchHit getSearchHit(String bookId) {
            SearchHit hit = new SearchHit();
            hit.setBookId(bookId);
            hit.setChapterNums(ImmutableList.of());
            return hit;
        }

        private void stubBookService(int offset, int limit) {
            IntStream.range(offset, offset + limit)
                    .mapToObj(String::valueOf)
                    .forEach(id -> when(bookService.findById(id))
                            .thenReturn(Optional.of(getBook(id))));
        }

        @Nonnull
        private Book getBook(String bookId) {
            Book book = new Book();
            book.setId(bookId);
            book.setChapters(ImmutableList.of());
            return book;
        }

        @Nonnull
        private ResultMatcher nThElementHasId(int index, String id) {
            return jsonPath("$.results[" + index + "].bookId", is(equalTo(id)));
        }

        @Nonnull
        private ResultMatcher resultHasSize(int i) {
            return jsonPath("$.results", hasSize(i));
        }

        @Nonnull
        private ResultMatcher resultIsEmpty() {
            return resultHasSize(0);
        }
    }
}