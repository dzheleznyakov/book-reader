package zh.bookreader.services.htmlservices

import zh.bookreader.model.documents.Chapter
import zh.bookreader.model.documents.DocumentType
import zh.bookreader.model.documents.EnclosingDocument
import java.io.File

class ChapterProxy(
        private val bookDir: File,
        private val filename: String,
        index: Int
) : Chapter() {
    init {
        this.id = filename.replace(".html", "")
        this.index = index
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