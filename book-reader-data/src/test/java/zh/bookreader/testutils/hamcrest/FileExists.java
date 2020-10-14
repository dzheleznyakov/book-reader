package zh.bookreader.testutils.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;

class FileExists extends TypeSafeMatcher<File> {
    @Override
    protected boolean matchesSafely(File item) {
        return item != null && item.exists();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("File should exist");
    }
}
