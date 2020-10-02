package zh.bookreader.services.search

import com.google.common.annotations.VisibleForTesting
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import zh.bookreader.services.IndexerService
import zh.bookreader.services.SearchService
import zh.bookreader.services.search.index.BookEntry
import zh.bookreader.services.search.index.IndexEntry
import zh.bookreader.services.util.SearchHit
import java.io.InputStream
import java.nio.file.Paths
import java.util.Scanner
import java.util.regex.Pattern

@Component
class SearchServiceImpl(
        @Qualifier("htmlIndexerService") private val indexer: IndexerService,
        @Value("\${zh.bookreader.search}") private val searchIsOn: Boolean
) : SearchService {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val parsedIdMap: MutableMap<Int, String> = mutableMapOf()
    private val parsedIndex: MutableMap<String, IndexEntry> = mutableMapOf()
    private val bookEntryPattern = Pattern.compile("(\\d+):(\\d+)\\[(.*)]?")
    private val indexFilePath = Paths.get(USER_HOME_PATH, SEARCH_INDEX_REL_PATH, INDEX_FILE_NAME)

    init {
        if (searchIsOn) loadIndex()
    }

    private fun loadIndex() {
        log.info("Starting index loading")
        indexer.index()

        val indexFile = indexFilePath.toFile()
        if (!indexFile.exists())
            log.info("Index file [{}] is not found. Cannot load index", indexFilePath)
        else {
            loadIndex(indexFile.inputStream())
            log.info("Index loading completed")
        }
    }

    @VisibleForTesting
    internal val idMap: Map<Int, String>
        get() = parsedIdMap.toMap()
    @VisibleForTesting
    internal val index: Map<String, IndexEntry>
        get() = parsedIndex.toMap()

    @VisibleForTesting
    internal fun loadIndex(input: InputStream) {
        val scanner = Scanner(input)
        while (scanner.hasNext()) {
            val line = scanner.nextLine()
            if (line == "#map")
                scanner.parseIdMap()
            if (line == "#index")
                scanner.parseIndex()
        }
    }

    private fun Scanner.parseIdMap() {
        parsedIdMap += nextLine().split(",")
                .map { it.split(":") }
                .map { pair -> pair[0].toInt() to pair[1] }
                .toMap()
    }

    private fun Scanner.parseIndex() {
        while (hasNext()) {
            val line = nextLine()
            if (!line.contains("=>"))
                continue
            val entry = line.split("=>")
            val word = entry[0]
            val indexEntry = IndexEntry()
            entry[1].split("]")
                .map {
                    with(bookEntryPattern.matcher(it)) {
                        if (find())
                            indexEntry.bookEntries += group(1).toInt() to BookEntry().parseData(group(2), group(3))
                    }
                }
            parsedIndex += word to indexEntry
        }
    }

    private fun BookEntry.parseData(score: String, chapters: String): BookEntry {
        this.score = score.toInt()
        this.chapters += chapters.split(",")
            .map {
                val tokens = it.split(":")
                tokens[0].toInt() to tokens[1].toInt()
            }
        return this
    }

    override fun find(query: List<String>?): List<SearchHit> {
        if (query == null || query.isEmpty())
            return emptyList()

        val wordsSet = query.toSet()
        return query.asSequence()
                .map { word -> word to index[word] }
                .filterNotNull()
                .map { (word, entry) -> word to entry.bookEntries }
                .filterNotNull()
                .flatMap { (word, bookEntries) -> bookEntries.toPairs().map { pair -> word to pair }.asSequence() }
                .groupBy { (_, bookIdToEntry) -> bookIdToEntry.first }
                .toPairs()
                .filter { (_, entry) -> entry.map { (word) -> word }.toSet() == wordsSet }
                .sortedByDescending { (_, bookEntries) -> bookEntries.map { it.second.second }.map(BookEntry::getScore).max() }
                .map { (id, entry) -> SearchHit().apply {
                    bookId = idMap[id]
                    chapterNums = toChapterNums(wordsSet, entry)
                } }
    }

    private fun toChapterNums(wordsSet: Set<String>, entry: List<Pair<String, Pair<Int, BookEntry>>>): List<Int> {
        return entry.map { it.first to it.second.second }
                .asSequence()
                .flatMap { (word, bookEntry) -> bookEntry.chapters.map { pair -> word to pair }.asSequence() }
                .groupBy { (_, chapterEntry) -> chapterEntry.key }
                .filter { (_, entry) -> entry.map { (word) -> word }.toSet() == wordsSet }
                .toPairs()
                .sortedByDescending { (_, chapterEntries) -> chapterEntries.map { it.second.value }.min() }
                .map { it.first }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <A, B> Sequence<Pair<A?, B?>>.filterNotNull(): Sequence<Pair<A, B>> = this
            .filter { it.first != null && it.second != null }
            .map { it as Pair<A, B> }

    private fun <A, B> Map<A, B>.toPairs(): List<Pair<A, B>> = this.entries.map { it.key to it.value }
}
