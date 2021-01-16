package zh.bookreader.services.search.trie

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import zh.bookreader.model.documents.Book

internal class TrieIndexerServiceTest {
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

//    private val indexer = TrieIndexerServiceTest(Mockito.mock(BookService::javaClass), )

    @Test
    @Disabled
    internal fun name() {
        TODO("Not yet implemented")
    }
}