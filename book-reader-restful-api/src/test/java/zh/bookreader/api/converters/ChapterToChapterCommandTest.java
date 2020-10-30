package zh.bookreader.api.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.ChapterCommand;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.model.documents.Chapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static zh.bookreader.api.ApiTestUtils.SEC_3;
import static zh.bookreader.api.testtuils.hamcrest.DocumentMatchers.stemsFrom;

class ChapterToChapterCommandTest {
    private static final int CHAPTER_INDEX = 42;

    private ChapterToChapterCommand converter;

    private Chapter chapter = Chapter.builder()
            .id("mock-id")
            .index(CHAPTER_INDEX)
            .content(SEC_3)
            .build();

    @BeforeEach
    void setUpConverter() {
        TextDocumentToTextDocumentCommandConverter textDocConverter = new TextDocumentToTextDocumentCommandConverter();
        ImageDocumentToImageDocumentCommandConverter imageDocConverter = new ImageDocumentToImageDocumentCommandConverter();
        EnclosingDocumentToEnclosingDocumentCommandConverter enclosingDocConverter = new EnclosingDocumentToEnclosingDocumentCommandConverter(
                textDocConverter, imageDocConverter);
        converter = new ChapterToChapterCommand(enclosingDocConverter);
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
        assertThat(command.getIndex(), is(CHAPTER_INDEX));

        assertThat(command.getContent(), is(instanceOf(EnclosingDocumentCommand.class)));
        EnclosingDocumentCommand content = (EnclosingDocumentCommand) command.getContent();
        assertThat(content, stemsFrom(SEC_3));
        assertThat(command.getTitle(), is("Mock Header 1"));
    }
}