package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import java.io.DataOutputStream

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class CollectionEncoder() : Encoder<Collection<*>> {
    companion object CODES {
        const val NO_COLLECTION_CODE = 104
        const val START_CODE = 102
        const val END_CODE = 103
    }

    override fun encode(out: DataOutputStream, col: Collection<*>?, encoders: Encoders) {
        if (col == null || col.isEmpty())
            out.writeByte(NO_COLLECTION_CODE)
        else {
            out.writeByte(START_CODE)
            out.writeInt(col.size)
            col.filterNotNull()
                .forEach { value -> encoders.get(value.javaClass).encode(out, value, encoders) }
            out.writeByte(END_CODE)
        }
    }

    override fun encodedClass(): Class<Collection<*>> = Collection::class.java
}