package zh.bookreader.services.htmlservices;

import com.google.common.io.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import zh.bookreader.model.history.ReadingHistoryItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.InputMismatchException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static zh.bookreader.services.htmlservices.HtmlReadingHistoryService.LAST_CHAPTER_RUBRIC;
import static zh.bookreader.services.htmlservices.TestBookConstants.BOOK_TEST_LIBRARY_PATH;
import static zh.bookreader.testutils.hamcrest.ZhMatchers.exists;

@DisplayName("Test HtmlReadingHistoryService")
class HtmlReadingHistoryServiceTest {
    private static final String BOOK_ID = "book-one";

    private HtmlReadingHistoryService service;
    private File notStartedBookHistoryFile;

    @BeforeEach
    void setUpService() {
        service = new HtmlReadingHistoryService("");
        service.setLibraryPath(Paths.get(BOOK_TEST_LIBRARY_PATH));
    }

    @BeforeEach
    void setUpHistoryFile() {
        notStartedBookHistoryFile = Paths
                .get(BOOK_TEST_LIBRARY_PATH, BOOK_ID, HtmlReadingHistoryService.HISTORY_FILE_NAME)
                .toFile();
    }

    @AfterEach
    void deleteHistoryFile() {
        if (notStartedBookHistoryFile.exists())
            notStartedBookHistoryFile.delete();
    }

    @Nested
    @DisplayName("Test getLastReadChapter(bookId)")
    class TestGetLastReadChapter {
        @Test
        @DisplayName("bookId is null")
        void bookIdIsNull() {
            assertSearchResult(ReadingHistoryItem.NULL, null);
        }

        @Test
        @DisplayName("bookId is not found")
        void bookNotFound() {
            assertSearchResult(ReadingHistoryItem.NULL, "some-book");
        }

        @Test
        @DisplayName("Library is not found")
        void libraryNotFound() {
            service.setLibraryPath(Paths.get("folder-does-not-exist"));

            assertSearchResult(ReadingHistoryItem.NULL, BOOK_ID);
        }

        @Test
        @DisplayName("Book has not been started yet: history file does not exist")
        void bookNotStarted_NoHistoryFile() {
            assertThat(notStartedBookHistoryFile, not(exists()));

            assertSearchResult(-1);
        }

        @Test
        @DisplayName("History file exists, but empty")
        void emptyHistoryFile() throws IOException {
            writeHistory("");

            assertSearchResult(ReadingHistoryItem.NULL, BOOK_ID);
        }

        @Test
        @DisplayName("History file contains the rubric, but no chapter index")
        void brokenHistoryFile_NoChapterIndex() throws IOException {
            writeHistory(LAST_CHAPTER_RUBRIC);

            assertSearchResult(ReadingHistoryItem.NULL, BOOK_ID);
        }

        @Test
        @DisplayName("Throw if history file has chapter index in wrong format")
        void brokenHistoryFile_ChapterIndexIsNotInteger() throws IOException {
            writeHistory(LAST_CHAPTER_RUBRIC + "\nforty-two");

            assertThrows(InputMismatchException.class, () -> service.getLastReadChapter(BOOK_ID));
        }

        @Test
        @DisplayName("Book has been started")
        void bookNotStarted_ThereIsHistoryFile() throws IOException {
            int expectedChapterIndex = 42;
            writeHistory(LAST_CHAPTER_RUBRIC + "\n" + expectedChapterIndex);

            assertSearchResult(42);
        }

        private void assertSearchResult(int expectedChapterIndex) {
            ReadingHistoryItem item = ReadingHistoryItem.builder()
                    .bookId(HtmlReadingHistoryServiceTest.BOOK_ID)
                    .lastChapterIndex(expectedChapterIndex)
                    .build();
            assertSearchResult(item, BOOK_ID);
        }

        private void assertSearchResult(ReadingHistoryItem expected, String bookId) {
            ReadingHistoryItem item = service.getLastReadChapter(bookId);

            assertThat(item, is(equalTo(expected)));
        }
    }

    private void writeHistory(String content) throws IOException {
        Files.write(content.getBytes(), notStartedBookHistoryFile);
        assertThat(notStartedBookHistoryFile, exists());
    }
}