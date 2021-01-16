package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.utils.collections.TrieNode
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

internal class TrieNodeEncoderTest {
    private lateinit var encoders: Encoders
    private lateinit var encoder: TrieNodeEncoder

    private lateinit var bytes: ByteArrayOutputStream
    private lateinit var out: DataOutputStream

    @BeforeEach
    internal fun setUp() {
        encoder = TrieNodeEncoder()
        encoders = Encoders(setOf(encoder, IntEncoder(), CollectionEncoder()))

        bytes = ByteArrayOutputStream()
        out = DataOutputStream(bytes)
    }

    @Test
    @DisplayName("Test encoding a null node")
    internal fun encodeNull() {
        assertEncodedTrieNode(null, -1)
    }

    @Test
    @DisplayName("Test encoding a node with no value and no children")
    internal fun encodeEmptyNode() {
        assertEncodedTrieNode(
            TrieNode('a'),
            100, 104, -26, 101)
    }

    @Test
    @DisplayName("Test encoding terminal node with one value")
    internal fun encodeTerminalNodeWithOneValue() {
        val node = TrieNode<Int>('a')
        node.add(10)

        assertEncodedTrieNode(node,
            100, 102, 0, 0, 0, 1, 0, 0, 0, 10, 103, -26, 101)
    }

    @Test
    @DisplayName("Test encoding terminal node with multiple values")
    internal fun encodeTerminalNodeWithMultipleValues() {
        val node = TrieNode<Int>('a')
        node.add(42)
        node.add(43)
        node.add(44)

        assertEncodedTrieNode(node,
            100,
            102,
            0, 0, 0, 3,
            0, 0, 0, 42,
            0, 0, 0, 43,
            0, 0, 0, 44,
            103,
            -26,
            101
        )
    }

    @Test
    @DisplayName("Encode a node with one child at 'a'")
    internal fun encodeValuelessNode_ChildA() {
        val node = TrieNode<Int>('-')
        val childA = TrieNode<Int>('a')
        node.setChild('a', childA)

        assertEncodedTrieNode(node,
            100,
            104,
            1, -25,
            101
        )
    }

    @Test
    @DisplayName("Encode a node with two children at 'a' and 'b'")
    internal fun encodeValuelessNode_ChildA_ChildB() {
        val node = TrieNode<Int>('-')
        val childA = TrieNode<Int>('a')
        val childB = TrieNode<Int>('b')
        node.setChild('a', childA)
        node.setChild('b', childB)

        assertEncodedTrieNode(node,
            100,
            104,
            2, -24,
            101
        )
    }

    @Test
    @DisplayName("Encode a node with two children at 'c' and 'd'")
    internal fun encodeValuelessNode_ChildC_ChildD() {
        val node = TrieNode<Int>('-')
        val childC = TrieNode<Int>('c')
        val childD = TrieNode<Int>('d')
        node.setChild('c', childC)
        node.setChild('d', childD)

        assertEncodedTrieNode(node,
            100,
            104,
            -2, 2, -22,
            101
        )
    }

    @Test
    @DisplayName("Encode a node with five children at 'c', 'd', 'l', 'm' and 'n'")
    internal fun encodeValuelessNode_ChildC_ChildD_ChildrenLToN() {
        val node = TrieNode<Int>('-')
        val childC = TrieNode<Int>('c')
        val childD = TrieNode<Int>('d')
        val childL = TrieNode<Int>('l')
        val childM = TrieNode<Int>('m')
        val childN = TrieNode<Int>('n')
        node.setChild('c', childC)
        node.setChild('d', childD)
        node.setChild('l', childL)
        node.setChild('m', childM)
        node.setChild('n', childN)

        assertEncodedTrieNode(node,
            100,
            104,
            -2, 2, -7, 3, -12,
            101
        )
    }

    private fun assertEncodedTrieNode(node: TrieNode<Int>?, vararg expectedBytes: Byte) {
        encoder.encode(out, node, encoders)

        assertArrayEquals(expectedBytes, bytes.toByteArray())
    }

    private fun ByteArray.concat(other: ByteArray): ByteArray {
        val res = ByteArray(this.size + other.size)
        System.arraycopy(this, 0, res, 0, this.size)
        System.arraycopy(other, 0, res, this.size, other.size)
        return res;
    }
}