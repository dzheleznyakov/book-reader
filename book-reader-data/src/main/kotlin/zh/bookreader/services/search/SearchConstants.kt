package zh.bookreader.services.search

const val USER_HOME_KEY = "user.home"
const val SEARCH_INDEX_REL_PATH = ".zh/BookParser/index"
const val LIBRARY_REL_PATH = ".zh/BookParser/savedContent"

val USER_HOME_PATH = System.getProperty(USER_HOME_KEY) ?: ""
const val INDEX_FILE_NAME = "index.zhi"

const val STOP_WORDS_LIST_PATH = "search-index/stopwords.txt"
