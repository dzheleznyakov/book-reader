package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.TocCommand;
import zh.bookreader.model.documents.Book;
import zh.bookreader.model.documents.Chapter;
import zh.bookreader.services.ChapterService;

import javax.annotation.Nullable;
import java.util.List;

@Component
public class BookToTocCommand implements Converter<Book, TocCommand> {
    private final ChapterService chapterService;

    public BookToTocCommand(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @Override
    public TocCommand convert(@Nullable Book book) {
        return book == null ? null : TocCommand.builder()
                .bookId(book.getId())
                .toc(getToc(book))
                .build();
    }

    private List<String[]> getToc(Book book) {
        return book.getChapters()
                .stream()
                .map(ch -> new String[]{ch.getId(), getChapterTitle(book, ch)})
                .collect(ImmutableList.toImmutableList());
    }

    private String getChapterTitle(Book book, Chapter ch) {
        return chapterService.getTitle(book.getId(), ch.getId());
    }
}
