package zh.bookreader.services.htmlservices

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.model.Book
import zh.bookreader.model.Document
import java.io.File

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
internal class BookProxyTest {
    private val bookWithNoCoverPath = "erroneousLibrary/book-no-cover"
    private val bookPath = "library/book-one"
    private val coverImagePath = "media/square.jpg"

    private var book: Book = getBookProxy(bookPath)

    private fun getBookProxy(libPath: String): BookProxy {
        val bookDir = this.javaClass.classLoader
                .getResource(libPath)
                .toURI()
                .run { File(this) }
        return BookProxy(bookDir)
    }

    @Test
    @DisplayName("Test BookProxy reading metadata")
    internal fun testBookMetadata() {
        with(book) {
            assertEquals("Book Title One", title)
            assertEquals(listOf("Author One-One", "Author One-Two", "Author Both"), authors);
            assertEquals("January 1970", releaseDate)
            assertEquals(listOf("Topic One", "Topic Two"), topics)
            assertEquals(mapOf(
                    "Mock resource 1" to "42",
                    "Mock resource 2" to "forty two"), resources)
        }
    }

    @Test
    @DisplayName("Test BookProxy fetches the cover image")
    internal fun testCoverImage() {
        val imageUri = this.javaClass.classLoader
                .getResource("${bookPath}/${coverImagePath}")
                .toURI()

        assertArrayEquals(File(imageUri).readBytes().toTypedArray(), book.image)
    }

    @Test
    @DisplayName("Test BookProxy tries to fetch non-existing cover image")
    internal fun testCoverImageNotExist() {
        book = getBookProxy(bookWithNoCoverPath)

        assertArrayEquals(arrayOf<Byte>(), book.image)
    }

    @Test
    @DisplayName("Test BookProxy has right chapters")
    internal fun testToc() {
        val expectedChapterNames: List<String> = listOf("preface.html", "ch01.html", "ch02.html", "app.html")
        val actualChapterNames = book.chapters.map { it.name }
        assertEquals(expectedChapterNames, actualChapterNames)
    }

    @Test
    @DisplayName("Test BookProxy can read book description")
    internal fun testBookDescription() {
        val expectedDescription: List<Document<*>> = getBook1Description()

        assertEquals(expectedDescription, book.description)
    }
}