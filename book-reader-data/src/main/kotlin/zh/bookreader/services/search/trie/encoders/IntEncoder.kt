package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import java.io.PrintWriter

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class IntEncoder : Encoder<Int?> {
    override fun encode(out: PrintWriter, value: Int?, encoders: Encoders) {
        if (value != null)
            out.append(value)
        out.flush()
    }

    @Suppress("UNCHECKED_CAST")
    override fun encodedClass(): Class<Int?> = 0::class.java as Class<Int?>
}