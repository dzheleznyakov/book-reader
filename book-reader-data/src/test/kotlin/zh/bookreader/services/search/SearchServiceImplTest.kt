package zh.bookreader.services.search

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Test SearchServiceImpl")
internal class SearchServiceImplTest {
    companion object {
        private const val BOOK_1_ID = "book-1-id"
        private const val BOOK_2_ID = "book-2-id"
    }

    private val service = SearchServiceImpl()

    @Nested
    @DisplayName("Test loadIndex()")
    inner class TestLoadIndex {
        @Test
        internal fun testLoad() {
            val fileContent = "#map" +
                    "\n0:$BOOK_1_ID,1:$BOOK_2_ID" +
                    "\n#index" +
                    "\nbook=>0:1[-1:1]1:1[-1:1]" +
                    "\nuno=>0:1[-1:1]" +
                    "\ndos=>1:2[-1:2]"

            val input = fileContent.byteInputStream()
            service.loadIndex(input)
        }
    }
}