package zh.bookreader.services.htmlservices

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.model.documents.Chapter
import java.io.File

@DisplayName("Test ChapterProxy")
internal class ChapterProxyTest {
    companion object {
        private const val CHAPTER_FILE_NAME = "ch01.html"
        private const val CHAPTER_ID = "ch01"
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "JAVA_CLASS_ON_COMPANION")
        private val BOOK_DIR = this.javaClass.classLoader
                .getResource("library/book-one")
                .toURI()
                .run { File(this) }
        private val CHAPTER_CONTENT = getChapter01Content()
    }

    private val chapter: Chapter = ChapterProxy(BOOK_DIR, CHAPTER_FILE_NAME);

    @Test
    @DisplayName("Test ChapterProxy returns its id")
    internal fun testGetId() {
        assertThat(chapter.id, `is`(equalTo(CHAPTER_ID)))
    }

    @Test
    @DisplayName("Test ChapterProxy returns the content")
    internal fun testProxyCanFetchContent() {
        val content = chapter.content
        assertThat(content, `is`(equalTo(CHAPTER_CONTENT)))
    }
}