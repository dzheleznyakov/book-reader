package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

internal class CollectionEncoderTest {
    companion object CODES {
        const val NO_COLLECTION_CODE: Byte = 104
    }

    private lateinit var encoder: CollectionEncoder

    private lateinit var encoders: Encoders
    private lateinit var bytes: ByteArrayOutputStream
    private lateinit var out: DataOutputStream

    @BeforeEach
    internal fun setUp() {
        encoder = CollectionEncoder()
        encoders = Encoders(setOf(encoder, IntEncoder()))
        bytes = ByteArrayOutputStream()
        out = DataOutputStream(bytes)
    }

    @Test
    @DisplayName("Test encoding null")
    internal fun encodeNull() =
        assertEncodedCollection(null, NO_COLLECTION_CODE)

    @Test
    @DisplayName("Test encoding an empty collection")
    internal fun encodeEmptyCollection() =
        assertEncodedCollection(setOf(), NO_COLLECTION_CODE)

    @Test
    @DisplayName("Test encoding a collection with a single element")
    internal fun encodeCollectionWithOneElement() = assertEncodedCollection(setOf(42),
        102, 0, 0, 0, 1, 0, 0, 0, 42, 103)

    @Test
    @DisplayName("Test encoding a collection with multiple elements")
    internal fun encodeCollectionWithMultipleElements() = assertEncodedCollection(setOf(42, 43),
        102, 0, 0, 0, 2, 0, 0, 0, 42, 0, 0, 0, 43, 103)

    private fun assertEncodedCollection(collection: Collection<Any>?, vararg expectedBytes: Byte) {
        encoder.encode(out, collection, encoders)

        assertArrayEquals(expectedBytes, bytes.toByteArray())
    }
}