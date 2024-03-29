package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.documents.Document
import zh.bookreader.model.documents.DocumentType
import zh.bookreader.testutils.PrintUtils

internal class DocIsHorizontalRule() : TypeSafeMatcher<Document<*>>() {
    override fun matchesSafely(item: Document<*>): Boolean {
        return item.documentType == DocumentType.HORIZONTAL
    }

    override fun describeTo(description: Description) {
        description.appendValue(DocumentType.HORIZONTAL)
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        mismatchDescription.appendText("was ").appendValue(item.documentType)
        PrintUtils.println("Wrong document type", PrintUtils.Color.RED)
    }
}