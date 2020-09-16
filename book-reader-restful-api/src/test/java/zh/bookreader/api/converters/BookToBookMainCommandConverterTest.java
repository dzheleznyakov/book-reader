package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.BookMainCommand;
import zh.bookreader.model.Book;
import zh.bookreader.model.Chapter;
import zh.bookreader.utils.ClassUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static zh.bookreader.api.converters.Utils.SEC_1;
import static zh.bookreader.api.converters.Utils.TEXT_DOC_2;
import static zh.bookreader.api.testtuils.hamcrest.DocumentMatchers.stemsFrom;

class BookToBookMainCommandConverterTest {
    private BookToBookMainCommandConverter converter;

    private Book book;

    @BeforeEach
    void setUpConverter() {
        TextDocumentToTextDocumentCommandConverter textConverter = new TextDocumentToTextDocumentCommandConverter();
        EnclosingDocumentToEnclosingDocumentCommandConverter enclosingConverter = new EnclosingDocumentToEnclosingDocumentCommandConverter(textConverter);
        converter = new BookToBookMainCommandConverter(textConverter, enclosingConverter);
    }

    @BeforeEach
    void setUpBook() {
        Chapter chapter1 = new Chapter();
        chapter1.setName("Mock Chapter Name 1");

        Chapter chapter2 = new Chapter();
        chapter2.setName("Mock Chapter Name 2");

        book = new Book();
        book.setId("mock-book-id");
        book.setTitle("Mock Book Title");
        book.setReleaseDate("January 1970");
        book.setAuthors(ImmutableList.of("Author One", "Author Two"));
        book.setTopics(ImmutableList.of("Topic One", "Topic Two"));
        book.setDescription(ImmutableList.of(TEXT_DOC_2, SEC_1));
        book.setResources(ImmutableMap.of(
                "Resource 1", "Value 1",
                "Resource 2", "Value 2"));
        book.setImage(ClassUtils.cast(new byte[]{0, 1, 2, 3}));
        book.setChapters(ImmutableList.of(chapter1, chapter2));
    }

    @Test
    @DisplayName("Test converting null Book")
    void testNullBookConversion() {
        BookMainCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Test converting a Book")
    void testBookConversion() {
        BookMainCommand command = converter.convert(book);

        assertThat(command, stemsFrom(book));
    }
}