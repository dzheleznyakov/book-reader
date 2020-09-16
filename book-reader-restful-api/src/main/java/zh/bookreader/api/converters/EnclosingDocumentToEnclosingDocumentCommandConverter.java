package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.DocumentCommand;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.model.Document;
import zh.bookreader.model.DocumentFormatting;
import zh.bookreader.model.EnclosingDocument;
import zh.bookreader.model.TextDocument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class EnclosingDocumentToEnclosingDocumentCommandConverter implements Converter<EnclosingDocument, EnclosingDocumentCommand> {
    private final TextDocumentToTextDocumentCommandConverter textDocConverter;

    public EnclosingDocumentToEnclosingDocumentCommandConverter(TextDocumentToTextDocumentCommandConverter textDocConverter) {
        this.textDocConverter = textDocConverter;
    }

    @Override
    public EnclosingDocumentCommand convert(@Nullable EnclosingDocument doc) {
        return doc == null ? null : EnclosingDocumentCommand.builder()
                .documentType(convertDocumentType(doc))
                .content(convertContent(doc))
                .id(doc.getId())
                .formatting(convertFormatting(doc))
                .build();

    }

    @Nonnull
    private String convertDocumentType(@Nonnull EnclosingDocument doc) {
        return doc.getDocumentType().name();
    }

    private ImmutableSet<String> convertFormatting(@Nonnull EnclosingDocument doc) {
        return doc.getFormatting()
                .stream()
                .map(DocumentFormatting::name)
                .collect(ImmutableSet.toImmutableSet());
    }

    private List<? extends DocumentCommand> convertContent(@Nonnull EnclosingDocument doc) {
        return doc.getContent()
                .stream()
                .map(this::convert)
                .filter(Objects::nonNull)
                .collect(ImmutableList.toImmutableList());
    }

    private <D extends Document<?>> DocumentCommand convert(D doc) {
        if (doc instanceof TextDocument)
            return textDocConverter.convert((TextDocument) doc);
        if (doc instanceof EnclosingDocument)
            return this.convert((EnclosingDocument) doc);
        log.warn("Document type is not supported: [{}]", doc.getClass());
        return null;
    }
}
