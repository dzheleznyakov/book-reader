package zh.bookreader.services.search.index

import com.google.common.annotations.VisibleForTesting
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component
import zh.bookreader.model.Book
import zh.bookreader.model.Document
import zh.bookreader.model.EnclosingDocument
import zh.bookreader.model.TextDocument
import zh.bookreader.services.BookService
import zh.bookreader.services.IndexerService
import zh.bookreader.services.search.INDEX_FILE_NAME
import zh.bookreader.services.search.LIBRARY_REL_PATH
import zh.bookreader.services.search.SEARCH_INDEX_REL_PATH
import zh.bookreader.services.search.STOP_WORDS_LIST_PATH
import zh.bookreader.services.search.USER_HOME_PATH
import java.io.File
import java.io.OutputStream
import java.nio.file.Paths
import java.util.LinkedList
import java.util.concurrent.atomic.AtomicInteger

@Component
@Slf4j
class HtmlIndexerService(private val bookService: BookService) : IndexerService {
    private var pathToIndex = Paths.get(USER_HOME_PATH, SEARCH_INDEX_REL_PATH)
    private var pathToLibrary = Paths.get(USER_HOME_PATH, LIBRARY_REL_PATH)
    private var pathToIndexFile = pathToIndex.resolve(INDEX_FILE_NAME)

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private val stopwords = this.javaClass.classLoader.getResource(STOP_WORDS_LIST_PATH)
            .toURI()
            .run { File(this) }
            .run {
                this.readLines()
                        .filter { it.isNotEmpty() }
                        .toHashSet()
            }
    private val idMap = mutableMapOf<Int, String>()
    private val index = mutableMapOf<String, IndexEntry>()

    override fun index() {
        if (shouldIndex())
            doIndex()
    }

    private fun doIndex() {
        val indexFile = pathToIndexFile.toFile()
        pathToIndex.toFile().mkdirs()
        if (indexFile.exists())
            indexFile.delete()
        indexFile.createNewFile()
        val output = indexFile.outputStream()
        val books = bookService.findAll() ?: listOf()
        index(output, books)
    }

    @VisibleForTesting
    internal fun setPathToIndex(pathToIndex: String) {
        this.pathToIndex = Paths.get(pathToIndex)
    }

    @VisibleForTesting
    internal fun setPathToLibrary(pathToLibrary: String) {
        this.pathToLibrary = Paths.get(pathToLibrary)
    }

    @VisibleForTesting
    internal fun shouldIndex(): Boolean {
        val libraryFile = pathToLibrary.toFile()
        if (!libraryFile.exists())
            return false

        val indexFile = pathToIndex.resolve(INDEX_FILE_NAME).toFile()
        return !indexFile.exists()
                || libraryFile.lastModified() >= indexFile.lastModified()
    }

    @VisibleForTesting
    internal fun index(output: OutputStream, books: List<Book>) {
        books.buildIndex()
        output.writeIdMap()
                .writeIndex()
                .flush()
    }

    private fun List<Book>.buildIndex() = indices.forEach { n ->
        this[n].apply {
            try {
                println("Indexing book[${id}] [${n + 1}/${this@buildIndex.size}]")
                idMap[n] = id
                indexTitle(n)
                indexDescription(n)
                indexChapters(n)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
    private fun Book.indexTitle(bookNum: Int) = indexText(title, bookNum, -1)
    private fun Book.indexDescription(bookNum: Int) = description.getText().forEach { indexText(it, bookNum, -1) }
    private fun Book.indexChapters(bookNum: Int) = chapters.indices
            .forEach { i ->
                val chapter = chapters[i]
                listOf(chapter.content).getText().forEach { indexText(it, bookNum, i )}
            }

    private fun List<Document<*>>.getText() = mutableListOf<String>().apply {
        val stack = LinkedList<Document<*>>(this@getText)
        while (!stack.isEmpty()) {
            val doc = stack.pop()
            if (doc is TextDocument) this.add(doc.content)
            else if (doc is EnclosingDocument) doc.content.forEach(stack::push)
        }
    }

    private fun indexText(text: String, bookNum: Int, chNum: Int) =
            text.split(" ", ",", ".", ";", ":", "-", "\"", "\\", "?", "!", "/", "|", "+", "*", "^", "%", "'")
                    .filter { it.isNotEmpty() }
                    .map { it.toLowerCase() }
                    .filter { !stopwords.contains(it) }
                    .forEach { word -> indexWord(word, bookNum, chNum) }

    private fun indexWord(word: String, bookNum: Int, chNum: Int) {
        val entry = index[word] ?: IndexEntry().apply { index[word] = this }
        entry.put(bookNum, chNum)
    }

    private fun OutputStream.writeIdMap(): OutputStream = write("#map").apply {
        idMap.map { (num, id) -> "$num:$id" }
                .joinToString(separator = ",")
                .apply { write("\n").write(this) }
    }

    private val count = AtomicInteger(0);
    private fun OutputStream.writeIndex(): OutputStream = write("\n#index").apply {
        index.map { (word, entry) -> word to entry }
                .asSequence()
                .map { (word, entry) -> "\n$word=>${entry}" }
                .forEach {
                    println("Writing in index [${count.incrementAndGet()}/${index.size}]")
                    write(it).flush()
                }
    }
    private fun OutputStream.write(s: String): OutputStream = apply { write(s.toByteArray()) }

}