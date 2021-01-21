package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import zh.bookreader.utils.collections.Trie
import java.io.DataOutputStream

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class TrieEncoder() : Encoder<Trie<*>> {
    companion object CODES {
        const val NULL_TRIE_CODE = 104
        const val START_CODE = 102
        const val END_CODE = 103
    }

    override fun encode(out: DataOutputStream, value: Trie<*>?, encoders: Encoders) {
        if (value == null)
            out.writeByte(NULL_TRIE_CODE)
        else {
            out.writeByte(START_CODE)
            value.streamNodesInPreorder()
                .forEach { encoders.get(it.javaClass).encode(out, it, encoders) }
            out.writeByte(END_CODE)
        }
    }

    override fun encodedClass() = Trie::class.java
}