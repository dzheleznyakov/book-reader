package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import java.io.PrintWriter

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class MapEncoder() : Encoder<Map<*, *>> {
    companion object CODES {
        const val DELIM = ','
        const val END_CODE = '#'
    }

    override fun encode(out: PrintWriter, value: Map<*, *>?, encoders: Encoders) {
        if (value != null && value.isNotEmpty())
            doEncode(out, value, encoders)
        out.flush()
    }

    private fun doEncode(out: PrintWriter, map: Map<*, *>, encoders: Encoders) {
        map.asSequence().forEachIndexed { i, (k, v) ->
            encodeMapEntry(k, v, i == map.size - 1, encoders, out)
        }
        out.append(END_CODE)
    }

    private fun encodeMapEntry(k: Any?, v: Any?, lastEntry: Boolean, encoders: Encoders, out: PrintWriter) {
        if (k == null || v == null)
            throw Encoder.EncodingException("Either key or value is null: key=[${k}], value=[${v}]")
        encoders.get(k.javaClass).encode(out, k, encoders)
        out.append(DELIM)
        encoders.get(v.javaClass).encode(out, v, encoders)
        if (!lastEntry)
            out.append(DELIM)
    }

    override fun encodedClass() = Map::class.java
}