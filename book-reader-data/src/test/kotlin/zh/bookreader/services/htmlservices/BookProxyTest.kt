package zh.bookreader.services.htmlservices

import com.google.common.collect.ImmutableList
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.model.documents.Book
import zh.bookreader.model.documents.Document
import zh.bookreader.model.documents.EnclosingDocument
import zh.bookreader.model.documents.TextDocument
import java.io.File
import java.nio.file.Paths

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@DisplayName("Test BookProxy")
internal class BookProxyTest {
    companion object {
        private const val BOOK_WITH_NO_COVER_PATH = "erroneousLibrary/book-no-cover"
        private const val BOOK_1_PATH = "library/book-one"
        private const val BOOK_2_PATH = "library/book-two"
        private const val PAGE_DESCRIPTION_FILE_NAME = "_main_page_description.html"
        private const val COVER_IMAGE_FILE = "media/square.jpg"
        private const val COVER_FILE = "cover.html"
    }

    private var book: Book = getBookProxy(BOOK_1_PATH)

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
                .getResource("${BOOK_1_PATH}/${COVER_IMAGE_FILE}")
                .toURI()

        assertArrayEquals(File(imageUri).readBytes().toTypedArray(), book.image)
    }

    @Test
    @DisplayName("Test BookProxy tries to fetch non-existing cover image")
    internal fun testCoverImageNotExist() {
        book = getBookProxy(BOOK_WITH_NO_COVER_PATH)

        assertArrayEquals(arrayOf<Byte>(), book.image)
    }

    @Test
    @DisplayName("Test BookProxy has right chapters")
    internal fun testToc() {
        val expectedChapterIds: List<String> = listOf("preface", "ch01", "ch02", "app")
        val actualChapterIds = book.chapters.map { it.id }
        assertEquals(expectedChapterIds, actualChapterIds)
    }

    @Test
    @DisplayName("Test BookProxy can read book description")
    internal fun testBookDescription() {
        val expectedDescription: List<Document<*>> = getBook1Description()

        assertEquals(expectedDescription, book.description)
    }

    @Test
    @DisplayName("If the _main_page_description.html does not exist, then book description is read from cover.html")
    internal fun testBookDescriptionFallsBackToCoverFile() {
        val book1FolderPath = BOOK_1_PATH.toPath()

        val descriptionFile = book1FolderPath.resolve(PAGE_DESCRIPTION_FILE_NAME).toFile()
        assertFalse(descriptionFile.exists())

        val coverFile = book1FolderPath.resolve(COVER_FILE).toFile()
        assertTrue(coverFile.exists())

        val coverFileContent = coverFile.getContent()

        val allDescriptionTextIsInCoverFile = getBookProxy(BOOK_1_PATH).getTextContent()
                .all { txt -> coverFileContent.contains(txt) }

        assertTrue(allDescriptionTextIsInCoverFile)
    }

    @Test
    internal fun testBookDescriptionReadFromSpecialFile() {
        val book2FolderPath = BOOK_2_PATH.toPath()

        val descriptionFile = book2FolderPath.resolve(PAGE_DESCRIPTION_FILE_NAME).toFile()
        assertTrue(descriptionFile.exists())

        val coverFile = book2FolderPath.resolve(COVER_FILE).toFile()
        assertTrue(coverFile.exists())

        val coverFileContent = coverFile.getContent()
        val descriptionFileContent = descriptionFile.getContent()

        val book2 = getBookProxy(BOOK_2_PATH)
        val allDescriptionTextIsInCoverFile = book2.getTextContent()
                .all { txt -> coverFileContent.contains(txt) }
        val allDescriptionTextIsInDescriptionFile = book2.getTextContent()
                .all { txt -> descriptionFileContent.contains(txt) }
        assertTrue(allDescriptionTextIsInDescriptionFile)
        assertFalse(allDescriptionTextIsInCoverFile)
    }

    private fun String.toPath() = this@BookProxyTest.javaClass.classLoader
            .getResource(this).toURI().run { Paths.get(this) }

    private fun File.getContent() = readLines().joinToString("\n")

    private fun BookProxy.getTextContent() = description
            .map { it as EnclosingDocument }
            .map { doc -> doc.stream()
                    .filter{ it is TextDocument }
                    .map { it as TextDocument }
                    .map(TextDocument::getContent)
                    .collect(ImmutableList.toImmutableList())
            }
            .flatten()
}