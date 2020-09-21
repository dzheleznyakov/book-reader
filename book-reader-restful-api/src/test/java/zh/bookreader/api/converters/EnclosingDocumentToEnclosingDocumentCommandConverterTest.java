package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.model.DocumentType;
import zh.bookreader.model.EnclosingDocument;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static zh.bookreader.api.ApiTestUtils.PAR;
import static zh.bookreader.api.ApiTestUtils.SEC_1;
import static zh.bookreader.api.ApiTestUtils.SEC_2;
import static zh.bookreader.api.ApiTestUtils.TEXT_DOC_1;
import static zh.bookreader.api.testtuils.hamcrest.DocumentMatchers.stemsFrom;

class EnclosingDocumentToEnclosingDocumentCommandConverterTest {
    private static final String METADATA_HREF_KEY = "@href";
    private static final String METADATA_DATA_TYPE_KEY = "type";
    private static final String METADATA_XREF_DATA_TYPE = "xref";

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

    @Test
    @DisplayName("Test converting an HREF document pointing to an external page")
    void testHrefConversion_External() {
        String href = "http://mock-link";
        EnclosingDocument hrefDoc = EnclosingDocument.builder(DocumentType.HREF)
                .withContent(TEXT_DOC_1)
                .withMetadata(ImmutableMap.of(METADATA_HREF_KEY, href))
                .build();

        EnclosingDocumentCommand command = converter.convert(hrefDoc);

        assertThat(command.getHref(), is(equalTo(href)));
    }

    @Test
    @DisplayName("Test converting an HREF document pointing to an internal page")
    void testHrefConversion_Internal() {
        String href = "mock-link.html#pos";
        EnclosingDocument hrefDoc = EnclosingDocument.builder(DocumentType.HREF)
                .withContent(TEXT_DOC_1)
                .withMetadata(ImmutableMap.of(
                        METADATA_HREF_KEY, href,
                        METADATA_DATA_TYPE_KEY, METADATA_XREF_DATA_TYPE))
                .build();

        EnclosingDocumentCommand command = converter.convert(hrefDoc);

        assertThat(command.getHref(), is(equalTo("mock-link#pos")));
    }
}