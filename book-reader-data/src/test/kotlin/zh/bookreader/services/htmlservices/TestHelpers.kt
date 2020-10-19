package zh.bookreader.services.htmlservices

import zh.bookreader.model.documents.Chapter
import zh.bookreader.model.documents.Document
import zh.bookreader.model.documents.DocumentFormatting
import zh.bookreader.model.documents.DocumentType
import zh.bookreader.model.documents.EnclosingDocument
import zh.bookreader.model.documents.TextDocument
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
        val text0 = getText("A zero paragraph goes here")
        val text1 = getText("Paragraph 1 content")
        val text2 = getText("Paragraph 2 content")
        val text3 = getText("Paragraph 3 content")

        val par0 = getParagraph(text0)
        val par1 = getParagraph(text1)
        val par2 = getParagraph(text2)
        val par3 = getParagraph(text3)

        val block = getBlock(listOf(par1, par2, par3))

        return listOf(par0, block)
}

fun getBook2Description(): List<Document<*>> {
        val text = getText("Book two description")
        val par = getParagraph(text)
        return listOf(par)
}

fun getChapter01Content(): Document<List<Document<*>>> {
        val title: EnclosingDocument = EnclosingDocument.builder(DocumentType.INLINED)
                .withContent(getText("Chapter Title"))
                .withFormatting(listOf(DocumentFormatting.TITLE))
                .withMetadata(mapOf("&tag" to "h1"))
                .build()

        val par11 = getParagraph(getText(("Paragraph 1.1.")))
        val par12 = getParagraph(getText(("Paragraph 1.2.")))
        val par21 = getParagraph(getText(("Paragraph 2.1.")))

        val block1 = getBlock(listOf(par11, par12))
        val block2 = getBlock(listOf(par21))

        return EnclosingDocument.builder(DocumentType.SECTION)
                .withContent(listOf(title, block1, block2))
                .withMetadata(mapOf("&tag" to "section"))
                .build()
}

fun getBlock(content: List<EnclosingDocument>): EnclosingDocument {
        return EnclosingDocument.builder(DocumentType.BLOCK)
                .withContent(content)
                .withMetadata(mapOf("&tag" to "div"))
                .build()
}

fun getHeader(text: String): EnclosingDocument =
        EnclosingDocument.builder(DocumentType.BLOCK)
                .withContent(getText(text))
                .withFormatting(setOf(DocumentFormatting.TITLE))
                .withMetadata(mapOf("&tag" to "h1"))
                .build()

fun getParagraph(textDoc: TextDocument): EnclosingDocument =
        EnclosingDocument.builder(DocumentType.PARAGRAPH)
                .withContent(listOf(textDoc))
                .withMetadata(mapOf("&tag" to "p"))
                .build()

fun getParagraph(text: String): EnclosingDocument =
        EnclosingDocument.builder(DocumentType.PARAGRAPH)
                .withContent(getText(text))
                .withMetadata(mapOf("&tag" to "p"))
                .build()

fun getText(content: String): TextDocument =
        TextDocument.builder(DocumentType.TEXT)
                .withContent(content)
                .build()

fun getChapter(document: EnclosingDocument) = Chapter().apply { content = document }