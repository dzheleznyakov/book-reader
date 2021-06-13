package zh.bookreader.api.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.HorizontalRuleDocumentCommand;
import zh.bookreader.model.documents.DocumentType;
import zh.bookreader.model.documents.HorizontalRuleDocument;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class HorizontalRuleDocumentToHorizontalRuleDocumentCommandConverterTest {
    private HorizontalRuleDocumentToHorizontalRuleDocumentCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new HorizontalRuleDocumentToHorizontalRuleDocumentCommandConverter();
    }

    @Test
    @DisplayName("Test converting null")
    void convertNull() {
        HorizontalRuleDocumentCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Test converting Horizontal Rule Document")
    void convertHorizontalRuleDocument() {
        HorizontalRuleDocument doc = new HorizontalRuleDocument();

        HorizontalRuleDocumentCommand command = converter.convert(doc);

        assertThat(command, is(notNullValue()));
        assertThat(command.getDocumentType(), is(DocumentType.HORIZONTAL.toString()));
    }
}