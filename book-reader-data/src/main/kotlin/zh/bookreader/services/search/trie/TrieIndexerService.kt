package zh.bookreader.services.search.trie

import zh.bookreader.model.documents.Book
import zh.bookreader.services.BookService
import zh.bookreader.services.IndexerService
import zh.bookreader.services.search.AbstractIndexerService
import zh.bookreader.services.search.IndexEntry
import zh.bookreader.services.search.table.SearchConfig
import zh.bookreader.services.search.table.TRIE_INDEX_FILE_NAME
import zh.bookreader.utils.collections.Trie
import java.io.OutputStream

class TrieIndexerService(
    bookService: BookService,
    searchConfig: SearchConfig
) : IndexerService,
    AbstractIndexerService(searchConfig, TRIE_INDEX_FILE_NAME, bookService)
{
    private val idMap = mutableMapOf<Int, String>()
    private val trie = Trie<IndexEntry>()

    override fun index(output: OutputStream, books: List<Book>) {
        TODO("Not yet implemented")
    }

    override fun cleanUp() {
        TODO("Not yet implemented")
    }
}