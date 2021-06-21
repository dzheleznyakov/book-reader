package zh.bookreader.services.search.trie.encoders

import java.io.DataOutputStream
import java.io.PrintWriter

interface Encoder<T> {
    fun encode(out: DataOutputStream, value: T?, encoders: Encoders) {}
    fun encode(out: PrintWriter, value: T?, encoders: Encoders) {}
    fun encodedClass(): Class<T>
    fun PrintWriter.append(o: Any): PrintWriter = apply { print(o) }

    class EncodingException(message: String) : RuntimeException(message)
}