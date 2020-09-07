package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.Document
import zh.bookreader.services.htmlservices.trim
import zh.bookreader.testutils.PrintUtils
import zh.bookreader.testutils.PrintUtils.Color

internal class DocHasMetadata : TypeSafeMatcher<Document<*>> {
    private val expected: Map<String, String>

    constructor(metadata: String) {
        expected = metadata.split(";")
                .trim()
                .map { it.split("=>").trim() }
                .map { entry -> entry[0] to entry[1] }
                .toMap()
    }

    constructor(metadata: Map<String, String>) {
        this.expected = metadata
    }

    override fun matchesSafely(item: Document<*>) = expected == item.metadata

    override fun describeTo(description: Description) {
        description.appendValue(expected)
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        mismatchDescription.appendText("was ").appendValue(item.metadata)
        PrintUtils.println("Wrong document metadata", Color.RED)
    }
}