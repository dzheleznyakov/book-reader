package zh.bookreader.services.search.index

class BookEntry {
    private var score = 0
    private val chapters = mutableMapOf<Int, Int>()

    fun increaseCount(ind: Int) {
        val count = chapters[ind] ?: 0
        chapters[ind] = count + 1
        score += 1
    }

    fun getScore() = score
    fun chaptersToString(): String = chapters
            .map { (ind, sc) -> "$ind:$sc" }
            .joinToString(separator = ",")
}