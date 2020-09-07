package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.Document

internal class DocHasContent(private val content: List<Document<*>>) : TypeSafeMatcher<Document<*>>() {
    override fun matchesSafely(item: Document<*>) = item.content == content

    override fun describeTo(description: Description) {
        description.appendValue(content)
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        mismatchDescription.appendText("was ").appendValue(item.content)
    }
}