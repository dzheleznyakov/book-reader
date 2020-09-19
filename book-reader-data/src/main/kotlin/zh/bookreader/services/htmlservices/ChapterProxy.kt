package zh.bookreader.services.htmlservices

import zh.bookreader.model.Chapter
import zh.bookreader.model.EnclosingDocument
import java.io.File

class ChapterProxy(private val bookDir: File, private val filename: String) : Chapter() {
    init {
        this.id = filename.replace(".html", "")
    }

    override fun getContent() = "$bookDir/$filename"
            .run { HtmlDocumentParser(this) }
            .run { parse() as EnclosingDocument }
}