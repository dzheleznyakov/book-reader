package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import java.io.DataOutputStream

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class MapEncoder() : Encoder<Map<*, *>> {
    companion object CODES {
        const val NULL_MAP_CODE = 104
        const val START_CODE = 102
        const val END_CODE = 103
    }

    override fun encode(out: DataOutputStream, value: Map<*, *>?, encoders: Encoders) {
        if (value == null) out.writeByte(NULL_MAP_CODE)
        else doEncode(out, value, encoders)
    }

    private fun doEncode(out: DataOutputStream, map: Map<*, *>, encoders: Encoders) {
        out.writeByte(START_CODE)
        out.writeInt(map.size)
        map.forEach { (k, v) -> encodeMapEntry(k, v, encoders, out) }
        out.writeByte(END_CODE)
    }

    private fun encodeMapEntry(k: Any?, v: Any?, encoders: Encoders, out: DataOutputStream) {
        if (k == null || v == null)
            throw Encoder.EncodingException("Either key or value is null: key=[${k}], value=[${v}]")
        encoders.get(k.javaClass).encode(out, k, encoders)
        encoders.get(v.javaClass).encode(out, v, encoders)
    }

    override fun encodedClass() = Map::class.java
}