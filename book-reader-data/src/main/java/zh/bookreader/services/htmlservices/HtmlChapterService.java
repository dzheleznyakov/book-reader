package zh.bookreader.services.htmlservices;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zh.bookreader.model.Book;
import zh.bookreader.model.Chapter;
import zh.bookreader.services.BookService;
import zh.bookreader.services.ChapterService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

@Component
@Slf4j
public class HtmlChapterService implements ChapterService {
    private static final String INDEX_FILE_NAME = "_ch_titles.zhi";
    private static final String METAINFO_FILE_NAME = "metainfo.txt";
    private static final String CHAPTER_TITLES_SECTION_HEADER = "#chapter_titles";
    private static final String TOC_KEY = "Chapter Files";
    private static final String INDEX_MAP_DELIM = "=>";

    private final BookService bookService;
    private String libraryPath;


    public HtmlChapterService(
            @Qualifier("htmlBookService") BookService bookService,
            @Value("${zh.bookreader.library.path}") String libraryPath
    ) {
        this.bookService = bookService;
        this.libraryPath = libraryPath;
    }

    @Override
    public String getTitle(String bookId, String chId) {
        Optional<Book> bookOptional = bookService.findById(bookId);
        if (bookOptional.isEmpty())
            return "";

        File indexFile = Paths.get(libraryPath, bookId, INDEX_FILE_NAME).toFile();
        boolean indexFileExist = indexFile.exists();
        String title = indexFileExist
                ? getTitleFromIndexFile(indexFile, bookId, chId)
                : bookOptional.get().getChapters()
                    .stream().filter(ch -> Objects.equals(ch.getId(), chId))
                    .findAny()
                    .map(Chapter::getFirstTitle)
                    .orElse("");
        if (!indexFileExist)
            indexTitles(indexFile, bookId);


        return title;
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

    private String getTitle(Scanner in, String chapterId) {
        while (in.hasNext()) {
            String line = in.nextLine();
            int ind = line.indexOf(INDEX_MAP_DELIM);
            if (ind >= 0) {
                String id = line.substring(0, ind);
                if (Objects.equals(id, chapterId))
                    return line.substring(Math.min(ind + INDEX_MAP_DELIM.length(), line.length()));

            }
        }
        return null;
    }

    private void indexTitles(File indexFile, String bookId) {
        boolean fileExists = ensureIndexFileExists(indexFile, bookId);
        if (fileExists)
            doIndexTitles(indexFile, bookId);
    }

    private void doIndexTitles(File indexFile, String bookId) {
        try (PrintWriter out = new PrintWriter(indexFile)) {
            out.print(CHAPTER_TITLES_SECTION_HEADER);
            bookService.findById(bookId)
                    .orElseThrow(() -> new FileSystemNotFoundException("Book [" + Paths.get(libraryPath, bookId).toAbsolutePath() + "] not found"))
                    .getChapters()
                    .forEach(ch -> out.print("\n" + ch.getId() + "=>" + ch.getFirstTitle()));
        } catch (FileNotFoundException fnfe) {
            log.error("Error while indexing chapter titles for book [{}]", bookId, fnfe);
        }
    }

    private boolean ensureIndexFileExists(File indexFile, String bookId) {
        return indexFile.exists() || tryToCreateIndexFile(indexFile, bookId);
    }

    private boolean tryToCreateIndexFile(File indexFile, String bookId) {
        try {
            return indexFile.createNewFile();
        } catch (IOException ioe) {
            log.error("Error while creating chapter titles index file for [{}]", bookId, ioe);
            return false;
        }
    }

    @VisibleForTesting
    void setLibraryPath(String libraryPath) {
        this.libraryPath = libraryPath;
    }
}
