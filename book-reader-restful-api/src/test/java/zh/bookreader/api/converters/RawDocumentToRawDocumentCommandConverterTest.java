package zh.bookreader.api.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.RawDocumentCommand;
import zh.bookreader.model.documents.DocumentType;
import zh.bookreader.model.documents.RawDocument;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class RawDocumentToRawDocumentCommandConverterTest {
    private RawDocumentToRawDocumentCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new RawDocumentToRawDocumentCommandConverter();
    }

    @Test
    @DisplayName("Test converting null")
    void convertNull() {
        RawDocumentCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Test converting RawDocument")
    void convertRawCommand() {
        String docContent = "content";
        RawDocument doc = RawDocument.builder()
                .withContent(docContent)
                .build();

        RawDocumentCommand command = converter.convert(doc);

        assertThat(command, is(notNullValue()));
        assertThat(command.getDocumentType(), is(DocumentType.RAW.toString()));
        assertThat(command.getContent(), is(docContent));
    }
}