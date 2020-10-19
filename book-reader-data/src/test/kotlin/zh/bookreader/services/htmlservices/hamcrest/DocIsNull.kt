package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.documents.Document
import zh.bookreader.testutils.PrintUtils

internal class DocIsNull() : TypeSafeMatcher<Document<*>>() {
    override fun matchesSafely(item: Document<*>) = item === Document.NULL

    override fun describeTo(description: Description) {
        description.appendValue(Document.NULL)
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        mismatchDescription.appendText("was ").appendValue(item)
        PrintUtils.println("Document is not NULL", PrintUtils.Color.RED)
    }
}