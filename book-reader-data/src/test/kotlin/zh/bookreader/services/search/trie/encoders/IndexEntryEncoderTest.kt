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
    internal fun encodeNull() = assertEncodedValue(null, "")

    @Test
    @DisplayName("Test encoding an empty entry")
    internal fun encodeEmptyIndexEntry() = assertEncodedValue(IndexEntry(), "")

    @Test
    @DisplayName("Test encoding an entry")
    internal fun encodeIndexEntry() {
        val entry = IndexEntry()
        entry.put(1, 42)

        assertEncodedValue(entry, "1,1|42,1##")
    }
}