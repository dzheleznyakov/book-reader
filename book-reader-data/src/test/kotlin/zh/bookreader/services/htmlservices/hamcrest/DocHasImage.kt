package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.Document
import zh.bookreader.model.ImageDocument
import zh.bookreader.testutils.PrintUtils

internal class DocHasImage(private val bytes: Array<Byte>) : TypeSafeMatcher<Document<*>>() {
    override fun matchesSafely(item: Document<*>) = item is ImageDocument && item.content.contentEquals(bytes)

    override fun describeTo(description: Description) {
        description.appendValue(bytes.size)
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        if (item is ImageDocument) {
            mismatchDescription.appendText("was ").appendValue(item.content.size)
            PrintUtils.println("Wrong image bytes (displaying length only)", PrintUtils.Color.RED)
        } else {
            PrintUtils.println("Wrong document type: ImageDocument required", PrintUtils.Color.RED)
        }
    }
}