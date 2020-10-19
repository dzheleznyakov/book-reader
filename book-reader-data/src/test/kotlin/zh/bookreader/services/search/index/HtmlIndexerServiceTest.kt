package zh.bookreader.services.search.index

import com.google.common.util.concurrent.Uninterruptibles
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import zh.bookreader.model.documents.Book
import zh.bookreader.services.htmlservices.HtmlBookService
import zh.bookreader.services.htmlservices.getBlock
import zh.bookreader.services.htmlservices.getChapter
import zh.bookreader.services.htmlservices.getHeader
import zh.bookreader.services.htmlservices.getParagraph
import zh.bookreader.services.htmlservices.getText
import zh.bookreader.services.search.INDEX_FILE_NAME
import zh.bookreader.services.search.hamcrest.matchesIndexMap
import java.io.ByteArrayOutputStream
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@DisplayName("Test HTML Indexer Service")
internal class HtmlIndexerServiceTest {
    private val pathToTestOutputFolder = "output/${this.javaClass.simpleName}"
    private val pathToTestIndexFolder = "$pathToTestOutputFolder/index"
    private val pathToLibrary = "$pathToTestOutputFolder/library"

    private var indexer = HtmlIndexerService(HtmlBookService(""))

    @BeforeEach
    internal fun setUpOutputFolder() {
        Paths.get(pathToTestIndexFolder).toFile().mkdirs()
        Paths.get(pathToLibrary).toFile().mkdirs()
    }

    @BeforeEach
    internal fun setUpIndexer() {
        indexer.setPathToIndex(pathToTestIndexFolder)
        indexer.setPathToLibrary(pathToLibrary)
    }

    @AfterEach
    internal fun tearDownOutputFolder() {
        Paths.get(pathToTestOutputFolder).toFile().deleteRecursively()
    }

    @Nested
    @DisplayName("Test shouldIndex()")
    inner class ShouldIndexTest {
        @Test
        @DisplayName("Should run indexing if there is no index file")
        internal fun testShouldIndexIfNoIndexFile() {
            val shouldIndex = indexer.shouldIndex()

            assertThat(shouldIndex, `is`(true))
        }

        @Test
        @DisplayName("Should not run indexing if the library folder does not exist")
        internal fun testShouldNotIndexIfLibraryDoesNotExist() {
            Paths.get(pathToLibrary).toFile().deleteRecursively()

            val shouldIndex = indexer.shouldIndex()

            assertThat(shouldIndex, `is`(false))
        }

        @Test
        @DisplayName("Should not run indexing if the library was updated before the index")
        internal fun testShouldNotIndexIfLibraryWasUpdatedBeforeIndex() {
            Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS)
            Paths.get(pathToTestIndexFolder, INDEX_FILE_NAME).toFile().createNewFile()

            val shouldIndex = indexer.shouldIndex()

            assertThat(shouldIndex, `is`(false))
        }

