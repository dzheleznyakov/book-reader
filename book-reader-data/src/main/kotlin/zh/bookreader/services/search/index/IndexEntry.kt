package zh.bookreader.services.search.index

class IndexEntry {
    internal val bookEntries = mutableMapOf<Int, BookEntry>()

    fun put(bookInd: Int, chInd: Int) {
        bookEntries.putIfAbsent(bookInd, BookEntry())
        bookEntries[bookInd]?.increaseCount(chInd)
    }

    override fun toString() = bookEntries
            .map { (bookInd, entry) -> "$bookInd:${entry.getScore()}[${entry.chaptersToString()}]" }
            .joinToString("")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IndexEntry) return false

        if (bookEntries != other.bookEntries) return false

        return true
    }

    override fun hashCode(): Int {
        return bookEntries.hashCode()
    }


}