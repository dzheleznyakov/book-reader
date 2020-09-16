package zh.bookreader.api.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.EnclosingDocumentCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static zh.bookreader.api.converters.Utils.PAR;
import static zh.bookreader.api.converters.Utils.SEC_1;
import static zh.bookreader.api.converters.Utils.SEC_2;
import static zh.bookreader.api.testtuils.hamcrest.DocumentMatchers.stemsFrom;

class EnclosingDocumentToEnclosingDocumentCommandConverterTest {
    private EnclosingDocumentToEnclosingDocumentCommandConverter converter;

    @BeforeEach
    void setUpConverter() {
        TextDocumentToTextDocumentCommandConverter textDocConverter = new TextDocumentToTextDocumentCommandConverter();
        converter = new EnclosingDocumentToEnclosingDocumentCommandConverter(textDocConverter);
    }

    @Test
    @DisplayName("Test converting null EnclosingDocument")
    void testNullDocumentConversion() {
        EnclosingDocumentCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Test converting EnclosingDocument containing one TextDocument")
    void testDocumentConversion_ContentIsTextDocument() {
        EnclosingDocumentCommand command = converter.convert(PAR);

        assertThat(command, stemsFrom(PAR));
    }

    @Test
    @DisplayName("Test converting EnclosingDocument containing one EnclosingDocument")
    void testDocumentConversion_ContentIsEnclosingDocument() {
        EnclosingDocumentCommand command = converter.convert(SEC_1);

        assertThat(command, stemsFrom(SEC_1));
    }

    @Test
    @DisplayName("Test converting EnclosingDocument with mixed content")
    void testDocumentConversion_MixedContent() {
        EnclosingDocumentCommand command = converter.convert(SEC_2);

        assertThat(command, stemsFrom(SEC_2));
    }
}