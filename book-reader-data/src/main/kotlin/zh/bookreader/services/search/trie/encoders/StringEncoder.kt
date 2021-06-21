package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import java.io.PrintWriter

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class StringEncoder : Encoder<String> {
    companion object CODES {
        const val NULL_STRING_CODE = 104
    }

    override fun encode(out: PrintWriter, value: String?, encoders: Encoders) {
        if (value != null)
            out.print(value)
        out.flush()
    }

    override fun encodedClass() = String::class.java
}