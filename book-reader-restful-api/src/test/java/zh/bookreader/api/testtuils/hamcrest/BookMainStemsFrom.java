package zh.bookreader.api.testtuils.hamcrest;

import com.google.common.collect.ImmutableList;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import zh.bookreader.api.commands.BookMainCommand;
import zh.bookreader.api.commands.DocumentCommand;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.api.commands.TextDocumentCommand;
import zh.bookreader.model.Book;
import zh.bookreader.model.Chapter;
import zh.bookreader.model.Document;
import zh.bookreader.model.EnclosingDocument;
import zh.bookreader.model.TextDocument;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class BookMainStemsFrom extends TypeSafeMatcher<BookMainCommand> {
    private final Book book;
    private TypeSafeMatcher<?> auxMatcher;
    private int failedChapterIndex = -1;

    public BookMainStemsFrom(@Nonnull Book book) {
        this.book = book;
    }

    @Override
    protected boolean matchesSafely(BookMainCommand item) {
        return item != null
                && Objects.equals(book.getId(), item.getId())
                && Objects.equals(book.getTitle(), item.getTitle())
                && Objects.equals(book.getReleaseDate(), item.getReleaseDate())
                && Objects.equals(book.getAuthors(), item.getAuthors())
                && Objects.equals(book.getTopics(), item.getTopics())
                && Objects.equals(book.getResources(), item.getResources())
                && Arrays.equals(book.getImage(), item.getImage())
                && Objects.equals(getToc(), item.getToc())
                && descriptionMatches(item)
                ;
    }

    private List<String> getToc() {
        return book.getChapters()
                .stream()
                .map(Chapter::getName)
                .collect(ImmutableList.toImmutableList());
    }

    private boolean descriptionMatches(BookMainCommand item) {
        if (book.getDescription().size() != item.getDescription().size())
            return false;
        if (book.getDescription().isEmpty())
            return true;
        return IntStream.range(0, book.getDescription().size())
                .allMatch(i -> matchesAsDocumentCommand(i, item));
    }

    private boolean matchesAsDocumentCommand(int i, BookMainCommand item) {
        Document<?> expected = book.getDescription().get(i);
        DocumentCommand actual = item.getDescription().get(i);
        return matchesAsTextDocumentCommand(i, expected, actual)
                || matchesAsEnclosedDocumentCommand(i, expected, actual);
    }

    private boolean matchesAsTextDocumentCommand(int i, Document<?> expected, DocumentCommand actual) {
        failedChapterIndex = i;
        return expected instanceof TextDocument
                && actual instanceof TextDocumentCommand
                && (auxMatcher = new TextDocStemsFrom((TextDocument) expected))
                .matches((TextDocumentCommand) actual);
    }

    private boolean matchesAsEnclosedDocumentCommand(int i, Document<?> expected, DocumentCommand actual) {
        failedChapterIndex = i;
        return expected instanceof EnclosingDocument
                && actual instanceof EnclosingDocumentCommand
                && (auxMatcher = new EnclosingDocStemsFrom((EnclosingDocument) expected))
                        .matches((EnclosingDocumentCommand) actual);
    }

    @Override
    public void describeTo(Description description) {
        if (auxMatcher == null)
            description.appendValue(book);
        else
            auxMatcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(BookMainCommand item, Description mismatchDescription) {
        if (auxMatcher == null)
            mismatchDescription.appendText("was ").appendValue(item);
        else
            auxMatcher.describeMismatch(item.getDescription().get(failedChapterIndex), mismatchDescription);
    }
}
