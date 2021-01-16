package zh.bookreader.services.search.table

import com.google.common.annotations.VisibleForTesting
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import zh.bookreader.model.documents.Book
import zh.bookreader.model.documents.Document
import zh.bookreader.model.documents.EnclosingDocument
import zh.bookreader.model.documents.TextDocument
import zh.bookreader.services.BookService
import zh.bookreader.services.IndexerService
import zh.bookreader.services.search.AbstractIndexerService
import zh.bookreader.services.search.IndexEntry
import java.io.OutputStream
import java.nio.file.Paths
import java.util.LinkedList
import java.util.concurrent.atomic.AtomicInteger

@Component("htmlIndexerService")
class HtmlIndexerService(
        bookService: BookService,
        searchConfig: SearchConfig
) : IndexerService,
    AbstractIndexerService(searchConfig, TABLE_INDEX_FILE_NAME, bookService)
{
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private val stopWords = getStopWords()

    private val idMap = mutableMapOf<Int, String>()

    private val index = mutableMapOf<String, IndexEntry>()

    override fun cleanUp() {
        idMap.clear()
        index.clear()
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
    override fun index(output: OutputStream, books: List<Book>) {
        log.info("Staring building index from the library")
        books.buildIndex()
        log.info("Persisting the built index")
        output.writeIdMap()
                .writeIndex()
                .flush()
    }

    private fun List<Book>.buildIndex() = indices.forEach { n ->
        this[n].apply {
            try {
                log.info("Indexing book[${id}] [${n + 1}/${this@buildIndex.size}]")
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
            text.split(Regex("\\W|\\d|_"))
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { it.toLowerCase() }
                    .filter { !stopWords.contains(it) }
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