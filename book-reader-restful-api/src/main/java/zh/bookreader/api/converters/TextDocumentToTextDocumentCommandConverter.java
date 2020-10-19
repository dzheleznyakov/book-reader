package zh.bookreader.api.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.TextDocumentCommand;
import zh.bookreader.model.documents.TextDocument;

import javax.annotation.Nullable;

@Component
public class TextDocumentToTextDocumentCommandConverter implements Converter<TextDocument, TextDocumentCommand> {
    @Override
    public TextDocumentCommand convert(@Nullable TextDocument doc) {
        return doc == null ? null : TextDocumentCommand.builder()
                .documentType(doc.getDocumentType().name())
                .content(doc.getContent())
                .build();
    }
}
