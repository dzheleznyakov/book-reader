package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.documents.Document
import zh.bookreader.model.documents.DocumentType
import zh.bookreader.testutils.PrintUtils

internal class DocIsBreakRule() : TypeSafeMatcher<Document<*>>() {
    override fun matchesSafely(item: Document<*>): Boolean {
        return item.documentType == DocumentType.BREAK
    }

    override fun describeTo(description: Description) {
        description.appendValue(DocumentType.BREAK)
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        mismatchDescription.appendText("was ").appendValue(item.documentType)
        PrintUtils.println("Wrong document type", PrintUtils.Color.RED)
    }
}