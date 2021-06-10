package zh.bookreader.services.search.trie.encoders

import com.google.common.collect.ImmutableList
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.utils.collections.Trie
import zh.bookreader.utils.collections.TrieNode
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.util.stream.Collectors

@DisplayName("TrieEncoder Test")
internal class TrieEncoderTest {
//    @BeforeEach
//    internal fun setUp() {
//        encoder = TrieEncoder()
//        encoders = Encoders(setOf(encoder, TrieNodeEncoder(), CollectionEncoder(), IntEncoder()))
//    }
//
//    @Test
//    @DisplayName("Test encoding null trie")
//    internal fun encodeNull() {
//        assertEncodedValue(null, 104)
//    }
//
//    @Test
//    @DisplayName("Encode an empty trie")
//    internal fun encodeEmptyTrie() {
//        assertEncodedValue(Trie<Int>(), 102, 100, 104, -26, 101, 103)
//    }
//
//    @Test
//    @DisplayName("Encode a simple shallow trie")
//    internal fun encodeShallowTrie() {
//        val trie = Trie<Int>()
//        trie.put("a", 1)
//        trie.put("z", 2)
//
//        assertEncodedValue(trie,
//            102,
//            100, 104, 1, -24, 1, 101,
//            100, 102, 0, 0, 0, 1, 0, 0, 0, 1, 103, -26, 101,
//            100, 102, 0, 0, 0, 1, 0, 0, 0, 2, 103, -26, 101,
//            103
//        )
//    }
//
//    @Test
//    @DisplayName("Encode a deeper trie")
//    internal fun encodeDeepTree() {
//        val trie = Trie<Int>()
//        trie.put("uno", 1)
//        trie.put("unico", 11)
//        trie.put("dos", 2)
//        trie.put("un", 111)
//
//        assertEncodedValue(trie,
//            102,
//            100, 104, -3, 1, -16, 1, -5, 101, // -
//            100, 104, -14, 1, -11, 101, // d
//            100, 104, -18, 1, -7, 101, // do
//            100, 102, 0, 0, 0, 1, 0, 0, 0, 2, 103, -26, 101, //dos[2]
//            100, 104, -13, 1, -12, 101, // u
//            100, 102, 0, 0, 0, 1, 0, 0, 0, 111, 103, -8, 1, -5, 1, -11, 101, // un[111]
//            100, 104, -2, 1, -23, 101, // uni
//            100, 104, -14, 1, -11, 101, // unic
//            100, 102, 0, 0, 0, 1, 0, 0, 0, 11, 103, -26, 101, // unico[11]
//            100, 102, 0, 0, 0, 1, 0, 0, 0, 1, 103, -26, 101, // uno[1]
//            103
//        )
//    }

    private lateinit var encoder: Encoder<Trie<*>>
    private lateinit var encoders: Encoders
    private lateinit var bytes: ByteArrayOutputStream
    private lateinit var out: DataOutputStream

    @BeforeEach
    internal fun setUp() {
        encoder = TrieEncoder()
        encoders = Encoders(setOf(encoder, IntegerEncoder()))
        bytes = ByteArrayOutputStream()
        out = DataOutputStream(bytes)
    }

    @Test
    @DisplayName("Test encoding null trie")
    internal fun encodeNull()  = assertEncodedTrie(null, "")

    @Test
    @DisplayName("Test encoding an empty trie")
    internal fun encodeEmptyTrie() = assertEncodedTrie(Trie(), "")

    @Test
    @DisplayName("Test trie containing a single branch")
    internal fun encodeOneWordTrie() = assertEncodedTrie(
        Trie<Int>().apply { put("one", 1) },
        "one[1]"
    )

    private fun assertEncodedTrie(trie: Trie<Int>?, expected: String) {
        encoder.encode(out, trie, encoders)

        val actual = String(bytes.toByteArray())
        assertThat(expected, `is`(equalTo(actual)))
    }

    @Test
    internal fun temp() {
        val trie = Trie<Int>()
        trie.put("uno", 1)
        trie.put("unico", 11)
        trie.put("dos", 2)
        trie.put("un", 111)
        trie.put("doce", 12)

        val triePhrase = trie.streamNodesInPreorder()
            .filter{ it.label != TrieNode.ROOT_LABEL}
            .map { node ->
                val builder: ImmutableList.Builder<String> = ImmutableList.builder()
                builder.add(node.label.toString())
                if (node.values != null && node.values.isNotEmpty()) {
                    val isTerminalNode = node.children.filterNotNull().count() == 0
                    val value = node.values[0]
                    if (isTerminalNode) builder.add("[").add(value.toString()).add("]").add(node.level.toString())
                    else builder.add("(").add(value.toString()).add(")")
                }
                return@map builder.build()
            }
            .flatMap { it.stream() }
            .collect(Collectors.joining())

        println(triePhrase)
        triePhrase.toByteArray().apply { println(size) }
            .forEach { print("${it} ") }
        println()
        println()

        val s = "uno[1]unico[11]un[111]dos[2]doce[12]"
        println(s)
        s.toByteArray().apply { println(size) }
            .forEach { print("${it} ") }
    }
}

/*
- d o c e
      s
  u n i c o
      o << 3
 */