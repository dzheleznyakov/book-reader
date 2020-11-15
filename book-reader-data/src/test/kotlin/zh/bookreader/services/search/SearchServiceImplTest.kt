package zh.bookreader.services.search

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import zh.bookreader.model.search.SearchHit
import zh.bookreader.services.IndexerService
import zh.bookreader.services.search.index.BookEntry
import zh.bookreader.services.search.index.IndexEntry

@DisplayName("Test SearchServiceImpl")
@ExtendWith(MockitoExtension::class)
internal class SearchServiceImplTest {
    companion object {
        private const val BOOK_0_ID = "book-1-id"
        private const val BOOK_1_ID = "book-2-id"
    }

    @Mock
    private lateinit var indexerService: IndexerService

    private object TestSearchConfig : SearchConfig {
        override val searchIndexRelPath: String
            get() = "index/"
        override val libraryRelPath: String
            get() = "library/"
        override val userHomePath: String
            get() = "user.home"
        override val indexFileName: String
            get() = "index.zhi"
        override fun <E : Any> E.getStopWords(): Set<String> = SearchConfigImpl().getStopWords()
    }

    private lateinit var service: SearchServiceImpl

    @BeforeEach
    internal fun setUpService() {
        MockitoAnnotations.initMocks(this)
        service = SearchServiceImpl(TestSearchConfig, indexerService, false)
    }

    private fun loadIndex(content: String) = service.loadIndex(content.byteInputStream())
            .also { service.setReady(true) }

    @Nested
    @DisplayName("Test loadIndex()")
    inner class TestLoadIndex {
        @Test
        @DisplayName("Test loading index from text")
        internal fun testLoad() {
            val fileContent = "#map" +
                    "\n0:$BOOK_0_ID,1:$BOOK_1_ID" +
                    "\n#index" +
                    "\nbook=>0:2[-1:1,1:1]1:1[-1:1]" +
                    "\nuno=>0:1[-1:1]" +
                    "\ndos=>1:2[-1:2]"

            loadIndex(fileContent)

            val actualIdMap = service.idMap
            val expectedIdMap = mapOf(0 to BOOK_0_ID, 1 to BOOK_1_ID)
            assertThat(actualIdMap, `is`(equalTo(expectedIdMap)))

            val actualIndex = service.index
            val expectedIndex = mapOf(
                    "book" to IndexEntry().apply {
                        bookEntries += 0 to BookEntry().apply {
                            score = 2
                            chapters += -1 to 1
                            chapters += 1 to 1
                        }
                        bookEntries += 1 to BookEntry().apply {
                            score = 1
                            chapters += -1 to 1
                        }
                    },
                    "uno" to IndexEntry().apply {
                        bookEntries += 0 to BookEntry().apply {
                            score = 1
                            chapters += -1 to 1
                        }
                    },
                    "dos" to IndexEntry().apply {
                        bookEntries += 1 to BookEntry().apply {
                            score = 2
                            chapters += -1 to 2
                        }
                    }
            )
            assertThat(actualIndex, `is`(equalTo(expectedIndex)))
        }
    }

