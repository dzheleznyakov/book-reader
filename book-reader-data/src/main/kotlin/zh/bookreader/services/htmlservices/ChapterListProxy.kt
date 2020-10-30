package zh.bookreader.services.htmlservices

import zh.bookreader.model.documents.Chapter
import java.io.File

class ChapterListProxy(private val bookDir: File, private val toc: List<String>) : List<Chapter> {
    override val size: Int
        get() = toc.size

    override fun contains(element: Chapter) = toc.contains("${element.id}.html")
    override fun containsAll(elements: Collection<Chapter>) = elements.map { "${it.id}.html" }.run { toc.containsAll(this) }
    override fun get(index: Int) = ChapterProxy(bookDir, toc[index], index)
    override fun indexOf(element: Chapter) = toc.indexOf("${element.id}.html")
    override fun lastIndexOf(element: Chapter) = toc.lastIndexOf("${element.id}.html")
    override fun isEmpty() = size == 0
    override fun subList(fromIndex: Int, toIndex: Int) = ChapterListProxy(bookDir, toc.subList(fromIndex, toIndex))
    override fun iterator() = ChapterProxyListIterator(bookDir, toc)
    override fun listIterator() = ChapterProxyListIterator(bookDir, toc)
    override fun listIterator(index: Int) = ChapterProxyListIterator(bookDir, toc, index)

    class ChapterProxyListIterator : ListIterator<Chapter> {
        private val tocListIterator: ListIterator<String>
        private val bookDir: File

        internal constructor(bookDir: File, toc: List<String>) {
            tocListIterator = toc.listIterator()
            this.bookDir = bookDir
        }

        internal constructor(bookDir: File, toc: List<String>, index: Int) {
            tocListIterator = toc.listIterator(index)
            this.bookDir = bookDir
        }

        override fun hasNext() = tocListIterator.hasNext()
        override fun hasPrevious() = tocListIterator.hasPrevious()
        override fun next() = ChapterProxy(bookDir, tocListIterator.next(), tocListIterator.previousIndex())
        override fun nextIndex() = tocListIterator.nextIndex()
        override fun previous() = ChapterProxy(bookDir, tocListIterator.previous(), tocListIterator.previousIndex())
        override fun previousIndex() = tocListIterator.previousIndex()

    }
}