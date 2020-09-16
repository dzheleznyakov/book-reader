package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.BookMainCommand;
import zh.bookreader.api.commands.DocumentCommand;
import zh.bookreader.model.Book;
import zh.bookreader.model.Chapter;
import zh.bookreader.model.Document;
import zh.bookreader.model.EnclosingDocument;
import zh.bookreader.model.TextDocument;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Component
public class BookToBookMainCommandConverter implements Converter<Book, BookMainCommand> {
    private final TextDocumentToTextDocumentCommandConverter textDocConverter;
    private final EnclosingDocumentToEnclosingDocumentCommandConverter enclosingDocConverter;

    public BookToBookMainCommandConverter(
            TextDocumentToTextDocumentCommandConverter textDocConverter,
            EnclosingDocumentToEnclosingDocumentCommandConverter enclosingDocConverter)
    {
        this.textDocConverter = textDocConverter;
        this.enclosingDocConverter = enclosingDocConverter;
    }

    @Override
    public BookMainCommand convert(@Nullable Book book) {
        if (book == null)
            return null;

        BookMainCommand command = BookMainCommand.builder()
                .id(book.getId())
                .title(book.getTitle())
                .releaseDate(book.getReleaseDate())
                .authors(book.getAuthors())
                .description(getDocumentCommands(book))
                .topics(book.getTopics())
                .resources(book.getResources())
                .image(book.getImage())
                .toc(getToc(book))
                .build();
        return command;
    }

    private List<? extends DocumentCommand> getDocumentCommands(Book book) {
        return book.getDescription()
                .stream()
                .map(this::getDocumentCommand)
                .filter(Objects::nonNull)
                .collect(ImmutableList.toImmutableList());
    }

    @Nullable
    private DocumentCommand getDocumentCommand(Document<?> doc) {
        if (doc instanceof EnclosingDocument)
            return enclosingDocConverter.convert((EnclosingDocument) doc);
        if (doc instanceof TextDocument)
            return textDocConverter.convert((TextDocument) doc);
        return null;
    }

    private List<String> getToc(Book book) {
        return book.getChapters()
                .stream()
                .map(Chapter::getName)
                .filter(Objects::nonNull)
                .collect(ImmutableList.toImmutableList());
    }
}
