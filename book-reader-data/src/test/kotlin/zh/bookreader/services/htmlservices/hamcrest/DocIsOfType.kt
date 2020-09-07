package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.Document
import zh.bookreader.testutils.PrintUtils
import zh.bookreader.testutils.PrintUtils.Color

internal class DocIsOfType(private val type: String) : TypeSafeMatcher<Document<*>>() {
    override fun matchesSafely(item: Document<*>): Boolean = item.documentType.toString() == type

    override fun describeTo(description: Description) {
        description.appendValue(type)
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        mismatchDescription.appendText("was ").appendValue(item.documentType)
        PrintUtils.println("Wrong document type", Color.RED)
    }
}