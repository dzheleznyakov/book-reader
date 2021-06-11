package zh.bookreader.api.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.BreakRuleDocumentCommand;
import zh.bookreader.model.documents.BreakRuleDocument;
import zh.bookreader.model.documents.DocumentType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class BreakRuleDocumentToBreakRuleDocumentCommandConverterTest {
    private BreakRuleDocumentToBreakRuleDocumentCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new BreakRuleDocumentToBreakRuleDocumentCommandConverter();
    }

    @Test
    @DisplayName("Test converting null")
    void convertNull() {
        BreakRuleDocumentCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Test converting non-null break rule document")
    void convertNonNull() {
        BreakRuleDocumentCommand command = converter.convert(BreakRuleDocument.builder().build());

        assertThat(command, is(notNullValue()));
        assertThat(command.getDocumentType(), is(DocumentType.BREAK.toString()));
    }
}