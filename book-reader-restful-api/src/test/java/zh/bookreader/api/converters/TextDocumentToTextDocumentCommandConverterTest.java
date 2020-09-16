package zh.bookreader.api.converters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.TextDocumentCommand;
import zh.bookreader.model.TextDocument;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static zh.bookreader.api.testtuils.hamcrest.DocumentMatchers.stemsFrom;
import static zh.bookreader.model.DocumentType.TEXT;

class TextDocumentToTextDocumentCommandConverterTest {
    private static final String CONTENT = "Some mock text.";

    private TextDocumentToTextDocumentCommandConverter converter = new TextDocumentToTextDocumentCommandConverter();

    private TextDocument doc = TextDocument.builder(TEXT)
            .withContent(CONTENT)
            .build();

    @Test
    @DisplayName("Test converting TextDocument to TextDocumentCommand")
    void testDocConversion() {
        TextDocumentCommand command = converter.convert(doc);

        assertThat(command, stemsFrom(doc));
    }

    @Test
    @DisplayName("Test converting null TextDocument")
    void testNullDocConversion() {
        TextDocumentCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }
}