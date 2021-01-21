package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.Assertions.assertArrayEquals
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

abstract class BaseEncoderTest<T> {
    protected lateinit var encoder: Encoder<T>
    protected lateinit var encoders: Encoders
    private var bytes = ByteArrayOutputStream()
    protected var out = DataOutputStream(bytes)

    protected fun <E> assertEncodedValue(value: E, vararg expectedBytes: Byte) where E : T? {
        encoder.encode(out, value, encoders)

        assertArrayEquals(expectedBytes, bytes.toByteArray())
    }
}