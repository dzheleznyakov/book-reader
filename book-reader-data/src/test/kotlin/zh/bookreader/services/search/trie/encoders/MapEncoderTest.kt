package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class MapEncoderTest : BaseEncoderTest<Map<*, *>>() {
    @BeforeEach
    internal fun setUp() {
        encoder = MapEncoder()
        encoders = Encoders(setOf(encoder, IntEncoder()))
    }

    @Test
    @DisplayName("Test encoding null")
    internal fun encodeNull() {
        assertEncodedValue(null, "")
    }

    @Test
    @DisplayName("Test encoding empty map")
    internal fun encodeEmptyMap() {
        assertEncodedValue(mapOf<Int, Int>(), "")
    }

    @Test
    @DisplayName("Test encoding map with one entry")
    internal fun encodeSingleEntryMap() {
        val map = mapOf(1 to 10)

        assertEncodedValue(map, "1,10#")
    }

    @Test
    @DisplayName("Test encoding map with two entries")
    internal fun encodeMapWithTwoEntries() {
        val map = mapOf(1 to 10, 2 to 20)

        assertEncodedValue(map, "1,10,2,20#")
    }

    @Test
    @DisplayName("Throw an exception if a key is null")
    internal fun throwEncodingExcepitonIfKeyIsNull() {
        val map = mapOf<Int?, Int>(null to 10)

        assertThrows<Encoder.EncodingException> { encoder.encode(out, map, encoders) }
    }

    @Test
    @DisplayName("Throw an exception if a value is null")
    internal fun throwEncodingExceptionIfValueIsNull() {
        val map = mapOf<Int, Int?>(1 to null)

        assertThrows<Encoder.EncodingException> { encoder.encode(out, map, encoders) }
    }
}