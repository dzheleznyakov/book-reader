package zh.bookreader.api.controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zh.bookreader.api.converters.ChapterListToChapterNavigationConverter;
import zh.bookreader.api.converters.ChapterToChapterCommandConverter;
import zh.bookreader.api.converters.EnclosingDocumentToEnclosingDocumentCommandConverter;
import zh.bookreader.api.converters.TextDocumentToTextDocumentCommandConverter;
import zh.bookreader.model.Book;
import zh.bookreader.model.Chapter;
import zh.bookreader.model.Document;
import zh.bookreader.model.DocumentFormatting;
import zh.bookreader.model.DocumentType;
import zh.bookreader.model.EnclosingDocument;
import zh.bookreader.model.TextDocument;
import zh.bookreader.services.BookService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ChapterControllerTest {
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String CHARACTER_ENCODING = StandardCharsets.UTF_8.toString();
    private static final String BOOK_ID = "book-id";
    private static final String CHAPTER_ID = "chapter-id";
    private static final DocumentType DOCUMENT_TYPE = DocumentType.SECTION;
    private static final String DOCUMENT_ID = "doc-id";
    private static final String DOCUMENT_CONTENT = "Chapter content";
    private static final DocumentFormatting DOCUMENT_FORMATTING = DocumentFormatting.EMPH;

    @Mock
    private BookService bookService;

    private ChapterController chapterController;

    private MockMvc mockMvc;

    private Chapter chapter;

    @BeforeEach
    void setUpController() {
        TextDocumentToTextDocumentCommandConverter textDocConverter = new TextDocumentToTextDocumentCommandConverter();
        EnclosingDocumentToEnclosingDocumentCommandConverter enclosingDocConverter = new EnclosingDocumentToEnclosingDocumentCommandConverter(textDocConverter);
        ChapterToChapterCommandConverter chapterConverter = new ChapterToChapterCommandConverter(enclosingDocConverter);
        ChapterListToChapterNavigationConverter navigationConverter = new ChapterListToChapterNavigationConverter();
        chapterController = new ChapterController(chapterConverter, bookService, navigationConverter);
    }

    @BeforeEach
    void setUpMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(chapterController)
                .build();
    }

    @BeforeEach
    void setUpDomainObjects() {
        Document<String> docContent = TextDocument.builder(DocumentType.TEXT)
                .withContent(DOCUMENT_CONTENT)
                .build();
        Document<List<Document<?>>> doc = EnclosingDocument.builder(DOCUMENT_TYPE)
                .withId(DOCUMENT_ID)
                .withContent(docContent)
                .withFormatting(ImmutableSet.of(DOCUMENT_FORMATTING))
                .withMetadata(ImmutableMap.of("&tag", "section"))
                .build();
        chapter = Chapter.builder()
                .id(CHAPTER_ID)
                .content(doc)
                .build();
    }

    @Nested
    @DisplayName("Test getting chapter content: GET /api/books/{id}/chapters/{chaptersId}")
    class TestGetChapter {
        private static final String URL_TEMPLATE = "/api/books/{id}/chapters/{chaptersId}";

        @Test
        @DisplayName("Happy path")
        void happyPath() throws Exception {
            Book book = new Book();
            book.setId(BOOK_ID);
            book.setChapters(ImmutableList.of(chapter));

            when(bookService.findById(BOOK_ID))
                    .thenReturn(Optional.of(book));

            mockMvc.perform(get(URL_TEMPLATE, BOOK_ID, CHAPTER_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(CONTENT_TYPE))
                    .andExpect(content().encoding(CHARACTER_ENCODING))
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.content.id", is(equalTo(DOCUMENT_ID))))
                    .andExpect(jsonPath("$.content.documentType", is(equalTo(DOCUMENT_TYPE.name()))))
                    .andExpect(jsonPath("$.content.formatting", hasSize(1)))
                    .andExpect(jsonPath("$.content.formatting[0]", is(equalTo(DOCUMENT_FORMATTING.name()))))
                    .andExpect(jsonPath("$.content.content[0].content", is(equalTo(DOCUMENT_CONTENT))));

            verify(bookService, times(1)).findById(BOOK_ID);
        }

        @Test
        @DisplayName("No book found")
        void noBookFound() throws Exception {
            when(bookService.findById(BOOK_ID))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get(URL_TEMPLATE, BOOK_ID, CHAPTER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").doesNotExist());

            verify(bookService, times(1)).findById(BOOK_ID);
        }

        @Test
        @DisplayName("No chapter found")
        void noChapterFound() throws Exception {
            Book book = new Book();
            book.setId(BOOK_ID);
            book.setChapters(ImmutableList.of());

            when(bookService.findById(BOOK_ID))
                    .thenReturn(Optional.of(book));

            mockMvc.perform(get(URL_TEMPLATE, BOOK_ID, CHAPTER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").doesNotExist());

            verify(bookService, times(1)).findById(BOOK_ID);
        }
    }

    @Nested
    @DisplayName("Test getting chapter navigation info: GET /api/books/{id}/chapters/{chapterId}/navigation")
    class TestGetChapterNavigation {
        private static final String URL_TEMPLATE = "/api/books/{id}/chapters/{chapterId}/navigation";
        private static final String BOOK_ID = "book-id";
        private static final String CHAPTER_ID = "ch02";

        private final List<String> CHAPTER_IDS = ImmutableList.of("ch01", "ch02", "ch03");
        private Book book;

        @BeforeEach
        void setUpBook() {
            ImmutableList<Chapter> chapters = CHAPTER_IDS.stream()
                    .map(id -> {
                        Chapter chapter = new Chapter();
                        chapter.setId(id);
                        return chapter;
                    })
                    .collect(ImmutableList.toImmutableList());

            book = new Book();
            book.setId(BOOK_ID);
            book.setChapters(chapters);
        }

        @Test
        @DisplayName("Happy path")
        void happyPath() throws Exception {
            when(bookService.findById(BOOK_ID))
                    .thenReturn(Optional.of(book));

            mockMvc.perform(get(URL_TEMPLATE, BOOK_ID, CHAPTER_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(CONTENT_TYPE))
                    .andExpect(content().encoding(CHARACTER_ENCODING))
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.prev", is(equalTo("ch01"))))
                    .andExpect(jsonPath("$.next", is(equalTo("ch03"))));

            verify(bookService, times(1)).findById(BOOK_ID);
        }

        @Test
        @DisplayName("No book found")
        void noBookFound() throws Exception {
            when(bookService.findById(BOOK_ID))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get(URL_TEMPLATE, BOOK_ID, CHAPTER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").doesNotExist());
        }
    }
}