package zh.bookreader.services.search.trie.encoders

import java.io.DataOutputStream

interface Encoder<T> {
    fun encode(out: DataOutputStream, value: T?, encoders: Encoders)
    fun encodedClass(): Class<T>
}