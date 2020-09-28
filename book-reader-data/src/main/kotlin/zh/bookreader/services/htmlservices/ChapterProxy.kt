package zh.bookreader.services.htmlservices

import zh.bookreader.model.Chapter
import zh.bookreader.model.DocumentType
import zh.bookreader.model.EnclosingDocument
import java.io.File

class ChapterProxy(private val bookDir: File, private val filename: String) : Chapter() {
    init {
        this.id = filename.replace(".html", "")
    }

    override fun getContent() = "$bookDir/$filename"
            .run { HtmlDocumentParser(this) }
            .run {
                val doc = parse()
                if (doc is EnclosingDocument) doc
                else EnclosingDocument.builder(DocumentType.SECTION)
                        .withContent(listOf(doc))
                        .build()
            }
}