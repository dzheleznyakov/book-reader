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

    override fun encode(out: DataOutputStream, value: Collection<*>?, encoders: Encoders) {
        if (value == null || value.isEmpty())
            out.writeByte(NO_COLLECTION_CODE)
        else {
            out.writeByte(START_CODE)
            out.writeInt(value.size)
            value.filterNotNull()
                .forEach { v -> encoders.get(v.javaClass).encode(out, v, encoders) }
            out.writeByte(END_CODE)
        }
        out.flush()
    }

    override fun encodedClass(): Class<Collection<*>> = Collection::class.java
}