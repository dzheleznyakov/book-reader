package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class CollectionEncoderTest : BaseEncoderTest<Collection<*>>() {
    @BeforeEach
    internal fun setUp() {
        encoder = CollectionEncoder()
        encoders = Encoders(setOf(encoder, IntEncoder()))
    }

    @Test
    @DisplayName("Test encoding null")
    internal fun encodeNull() =
        assertEncodedValue(null, "")

    @Test
    @DisplayName("Test encoding an empty collection")
    internal fun encodeEmptyCollection() =
        assertEncodedValue(setOf<Any>(), "")

    @Test
    @DisplayName("Test encoding a collection with a single element")
    internal fun encodeCollectionWithOneElement() = assertEncodedValue(setOf(42),
        "42#")

    @Test
    @DisplayName("Test encoding a collection with multiple elements")
    internal fun encodeCollectionWithMultipleElements() = assertEncodedValue(setOf(42, 43),
        "42,43#")
}