    @Nested
    @DisplayName("Test find(query)")
    inner class TestFind {
        private val indexStringPrefix = "#map" +
                "\n0:$BOOK_0_ID,1:$BOOK_1_ID" +
                "\n#index"

        @Test
        @DisplayName("It should return an empty list if the index has not been parsed yet")
        internal fun shouldReturnEmptyListIfIndexedNotParsedYet() {
            loadIndex(indexStringPrefix +
                    "\nword=>0:1[-1:1]1:1[-1:1]")
            service.setReady(false)

            val result = service.find(listOf("word"))

            assertThat(result, `is`(empty()))
        }

        @Test
        @DisplayName("Test null query")
        internal fun testNullQuery() {
            val result = service.find(null)

            assertThat(result, `is`(empty()))
        }

        @Test
        @DisplayName("Test empty query")
        internal fun testEmptyQuery() {
            val result = service.find(emptyList())

            assertThat(result, `is`(empty()))
        }

        @Test
        @DisplayName("Test one word query")
        internal fun testOneWordQuery() {
            loadIndex(indexStringPrefix +
                    "\nword=>0:1[-1:1]1:1[-1:1]")

            val result = service.find(listOf("word"))

            val expected = listOf(
                    SearchHit().apply { bookId = BOOK_0_ID; chapterNums = listOf(-1) },
                    SearchHit().apply { bookId = BOOK_1_ID; chapterNums = listOf(-1) }
            )
            assertThat(result, `is`(equalTo(expected)))
        }

        @Test
        @DisplayName("Test that only relevant results return (one word query)")
        internal fun testReturnOnlyMatchingBooks_OneWord() {
            val indexFileContent = indexStringPrefix +
                    "\nword=>1:1[-1:1]"
            val query = listOf("word")
            val expected = listOf(
                    SearchHit().apply { bookId = BOOK_1_ID; chapterNums = listOf(-1) }
            )

            assertResult(expected, query, indexFileContent)
        }

        @Test
        @DisplayName("Test that the results are sorted by score")
        internal fun testOderResultsByScore_OneWord() {
            assertBookIds(listOf(BOOK_1_ID, BOOK_0_ID), listOf("word"),
                    indexStringPrefix +
                    "\nword=>0:1[-1:1]1:2[-1:2]")
        }

        @Test
        @DisplayName("Return a book that matches both words")
        internal fun testTwoWords() {
            assertBookIds(listOf(BOOK_0_ID), listOf("foo", "bar"),
                    indexStringPrefix +
                    "\nfoo=>0:1[-1:1]" +
                    "\nbar=>0:1[-1:1]1:1[-1:1]")

        }

        @Test
        @DisplayName("Return nothing is no book matches all words")
        internal fun testTwoWords_BookShouldMatchAllWords() {
            assertBookIds(emptyList(), listOf("foo", "bar"),
                    indexStringPrefix +
                    "\nfoo=>0:1[-1:1]" +
                    "\nbar=>1:1[-1:1]")
        }

        @Test
        @DisplayName("Results should be ordered by max score amongst the scores for all words")
        internal fun testTwoWords_BooksShouldBeOrderedByMaxHit() {
            assertBookIds(listOf(BOOK_1_ID, BOOK_0_ID), listOf("foo", "bar"),
                    indexStringPrefix +
                    "\nfoo=>0:3[-1:4]1:3[-1:3]" +
                    "\nbar=>0:3[-1:3]1:4[-1:4]")
        }

        @Test
        @DisplayName("Only chapters that match all words appear in the result")
        internal fun testChapterList_ChaptersShouldMatchAllWords() {
            assertResult(listOf(
                    SearchHit().apply { bookId = BOOK_0_ID; chapterNums = listOf(1) }
                ), listOf("foo", "bar"),
                    indexStringPrefix +
                    "\nfoo=>0:2[0:1,1:1]" +
                    "\nbar=>0:1[1:1]")
        }

        @Test
        @DisplayName("Chapters are ordered by their min score")
        internal fun testChapterList_ChaptersAreOrderedByScore() {
            assertResult(listOf(
                    SearchHit().apply { bookId = BOOK_0_ID; chapterNums = listOf(1, 0) }
            ), listOf("foo", "bar"),
                    indexStringPrefix +
                    "\nfoo=>0:6[0:4,1:2]" +
                    "\nbar=>0:3[0:1,1:2]")
        }

        @Test
        @DisplayName("Querying is case insensitive")
        internal fun testInputQuery_CaseShouldBeIgnored() {
            assertBookIds(listOf(BOOK_0_ID), listOf("fOO", "BaR"),
                    indexStringPrefix +
                    "\nfoo=>0:1[0:1]" +
                    "\nbar=>0:1[0:1]")
        }

        @Test
        @DisplayName("Non-letter characters in query are ignored")
        internal fun testInputQuery_IgnoreNonLetterCharacters() {
            assertBookIds(listOf(BOOK_0_ID), listOf("fo0o", "B_ar!"),
                    indexStringPrefix +
                            "\nfoo=>0:1[0:1]" +
                            "\nbar=>0:1[0:1]")
        }

        private fun assertBookIds(expectedBookIds: List<String>, query: List<String>, indexFileContent: String) {
            loadIndex(indexFileContent)
            val actualBookIds = service.find(query).map(SearchHit::getBookId)
            assertThat(actualBookIds, `is`(equalTo(expectedBookIds)))
        }

        private fun assertResult(expected: List<SearchHit>, query: List<String>, indexFileContent: String) {
            loadIndex(indexFileContent)
            val result = service.find(query)
            assertThat(result, `is`(equalTo(expected)))
        }
    }
}
