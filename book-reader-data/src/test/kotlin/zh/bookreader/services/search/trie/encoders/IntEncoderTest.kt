package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class IntEncoderTest : BaseEncoderTest<Int?>() {
    @BeforeEach
    internal fun setUp() {
        encoder = IntEncoder()
        encoders = Encoders(setOf(encoder))
    }

    @Test
    @DisplayName("Test encoding null")
    internal fun encodeNull() = assertEncodedValue(null);

    @Test
    @DisplayName("Test encoding not null value")
    internal fun encodeValue() = assertEncodedValue(100, 0, 0, 0, 100)
}