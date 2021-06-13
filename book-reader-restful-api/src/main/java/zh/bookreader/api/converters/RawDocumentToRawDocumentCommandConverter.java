package zh.bookreader.api.converters;

import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.RawDocumentCommand;
import zh.bookreader.model.documents.RawDocument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Component
public class RawDocumentToRawDocumentCommandConverter
        implements BaseDocumentConverter<RawDocument, RawDocumentCommand> {
    @Override
    public RawDocumentCommand convert(@Nullable RawDocument source) {
        return source == null ? null : RawDocumentCommand.builder()
                .documentType(source.getDocumentType().toString())
                .content(source.getContent())
                .build();
    }

    @Nonnull
    @Override
    public Class<RawDocument> getSourceClass() {
        return RawDocument.class;
    }
}
