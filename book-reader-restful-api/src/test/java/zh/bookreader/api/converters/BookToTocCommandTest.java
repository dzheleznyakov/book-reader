package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import zh.bookreader.api.commands.TocCommand;
import zh.bookreader.model.documents.Book;
import zh.bookreader.model.documents.Chapter;
import zh.bookreader.services.ChapterService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Test BookToTocCommandConverter")
class BookToTocCommandTest {
    private static final String BOOK_ID = "book-id";
    private static final String CHAPTER_1_ID = "ch-1";
    private static final String CHAPTER_2_ID = "ch-2";
    private static final String CHAPTER_1_TITLE = "Chapter One";
    private static final String CHAPTER_2_TITLE = "Chapter Two";

    @Mock
    private ChapterService chapterService;

    private BookToTocCommand converter;

    private Book book;

    @BeforeEach
    void setUpConverter() {
        MockitoAnnotations.initMocks(this);

        converter = new BookToTocCommand(chapterService);
    }

    @BeforeEach
    void setUpBook() {
        Chapter ch1 = new Chapter();
        ch1.setId(CHAPTER_1_ID);

        Chapter ch2 = new Chapter();
        ch2.setId(CHAPTER_2_ID);

        book = new Book();
        book.setId(BOOK_ID);
        book.setChapters(ImmutableList.of(ch1, ch2));
    }

    @Test
    void testConvertingNull() {
        TocCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    void testConvertingBook() {
        given(chapterService.getTitle(BOOK_ID, CHAPTER_1_ID)).willReturn(CHAPTER_1_TITLE);
        given(chapterService.getTitle(BOOK_ID, CHAPTER_2_ID)).willReturn(CHAPTER_2_TITLE);

        TocCommand command = converter.convert(book);

        assertThat(command, is(notNullValue()));
        assertThat(command.getBookId(), is(BOOK_ID));
        assertThat(command.getToc(), hasSize(2));
        assertThat(command.getToc().get(0)[0], is(CHAPTER_1_ID));
        assertThat(command.getToc().get(0)[1], is(CHAPTER_1_TITLE));
        assertThat(command.getToc().get(1)[0], is(CHAPTER_2_ID));
        assertThat(command.getToc().get(1)[1], is(CHAPTER_2_TITLE));
        verify(chapterService, times(1)).getTitle(BOOK_ID, CHAPTER_1_ID);
        verify(chapterService, times(1)).getTitle(BOOK_ID, CHAPTER_2_ID);
    }
}