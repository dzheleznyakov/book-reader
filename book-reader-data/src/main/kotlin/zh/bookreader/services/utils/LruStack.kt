package zh.bookreader.services.utils

import java.util.EmptyStackException
import java.util.LinkedList
import java.util.Optional

class LruStack<E>(private val capacity: Int) : Iterable<E> {
    private var _size: Int = 0
    val size: Int
        get() = _size

    private var head: Node<E>? = null
    private var tail: Node<E>? = null

    fun isEmpty() = size == 0

    fun push(e: E) {
        if (size == capacity)
            removeTail()
        if (_size < capacity)
            _size += 1
        addToHead(Node(e))
    }

    fun pop(): Optional<E> {
        if (size > 0)
            _size -= 1

        val h = head
        head = h?.next
        return Optional.ofNullable(h?.value)
    }

    fun peek(): E? = head?.value ?: throw EmptyStackException()

    fun findBy(predicate: (E) -> Boolean): Optional<E> = NodeIterator().run {
        while (hasNext()) {
            val node = next()
            if (predicate(node.value)) {
                unlink(node)
                addToHead(node)
                return@run node.value
            }
        }
        null
    }.run {
        Optional.ofNullable(this)
    }

    fun findAllBy(predicate: (E) -> Boolean): List<E> = NodeIterator().run {
        val nodes = LinkedList<Node<E>>()
        while (hasNext()) {
            val node = next()
            if (predicate(node.value)) {
                unlink(node)
                nodes.add(node)
            }
        }
        nodes
    }.apply {
        for (i in indices.reversed())
            addToHead(this[i])
    }.run {
        map { it.value }
    }

    private fun addToHead(node: Node<E>) = node.apply {
        if (head == null)
            head = this
        else {
            head!!.prev = this
            this.next = head
            head = this
        }

        if (tail == null)
            tail = this
    }

    private fun removeTail() =
            if (tail == null)
                null
            else {
                val prev = tail!!.prev
                prev!!.next = null
                tail!!.prev = null
                val t = tail
                tail = prev
                t
            }

    private fun unlink(node: Node<E>) {
        val prev = node.prev
        val next = node.next

        if (prev != null) {
            node.prev = null
            prev.next = next
        }

        if (next != null) {
            node.next = null
            next.prev = prev
        }
    }

    override fun iterator(): Iterator<E> = ValueIterator()

    class Node<E>(val value: E) {
        var prev: Node<E>? = null
        var next: Node<E>? = null
    }

    inner class ValueIterator : Iterator<E> {
        private val nodeIterator = NodeIterator()

        override fun hasNext() = nodeIterator.hasNext()

        override fun next(): E = nodeIterator.next().value
    }

    inner class NodeIterator : Iterator<Node<E>> {
        private var curr: Node<E>? = head

        override fun hasNext() = curr != null

        override fun next(): Node<E> {
            val el = curr ?: throw IllegalStateException("There is no next element")
            return el.also { curr = el.next }
        }
    }
}
