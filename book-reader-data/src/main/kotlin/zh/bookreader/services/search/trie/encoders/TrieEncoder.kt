package zh.bookreader.services.search.trie.encoders

import zh.bookreader.utils.collections.Trie
import java.io.DataOutputStream

class TrieEncoder : Encoder<Trie<*>> {
    override fun encode(out: DataOutputStream, value: Trie<*>?, encoders: Encoders) {
        if (value != null)
            value.streamNodesInPreorder()
                .forEach { out.writeChar(it.label as Int) }
    }

    override fun encodedClass(): Class<Trie<*>> = Trie::class.java
}