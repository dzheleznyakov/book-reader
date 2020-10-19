package zh.bookreader.api.testtuils.hamcrest;

import com.google.common.collect.ImmutableSet;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import zh.bookreader.api.commands.DocumentCommand;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.api.commands.TextDocumentCommand;
import zh.bookreader.api.testtuils.ToStringHelper;
import zh.bookreader.model.documents.Document;
import zh.bookreader.model.documents.DocumentFormatting;
import zh.bookreader.model.documents.EnclosingDocument;
import zh.bookreader.model.documents.TextDocument;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

public class EnclosingDocStemsFrom extends TypeSafeMatcher<EnclosingDocumentCommand> {
    private final EnclosingDocument doc;

    public EnclosingDocStemsFrom(@Nonnull EnclosingDocument doc) {
        this.doc = doc;
    }

    @Override
    protected boolean matchesSafely(EnclosingDocumentCommand item) {
        return item != null
                && Objects.equals(item.getDocumentType(), doc.getDocumentType().name())
                && Objects.equals(item.getId(), doc.getId())
                && formattingMatches(item)
                && contentMatches(item);
    }

    private boolean formattingMatches(EnclosingDocumentCommand item) {
        Set<String> actual = item.getFormatting();
        ImmutableSet<String> expected = doc.getFormatting()
                .stream()
                .map(DocumentFormatting::name)
                .collect(ImmutableSet.toImmutableSet());
        return Objects.equals(actual, expected);
    }

    private boolean contentMatches(EnclosingDocumentCommand item) {
        if (item.getContent().size() != doc.getContent().size())
            return false;
        if (item.getContent().isEmpty())
            return true;
        return IntStream.range(0, doc.getContent().size())
                .allMatch(i -> matchesAsDocumentCommand(i, item));
    }

    private boolean matchesAsDocumentCommand(int i, EnclosingDocumentCommand item) {
        Document<?> expected = doc.getContent().get(i);
        DocumentCommand actual = item.getContent().get(i);
        return matchesAsTextDocumentCommand(expected, actual)
                || matchesAsEnclosedDocumentCommand(expected, actual);
    }

    private boolean matchesAsTextDocumentCommand(Document<?> expected, DocumentCommand actual) {
        return expected instanceof TextDocument
                && actual instanceof TextDocumentCommand
                && new TextDocStemsFrom((TextDocument) expected).matchesSafely((TextDocumentCommand) actual);
    }

    private boolean matchesAsEnclosedDocumentCommand(Document<?> expected, DocumentCommand actual) {
        return expected instanceof EnclosingDocument
                && actual instanceof EnclosingDocumentCommand
                && new EnclosingDocStemsFrom((EnclosingDocument) expected).matchesSafely((EnclosingDocumentCommand) actual);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(ToStringHelper.toString(doc));
    }

    @Override
    protected void describeMismatchSafely(EnclosingDocumentCommand item, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendText(ToStringHelper.toString(item));
    }
}
