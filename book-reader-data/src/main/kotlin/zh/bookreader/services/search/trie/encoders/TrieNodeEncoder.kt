package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import zh.bookreader.utils.collections.TrieNode
import java.io.DataOutputStream

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class TrieNodeEncoder() : Encoder<TrieNode<*>> {
    companion object Codes {
        const val NULL_NODE_CODE = -1
        const val NODE_START_CODE = 100
        const val NODE_END_CODE = 101
    }

    override fun encode(out: DataOutputStream, value: TrieNode<*>?, encoders: Encoders) {
        if (value == null)
            out.writeByte(NULL_NODE_CODE)
        else {
            out.writeByte(NODE_START_CODE)
            out.writeValues(value, encoders)
            out.writeChildrenMetadata(value)
            out.writeByte(NODE_END_CODE)
        }
        out.flush()
    }

    @Suppress("UNCHECKED_CAST")
    private fun DataOutputStream.writeValues(node: TrieNode<*>, encoders: Encoders) = encoders
        .get(node.values::class.java as Class<MutableList<*>>)
        .encode(this, node.values, encoders)

    private fun DataOutputStream.writeChildrenMetadata(node: TrieNode<*>) {
        var ind = 0
        while (ind <= 'z' - 'a') {
            val absentChildrenCount = node.countAbsentChildren(ind)
            if (absentChildrenCount > 0)
                writeByte(-absentChildrenCount)
            ind += absentChildrenCount

            val existingChildrenCount = node.countExistingChildren(ind)
            if (existingChildrenCount > 0)
                writeByte(existingChildrenCount)
            ind += existingChildrenCount
        }
    }

    private fun TrieNode<*>.countAbsentChildren(from: Int) =
        countChildren(from) { ind -> !hasChild((ind + 'a'.toInt()).toChar()) }

    private fun TrieNode<*>.countExistingChildren(from: Int) =
        countChildren(from) { ind -> hasChild((ind + 'a'.toInt()).toChar()) }

    private fun TrieNode<*>.countChildren(ind: Int, shouldCountChild: TrieNode<*>.(Int) -> Boolean): Int {
        var pos = ind
        while (pos <= 'z' - 'a' && shouldCountChild(pos))
            ++pos
        return pos - ind;
    }

    override fun encodedClass() = TrieNode::class.java
}