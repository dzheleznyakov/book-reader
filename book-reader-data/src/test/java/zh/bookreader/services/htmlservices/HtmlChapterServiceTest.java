package zh.bookreader.services.htmlservices;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static zh.bookreader.services.htmlservices.TestBookConstants.BOOK_TEST_LIBRARY_PATH;
import static zh.bookreader.testutils.hamcrest.ZhMatchers.exists;
import static zh.bookreader.testutils.hamcrest.ZhMatchers.hasContent;

@DisplayName("Test HtmlChapterService")
class HtmlChapterServiceTest {
    private static final String LIBRARY_PATH = BOOK_TEST_LIBRARY_PATH;
    private static final String BOOK_ID = "book-one";
    private static final String CHAPTER_ID = "ch01";
    private static final String CHAPTER_TITLE = "Chapter Title";
    private static final String CH_TITLES_INDEX_PATH = LIBRARY_PATH + "/book-one/_ch_titles.zhi";

    private HtmlChapterService service;

    @BeforeEach
    void setUpService() {
        HtmlBookService bookService = new HtmlBookService("");
        bookService.setLibraryPath(Paths.get(BOOK_TEST_LIBRARY_PATH).toString());
        service = new HtmlChapterService(bookService, "");
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
            assertThat(indexFile, exists());
            assertThat(indexFile, hasContent(INDEX_FILE_CONTENT));
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
        void testIndexedFileExists() throws IOException {
            File indexFile = getIndexFile();
            writeToIndexFile(INDEX_FILE_CONTENT);

            sleepUninterruptibly(1, TimeUnit.MILLISECONDS);
            long before = System.currentTimeMillis();
            sleepUninterruptibly(1, TimeUnit.MILLISECONDS);
            String title = service.getTitle(BOOK_ID, CHAPTER_ID);

            assertThat(title, is(CHAPTER_TITLE));

            BasicFileAttributes attributes = Files.readAttributes(indexFile.toPath(), BasicFileAttributes.class);
            long lastModified = attributes.lastModifiedTime().toMillis();
            assertThat(lastModified, lessThan(before));

            long lastAccessed = attributes.lastAccessTime().toMillis();
            assertThat(lastAccessed, greaterThan(before));
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