package zh.bookreader.services.htmlservices;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import zh.bookreader.model.Book;
import zh.bookreader.services.BookService;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class HtmlBookService implements BookService {
    private final URI pathToLibrary;

    @VisibleForTesting
    HtmlBookService(URI pathToLibrary) {
        this.pathToLibrary = pathToLibrary;
    }

    @Nonnull
    @Override
    public List<Book> findAll() {
        File libraryDir = new File(pathToLibrary);
        File[] books = libraryDir.listFiles(File::isDirectory);
        return books == null ? ImmutableList.of() : Arrays.stream(books)
                .map(this::getBook)
                .collect(ImmutableList.toImmutableList());
    }

    @Nonnull
    private Book getBook(File bookDir) {
        Book book = new BookProxy(bookDir);
        return book;
    }

    @Nonnull
    @Override
    public Optional<Book> findById() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public List<Book> findByTitle(String title) {
        return ImmutableList.of();
    }

    @Nonnull
    @Override
    public List<Book> findByAuthor(String author) {
        return ImmutableList.of();
    }

    @Nonnull
    @Override
    public List<Book> findByTopic(String topic) {
        return ImmutableList.of();
    }
}
