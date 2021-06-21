package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import zh.bookreader.utils.collections.Trie
import java.io.PrintWriter

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class TrieEncoder : Encoder<Trie<*>> {
    companion object CODES {
        const val START_CODE = '<'
        const val END_CODE = '>'
    }

    override fun encode(out: PrintWriter, value: Trie<*>?, encoders: Encoders) {
        if (value == null) out.append(START_CODE).append(END_CODE)
        else encoders.get(value.root.javaClass).encode(out, value.root, encoders)
        out.flush()
    }

    override fun encodedClass() = Trie::class.java
}