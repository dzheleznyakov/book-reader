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
}
