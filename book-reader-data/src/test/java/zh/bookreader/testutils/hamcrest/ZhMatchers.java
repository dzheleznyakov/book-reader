package zh.bookreader.testutils.hamcrest;

import org.hamcrest.Matcher;
import zh.bookreader.model.Book;

import java.util.Optional;

public class ZhMatchers {
    public static Matcher<Book> isEqualTo(Book book) {
        return new BookIsEqual(book);
    }

    public static Matcher<Optional<?>> isEmpty() {
        return new OptionalIsEmpty();
    }
}
