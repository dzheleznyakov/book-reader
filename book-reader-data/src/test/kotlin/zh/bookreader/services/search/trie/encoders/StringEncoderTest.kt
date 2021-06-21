package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class StringEncoderTest : BaseEncoderTest<String>() {
    @BeforeEach
    internal fun setUp() {
        encoder = StringEncoder()
        encoders = Encoders(setOf(encoder))
    }

    @Test
    @DisplayName("Test encoding null string")
    internal fun encodeNull() = assertEncodedValue(null, "")

    @Test
    @DisplayName("Test encoding empty string")
    internal fun encodeEmptyString() = assertEncodedValue("")

    @Test
    @DisplayName("Test encoding a string")
    internal fun encodeString() = assertEncodedValue("abc", "abc")
}