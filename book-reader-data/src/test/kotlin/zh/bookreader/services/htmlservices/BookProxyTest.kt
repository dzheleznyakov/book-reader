package zh.bookreader.services.htmlservices

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.model.Book
import zh.bookreader.model.Document
import zh.bookreader.model.DocumentType.*
import zh.bookreader.model.EnclosingDocument
import zh.bookreader.model.TextDocument
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
            assertEquals(listOf("Author 1.1", "Author 1.2"), authors)
            assertEquals("January 1970", releaseDate)
            assertEquals(listOf("Test 1", "Test 2"), topics)
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
        val text0: TextDocument = TextDocument.builder(TEXT)
                .withContent("A zero paragraph goes here")
                .build()
        val text1: TextDocument = TextDocument.builder(TEXT)
                .withContent("Paragraph 1 content")
                .build()
        val text2: TextDocument = TextDocument.builder(TEXT)
                .withContent("Paragraph 2 content")
                .build()
        val text3: TextDocument = TextDocument.builder(TEXT)
                .withContent("Paragraph 3 content")
                .build()

        val par0: EnclosingDocument = getParagraph(text0)
        val par1: EnclosingDocument = getParagraph(text1)
        val par2: EnclosingDocument = getParagraph(text2)
        val par3: EnclosingDocument = getParagraph(text3)

        val block: EnclosingDocument = EnclosingDocument.builder(BLOCK)
                .withContent(listOf(par1, par2, par3))
                .withMetadata(mapOf("&tag" to "div"))
                .build()

        val expectedDescription: List<Document<*>> = listOf(par0, block)

        assertEquals(expectedDescription, book.description)
    }

    private fun getParagraph(text0: TextDocument): EnclosingDocument {
        return EnclosingDocument.builder(PARAGRAPH)
                .withContent(listOf(text0))
                .withMetadata(mapOf("&tag" to "p"))
                .build()
    }
}