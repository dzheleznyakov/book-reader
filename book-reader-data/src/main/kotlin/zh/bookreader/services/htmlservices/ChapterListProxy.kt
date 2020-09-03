package zh.bookreader.services.htmlservices

import zh.bookreader.model.Chapter

class ChapterListProxy(private val toc: List<String>) : List<Chapter> {
    override val size: Int = toc.size

    override fun contains(element: Chapter) = toc.contains(element.name)

    override fun containsAll(elements: Collection<Chapter>): Boolean {
        return elements.map { it.name }.run { toc.containsAll(this) }
    }

    override fun get(index: Int) = ChapterProxy(toc[index])

    override fun indexOf(element: Chapter) = toc.indexOf(element.name)

    override fun isEmpty() = size == 0

    override fun iterator() = ChapterProxyIterator(toc)

    override fun lastIndexOf(element: Chapter) = toc.lastIndexOf(element.name)

    override fun listIterator(): ListIterator<Chapter> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<Chapter> {
        TODO("Not yet implemented")
    }

    override fun subList(fromIndex: Int, toIndex: Int) = ChapterListProxy(toc.subList(fromIndex, toIndex))

    class ChapterProxyIterator(toc: List<String>) : Iterator<Chapter> {
        private var tocIterator = toc.iterator()

        override fun hasNext(): Boolean {
            return tocIterator.hasNext()
        }

        override fun next(): Chapter {
            return ChapterProxy(tocIterator.next())
        }
    }
}