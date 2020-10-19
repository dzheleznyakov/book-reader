package zh.bookreader.api.testtuils.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import zh.bookreader.api.commands.TextDocumentCommand;
import zh.bookreader.api.testtuils.ToStringHelper;
import zh.bookreader.model.documents.TextDocument;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TextDocStemsFrom extends TypeSafeMatcher<TextDocumentCommand> {
    private final TextDocument doc;

    TextDocStemsFrom(@Nonnull TextDocument doc) {
        this.doc = doc;
    }

    @Override
    protected boolean matchesSafely(TextDocumentCommand item) {
        return item != null
                && Objects.equals(item.getDocumentType(), doc.getDocumentType().name())
                && Objects.equals(item.getContent(), doc.getContent());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(ToStringHelper.toString(doc));
    }

    @Override
    protected void describeMismatchSafely(TextDocumentCommand item, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendText(ToStringHelper.toString(item));
    }
}
