package zh.bookreader.services.search.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class MatchesIndex(expected: String) : TypeSafeMatcher<String>() {
    companion object {
        const val INDEX_ENTRY_DELIM = "=>"
    }
    private val expectedMap = expected.toMap()
    private val expectedLines = expected.toLines()

    private fun String.toLines() = split("\n").filter { !it.contains(INDEX_ENTRY_DELIM) }.toSet()
    private fun String.toMap() = split("\n").filter { it.contains(INDEX_ENTRY_DELIM) }
            .map {
                val tokens = it.split(INDEX_ENTRY_DELIM)
                tokens[0] to tokens[1].split("]").toSet()
            }
            .toMap()

    override fun matchesSafely(item: String) = item.toLines() == expectedLines
            && item.toMap() == expectedMap

    override fun describeTo(description: Description) {
        description.appendValue(expectedLines.toString() + "\n" + expectedMap.toString())
    }

    override fun describeMismatchSafely(item: String, mismatchDescription: Description) {
        mismatchDescription.appendText("was ").appendValue(item.toLines().toString() + "\n" + item.toMap().toString())
    }
}