package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.DocumentCommand;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.api.commands.ImageDocumentCommand;
import zh.bookreader.api.commands.RawDocumentCommand;
import zh.bookreader.model.documents.BreakRuleDocument;
import zh.bookreader.model.documents.DocumentType;
import zh.bookreader.model.documents.EnclosingDocument;
import zh.bookreader.model.documents.HorizontalRuleDocument;
import zh.bookreader.model.documents.ImageDocument;
import zh.bookreader.model.documents.RawDocument;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
    private static final String METADATA_COLSPAN_KEY = "@colspan";

    private EnclosingDocumentToEnclosingDocumentCommandConverter converter;

    @BeforeEach
    void setUpConverter() {
        converter = new EnclosingDocumentToEnclosingDocumentCommandConverter(
                ImmutableSet.of(
                        new TextDocumentToTextDocumentCommandConverter(),
                        new ImageDocumentToImageDocumentCommandConverter(),
                        new BreakRuleDocumentToBreakRuleDocumentCommandConverter(),
                        new RawDocumentToRawDocumentCommandConverter(),
                        new HorizontalRuleDocumentToHorizontalRuleDocumentCommandConverter())
        );
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
    @DisplayName("Test converting Enclosing Document containing one BreakRule Document")
    void testDocumentConversion_ContentIsBreakRuleDocument() {
        EnclosingDocument doc = EnclosingDocument.builder(DocumentType.SECTION)
                .withContent(BreakRuleDocument.builder().build())
                .build();

        EnclosingDocumentCommand command = converter.convert(doc);

        List<? extends DocumentCommand> content = command.getContent();

        assertThat(content, is(notNullValue()));
        assertThat(content, hasSize(1));
        assertThat(content.get(0).getDocumentType(), is(DocumentType.BREAK.toString()));
    }

    @Test
    @DisplayName("Test converting Enclosing Document containing one HorizontalRule Document")
    void testDocumentConversion_ContentIsHorizontalRuleDocument() {
        EnclosingDocument doc = EnclosingDocument.builder(DocumentType.SECTION)
                .withContent(new HorizontalRuleDocument())
                .build();

        EnclosingDocumentCommand command = converter.convert(doc);

        List<? extends DocumentCommand> content = command.getContent();

        assertThat(content, is(notNullValue()));
        assertThat(content, hasSize(1));
        assertThat(content.get(0).getDocumentType(), is(DocumentType.HORIZONTAL.toString()));
    }

    @Test
    @DisplayName("Test converting Enclosing Document containing one Raw Document")
    void testDocumentConversion_ContentIsRawDocument() {
        String docContent = "content";
        EnclosingDocument doc = EnclosingDocument.builder(DocumentType.SECTION)
                .withContent(RawDocument.builder().withContent(docContent).build())
                .build();

        EnclosingDocumentCommand command = converter.convert(doc);

        List<? extends DocumentCommand> content = command.getContent();

        assertThat(content, is(notNullValue()));
        assertThat(content, hasSize(1));
        RawDocumentCommand rawDocCommand = (RawDocumentCommand) content.get(0);
        assertThat(rawDocCommand.getDocumentType(), is(DocumentType.RAW.toString()));
        assertThat(rawDocCommand.getContent(), is(docContent));
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

    @Test
    void testConvertingEnclosingDocumentWithImage() {
        ImageDocument imageDocument = ImageDocument.builder(DocumentType.IMAGE)
                .build();
        EnclosingDocument doc = EnclosingDocument.builder(DocumentType.BLOCK)
                .withContent(imageDocument)
                .build();

        EnclosingDocumentCommand command = converter.convert(doc);

        List<? extends DocumentCommand> content = command.getContent();
        assertThat(content.get(0), is(instanceOf(ImageDocumentCommand.class)));
    }

    @Test
    @DisplayName("Test converting a table cell with colspan attribute")
    void testConvertingTableDataCellWithColspan() {
        String colspanValue = "42";
        EnclosingDocument tableCellDoc = EnclosingDocument.builder(DocumentType.TABLE)
                .withContent(TEXT_DOC_1)
                .withMetadata(ImmutableMap.of(
                        METADATA_COLSPAN_KEY, colspanValue))
                .build();

        EnclosingDocumentCommand command = converter.convert(tableCellDoc);
        Map<String, String> metadata = command.getMetadata();

        String actualColspanValue = metadata.get(METADATA_COLSPAN_KEY);
        assertThat(actualColspanValue, is(notNullValue()));
        assertThat(actualColspanValue, is(colspanValue));
    }
}