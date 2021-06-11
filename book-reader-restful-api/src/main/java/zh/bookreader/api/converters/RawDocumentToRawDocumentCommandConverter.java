package zh.bookreader.api.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.RawDocumentCommand;
import zh.bookreader.model.documents.RawDocument;

import javax.annotation.Nullable;

@Component
public class RawDocumentToRawDocumentCommandConverter implements Converter<RawDocument, RawDocumentCommand> {
    @Override
    public RawDocumentCommand convert(@Nullable RawDocument source) {
        return source == null ? null : RawDocumentCommand.builder()
                .documentType(source.getDocumentType().toString())
                .content(source.getContent())
                .build();
    }
}
