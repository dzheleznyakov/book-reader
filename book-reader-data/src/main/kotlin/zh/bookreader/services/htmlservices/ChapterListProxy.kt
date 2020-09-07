package zh.bookreader.services.htmlservices

import zh.bookreader.model.Chapter

class ChapterListProxy(private val toc: List<String>) : List<Chapter> {
    override val size: Int
        get() = toc.size

    override fun contains(element: Chapter) = toc.contains(element.name)
    override fun containsAll(elements: Collection<Chapter>) = elements.map { it.name }.run { toc.containsAll(this) }
    override fun get(index: Int) = ChapterProxy(toc[index])
    override fun indexOf(element: Chapter) = toc.indexOf(element.name)
    override fun isEmpty() = size == 0
    override fun iterator() = ChapterProxyIterator(toc)
    override fun lastIndexOf(element: Chapter) = toc.lastIndexOf(element.name)
    override fun listIterator() = ChapterProxyListIterator(toc)
    override fun listIterator(index: Int) = ChapterProxyListIterator(toc, index)
    override fun subList(fromIndex: Int, toIndex: Int) = ChapterListProxy(toc.subList(fromIndex, toIndex))

    class ChapterProxyIterator(toc: List<String>) : Iterator<Chapter> {
        private val tocIterator = toc.iterator()
        override fun hasNext() = tocIterator.hasNext()
        override fun next() = ChapterProxy(tocIterator.next())
    }

    class ChapterProxyListIterator : ListIterator<Chapter> {
        private val tocListIterator: ListIterator<String>

        constructor(toc: List<String>) {
            tocListIterator = toc.listIterator()
        }

        constructor(toc: List<String>, index: Int) {
            tocListIterator = toc.listIterator(index)
        }

        override fun hasNext() = tocListIterator.hasNext()
        override fun hasPrevious() = tocListIterator.hasPrevious()
        override fun next() = ChapterProxy(tocListIterator.next())
        override fun nextIndex() = tocListIterator.nextIndex()
        override fun previous() = ChapterProxy(tocListIterator.previous())
        override fun previousIndex() = tocListIterator.previousIndex()

    }
}