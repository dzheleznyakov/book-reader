package zh.bookreader.services.htmlservices

import zh.bookreader.model.Book
import java.io.File
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths

class BookListProxy(private val libraryUri: URI) : List<Book> {
    private var catalog: List<Path> = File(libraryUri)
            .listFiles(File::isDirectory)
            ?.map(File::toPath)
            ?: listOf()

    private constructor(libraryUri: URI, catalog: List<Path>): this(libraryUri) {
        this.catalog = catalog
    }

    override val size: Int
        get() = catalog.size

    override fun contains(element: Book) = catalog.contains(libraryUri.resolve(element))
    override fun containsAll(elements: Collection<Book>) = catalog.containsAll(elements.map { libraryUri.resolve(it) })
    override fun get(index: Int) = BookProxy(catalog[index].toFile())
    override fun indexOf(element: Book) = catalog.indexOf(libraryUri.resolve(element))
    override fun lastIndexOf(element: Book) = catalog.lastIndexOf(libraryUri.resolve(element))
    override fun isEmpty() = catalog.isEmpty()
    override fun subList(fromIndex: Int, toIndex: Int) = BookListProxy(libraryUri, catalog.subList(fromIndex, toIndex))
    override fun iterator() = BookProxyListIterator(catalog.listIterator())
    override fun listIterator() = BookProxyListIterator(catalog.listIterator())
    override fun listIterator(index: Int) = BookProxyListIterator(catalog.listIterator(index))

    private fun URI.resolve(book: Book) = Paths.get(this).resolve(book.id)

    class BookProxyListIterator(private val catListIterator: ListIterator<Path>) : ListIterator<Book> {
        override fun hasNext() = catListIterator.hasNext()
        override fun hasPrevious() = catListIterator.hasPrevious()
        override fun next() = BookProxy(catListIterator.next().toFile())
        override fun nextIndex() = catListIterator.nextIndex()
        override fun previous() = BookProxy(catListIterator.previous().toFile())
        override fun previousIndex() = catListIterator.previousIndex()
    }
}