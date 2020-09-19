package zh.bookreader.api.converters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.ChapterCommand;
import zh.bookreader.api.commands.DocumentCommand;
import zh.bookreader.model.Chapter;
import zh.bookreader.model.Document;
import zh.bookreader.model.EnclosingDocument;

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
                        .content(convert(chapter.getContent()))
                        .build();
    }

    private DocumentCommand convert(Document<List<Document<?>>> doc) {
        if (doc instanceof EnclosingDocument)
            return enclosingDocConverter.convert((EnclosingDocument) doc);
        log.warn("Document type is not supported for chapters: [{}]", doc.getClass());
        return null;
    }
}
