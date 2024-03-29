package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.ApiTestUtils;
import zh.bookreader.api.commands.BookMainCommand;
import zh.bookreader.model.documents.Book;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static zh.bookreader.api.testtuils.hamcrest.DocumentMatchers.stemsFrom;

class BookToBookMainCommandConverterTest {
    private static final Book BOOK = ApiTestUtils.getBook();

    private BookToBookMainCommandConverter converter;

    @BeforeEach
    void setUpConverter() {
        TextDocumentToTextDocumentCommandConverter textConverter = new TextDocumentToTextDocumentCommandConverter();
        ImageDocumentToImageDocumentCommandConverter imageDocConverter = new ImageDocumentToImageDocumentCommandConverter();
        EnclosingDocumentToEnclosingDocumentCommandConverter enclosingConverter = new EnclosingDocumentToEnclosingDocumentCommandConverter(
                ImmutableSet.of(
                        textConverter,
                        imageDocConverter,
                        new BreakRuleDocumentToBreakRuleDocumentCommandConverter(),
                        new RawDocumentToRawDocumentCommandConverter()));
        converter = new BookToBookMainCommandConverter(textConverter, enclosingConverter);
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
        BookMainCommand command = converter.convert(BOOK);

        assertThat(command, stemsFrom(BOOK));
    }
}