package zh.bookreader.api.testtuils.hamcrest;

import org.hamcrest.TypeSafeMatcher;
import org.jetbrains.annotations.Contract;
import zh.bookreader.api.commands.BookMainCommand;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.api.commands.TextDocumentCommand;
import zh.bookreader.model.Book;
import zh.bookreader.model.EnclosingDocument;
import zh.bookreader.model.TextDocument;

import javax.annotation.Nonnull;

public class DocumentMatchers {
    @Nonnull
    @Contract("_ -> new")
    public static TypeSafeMatcher<TextDocumentCommand> stemsFrom(@Nonnull TextDocument doc) {
        return new TextDocStemsFrom(doc);
    }

    @Nonnull
    @Contract("_ -> new")
    public static TypeSafeMatcher<EnclosingDocumentCommand> stemsFrom(@Nonnull EnclosingDocument doc) {
        return new EnclosingDocStemsFrom(doc);
    }

    @Nonnull
    @Contract("_ -> new")
    public static TypeSafeMatcher<BookMainCommand> stemsFrom(@Nonnull Book book) {
        return new BookMainStemsFrom(book);
    }
}
