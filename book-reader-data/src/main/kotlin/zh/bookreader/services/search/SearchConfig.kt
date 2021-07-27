package zh.bookreader.services.search

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.utils.readLines
import java.util.Scanner

interface SearchConfig {
    val searchIndexRelPath: String
    val libraryRelPath: String
    val userHomePath: String
    val indexFileName: String
    fun <E : Any> E.getStopWords(): Set<String>
}

//@Component
//class SearchConfigImpl : SearchConfig {
abstract class BaseSearchConfig : SearchConfig {
    val USER_HOME_KEY = "user.home"
    val STOP_WORDS_LIST_PATH = "search-index/stopwords.txt"

    @Value("\${zh.bookreader.library.index.path}")
    private lateinit var _searchIndexRelPath: String
    override val searchIndexRelPath: String
        get() = _searchIndexRelPath

    @Value("\${zh.bookreader.library.path}")
    private lateinit var _libraryRelPath: String
    override val libraryRelPath: String
        get() = _libraryRelPath

    override val userHomePath: String
        get() = System.getProperty(USER_HOME_KEY) ?: ""

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun <E : Any> E.getStopWords(): Set<String> {
        val stopWordsAsInputStream = this.javaClass.classLoader.getResourceAsStream(STOP_WORDS_LIST_PATH) ?: return setOf()
        return Scanner(stopWordsAsInputStream)
                .readLines()
                .filter { it.isNotEmpty() }
                .toSet()
    }
}

@Component
@Profile(SEARCH_PERSISTENCE_TABLE_PROFILE)
class SearchConfigImpl : BaseSearchConfig() {
    override val indexFileName: String
        get() = "index.zhi"
}

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class TrieSearchConfigImpl : BaseSearchConfig() {
    override val indexFileName: String
        get() = "tr_index.zhi"
}
