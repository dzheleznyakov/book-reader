package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.model.DocumentFormatting;
import zh.bookreader.model.DocumentType;
import zh.bookreader.model.EnclosingDocument;
import zh.bookreader.model.TextDocument;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static zh.bookreader.api.testtuils.hamcrest.DocumentMatchers.stemsFrom;

class EnclosingDocumentToEnclosingDocumentCommandConverterTest {
    private static final String TEXT_1 = "Mock text 1.";
    private static final String TEXT_2 = "Mock text 2.";
    private static final String PAR_1_ID = "mock-par-id";
    private static final String SEC_1_ID = "mock-sec-1-id";
    private static final String SEC_2_ID = "mock-sec-2-id";
    private static final ImmutableSet<DocumentFormatting> FORMATTING = ImmutableSet.of(DocumentFormatting.NOTE, DocumentFormatting.SIMPLE);

    private TextDocumentToTextDocumentCommandConverter textDocConverter = new TextDocumentToTextDocumentCommandConverter();
    private EnclosingDocumentToEnclosingDocumentCommandConverter converter = new EnclosingDocumentToEnclosingDocumentCommandConverter(textDocConverter);

    private TextDocument textDoc1 = TextDocument.builder(DocumentType.TEXT)
            .withContent(TEXT_1)
            .build();

    private TextDocument textDoc2 = TextDocument.builder(DocumentType.TEXT)
            .withContent(TEXT_2)
            .build();

    private EnclosingDocument par = EnclosingDocument.builder(DocumentType.PARAGRAPH)
            .withContent(textDoc1)
            .withFormatting(FORMATTING)
            .withId(PAR_1_ID)
            .build();

    private EnclosingDocument sec1 = EnclosingDocument.builder(DocumentType.SECTION)
            .withContent(par)
            .withFormatting(FORMATTING)
            .withId(SEC_1_ID)
            .build();

    private EnclosingDocument sec2 = EnclosingDocument.builder(DocumentType.SECTION)
            .withContent(par)
            .withContent(textDoc2)
            .withFormatting(FORMATTING)
            .withId(SEC_2_ID)
            .build();

    @Test
    @DisplayName("Test converting null EnclosingDocument")
    void testNullDocumentConversion() {
        EnclosingDocumentCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Test converting EnclosingDocument containing one TextDocument")
    void testDocumentConversion_ContentIsTextDocument() {
        EnclosingDocumentCommand command = converter.convert(par);

        assertThat(command, stemsFrom(par));
    }

    @Test
    @DisplayName("Test converting EnclosingDocument containing one EnclosingDocument")
    void testDocumentConversion_ContentIsEnclosingDocument() {
        EnclosingDocumentCommand command = converter.convert(sec1);

        assertThat(command, stemsFrom(sec1));
    }

    @Test
    @DisplayName("Test converting EnclosingDocument with mixed content")
    void testDocumentConversion_MixedContent() {
        EnclosingDocumentCommand command = converter.convert(sec2);

        assertThat(command, stemsFrom(sec2));
    }
}