package zh.bookreader.services.htmlservices;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static zh.bookreader.services.htmlservices.TestBookConstants.BOOK_TEST_LIBRARY_PATH;
import static zh.bookreader.testutils.hamcrest.ZhMatchers.exists;

@DisplayName("Test HtmlChapterService")
class HtmlChapterServiceTest {
    private static final String LIBRARY_PATH = BOOK_TEST_LIBRARY_PATH;
    private static final String BOOK_ID = "book-one";
    private static final String CHAPTER_ID = "ch01";
    private static final String CHAPTER_TITLE = "Chapter Title";
    private static final String CH_TITLES_INDEX_PATH = LIBRARY_PATH + "/book-one/_ch_titles.zhi";

    @Mock
    private ChapterTitleIndexerService chapterTitleIndexerService;

    private HtmlChapterService service;

    @BeforeEach
    void setUpService() {
        MockitoAnnotations.initMocks(this);

        HtmlBookService bookService = new HtmlBookService("");
        bookService.setLibraryPath(Paths.get(BOOK_TEST_LIBRARY_PATH).toString());
        service = new HtmlChapterService(bookService, "", chapterTitleIndexerService);
        service.setLibraryPath(LIBRARY_PATH);
    }

    @AfterEach
    void removeIndexFile() {
        File indexFile = new File(CH_TITLES_INDEX_PATH);
        if (indexFile.exists())
            indexFile.delete();
    }

    @Nested
    @DisplayName("Test getTitle(bookId, chId)")
    class TestGetTitle {

        private static final String INDEX_FILE_CONTENT = "#chapter_titles" +
                "\npreface=>" +
                "\nch01=>Chapter Title" +
                "\nch02=>" +
                "\napp=>";

        @Test
        @DisplayName("Test getTitle(bookId, chapterId) when the titles are not indexed")
        void noIndexFile() {
            File indexFile = getIndexFile();

            assertThat(indexFile, not(exists()));

            String title = service.getTitle(BOOK_ID, CHAPTER_ID);

            assertThat(title, is(CHAPTER_TITLE));
            verify(chapterTitleIndexerService, times(1))
                    .index(any(File.class), anyString(), anyString());
        }

        @Test
        @DisplayName("Test getTitle(bookId, chapterId) when the book folder does not exist")
        void notExistingBook() {
            String title = service.getTitle("some-book", CHAPTER_ID);

            assertThat(title, is(""));
        }

        @Test
        @DisplayName("Test getTitle(bookId, chapterId) when the chapter does not exist")
        void notExistingChapter() {
            String title = service.getTitle(BOOK_ID, "some-chapter");

            assertThat(title, is(""));
        }

        @Test
        @DisplayName("Test getTitle(bookId, chapterId) when the titles are indexed")
        void testIndexedFileExists() throws FileNotFoundException {
            File indexFile = getIndexFile();
            writeToIndexFile(INDEX_FILE_CONTENT);

            assertThat(indexFile, exists());

            String title = service.getTitle(BOOK_ID, CHAPTER_ID);

            assertThat(title, is(CHAPTER_TITLE));

            verify(chapterTitleIndexerService, times(0))
                    .index(any(File.class), anyString(), anyString());
        }

        @Test
        @DisplayName("Test getTitle(bookId, chapterId) when the titles indexed, but chapterId is wrong")
        void testIndexedFileExistButChapterIdIsWrong() throws FileNotFoundException {
            writeToIndexFile(INDEX_FILE_CONTENT);

            assertThat(getIndexFile(), exists());

            String title = service.getTitle(BOOK_ID, "fake-chapter-id");

            assertThat(title, is(nullValue()));
        }
    }

    @Nonnull
    private File getIndexFile() {
        return Paths.get(CH_TITLES_INDEX_PATH).toFile();
    }

    private void writeToIndexFile(String text) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(getIndexFile())) {
            out.print(text);
        }
    }
}