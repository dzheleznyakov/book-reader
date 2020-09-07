package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.Document
import zh.bookreader.services.htmlservices.trim
import zh.bookreader.testutils.PrintUtils
import zh.bookreader.testutils.PrintUtils.Color

internal class DocHasFormatting(private val formatting: String) : TypeSafeMatcher<Document<*>>() {
    private var expected = setOf<String>()
    private var actual = setOf<String>()

    override fun matchesSafely(item: Document<*>): Boolean {
        expected =
                if (formatting == "_") setOf()
                else formatting.split(";")
                        .trim()
                        .toSet()
        actual = item.formatting
                .map(Any::toString)
                .toSet()
        return expected == actual
    }

    override fun describeTo(description: Description) {
        description.appendValue(expected)
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        mismatchDescription.appendText("was ").appendValue(actual)
        PrintUtils.println("Wrong document formatting", Color.RED)
    }
}