package zh.bookreader.services.htmlservices;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zh.bookreader.model.history.ReadingHistoryItem;
import zh.bookreader.services.ReadingHistoryService;
import zh.bookreader.utils.CompositeSupplier;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

@Service
public class HtmlReadingHistoryService implements ReadingHistoryService {
    @VisibleForTesting
    static final String HISTORY_FILE_NAME = "_history.zhi";
    static final String LAST_CHAPTER_RUBRIC = "#last-chapter";

    private Path libraryPath;

    public HtmlReadingHistoryService(@Value("${zh.bookreader.library.path}") String libraryPath) {
        String userHome = System.getProperty("user.home");
        if (userHome == null)
            userHome = "";
        this.libraryPath = Paths.get(userHome, libraryPath);
    }

    @Override
    public void saveLastReadChapter(String bookId, int chapterIndex) {
        try {
            String content = LAST_CHAPTER_RUBRIC + "\n" + chapterIndex;
            Files.write(content.getBytes(), getHistoryFile(bookId));
        } catch (IOException e) {
            throw new FailedToWriteToHistoryFile(getHistoryFile(bookId), e);
        }
    }

    @Nonnull
    @Override
    public ReadingHistoryItem getLastReadChapter(String bookId) {
        return CompositeSupplier
                .main(() -> bookId != null && getHistoryFile(bookId).exists(), () -> readHistory(bookId))
                .withFallback(() -> bookId == null, ReadingHistoryItem.NULL)
                .withFallback(() -> !getBookPath(bookId).toFile().exists(), ReadingHistoryItem.NULL)
                .withFallback(() -> !getHistoryFile(bookId).exists(), () -> getDefaultItem(bookId))
                .build()
                .get();
    }

    private ReadingHistoryItem getDefaultItem(String bookId) {
        return ReadingHistoryItem.builder()
                .bookId(bookId)
                .lastChapterIndex(-1)
                .build();
    }

    @Nonnull
    private File getHistoryFile(String bookId) {
        return getBookPath(bookId).resolve(HISTORY_FILE_NAME).toFile();
    }

    @Nonnull
    private Path getBookPath(String bookId) {
        return libraryPath.resolve(bookId);
    }

    private ReadingHistoryItem readHistory(String bookId) {
        File historyFile = getHistoryFile(bookId);
        ReadingHistoryItem readingHistoryItem;
        try (Scanner input = openScanner(historyFile)) {
            readingHistoryItem = readHistory(input, bookId);
        }
        return readingHistoryItem;
    }

    private ReadingHistoryItem readHistory(Scanner input, String bookId) {
        while (input.hasNext())
            if (LAST_CHAPTER_RUBRIC.equals(input.nextLine().trim()))
                return getReadingHistoryItem(input, bookId);
        return ReadingHistoryItem.NULL;
    }

    private ReadingHistoryItem getReadingHistoryItem(Scanner input, String bookId) {
        return input.hasNext()
                ? ReadingHistoryItem.builder()
                        .bookId(bookId)
                        .lastChapterIndex(input.nextInt())
                        .build()
                : ReadingHistoryItem.NULL;
    }

    @Nonnull
    private Scanner openScanner(File historyFile) {
        try {
            return new Scanner(historyFile);
        } catch (FileNotFoundException e) {
            throw new FailedToOpenHistoryFile(historyFile, e);
        }
    }

    @VisibleForTesting
    void setLibraryPath(Path libraryPath) {
        this.libraryPath = libraryPath;
    }

    public static class FailedToOpenHistoryFile extends RuntimeException {
        public FailedToOpenHistoryFile(File file, Throwable cause) {
            super(String.format("Failed to open history file [%s]", file.getAbsolutePath()), cause);
        }
    }

    public static class FailedToWriteToHistoryFile extends RuntimeException {
        public FailedToWriteToHistoryFile(File file, Throwable cause) {
            super(String.format("Failed to write to history file [%s]", file.getAbsolutePath()), cause);
        }
    }
}
