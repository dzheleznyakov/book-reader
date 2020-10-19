package zh.bookreader.api.converters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.ChapterCommand;
import zh.bookreader.api.commands.DocumentCommand;
import zh.bookreader.model.documents.Chapter;
import zh.bookreader.model.documents.Document;
import zh.bookreader.model.documents.DocumentFormatting;
import zh.bookreader.model.documents.EnclosingDocument;

import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@Component
public class ChapterToChapterCommandConverter implements Converter<Chapter, ChapterCommand> {
    private final EnclosingDocumentToEnclosingDocumentCommandConverter enclosingDocConverter;

    public ChapterToChapterCommandConverter(EnclosingDocumentToEnclosingDocumentCommandConverter enclosingDocConverter) {
        this.enclosingDocConverter = enclosingDocConverter;
    }

    @Override
    public ChapterCommand convert(@Nullable Chapter chapter) {
        return chapter == null
                ? null
                : ChapterCommand.builder()
                        .title(getTitle(chapter.getContent()))
                        .content(convert(chapter.getContent()))
                        .build();
    }

    private String getTitle(Document<List<Document<?>>> doc) {
        return doc instanceof EnclosingDocument
                ? getFirstHeader((EnclosingDocument) doc)
                : "";
    }

    private String getFirstHeader(EnclosingDocument doc) {
        EnclosingDocument header = (EnclosingDocument) doc
                .findFirst(d -> d instanceof EnclosingDocument && d.getFormatting().contains(DocumentFormatting.TITLE));
        return header == null
                ? ""
                : header.text();
    }

    private DocumentCommand convert(Document<List<Document<?>>> doc) {
        if (doc instanceof EnclosingDocument)
            return enclosingDocConverter.convert((EnclosingDocument) doc);
        log.warn("Document type is not supported for chapters: [{}]", doc.getClass());
        return null;
    }
}
