package zh.bookreader.api.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.TextDocumentCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static zh.bookreader.api.converters.Utils.TEXT_DOC_1;
import static zh.bookreader.api.testtuils.hamcrest.DocumentMatchers.stemsFrom;

class TextDocumentToTextDocumentCommandConverterTest {
    private TextDocumentToTextDocumentCommandConverter converter;

    @BeforeEach
    void setUpConverter() {
        converter = new TextDocumentToTextDocumentCommandConverter();
    }

    @Test
    @DisplayName("Test converting TextDocument to TextDocumentCommand")
    void testDocConversion() {
        TextDocumentCommand command = converter.convert(TEXT_DOC_1);

        assertThat(command, stemsFrom(TEXT_DOC_1));
    }

    @Test
    @DisplayName("Test converting null TextDocument")
    void testNullDocConversion() {
        TextDocumentCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }
}