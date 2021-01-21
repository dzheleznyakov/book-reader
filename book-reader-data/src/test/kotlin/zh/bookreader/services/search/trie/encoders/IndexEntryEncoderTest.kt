package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.services.search.IndexEntry

internal class IndexEntryEncoderTest : BaseEncoderTest<IndexEntry>() {
    @BeforeEach
    internal fun setUp() {
        encoder = IndexEntryEncoder()
        encoders = Encoders(setOf(encoder, BookEntryEncoder(), MapEncoder(), IntEncoder()))
    }

    @Test
    @DisplayName("Test encoding null")
    internal fun encodeNull() = assertEncodedValue(null, 104)

    @Test
    @DisplayName("Test encoding an empty entry")
    internal fun encodeEmptyIndexEntry() = assertEncodedValue(IndexEntry(), 100, 102, 0, 0, 0, 0, 103, 101)

    @Test
    @DisplayName("Test encoding an entry")
    internal fun encodeIndexEntry() {
        val entry = IndexEntry()
        entry.put(1, 42)

        assertEncodedValue(entry,
            100,
            102,
            0, 0, 0, 1,
            0, 0, 0, 1,
            100,
            102,
            0, 0, 0, 1,
            0, 0, 0, 42,
            0, 0, 0, 1,
            103,
            101,
            103,
            101
        )
    }
}