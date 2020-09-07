package zh.bookreader.services.htmlservices

import zh.bookreader.model.Book
import zh.bookreader.model.Document
import zh.bookreader.model.EnclosingDocument
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

class BookProxy(private val bookDir: File) : Book() {
    private var coverImagePath: String = ""
    private val res = mutableMapOf<String, Pair<String, String>>()

    init {
        id = bookDir.name
        Paths.get(bookDir.absolutePath, "metainfo.txt")
                .toFile()
                .readLines(StandardCharsets.UTF_8)
                .forEach { line -> line.parseMetainfoLine() }
        buildResources()
    }

    private fun String.parseMetainfoLine() = when {
        isBookTitle() -> { this@BookProxy.title = getBookTitle() }
        isAuthors() -> { this@BookProxy.authors = getAuthors() }
        isReleaseDate() -> { this@BookProxy.releaseDate = getReleaseDate() }
        isTopics() -> { this@BookProxy.topics = getTopics() }
        isCoverPageImage() -> { this@BookProxy.coverImagePath = getCoverPageImage() }
        isResourceLabel() -> { cacheResourceLabel() }
        isResourceValue() -> { cacheResourceValue() }
        isChapters() -> { this@BookProxy.chapters = ChapterListProxy(getChapters()) }
        else -> {}
    }

    private fun String.isBookTitle() = startsWith(BOOK_TITLE_PREFIX)
    private fun String.getBookTitle() = substring(BOOK_TITLE_PREFIX.length)

    private fun String.isAuthors() = startsWith(AUTHORS_PREFIX)
    private fun String.getAuthors() = getAsList(AUTHORS_PREFIX)

    private fun String.isReleaseDate() = startsWith(RELEASE_DATE_PREFIX)
    private fun String.getReleaseDate() = substring(RELEASE_DATE_PREFIX.length)

    private fun String.isTopics() = startsWith(TOPICS_PREFIX)
    private fun String.getTopics() = getAsList(TOPICS_PREFIX)

    private fun String.isResourceLabel() = matches(RESOURCE_LABEL_REGEX)
    private fun String.cacheResourceLabel() {
        getResourceData(RESOURCE_LABEL_REGEX)?.apply { res[first] = second to (res[first]?.second ?: "") }
    }

    private fun String.isResourceValue() = matches(RESOURCE_VALUE_REGEX)
    private fun String.cacheResourceValue() {
        getResourceData(RESOURCE_VALUE_REGEX)?.apply { res[first] = (res[first]?.first ?: "") to second }
    }

    private fun String.isCoverPageImage() = startsWith(COVER_PAGE_IMAGE_PREFIX)
    private fun String.getCoverPageImage() = substring(COVER_PAGE_IMAGE_PREFIX.length)

    private fun String.isChapters() = startsWith(CHAPTER_FILES_PREFEX)
    private fun String.getChapters() = substring(CHAPTER_FILES_PREFEX.length).split(",")

    private fun String.getAsList(prefix: String) = substring(prefix.length).split(";")
            .map { it.trim() }
            .filter { it != "" }

    private fun String.getResourceData(regex: Regex): Pair<String, String>? {
        val matchEntire = regex.matchEntire(this) ?: return null
        val groups = matchEntire.groups
        val num = groups[1]?.value ?: return null
        val label = groups[2]?.value ?: return null
        return num to label
    }

    private fun buildResources() {
        this.resources = res.values.toMap()
    }

    override fun getImage(): Array<Byte> {
        if (super.getImage().isEmpty())
            super.setImage(getImageBytes())
        return super.getImage()
    }

    private fun getImageBytes(): Array<Byte> {
        val imageFile = bookDir.toPath().resolve(coverImagePath).toFile()
        val isReadable = imageFile.exists() && imageFile.isFile
        return if (isReadable) imageFile.readBytes().toTypedArray()
        else arrayOf()
    }

    override fun getDescription(): List<Document<*>> {
        val descrFilename = "$bookDir/cover.html"
        val parser = HtmlDocumentParser(descrFilename)
        val fileText = File(descrFilename).run { if (exists()) readText().trim() else "" }
        val doc = parser.parseFileContent("<div>$fileText</div>", bookDir.absolutePath) as EnclosingDocument
        return doc.content
    }
}