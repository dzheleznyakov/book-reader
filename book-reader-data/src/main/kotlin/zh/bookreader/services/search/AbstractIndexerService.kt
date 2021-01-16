package zh.bookreader.services.search

import com.google.common.annotations.VisibleForTesting
import zh.bookreader.model.documents.Book
import zh.bookreader.services.BookService
import zh.bookreader.services.IndexerService
import zh.bookreader.services.search.table.SearchConfig
import java.io.OutputStream
import java.nio.file.Paths

abstract class AbstractIndexerService(
    searchConfig: SearchConfig,
    private val indexFileName: String,
    protected val bookService: BookService
)
    : IndexerService
{
    internal var pathToIndex = Paths.get(searchConfig.userHomePath, searchConfig.searchIndexRelPath)
    internal var pathToLibrary = Paths.get(searchConfig.userHomePath, searchConfig.libraryRelPath)
    internal var pathToIndexFile = pathToIndex.resolve(indexFileName)

    override fun index() {
        if (shouldIndex())
            doIndex()
    }

    @VisibleForTesting
    internal fun shouldIndex(): Boolean {
        val libraryFile = pathToLibrary.toFile()
        if (!libraryFile.exists())
            return false

        val indexFile = pathToIndex.resolve(indexFileName).toFile()
        return !indexFile.exists()
                || libraryFile.lastModified() >= indexFile.lastModified()
    }

    protected fun doIndex() {
        val indexFile = pathToIndexFile.toFile()
        pathToIndex.toFile().mkdirs()
        if (indexFile.exists())
            indexFile.delete()
        indexFile.createNewFile()
        val output = indexFile.outputStream()
        val books = bookService.findAll()
        index(output, books)
        cleanUp()
    }

    protected abstract fun cleanUp()
    @VisibleForTesting
    internal abstract fun index(output: OutputStream, books: List<Book>)
}