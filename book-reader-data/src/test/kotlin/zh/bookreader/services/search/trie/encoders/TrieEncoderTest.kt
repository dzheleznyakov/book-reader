package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.utils.collections.Trie

internal class TrieEncoderTest : BaseEncoderTest<Trie<*>>() {
    @BeforeEach
    internal fun setUp() {
        encoder = TrieEncoder()
        encoders = Encoders(setOf(encoder, TrieNodeEncoder(), CollectionEncoder(), IntEncoder()))
    }

    @Test
    @DisplayName("Test encoding null trie")
    internal fun encodeNull() {
        assertEncodedValue(null, 104)
    }

    @Test
    @DisplayName("Encode an empty trie")
    internal fun encodeEmptyTrie() {
        assertEncodedValue(Trie<Int>(), 102, 100, 104, -26, 101, 103)
    }

    @Test
    @DisplayName("Encode a simple shallow trie")
    internal fun encodeShallowTrie() {
        val trie = Trie<Int>()
        trie.put("a", 1)
        trie.put("z", 2)

        assertEncodedValue(trie,
            102,
            100, 104, 1, -24, 1, 101,
            100, 102, 0, 0, 0, 1, 0, 0, 0, 1, 103, -26, 101,
            100, 102, 0, 0, 0, 1, 0, 0, 0, 2, 103, -26, 101,
            103
        )
    }

    @Test
    @DisplayName("Encode a deeper trie")
    internal fun encodeDeepTree() {
        val trie = Trie<Int>()
        trie.put("uno", 1)
        trie.put("unico", 11)
        trie.put("dos", 2)
        trie.put("un", 111)

        assertEncodedValue(trie,
            102,
            100, 104, -3, 1, -16, 1, -5, 101, // -
            100, 104, -14, 1, -11, 101, // d
            100, 104, -18, 1, -7, 101, // do
            100, 102, 0, 0, 0, 1, 0, 0, 0, 2, 103, -26, 101, //dos[2]
            100, 104, -13, 1, -12, 101, // u
            100, 102, 0, 0, 0, 1, 0, 0, 0, 111, 103, -8, 1, -5, 1, -11, 101, // un[111]
            100, 104, -2, 1, -23, 101, // uni
            100, 104, -14, 1, -11, 101, // unic
            100, 102, 0, 0, 0, 1, 0, 0, 0, 11, 103, -26, 101, // unico[11]
            100, 102, 0, 0, 0, 1, 0, 0, 0, 1, 103, -26, 101, // uno[1]
            103
        )
    }
}