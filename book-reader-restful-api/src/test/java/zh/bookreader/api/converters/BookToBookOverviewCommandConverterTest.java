package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.BookOverviewCommand;
import zh.bookreader.model.Book;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class BookToBookOverviewCommandConverterTest {
    private static final String ID = "book-id";
    private static final String TITLE = "Book Title";
    private static final List<String> AUTHORS = ImmutableList.of("Jon Snow", "Jane Dow");
    private static final List<String> TOPICS = ImmutableList.of("Topic One", "Topic Two");
    private static final Byte[] IMAGE = {0, 1, 2};

    private BookToBookOverviewCommandConverter converter = new BookToBookOverviewCommandConverter();

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(ID);
        book.setTitle(TITLE);
        book.setAuthors(AUTHORS);
        book.setTopics(TOPICS);
        book.setImage(IMAGE);
    }

    @Test
    @DisplayName("Test converting Book into BookOverviewCommand")
    void testBookConversion() {
        BookOverviewCommand command = converter.convert(book);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(equalTo(ID)));
        assertThat(command.getTitle(), is(equalTo(TITLE)));
        assertThat(command.getAuthors(), is(equalTo(AUTHORS)));
        assertThat(command.getTopics(), is(equalTo(TOPICS)));
        assertThat(command.getImage(), is(equalTo(IMAGE)));
    }
}