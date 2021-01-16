package zh.bookreader.services.search.trie.encoders

import java.io.DataOutputStream

class IntEncoder() : Encoder<Integer> {
    override fun encode(out: DataOutputStream, value: Integer?, encoders: Encoders) {
        if (value != null) out.writeInt(value as Int)
    }

    override fun encodedClass(): Class<Integer> = Integer::class.java
}

class StringEncoder() : Encoder<String> {
    override fun encode(out: DataOutputStream, value: String?, encoders: Encoders) {
    }

    override fun encodedClass(): Class<String> = String::class.java
}