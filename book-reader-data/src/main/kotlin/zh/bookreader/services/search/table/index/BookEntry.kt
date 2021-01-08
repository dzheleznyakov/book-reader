package zh.bookreader.services.search.table.index

class BookEntry {
    internal var score = 0
    internal val chapters = mutableMapOf<Int, Int>()

    fun increaseCount(ind: Int) {
        val count = chapters[ind] ?: 0
        chapters[ind] = count + 1
        score += 1
    }

    fun getScore() = score
    fun chaptersToString(): String = chapters
            .map { (ind, sc) -> "$ind:$sc" }
            .joinToString(separator = ",")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BookEntry) return false

        if (score != other.score) return false
        if (chapters != other.chapters) return false

        return true
    }

    override fun hashCode(): Int {
        var result = score
        result = 31 * result + chapters.hashCode()
        return result
    }


}