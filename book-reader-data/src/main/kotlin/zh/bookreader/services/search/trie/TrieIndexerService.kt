package zh.bookreader.services.search.trie

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.model.documents.Book
import zh.bookreader.model.documents.Document
import zh.bookreader.model.documents.EnclosingDocument
import zh.bookreader.model.documents.TextDocument
import zh.bookreader.services.BookService
import zh.bookreader.services.IndexerService
import zh.bookreader.services.search.AbstractIndexerService
import zh.bookreader.services.search.IndexEntry
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import zh.bookreader.services.search.table.SearchConfig
import zh.bookreader.services.search.table.TRIE_INDEX_FILE_NAME
import zh.bookreader.services.search.table.getStopWords
import zh.bookreader.services.search.trie.encoders.Encoder
import zh.bookreader.services.search.trie.encoders.Encoders
import zh.bookreader.utils.collections.Trie
import java.io.DataOutputStream
import java.io.OutputStream
import java.util.LinkedList
import javax.annotation.PostConstruct

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class TrieIndexerService(
    bookService: BookService,
    searchConfig: SearchConfig,
    private val encoders: Encoders
) : IndexerService,
    AbstractIndexerService(searchConfig, TRIE_INDEX_FILE_NAME, bookService)
{
    private val log = LoggerFactory.getLogger(this.javaClass)
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private val stopWords = getStopWords()
    private val idMap = mutableMapOf<Int, String>()
    private val trie = Trie<IndexEntry>()

    @PostConstruct
    fun init() {
        Thread(this::index).apply {
            name = "trieIndexer"
        }.start()
    }

    override fun index(output: OutputStream, books: List<Book>) {
        log.info("Staring building index from the library")
        books.buildIndex()
        log.info("Persisting the built index")
        val dataStream = DataOutputStream(output)
//        dataStream.writeIdMap()
//        dataStream.writeTrie()
        dataStream.close()
        log.info("Finished persisting the library index")
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

    private fun List<Document<*>>.getText(): List<String> = mutableListOf<String>().apply {
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
        val entry = if (!trie.contains(word) || trie.get(word).isEmpty())
                        IndexEntry().apply { trie.put(word, this) }
                    else
                        trie.get(word)[0]
        entry.put(bookNum, chNum)
    }

    private fun DataOutputStream.writeIdMap() {
        (encoders.get(idMap::class.java) as Encoder<Map<*, *>>)
            .encode(this, idMap, encoders)
        flush()
    }

    private fun DataOutputStream.writeTrie() {
        (encoders.get(trie::class.java) as Encoder<Trie<*>>)
            .encode(this, trie, encoders)
        flush()
    }

    override fun cleanUp() {
        idMap.clear()
//        trie = Trie()
    }
}