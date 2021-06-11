package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.DocumentCommand;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.model.documents.BreakRuleDocument;
import zh.bookreader.model.documents.Document;
import zh.bookreader.model.documents.DocumentFormatting;
import zh.bookreader.model.documents.EnclosingDocument;
import zh.bookreader.model.documents.ImageDocument;
import zh.bookreader.model.documents.RawDocument;
import zh.bookreader.model.documents.TextDocument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class EnclosingDocumentToEnclosingDocumentCommandConverter implements Converter<EnclosingDocument, EnclosingDocumentCommand> {
    private final TextDocumentToTextDocumentCommandConverter textDocConverter;
    private final ImageDocumentToImageDocumentCommandConverter imageDocConverter;
    private final BreakRuleDocumentToBreakRuleDocumentCommandConverter breakRuleDocumentConverter;
    private final RawDocumentToRawDocumentCommandConverter rawDocumentConverter;

    public EnclosingDocumentToEnclosingDocumentCommandConverter(
            TextDocumentToTextDocumentCommandConverter textDocConverter,
            ImageDocumentToImageDocumentCommandConverter imageDocConverter,
            BreakRuleDocumentToBreakRuleDocumentCommandConverter breakRuleDocumentConverter,
            RawDocumentToRawDocumentCommandConverter rawDocumentConverter
    ) {
        this.textDocConverter = textDocConverter;
        this.imageDocConverter = imageDocConverter;
        this.breakRuleDocumentConverter = breakRuleDocumentConverter;
        this.rawDocumentConverter = rawDocumentConverter;
    }

    @Override
    public EnclosingDocumentCommand convert(@Nullable EnclosingDocument doc) {
        return doc == null ? null : EnclosingDocumentCommand.builder()
                .documentType(convertDocumentType(doc))
                .content(convertContent(doc))
                .id(doc.getId())
                .formatting(convertFormatting(doc))
                .href(getHref(doc))
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
        if (doc instanceof RawDocument)
            return rawDocumentConverter.convert((RawDocument) doc);
        if (doc instanceof TextDocument)
            return textDocConverter.convert((TextDocument) doc);
        if (doc instanceof ImageDocument)
            return imageDocConverter.convert((ImageDocument) doc);
        if (doc instanceof EnclosingDocument)
            return this.convert((EnclosingDocument) doc);
        if (doc instanceof BreakRuleDocument)
            return breakRuleDocumentConverter.convert((BreakRuleDocument) doc);
        log.warn("Document type is not supported: [{}]", doc.getClass());
        return null;
    }

    private String getHref(EnclosingDocument doc) {
        Map<String, String> metadata = doc.getMetadata();
        String href = metadata.get("@href");
        if (href == null)
            return href;
        return Objects.equals(metadata.get("type"), "xref")
                ? href.replace(".html", "")
                : href;
    }
}
