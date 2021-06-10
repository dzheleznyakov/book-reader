package zh.bookreader.services.search.trie.encoders

import java.io.DataOutputStream

class IntegerEncoder : Encoder<Integer> {
    override fun encode(out: DataOutputStream, value: Integer?, encoders: Encoders) =
        out.writeChars(value.toString())

    override fun encodedClass(): Class<Integer> = Integer::class.java
}