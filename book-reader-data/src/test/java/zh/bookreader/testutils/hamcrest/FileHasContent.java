package zh.bookreader.testutils.hamcrest;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

class FileHasContent extends TypeSafeMatcher<File> {
    private final String expectedContent;
    private String actualContent;

    FileHasContent(String expectedContent) {
        this.expectedContent = expectedContent;
    }

    @Override
    protected boolean matchesSafely(File item) {
        if (item == null || !item.exists())
            return false;

        try (Scanner input = new Scanner(item)) {
            actualContent = String.join("\n", Files.readLines(item, Charsets.UTF_8));
            return Objects.equals(expectedContent, this.actualContent);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expectedContent);
    }

    @Override
    protected void describeMismatchSafely(File item, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendValue(actualContent);
    }
}
