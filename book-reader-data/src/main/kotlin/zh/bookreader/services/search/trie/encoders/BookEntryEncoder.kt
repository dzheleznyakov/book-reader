package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.BookEntry
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import java.io.DataOutputStream

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class BookEntryEncoder : Encoder<BookEntry> {
    companion object CODES {
        const val NULL_VALUE = 104
        const val OBJECT_START_CODE = 100
        const val OBJECT_END_CODE = 101
    }

    override fun encode(out: DataOutputStream, value: BookEntry?, encoders: Encoders) {
        if (value == null)
            out.writeByte(NULL_VALUE)
        else {
            out.writeByte(OBJECT_START_CODE)
            val chapters: Map<Int, Int> = value.chapters
            val mapEncoder: Encoder<Map<*, *>> = encoders.get(chapters::class.java) as Encoder<Map<*, *>>
            mapEncoder.encode(out, chapters, encoders)
            out.writeByte(OBJECT_END_CODE)
        }
    }

    override fun encodedClass() = BookEntry::class.java
}