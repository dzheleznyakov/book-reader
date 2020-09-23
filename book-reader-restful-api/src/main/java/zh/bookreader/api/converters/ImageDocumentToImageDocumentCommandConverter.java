package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableSet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.ImageDocumentCommand;
import zh.bookreader.model.ImageDocument;

import javax.annotation.Nullable;
import java.util.Set;

@Component
public class ImageDocumentToImageDocumentCommandConverter implements Converter<ImageDocument, ImageDocumentCommand> {
    private static final String METADATA_WIDTH_KEY = "@width";
    private static final String METADATA_HEIGHT_KEY = "@height";

    @Override
    public ImageDocumentCommand convert(@Nullable ImageDocument doc) {
        return doc == null
                ? null
                : ImageDocumentCommand.builder()
                        .documentType(doc.getDocumentType().toString())
                        .id(doc.getId())
                        .formatting(getFormatting(doc))
                        .content(doc.getContent())
                        .width(getWidth(doc))
                        .height(getHeight(doc))
                        .build();
    }

    private Set<String> getFormatting(ImageDocument doc) {
        return doc.getFormatting().stream()
                .map(Enum::name)
                .collect(ImmutableSet.toImmutableSet());
    }

    private String getWidth(ImageDocument doc) {
        return doc.getMetadata().get(METADATA_WIDTH_KEY);
    }

    private String getHeight(ImageDocument doc) {
        return doc.getMetadata().get(METADATA_HEIGHT_KEY);
    }
}
