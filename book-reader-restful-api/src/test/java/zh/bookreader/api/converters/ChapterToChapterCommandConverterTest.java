package zh.bookreader.api.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.ChapterCommand;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.model.Chapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static zh.bookreader.api.ApiTestUtils.SEC_2;
import static zh.bookreader.api.testtuils.hamcrest.DocumentMatchers.stemsFrom;

class ChapterToChapterCommandConverterTest {
    private ChapterToChapterCommandConverter converter;

    private Chapter chapter = Chapter.builder()
            .id("mock-id")
            .content(SEC_2)
            .build();

    @BeforeEach
    void setUpConverter() {
        TextDocumentToTextDocumentCommandConverter textDocConverter = new TextDocumentToTextDocumentCommandConverter();
        ImageDocumentToImageDocumentCommandConverter imageDocConverter = new ImageDocumentToImageDocumentCommandConverter();
        EnclosingDocumentToEnclosingDocumentCommandConverter enclosingDocConverter = new EnclosingDocumentToEnclosingDocumentCommandConverter(
                textDocConverter, imageDocConverter);
        converter = new ChapterToChapterCommandConverter(enclosingDocConverter);
    }

    @Test
    @DisplayName("Test converting null value")
    void testNullChapterConversion() {
        ChapterCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Test converting a chapter")
    void testChapterConversion() {
        ChapterCommand command = converter.convert(chapter);

        assertThat(command, is(notNullValue()));
        assertThat(command.getContent(), is(instanceOf(EnclosingDocumentCommand.class)));

        EnclosingDocumentCommand content = (EnclosingDocumentCommand) command.getContent();
        assertThat(content, stemsFrom(SEC_2));
    }
}