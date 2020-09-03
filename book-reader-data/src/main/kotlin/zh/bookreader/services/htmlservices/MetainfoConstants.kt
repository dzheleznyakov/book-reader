package zh.bookreader.services.htmlservices

const val BOOK_TITLE_PREFIX = "Book Title="
const val AUTHORS_PREFIX = "Authors="
const val RELEASE_DATE_PREFIX = "Release Date="
const val TOPICS_PREFIX = "Topics="
const val COVER_PAGE_IMAGE_PREFIX = "Cover Page Image="
const val CHAPTER_FILES_PREFEX = "Chapter Files="
val RESOURCE_LABEL_REGEX = Regex("Resource #(\\d) Label=(.*)")
val RESOURCE_VALUE_REGEX = Regex("Resource #(\\d) Value=(.*)")