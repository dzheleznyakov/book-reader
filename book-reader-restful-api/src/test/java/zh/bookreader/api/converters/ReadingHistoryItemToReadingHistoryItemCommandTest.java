package zh.bookreader.api.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.ReadingHistoryItemCommand;
import zh.bookreader.model.history.ReadingHistoryItem;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class ReadingHistoryItemToReadingHistoryItemCommandTest {
    private static final String BOOK_ID = "mock-id";
    private static final int CHAPTER_INDEX = 42;

    private ReadingHistoryItemToReadingHistoryItemCommand converter;

    @BeforeEach
    void setUp() {
        converter = new ReadingHistoryItemToReadingHistoryItemCommand();
    }

    @Test
    @DisplayName("Return null when null is passed")
    void testNullItem() {
        ReadingHistoryItemCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    void testConversion() {
        ReadingHistoryItem item = ReadingHistoryItem.builder()
                .bookId(BOOK_ID)
                .lastChapterIndex(CHAPTER_INDEX)
                .build();

        ReadingHistoryItemCommand command = converter.convert(item);

        assertThat(command, is(notNullValue()));
        assertThat(command.getBookId(), is(equalTo(BOOK_ID)));
        assertThat(command.getLastChapterIndex(), is(equalTo(CHAPTER_INDEX)));
    }
}