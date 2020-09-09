package zh.bookreader.api.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.BookOverviewCommand;
import zh.bookreader.model.Book;

@Component
public class BookToBookOverviewCommandConverter implements Converter<Book, BookOverviewCommand> {
    @Override
    public BookOverviewCommand convert(Book book) {
        return BookOverviewCommand.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authors(book.getAuthors())
                .topics(book.getTopics())
                .image(book.getImage())
                .build();
    }
}
