package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import java.io.PrintWriter

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class CollectionEncoder : Encoder<Collection<*>> {
    companion object CODES {
        const val END_CODE = '#'
    }

    override fun encode(out: PrintWriter, value: Collection<*>?, encoders: Encoders) {
        if (value != null && value.isNotEmpty()) {
            encodeContent(value, encoders, out)
            out.append(END_CODE)
        }
        out.flush()
    }

    private fun encodeContent(content: Collection<*>, encoders: Encoders, out: PrintWriter) = content.filterNotNull()
        .forEachIndexed { i, v ->
            encoders.get(v.javaClass).encode(out, v, encoders)
            if (i < content.size - 1) out.append(',')
        }

    override fun encodedClass(): Class<Collection<*>> = Collection::class.java
}