package zh.bookreader.services.search

import java.io.File

const val USER_HOME_KEY = "user.home"
const val SEARCH_INDEX_REL_PATH = ".zh/BookParser/index"
const val LIBRARY_REL_PATH = ".zh/BookParser/savedContent"

val USER_HOME_PATH = System.getProperty(USER_HOME_KEY) ?: ""
const val INDEX_FILE_NAME = "index.zhi"

const val STOP_WORDS_LIST_PATH = "search-index/stopwords.txt"

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun <E : Any> E.getStopWords() = this.javaClass.classLoader.getResource(STOP_WORDS_LIST_PATH)
        .toURI()
        .run { File(this) }
        .run {
            this.readLines()
                    .filter { it.isNotEmpty() }
                    .toHashSet()
        }
