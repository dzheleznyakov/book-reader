package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.PrintWriter

abstract class BaseEncoderTest<T> {
    protected lateinit var encoder: Encoder<T>
    protected lateinit var encoders: Encoders
    private var bytes = ByteArrayOutputStream()
    protected var out = DataOutputStream(bytes)
    protected var outPW = PrintWriter(bytes)

    protected fun <E> assertEncodedValue(value: E, vararg expectedBytes: Byte) where E : T? {
        encoder.encode(out, value, encoders)

        assertArrayEquals(expectedBytes, bytes.toByteArray())
    }

    protected fun <E> assertEncodedValue(value: E, expected: String) where E : T? {
        encoder.encode(outPW, value, encoders)

        val byteArray = bytes.toByteArray()
        val actual = String(byteArray)
        assertEquals(expected, actual)
    }
}