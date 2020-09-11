package zh.bookreader.services.htmlservices;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zh.bookreader.model.Book;
import zh.bookreader.services.BookService;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

import static java.util.stream.Collectors.joining;

@Slf4j
@Component
public class HtmlBookService implements BookService {
    @Value("${zh.bookreader.library.path}")
    private String libraryPath;

    private URI libraryUri;

    public HtmlBookService() {
    }

    @VisibleForTesting
    HtmlBookService(String libraryPath) {
        this.libraryPath = libraryPath;
    }

    @Nonnull
    @Override
    public List<Book> findAll() {
        File libraryDir = new File(getLibraryUri());
        File[] books = libraryDir.listFiles(File::isDirectory);
        return books == null ? ImmutableList.of() : Arrays.stream(books)
                .map(this::getBook)
                .collect(ImmutableList.toImmutableList());
    }

    @Nonnull
    @Override
    public Optional<Book> findById(@Nonnull String id) {
        File libraryDir = new File(getLibraryUri());
        File[] files = libraryDir.listFiles((dir, name) -> Objects.equals(name, id));
        return files == null || files.length == 0
                ? Optional.empty()
                : Optional.of(getBook(files[0]));
    }

    @Nonnull
    @Override
    public List<Book> findByTitle(@Nonnull String title) {
        return findBooksByQuery(title, this::doesQueryMatchTitle);
    }

    @Nonnull
    @Override
    public List<Book> findByAuthor(String author) {
        return findBooksByQuery(author, this::doesQueryMatchAuthors);
    }

    @Nonnull
    @Override
    public List<Book> findByTopic(String topic) {
        return findBooksByQuery(topic, this::doesQueryMatchTopics);
    }

    @Override
    public int count() {
        return findAll().size();
    }

    @SneakyThrows
    private URI getLibraryUri() {
        if (libraryUri == null)
            libraryUri = Paths.get(libraryPath).toUri();
        return libraryUri;
    }

    private ImmutableList<Book> findBooksByQuery(@NotNull String rawQuery, BiPredicate<String, Book> bookFilter) {
        String query = cleanUpSearchQuery(rawQuery);
        return searchQueryIsTooShort(query)
                ? ImmutableList.of()
                : findAll().stream()
                .filter(book -> bookFilter.test(query, book))
                .collect(ImmutableList.toImmutableList());
    }

    @Nonnull
    private Book getBook(File bookDir) {
        return new BookProxy(bookDir);
    }

    @Nonnull
    private String cleanUpSearchQuery(@Nonnull String title) {
        return title.replaceAll("[^\\w\\d -]", "");
    }

    private boolean searchQueryIsTooShort(String query) {
        return query.replaceAll("\\s", "").length() <= 2;
    }

    private boolean doesQueryMatchTitle(String query, Book book) {
        return book.getTitle().matches(buildSearchRegex(query));
    }

    private boolean doesQueryMatchAuthors(String query, Book book) {
        return book.getAuthors()
                .stream()
                .anyMatch(a -> a.matches(buildSearchRegex(query)));
    }

    private boolean doesQueryMatchTopics(String query, Book book) {
        return book.getTopics()
                .stream()
                .anyMatch(a -> a.matches(buildSearchRegex(query)));
    }

    @Nonnull
    private String buildSearchRegex(String query) {
        return Arrays.stream(query.split(" "))
                .collect(joining(".*", "(?i).*", ".*"));
    }
}
