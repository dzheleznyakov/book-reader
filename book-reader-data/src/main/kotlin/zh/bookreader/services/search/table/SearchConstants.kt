package zh.bookreader.services.search.table

import zh.bookreader.services.utils.readLines
import java.util.Scanner

const val USER_HOME_KEY = "user.home"
const val SEARCH_INDEX_REL_PATH = ".zh/BookParser/index"
const val LIBRARY_REL_PATH = ".zh/BookParser/savedContent"

val USER_HOME_PATH = System.getProperty(USER_HOME_KEY) ?: ""
const val INDEX_FILE_NAME = "index.zhi"

const val STOP_WORDS_LIST_PATH = "search-index/stopwords.txt"

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun <E : Any> E.getStopWords(): Set<String> {
    val stopWordsAsInputStream = this.javaClass.classLoader.getResourceAsStream(STOP_WORDS_LIST_PATH) ?: return setOf()
    return Scanner(stopWordsAsInputStream)
            .readLines()
            .filter { it.isNotEmpty() }
            .toSet()
}
