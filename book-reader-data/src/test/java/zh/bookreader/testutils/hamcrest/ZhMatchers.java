package zh.bookreader.testutils.hamcrest;

import org.hamcrest.Matcher;
import zh.bookreader.model.documents.Book;

import java.io.File;
import java.util.Optional;

public class ZhMatchers {
    public static Matcher<Book> isEqualTo(Book book) {
        return new BookIsEqual(book);
    }

    public static Matcher<Optional<?>> isEmpty() {
        return new OptionalIsEmpty();
    }

    public static Matcher<File> exists() {
        return new FileExists();
    }

    public static Matcher<File> hasContent(String content) {
        return new FileHasContent(content);
    }
}
