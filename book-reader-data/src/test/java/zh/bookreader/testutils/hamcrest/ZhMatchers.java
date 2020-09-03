package zh.bookreader.testutils.hamcrest;

import org.hamcrest.Matcher;
import zh.bookreader.model.Book;

public class ZhMatchers {
    public static Matcher<Book> isEqualTo(Book book) {
        return new BookIsEqual(book);
    }
}
