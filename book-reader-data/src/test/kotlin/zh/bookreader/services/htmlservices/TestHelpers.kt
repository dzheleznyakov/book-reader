package zh.bookreader.services.htmlservices

import zh.bookreader.model.Document
import zh.bookreader.model.DocumentType
import zh.bookreader.model.EnclosingDocument
import zh.bookreader.model.TextDocument
import java.io.File
import java.net.URI

internal fun List<String>.trim(): List<String> = map(String::trim)

internal fun String.toTextDoc(): TextDocument? =
        if (this == "_") null
        else TextDocument.builder(DocumentType.TEXT)
                .withContent(this)
                .build()

internal fun TextDocument.wrapInPar(): Document<*> =
        EnclosingDocument.builder(DocumentType.PARAGRAPH)
                .withContent(listOf(this))
                .withMetadata(mapOf("&tag" to "p"))
                .build()

internal fun URI.toBytes() = File(this).readBytes().toTypedArray()

internal fun Pair<String, String>.toHtmlString(): String {
        val classAttribute = if (second == "_") "" else " class=\"$second\""
        return "<$first$classAttribute>mock text</$first>"
}

internal const val TAG_TO_IGNORE = "<div class=\"annotator-modal-wrapper annotator-delete-confirm-modal\"></div>"

fun getBook1Description(): List<Document<*>> {
        val text0: TextDocument = TextDocument.builder(DocumentType.TEXT)
                .withContent("A zero paragraph goes here")
                .build()
        val text1: TextDocument = TextDocument.builder(DocumentType.TEXT)
                .withContent("Paragraph 1 content")
                .build()
        val text2: TextDocument = TextDocument.builder(DocumentType.TEXT)
                .withContent("Paragraph 2 content")
                .build()
        val text3: TextDocument = TextDocument.builder(DocumentType.TEXT)
                .withContent("Paragraph 3 content")
                .build()

        val par0: EnclosingDocument = getParagraph(text0)
        val par1: EnclosingDocument = getParagraph(text1)
        val par2: EnclosingDocument = getParagraph(text2)
        val par3: EnclosingDocument = getParagraph(text3)

        val block: EnclosingDocument = EnclosingDocument.builder(DocumentType.BLOCK)
                .withContent(listOf(par1, par2, par3))
                .withMetadata(mapOf("&tag" to "div"))
                .build()

        val expectedDescription: List<Document<*>> = listOf(par0, block)
        return expectedDescription
}

internal fun getParagraph(text0: TextDocument): EnclosingDocument {
        return EnclosingDocument.builder(DocumentType.PARAGRAPH)
                .withContent(listOf(text0))
                .withMetadata(mapOf("&tag" to "p"))
                .build()
}