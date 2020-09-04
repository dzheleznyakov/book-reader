package zh.bookreader.testutils.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Optional;

class OptionalIsEmpty extends TypeSafeMatcher<Optional<?>> {
    @Override
    protected boolean matchesSafely(Optional<?> item) {
        return item.isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("the optional should be empty");
    }

    @Override
    protected void describeMismatchSafely(Optional<?> item, Description mismatchDescription) {
        mismatchDescription.appendText("the optional contains <")
                .appendValue(item.get())
                .appendText(">");
    }
}
