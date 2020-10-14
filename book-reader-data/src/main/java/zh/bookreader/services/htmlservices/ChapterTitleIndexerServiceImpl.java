package zh.bookreader.services.htmlservices;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import zh.bookreader.services.BookService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Paths;

@Component
@Slf4j
public class ChapterTitleIndexerServiceImpl implements ChapterTitleIndexerService {
    private static final String CHAPTER_TITLES_SECTION_HEADER = "#chapter_titles";

    private final PublishSubject<Payload> indexSubject = PublishSubject.create();
    private final Disposable indexSubscription = indexSubject
            .observeOn(Schedulers.single())
            .subscribe(this::doIndex);

    private final BookService bookService;

    public ChapterTitleIndexerServiceImpl(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void index(File indexFile, String bookId, String libraryPath) {
        indexSubject.onNext(new Payload(indexFile, bookId, libraryPath));
    }

    private void doIndex(Payload payload) {
        if (payload.indexFile.exists())
            return;
        boolean fileCreated = tryToCreateIndexFile(payload);
        if (fileCreated)
            doIndexTitles(payload);
    }

    private boolean tryToCreateIndexFile(Payload payload) {
        try {
            return payload.indexFile.createNewFile();
        } catch (IOException ioe) {
            log.error("Error while creating chapter titles index file for [{}]", payload.bookId, ioe);
            return false;
        }
    }

    private void doIndexTitles(Payload payload) {
        File indexFile = payload.indexFile;
        String bookId = payload.bookId;
        String libraryPath = payload.libraryPath;
        log.info("Indexing chapter titles for book [{}]", bookId);
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

    private static class Payload {
        final File indexFile;
        final String bookId;
        final String libraryPath;

        private Payload(File indexFile, String bookId, String libraryPath) {
            this.indexFile = indexFile;
            this.bookId = bookId;
            this.libraryPath = libraryPath;
        }
    }
}
