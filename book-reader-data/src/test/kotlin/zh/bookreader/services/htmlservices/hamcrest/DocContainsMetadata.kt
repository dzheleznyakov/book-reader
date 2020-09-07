package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.Document
import zh.bookreader.testutils.PrintUtils

class DocContainsMetadata(private val metadata: Map<String, String>) : TypeSafeMatcher<Document<*>>() {
    override fun matchesSafely(item: Document<*>) = item.metadata.entries.containsAll(metadata.entries)

    override fun describeTo(description: Description) {
        description.appendValue(metadata.entries.toList().sortedBy { it.key })
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        var diff = item.metadata.entries.toMutableList()
        diff.retainAll(this.metadata.entries)
        diff.sortBy { it.key }
        mismatchDescription.appendText("was ").appendValue(diff)
        PrintUtils.println("Document metadata does not contain all expected values", PrintUtils.Color.RED)
    }
}