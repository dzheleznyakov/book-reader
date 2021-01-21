package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class CollectionEncoderTest : BaseEncoderTest<Collection<*>>() {
    companion object CODES {
        const val NO_COLLECTION_CODE: Byte = 104
    }

    @BeforeEach
    internal fun setUp() {
        encoder = CollectionEncoder()
        encoders = Encoders(setOf(encoder, IntEncoder()))
    }

    @Test
    @DisplayName("Test encoding null")
    internal fun encodeNull() =
        assertEncodedValue(null, NO_COLLECTION_CODE)

    @Test
    @DisplayName("Test encoding an empty collection")
    internal fun encodeEmptyCollection() =
        assertEncodedValue(setOf<Any>(), NO_COLLECTION_CODE)

    @Test
    @DisplayName("Test encoding a collection with a single element")
    internal fun encodeCollectionWithOneElement() = assertEncodedValue(setOf(42),
        102, 0, 0, 0, 1, 0, 0, 0, 42, 103)

    @Test
    @DisplayName("Test encoding a collection with multiple elements")
    internal fun encodeCollectionWithMultipleElements() = assertEncodedValue(setOf(42, 43),
        102, 0, 0, 0, 2, 0, 0, 0, 42, 0, 0, 0, 43, 103)
}