package zh.bookreader.testutils.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import zh.bookreader.model.Book;
import zh.bookreader.testutils.PrintUtils;
import zh.bookreader.testutils.PrintUtils.Color;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

class BookIsEqual extends TypeSafeMatcher<Book> {
    private final Book book;
    private String fieldUnderTest = "";
    private Function<Book, ?> descriptionValue = b -> null;

    BookIsEqual(@Nonnull Book book) {
        this.book = book;
    }

    @Override
    protected boolean matchesSafely(Book item) {
        return item != null
                && equals(item, "id", Book::getId)
                && equals(item, "authors", Book::getAuthors)
                && equals(item, "releaseDate", Book::getReleaseDate)
                && equals(item, "topics", Book::getTopics)
                && equals(item, "description", Book::getDescription)
                && equals(item, "resources", Book::getResources)
                && equals(item, "image", Book::getImage)
                && equals(item, "chapters", Book::getChapters);
    }

    private boolean equals(Book item, String fieldUnderTest, Function<Book, ?> value) {
        this.fieldUnderTest = fieldUnderTest;
        this.descriptionValue = value;
        return equals(value.apply(book), value.apply(item));
    }

    @SuppressWarnings("unchecked")
    private boolean equals(Object expected, Object actual) {
        if (actual == null)
            return actual == expected;
        if (actual.getClass().isArray())
                return Arrays.equals((Object[]) expected, (Object[]) actual);
        if (actual instanceof Collection && expected instanceof Collection) {
            Collection<Object> ac = (Collection<Object>) actual;
            Collection<Object> ex = (Collection<Object>) expected;
            return ex.size() == ac.size() && ac.containsAll(ex);
        }
        return Objects.equals(expected, actual);
    }

    @Override
    public void describeTo(Description description) {
        Object fieldValue = descriptionValue.apply(book);
        PrintUtils.print("Expected Book." + fieldUnderTest + " to be [" + fieldValue + "]", Color.RED);
        description.appendValue(fieldValue);
    }

    @Override
    protected void describeMismatchSafely(Book item, Description mismatchDescription) {
        Object fieldValue = descriptionValue.apply(item);
        PrintUtils.println(", but it is [" + fieldValue + "]", Color.RED);
        mismatchDescription.appendText("was ").appendValue(fieldValue);
    }
}
