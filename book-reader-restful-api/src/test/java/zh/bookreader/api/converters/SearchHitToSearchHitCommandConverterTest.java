package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.bookreader.api.commands.SearchHitCommand;
import zh.bookreader.model.Book;
import zh.bookreader.model.Chapter;
import zh.bookreader.services.BookService;
import zh.bookreader.services.util.SearchHit;
import zh.bookreader.utils.ClassUtils;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchHitToSearchHitCommandConverterTest {
    private static final String BOOK_ID = "book-id";
    private static final List<String> AUTHORS = ImmutableList.of("Author One", "Author Two");
    private static final List<String> TOPICS = ImmutableList.of("Topic One", "Topic Two");
    private static final Byte[] IMAGE = ClassUtils.cast(new byte[]{0, 1, 2, 3});

    private SearchHitToSearchHitCommandConverter converter;

    @Mock
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUpConverter() {
        converter = new SearchHitToSearchHitCommandConverter(bookService);
    }

    @BeforeEach
    void setUpBook() {
        book = new Book();
        book.setId(BOOK_ID);
        book.setAuthors(AUTHORS);
        book.setTopics(TOPICS);
        book.setImage(IMAGE);

        Chapter ch0 = new Chapter();
        ch0.setId("ch-0");
        Chapter ch1 = new Chapter();
        ch1.setId("ch-1");
        Chapter ch2 = new Chapter();
        ch2.setId("ch-2");
        book.setChapters(ImmutableList.of(ch0, ch1, ch2));
    }

    @Test
    @DisplayName("Test null SearchHit")
    void testConvertingNullSearchHit() {
        SearchHitCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Test SearchHit does not match any book")
    void testConversionWhenNoBookFound() {
        when(bookService.findById(BOOK_ID))
                .thenReturn(Optional.empty());

        SearchHit hit = new SearchHit();
        hit.setBookId(BOOK_ID);
        hit.setChapterNums(ImmutableList.of(0, 2));

        SearchHitCommand command = converter.convert(hit);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Test converting SearchHit")
    void testConvertingSearchHit() {
        when(bookService.findById(BOOK_ID))
                .thenReturn(Optional.of(book));

        SearchHit hit = new SearchHit();
        hit.setBookId(BOOK_ID);
        hit.setChapterNums(ImmutableList.of(0, 2));

        SearchHitCommand command = converter.convert(hit);

        assertThat(command, is(notNullValue()));
        assertThat(command.getBookId(), is(equalTo(BOOK_ID)));
        assertThat(command.getAuthors(), is(equalTo(AUTHORS)));
        assertThat(command.getTopics(), is(equalTo(TOPICS)));
        assertThat(command.getImage(), is(equalTo(IMAGE)));

        List<String> chapterIds = command.getChapterIds()
                .stream()
                .map(e -> e[0])
                .collect(ImmutableList.toImmutableList());
        assertThat(chapterIds, is(equalTo(ImmutableList.of("ch-0", "ch-2"))));

        verify(bookService, times(1)).findById(BOOK_ID);
    }

    @Test
    @DisplayName("Matches in the book description are filtered out")
    void testMatchInBookDescription() {
        when(bookService.findById(BOOK_ID))
                .thenReturn(Optional.of(book));

        SearchHit hit = new SearchHit();
        hit.setBookId(BOOK_ID);
        hit.setChapterNums(ImmutableList.of(-1));

        SearchHitCommand command = converter.convert(hit);

        assertThat(command, is(notNullValue()));

        List<String[]> chapterIds = command.getChapterIds();
        assertThat(chapterIds, is(equalTo(ImmutableList.of())));

        verify(bookService, times(1)).findById(BOOK_ID);
    }
}