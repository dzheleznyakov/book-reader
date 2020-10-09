package zh.bookreader.model;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test EnclosingDocument")
class EnclosingDocumentTest {
    private EnclosingDocument document;
    private EnclosingDocument header1;
    private EnclosingDocument header2;
    private TextDocument par1_2;
    private TextDocument par1_1;
    private TextDocument par2_1;
    private TextDocument par2_2;
    private EnclosingDocument par1;
    private EnclosingDocument par2;

    @BeforeEach
    void setUpDocument() {
        header1 = EnclosingDocument.builder(DocumentType.INLINED)
                .withFormatting(ImmutableList.of(DocumentFormatting.TITLE))
                .withContent(TextDocument.builder().withContent("First Header").build())
                .build();
        header2 = EnclosingDocument.builder(DocumentType.INLINED)
                .withFormatting(ImmutableList.of(DocumentFormatting.TITLE))
                .withContent(TextDocument.builder().withContent("Second Header").build())
                .build();
        par1_1 = TextDocument.builder().withContent("One alpha beta gamma").build();
        par1_2 = TextDocument.builder().withContent("Two delta epsilon zeta").build();
        par2_1 = TextDocument.builder().withContent("Three Iota Kappa Lambda").build();
        par2_2 = TextDocument.builder().withContent("Four Mu Nu Omega").build();

        par1 = EnclosingDocument.builder(DocumentType.PARAGRAPH)
                .withContent(ImmutableList.of(header1, par1_1, par1_2))
                .build();
        par2 = EnclosingDocument.builder(DocumentType.PARAGRAPH)
                .withContent(ImmutableList.of(header2, par2_1, par2_2))
                .build();

        document = EnclosingDocument.builder(DocumentType.BLOCK)
                .withContent(ImmutableList.of(par1, par2))
                .build();
    }

    @Test
    @DisplayName("Find first enclosing document")
    void testFindFirst_EnclosingDoc() {
        Document<?> doc = document.findFirst(d -> d instanceof EnclosingDocument);

        assertThat(doc, is(notNullValue()));
        assertThat(doc, is(document));
    }

    @Test
    @DisplayName("Find first text document")
    void testFindFirst_TextDoc() {
        Document<?> doc = document.findFirst(d -> d instanceof TextDocument);

        assertThat(doc, is(notNullValue()));
        assertThat(doc, is(header1.getContent().get(0)));
    }

    @Test
    @DisplayName("Return null when nothing found")
    void testFindFirst_NotFound() {
        Document<?> doc = document.findFirst(d -> false);

        assertThat(doc, is(nullValue()));
    }

    @Test
    @DisplayName("Find first text document containing certain substring")
    void testFindFirst_ByText() {
        Document<?> doc = document.findFirst(d -> (d instanceof TextDocument) && ((TextDocument) d).getContent().contains("Kappa"));

        assertThat(doc, is(notNullValue()));
        assertThat(doc, is(par2_1));
    }

    @Test
    @DisplayName("Find first header")
    void testFindFirst_Title() {
        Document<?> doc = document.findFirst(d -> d instanceof EnclosingDocument && d.getFormatting().contains(DocumentFormatting.TITLE));

        assertThat(doc, is(notNullValue()));
        assertThat(doc, is(header1));
    }

    @Test
    @DisplayName("Return null when the passed condition is null")
    void testFindFirst_NullCondition() {
        Document<?> doc = document.findFirst(null);

        assertThat(doc, is(nullValue()));
    }

    @Test
    @DisplayName("Extract the text from the enclosing document")
    void testGetText_EnclosesTextDocuments() {
        String text = par1.text();

        assertThat(text, is(notNullValue()));
        assertThat(text, is("First Header" + par1_1.getContent() + par1_2.getContent()));
    }

    @Test
    @DisplayName("Return empty string if there are no text documents")
    void testGetText_NoTextDocuments() {
        document = EnclosingDocument.builder(DocumentType.BLOCK)
                .withContent(ImageDocument.builder().build())
                .build();

        String text = document.text();

        assertThat(text, is(""));
    }
}