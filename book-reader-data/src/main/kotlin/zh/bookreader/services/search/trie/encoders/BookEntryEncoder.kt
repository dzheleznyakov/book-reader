package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.BookEntry
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import java.io.PrintWriter

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class BookEntryEncoder : Encoder<BookEntry> {
    companion object CODES {
        const val SEPARATOR = '|'
        const val END_CODE = '#'
    }

    override fun encode(out: PrintWriter, value: BookEntry?, encoders: Encoders) {
        if (value != null)
            out.encodeScore(value)
                .encodeChapters(value.chapters, encoders)
        out.flush()
    }

    private fun PrintWriter.encodeScore(value: BookEntry): PrintWriter = apply{
        append(value.score).append(SEPARATOR)
    }

    @Suppress("UNCHECKED_CAST")
    private fun PrintWriter.encodeChapters(chapters: Map<Int, Int>, encoders: Encoders): PrintWriter = apply {
        val encoder = encoders.get(chapters::class.java) as Encoder<Map<*, *>>
        encoder.encode(this, chapters, encoders)
    }

    override fun encodedClass() = BookEntry::class.java
}