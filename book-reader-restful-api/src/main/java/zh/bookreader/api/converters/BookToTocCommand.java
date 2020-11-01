package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.TocCommand;
import zh.bookreader.model.documents.Book;

import javax.annotation.Nullable;
import java.util.List;

@Component
public class BookToTocCommand implements Converter<Book, TocCommand> {
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
                .map(ch -> new String[]{ch.getId(), ch.getFirstTitle()})
                .collect(ImmutableList.toImmutableList());
    }
}
