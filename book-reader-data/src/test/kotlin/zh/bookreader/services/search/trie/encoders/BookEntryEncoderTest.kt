package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.services.search.BookEntry

internal class BookEntryEncoderTest : BaseEncoderTest<BookEntry>() {
    @BeforeEach
    internal fun setUp() {
        encoder = BookEntryEncoder()
        encoders = Encoders(setOf(encoder, MapEncoder(), IntEncoder()))
    }

    @Test
    @DisplayName("Test encoding null BookEntry")
    internal fun encodeNull() = assertEncodedValue(null, 104)

    @Test
    @DisplayName("Test encoding an empty BookEntry")
    internal fun encodeEmptyBookEntry() = assertEncodedValue(BookEntry(), 100, 102, 0, 0, 0, 0, 103, 101)

    @Test
    @DisplayName("Test encoding a BookEntry")
    internal fun encodeBookEntry() {
        val bookEntry = BookEntry()
        bookEntry.increaseCount(1)
        bookEntry.increaseCount(1)
        bookEntry.increaseCount(1)
        bookEntry.increaseCount(2)

        assertEncodedValue(bookEntry,
            100,
            102,
            0, 0, 0, 2,
            0, 0, 0, 1,
            0, 0, 0, 3,
            0, 0, 0, 2,
            0, 0, 0, 1,
            103,
            101
        )
    }
}