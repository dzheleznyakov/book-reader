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
    internal fun encodeNull() = assertEncodedValue(null, "")

    @Test
    @DisplayName("Test encoding an empty BookEntry")
    internal fun encodeEmptyBookEntry() = assertEncodedValue(BookEntry(), "0|")

    @Test
    @DisplayName("Test encoding a BookEntry")
    internal fun encodeBookEntry() {
        val bookEntry = BookEntry()
        bookEntry.increaseCount(1)
        bookEntry.increaseCount(1)
        bookEntry.increaseCount(1)
        bookEntry.increaseCount(2)

        assertEncodedValue(bookEntry, "4|1,3,2,1#")
    }
}