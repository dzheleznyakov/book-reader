package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.DocumentCommand;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.model.documents.Document;
import zh.bookreader.model.documents.DocumentFormatting;
import zh.bookreader.model.documents.EnclosingDocument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Component
@Slf4j
public class EnclosingDocumentToEnclosingDocumentCommandConverter
        implements Converter<EnclosingDocument, EnclosingDocumentCommand> {
    private final Map<
            Class<?>,
            BaseDocumentConverter<?, ?>
    > convertersByDocClass;

    public EnclosingDocumentToEnclosingDocumentCommandConverter(Set<BaseDocumentConverter<? extends Document<?>, ?>> converters) {
        this.convertersByDocClass = converters.stream()
                .collect(ImmutableMap.toImmutableMap(BaseDocumentConverter::getSourceClass, Function.identity()));
    }

    @Override
    public EnclosingDocumentCommand convert(@Nullable EnclosingDocument doc) {
        return doc == null ? null : EnclosingDocumentCommand.builder()
                .documentType(convertDocumentType(doc))
                .content(convertContent(doc))
                .id(doc.getId())
                .formatting(convertFormatting(doc))
                .href(getHref(doc))
                .metadata(convertMetadata(doc))
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
        if (doc.getClass() == EnclosingDocument.class)
            return this.convert((EnclosingDocument) doc);

        Class<? extends Document> docClass = doc.getClass();
        BaseDocumentConverter<D, ?> converter = (BaseDocumentConverter<D, ?>) convertersByDocClass.get(docClass);
        if (converter == null) {
            log.warn("Document type is not supported: [{}]", doc.getClass());
            return null;
        }
        return (DocumentCommand) converter.convert(doc);
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

    private Map<String, String> convertMetadata(EnclosingDocument doc) {
        Map<String, String> metadata = doc.getMetadata();
        final String COLSPAN_KEY = "@colspan";
        String colspan = metadata.get(COLSPAN_KEY);
        return colspan == null ? null : ImmutableMap.of(COLSPAN_KEY, colspan);
    }
}
