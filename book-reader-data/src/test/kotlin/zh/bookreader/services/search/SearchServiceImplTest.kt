package zh.bookreader.services.search

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import zh.bookreader.services.IndexerService
import zh.bookreader.services.search.index.BookEntry
import zh.bookreader.services.search.index.IndexEntry

@DisplayName("Test SearchServiceImpl")
@ExtendWith(MockitoExtension::class)
internal class SearchServiceImplTest {
    companion object {
        private const val BOOK_1_ID = "book-1-id"
        private const val BOOK_2_ID = "book-2-id"
    }

    @Mock
    private lateinit var indexerService: IndexerService

    private lateinit var service: SearchServiceImpl

    @BeforeEach
    internal fun setUpService() {
        MockitoAnnotations.initMocks(this)
        service = SearchServiceImpl(indexerService, false)
    }

    @Nested
    @DisplayName("Test loadIndex()")
    inner class TestLoadIndex {
        @Test
        @DisplayName("Test loading index from text")
        internal fun testLoad() {
            val fileContent = "#map" +
                    "\n0:$BOOK_1_ID,1:$BOOK_2_ID" +
                    "\n#index" +
                    "\nbook=>0:1[-1:1]1:1[-1:1]" +
                    "\nuno=>0:1[-1:1]" +
                    "\ndos=>1:2[-1:2]"

            val input = fileContent.byteInputStream()
            service.loadIndex(input)

            val actualIdMap = service.idMap
            val expectedIdMap = mapOf(0 to BOOK_1_ID, 1 to BOOK_2_ID)
            assertThat(actualIdMap, `is`(equalTo(expectedIdMap)))

            val actualIndex = service.index
            val expectedIndex = mapOf(
                    "book" to IndexEntry().apply {
                        bookEntries += 0 to BookEntry().apply {
                            score = 1
                            chapters += -1 to 1
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
}