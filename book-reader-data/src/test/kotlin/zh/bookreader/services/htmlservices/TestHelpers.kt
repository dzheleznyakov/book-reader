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

