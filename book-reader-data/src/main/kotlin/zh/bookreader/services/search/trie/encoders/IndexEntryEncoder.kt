package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.IndexEntry
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import java.io.PrintWriter

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class IndexEntryEncoder : Encoder<IndexEntry> {
    companion object CODES {
        const val NULL_VALUE = 104
        const val OBJECT_START_CODE = 100
        const val OBJECT_END_CODE = 101
    }

    override fun encode(out: PrintWriter, value: IndexEntry?, encoders: Encoders) {
        if (value != null) {
            @Suppress("UNCHECKED_CAST") val encoder = encoders.get(value.bookEntries::class.java) as Encoder<Map<*, *>>
            encoder.encode(out, value.bookEntries, encoders)
        }
        out.flush()
    }

    override fun encodedClass() = IndexEntry::class.java
}