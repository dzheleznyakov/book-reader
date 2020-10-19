package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.ImageDocumentCommand;
import zh.bookreader.model.documents.DocumentFormatting;
import zh.bookreader.model.documents.DocumentType;
import zh.bookreader.model.documents.ImageDocument;
import zh.bookreader.utils.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test conversion of ImageDocument to ImageDocumentCommand")
class ImageDocumentToImageDocumentCommandConverterTest {
    private static final String IMAGE_PATH = "images/square.jpg";
    private static final String IMAGE_ID = "image-id";
    private static final Set<DocumentFormatting> IMAGE_FORMATTING = ImmutableSet.of(DocumentFormatting.NOTE);
    private static final String IMAGE_WIDTH = "200";
    private static final String IMAGE_HEIGHT = "300";

    private ImageDocumentToImageDocumentCommandConverter converter;

    private ImageDocument imageDocument;

    @BeforeEach
    void setUpConverter() {
        converter = new ImageDocumentToImageDocumentCommandConverter();
    }

    @BeforeEach
    void setUpDocument() throws URISyntaxException, IOException {
        var imageUri = this.getClass().getClassLoader().getResource(IMAGE_PATH).toURI();
        File imageFile = new File(imageUri);
        var bytes = ClassUtils.cast(Files.toByteArray(imageFile));
        imageDocument = ImageDocument.builder(DocumentType.IMAGE)
                .withContent(bytes)
                .withId(IMAGE_ID)
                .withFormatting(IMAGE_FORMATTING)
                .withMetadata(ImmutableMap.of(
                        "&tag", "img",
                        "@width", IMAGE_WIDTH,
                        "@height", IMAGE_HEIGHT))
                .build();
    }

    @Test
    @DisplayName("Test conversion of null document")
    void convertNullDocument() {
        ImageDocumentCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Test conversion of not null image document")
    void convertDocument() {
        ImageDocumentCommand command = converter.convert(imageDocument);

        assertThat(command, is(notNullValue()));
        assertThat(command.getDocumentType(), is(equalTo(DocumentType.IMAGE.name())));
        assertThat(command.getId(), is(equalTo(IMAGE_ID)));
        assertThat(command.getFormatting(), is(equalTo(ImmutableSet.of("NOTE"))));
        assertThat(command.getContent(), is(equalTo(imageDocument.getContent())));
        assertThat(command.getWidth(), is(equalTo("200")));
        assertThat(command.getHeight(), is(equalTo("300")));
    }
}