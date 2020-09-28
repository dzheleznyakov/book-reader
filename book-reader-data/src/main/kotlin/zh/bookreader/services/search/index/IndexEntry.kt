package zh.bookreader.services.search.index

class IndexEntry {
    private val bookEntries = mutableMapOf<Int, BookEntry>()

    fun put(bookInd: Int, chInd: Int) {
        bookEntries.putIfAbsent(bookInd, BookEntry())
        bookEntries[bookInd]?.increaseCount(chInd)
    }

    override fun toString() = bookEntries
            .map { (bookInd, entry) -> "$bookInd:${entry.getScore()}[${entry.chaptersToString()}]" }
            .joinToString("")
}