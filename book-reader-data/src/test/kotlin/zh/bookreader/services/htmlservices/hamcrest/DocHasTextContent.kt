package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import zh.bookreader.model.Document
import zh.bookreader.model.DocumentType
import zh.bookreader.model.EnclosingDocument
import zh.bookreader.model.TextDocument
import zh.bookreader.testutils.PrintUtils
import zh.bookreader.testutils.PrintUtils.Color

internal class DocHasTextContent(private val text: String) : TypeSafeMatcher<Document<*>>() {
    override fun matchesSafely(item: Document<*>) = when(item) {
        is EnclosingDocument -> matchEnclosingDocument(item)
        is TextDocument -> matchTextDocument(item)
        else -> throw IllegalArgumentException("Should never come to this point")
    }

    private fun matchEnclosingDocument(item: EnclosingDocument) =
        item.content == if (text == "_") listOf<TextDocument>()
                        else listOf(TextDocument
                                .builder(DocumentType.TEXT)
                                .withContent(text)
                                .build())

    private fun matchTextDocument(item: TextDocument) = item.content == text

    override fun describeTo(description: Description) {
        description.appendValue(text)
    }

    override fun describeMismatchSafely(item: Document<*>, mismatchDescription: Description) {
        mismatchDescription.appendText("was ").appendValue(item.content)
        PrintUtils.println("Wrong document content", Color.RED)
    }
}