        @Test
        internal fun testShouldIndexIfLibraryWasUpdatedAfterIndex() {
            Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS)
            Paths.get(pathToTestIndexFolder, INDEX_FILE_NAME).toFile().createNewFile()
            Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS)
            Paths.get(pathToLibrary, "file.txt").toFile().createNewFile()

            val shouldIndex = indexer.shouldIndex()

            assertThat(shouldIndex, `is`(true))
        }
    }

    @Nested
    @DisplayName("Test index()")
    inner class IndexTest {
        private val book1Id = "book-1-id"
        private val book2Id = "book-2-id"

        private val descr1Title = "Alpha Beta Uno"
        private val descr1Par1 = "Gamma Delta Dos"
        private val descr1Par2 = "Gamma Epsilon Tres"

        private val descr2Title = "Zeta Tres"
        private val descr2Par1 = "Kappa Lambda Dos"
        private val descr2Par2 = "Gamma Lambda Dos"

        private val book1 = Book().apply {
            id = book1Id
            title = ""
            description = emptyList()
        }
        private val book2 = Book().apply {
            id = book2Id
            title = ""
            description = emptyList()
        }

        private val books = listOf(book1, book2)

        @Test
        @DisplayName("Book title is indexed")
        internal fun titleIsIndexed() {
            book1.title = "Book Uno"
            book2.title = "Book Dos Dos"

            assertIndex("#map" +
                    "\n0:$book1Id,1:$book2Id" +
                    "\n#index" +
                    "\nbook=>0:1[-1:1]1:1[-1:1]" +
                    "\nuno=>0:1[-1:1]" +
                    "\ndos=>1:2[-1:2]")
        }

        @Test
        @DisplayName("Book description is indexed")
        internal fun descriptionIsIndexed() {
            book1.description = listOf(
                    getHeader(descr1Title),
                    getParagraph(getText(descr1Par1)),
                    getParagraph(getText(descr1Par2))
            )
            book2.description = listOf(
                    getHeader(descr2Title),
                    getParagraph(getText(descr2Par1)),
                    getParagraph(getText(descr2Par2))
            )

            assertIndex("#map" +
                    "\n0:$book1Id,1:$book2Id" +
                    "\n#index" +
                    "\nalpha=>0:1[-1:1]" +
                    "\nbeta=>0:1[-1:1]" +
                    "\nuno=>0:1[-1:1]" +
                    "\ngamma=>0:2[-1:2]1:1[-1:1]" +
                    "\ndelta=>0:1[-1:1]" +
                    "\ndos=>0:1[-1:1]1:2[-1:2]" +
                    "\nepsilon=>0:1[-1:1]" +
                    "\ntres=>0:1[-1:1]1:1[-1:1]" +
                    "\nzeta=>1:1[-1:1]" +
                    "\nkappa=>1:1[-1:1]" +
                    "\nlambda=>1:2[-1:2]")
        }

        @Test
        @DisplayName("Book chapters are indexed")
        internal fun chaptersAreIndexed() {
            book1.chapters = listOf(
                    getChapter(getBlock(listOf(getHeader(descr1Title), getParagraph(descr1Par1)))),
                    getChapter(getBlock(listOf(getParagraph(descr1Par2)))))
            book2.chapters = listOf(
                    getChapter(getBlock(listOf(getHeader(descr2Title), getParagraph(descr2Par1)))),
                    getChapter(getBlock(listOf(getParagraph(descr2Par2)))))

            assertIndex("#map" +
                    "\n0:$book1Id,1:$book2Id" +
                    "\n#index" +
                    "\nalpha=>0:1[0:1]" +
                    "\nbeta=>0:1[0:1]" +
                    "\nuno=>0:1[0:1]" +
                    "\ngamma=>0:2[0:1,1:1]1:1[1:1]" +
                    "\ndelta=>0:1[0:1]" +
                    "\ndos=>0:1[0:1]1:2[0:1,1:1]" +
                    "\nepsilon=>0:1[1:1]" +
                    "\ntres=>0:1[1:1]1:1[0:1]" +
                    "\nzeta=>1:1[0:1]" +
                    "\nkappa=>1:1[0:1]" +
                    "\nlambda=>1:2[0:1,1:1]")
        }

        @Test
        @DisplayName("Stop words should not be indexed")
        internal fun doNotIndexStopWords() {
            book1.title = "This Is a Stop Word"

            assertIndex("#map" +
                    "\n0:$book1Id,1:$book2Id" +
                    "\n#index" +
                    "\nword=>0:1[-1:1]")
        }

        @Test
        @DisplayName("Commas etc are stripped out")
        internal fun commasAndOtherSignsAreStrippedOut() {
            book1.title = "Comma, dot."

            assertIndex("#map" +
                    "\n0:$book1Id,1:$book2Id" +
                    "\n#index" +
                    "\ncomma=>0:1[-1:1]" +
                    "\ndot=>0:1[-1:1]")
        }

        private fun assertIndex(expected: String) {
            val output = ByteArrayOutputStream()

            indexer.index(output, books)
            val actual = output.toString()

            assertThat(actual, matchesIndexMap(expected))
        }
    }
}