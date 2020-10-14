package zh.bookreader.testutils.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.FileNotFoundException;
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
            StringBuilder sb = new StringBuilder();
            while (input.hasNext()) {
                if (sb.length() > 0)
                    sb.append('\n');
                sb.append(input.nextLine());
            }
            actualContent = sb.toString();
            return Objects.equals(expectedContent, actualContent);
        } catch (FileNotFoundException e) {
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
