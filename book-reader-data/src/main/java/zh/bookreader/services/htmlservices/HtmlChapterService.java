package zh.bookreader.services.htmlservices;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zh.bookreader.model.documents.Book;
import zh.bookreader.model.documents.Chapter;
import zh.bookreader.services.BookService;
import zh.bookreader.services.ChapterService;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import static com.google.common.base.MoreObjects.firstNonNull;

@Slf4j
@Service
public class HtmlChapterService implements ChapterService {
    private static final String INDEX_FILE_NAME = "_ch_titles.zhi";
    private static final String CHAPTER_TITLES_SECTION_HEADER = "#chapter_titles";
    private static final String INDEX_MAP_DELIM = "=>";

    private final BookService bookService;
    private String libraryPath;
    private final ChapterTitleIndexerService chapterTitleIndexer;

    public HtmlChapterService(
            @Qualifier("htmlBookService") BookService bookService,
            @Value("${zh.bookreader.library.path}") String libraryPath,
            ChapterTitleIndexerService chapterTitleIndexer) {
        this.bookService = bookService;
        this.chapterTitleIndexer = chapterTitleIndexer;
        String userHome = firstNonNull(System.getProperty("user.home"), "");
        this.libraryPath = Paths.get(userHome, libraryPath).toString();
    }

    @Override
    public String getTitle(String bookId, String chId) {
        Optional<Book> bookOptional = bookService.findById(bookId);
        return bookOptional.isPresent()
                ? getTitle(chId, bookOptional.get())
                : "";
    }

    @Nullable
    private String getTitle(String chId, Book book) {
        String bookId = book.getId();

        File indexFile = Paths.get(libraryPath, bookId, INDEX_FILE_NAME).toFile();
        boolean indexFileExists = indexFile.exists();
        if (!indexFileExists)
            chapterTitleIndexer.index(indexFile, bookId, libraryPath);

        return getTitle(chId, book, indexFile, indexFileExists);
    }

    private String getTitle(String chId, Book book, File indexFile, boolean indexFileExist) {
        String bookId = book.getId();
        return indexFileExist
                    ? getTitleFromIndexFile(indexFile, bookId, chId)
                    : getTitleFromChapterContent(chId, book);
    }

    private String getTitleFromIndexFile(File indexFile, String bookId, String chId) {
        try(Scanner in = new Scanner(indexFile)) {
            while (in.hasNext())
                if (Objects.equals(in.nextLine(), CHAPTER_TITLES_SECTION_HEADER))
                    return getTitle(in, chId);
        } catch (IOException e) {
            log.error("Error while retrieving chapter id=[{}] for book=[{}]", chId, bookId, e);
            return "";
        }
        return null;
    }

    @Nonnull
    private String getTitleFromChapterContent(String chId, Book book) {
        return book.getChapters()
                .stream().filter(ch -> Objects.equals(ch.getId(), chId))
                .findAny()
                .map(Chapter::getFirstTitle)
                .orElse("");
    }

    private String getTitle(Scanner in, String chapterId) {
        String title;
        while (in.hasNext())
            if ((title = getTitleFromLine(chapterId, in.nextLine())) != null)
                return title;
        return null;
    }

    @Nullable
    private String getTitleFromLine(@Nonnull String chapterId, String line) {
        int ind = line.indexOf(INDEX_MAP_DELIM);
        String id = ind < 0 ? null : line.substring(0, ind);
        return Objects.equals(id, chapterId)
                ? line.substring(Math.min(ind + INDEX_MAP_DELIM.length(), line.length()))
                : null;
    }

    @VisibleForTesting
    void setLibraryPath(String libraryPath) {
        this.libraryPath = libraryPath;
    }
}
