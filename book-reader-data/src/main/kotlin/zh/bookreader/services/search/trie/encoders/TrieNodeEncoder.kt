package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE
import zh.bookreader.utils.collections.TrieNode
import java.io.PrintWriter

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class TrieNodeEncoder() : Encoder<TrieNode<*>> {
    companion object Codes {
        const val NULL_NODE_CODE = -1
        const val NODE_START_CODE = '<'
        const val NODE_END_CODE = '>'
    }

    override fun encode(out: PrintWriter, value: TrieNode<*>?, encoders: Encoders) {
        if (value != null)
            out.append(NODE_START_CODE)
                .append(value.label)
                .writeValues(value, encoders)
                .writeChildren(value, encoders)
                .append(NODE_END_CODE)
        out.flush()
    }

    @Suppress("UNCHECKED_CAST")
    private fun PrintWriter.writeValues(node: TrieNode<*>, encoders: Encoders): PrintWriter = apply {
        encoders
            .get(node.values::class.java as Class<MutableList<*>>)
            .encode(this, node.values, encoders)
    }

    private fun PrintWriter.writeChildren(node: TrieNode<*>, encoders: Encoders): PrintWriter = apply {
        if (node.children != null)
            node.children
                .filterNotNull()
                .forEach { encode(this, it, encoders) }
    }

    override fun encodedClass() = TrieNode::class.java
}