package zh.bookreader.services.search.trie.encoders

import zh.bookreader.utils.collections.Trie
import java.io.DataOutputStream

class TrieEncoder() : Encoder<Trie<*>> {
    companion object CODES {
        const val NULL_TRIE_CODE = 104
        const val START_CODE = 102
        const val END_CODE = 103
    }

    override fun encode(out: DataOutputStream, trie: Trie<*>?, encoders: Encoders) {
        if (trie == null)
            out.writeByte(NULL_TRIE_CODE)
        else {
            out.writeByte(START_CODE)
            trie.streamNodesInPreorder()
                .forEach { encoders.get(it.javaClass).encode(out, it, encoders) }
            out.writeByte(END_CODE)
        }
    }

    override fun encodedClass() = Trie::class.java
}