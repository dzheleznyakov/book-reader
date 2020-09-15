package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.Document
import zh.bookreader.testutils.PrintUtils

internal class DocHasId(private val id: String) : TypeSafeMatcher<Document<*>>() {
    override fun matchesSafely(item: Document<*>) = item.id == if (id == "_") "" else id

    override fun describeTo(description: Description) {
        description.appendValue(id)
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        mismatchDescription.appendText("was ").appendValue(item.id)
        PrintUtils.println("Wrong document id", PrintUtils.Color.RED)
    }
}