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
        assertEncodedValue(null, "<>")
    }

    @Test
    @DisplayName("Encode an empty trie")
    internal fun encodeEmptyTrie() {
        assertEncodedValue(Trie<Int>(), "<->")
    }

    @Test
    @DisplayName("Encode a simple shallow trie")
    internal fun encodeShallowTrie() {
        val trie = Trie<Int>()
        trie.put("a", 1)
        trie.put("z", 2)

        assertEncodedValue(trie, "<-<a1#><z2#>>")
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
            "<-" +
                "<d" +
                    "<o" +
                        "<s2#>" +
                    ">" +
                ">" +
                "<u" +
                    "<n111#" +
                        "<i" +
                            "<c" +
                                "<o11#>" +
                            ">" +
                        ">" +
                        "<o1#>" +
                    ">" +
                ">" +
            ">")
    }
}