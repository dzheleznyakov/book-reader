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
import static zh.bookreader.testutils.hamcrest.ZhMatchers.hasContent;

@DisplayName("Test HtmlReadingHistoryService")
class HtmlReadingHistoryServiceTest {
    private static final String BOOK_ID = "book-one";
    private static final int CHAPTER_INDEX = 42;

    private HtmlReadingHistoryService service;
    private File historyFile;

    @BeforeEach
    void setUpService() {
        service = new HtmlReadingHistoryService("");
        service.setLibraryPath(Paths.get(BOOK_TEST_LIBRARY_PATH));
    }

    @BeforeEach
    void setUpHistoryFile() {
        historyFile = Paths
                .get(BOOK_TEST_LIBRARY_PATH, BOOK_ID, HtmlReadingHistoryService.HISTORY_FILE_NAME)
                .toFile();
    }

    @AfterEach
    void deleteHistoryFile() {
        if (historyFile.exists())
            historyFile.delete();
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
            assertThat(historyFile, not(exists()));

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

    @Nested
    @DisplayName("Test saveLastReadChapter(bookId, chapterId)")
    class TestSaveLastReadChapter {
        @Test
        @DisplayName("Save last read chapter when the history file does not exist")
        void saveLastReadChapter_FileDoesNotExist() {
            assertThat(historyFile, not(exists()));

            service.saveLastReadChapter(BOOK_ID, CHAPTER_INDEX);

            assertThat(historyFile, exists());
            assertThat(historyFile, hasContent(LAST_CHAPTER_RUBRIC + "\n" + CHAPTER_INDEX));
        }

        @Test
        @DisplayName("Save last read chapte when the history file exists")
        void saveLastReadChapter_FileExists() throws IOException {
            writeHistory(LAST_CHAPTER_RUBRIC + "\n" + 1);
            assertThat(historyFile, exists());

            service.saveLastReadChapter(BOOK_ID, CHAPTER_INDEX);

            assertThat(historyFile, hasContent(LAST_CHAPTER_RUBRIC + "\n" + CHAPTER_INDEX));
        }
    }

    private void writeHistory(String content) throws IOException {
        Files.write(content.getBytes(), historyFile);
        assertThat(historyFile, exists());
    }
}