package zh.bookreader.services;

import zh.bookreader.model.Book;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface BookService {
    @Nonnull
    List<Book> findAll();

    @Nonnull
    Optional<Book> findById(String id);

    @Nonnull
    List<Book> findByTitle(String title);

    @Nonnull
    List<Book> findByAuthor(String author);

    @Nonnull
    List<Book> findByTopic(String topic);
}
