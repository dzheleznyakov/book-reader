package zh.bookreader.services.utils

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.EmptyStackException

@DisplayName("Test LRU Stack")
internal class LruStackTest {
    @Nested
    @DisplayName("Test size and capacity of the LRU stack")
    inner class TestSizeAndCapacity {
        @Test
        @DisplayName("LRU Stack is created with zero size")
        internal fun lruStackIsCreatedWithZeroSize() {
            val stack = LruStack<Int>(10)

            assertThat(stack.size, `is`(0))
        }

        @Test
        @DisplayName("When pushing to the stack, its size increases")
        internal fun whenPushingToStackSizeIncreasing() {
            val stack = LruStack<Int>(10)

            stack.push(0)
            stack.push(1)
            stack.push(2)

            assertThat(stack.size, `is`(3))
        }

        @Test
        @DisplayName("Stack size cannot exceed its capacity")
        internal fun sizeCannotExceedCapacity() {
            val capacity = 2
            val stack = LruStack<Int>(capacity)

            stack.push(0)
            stack.push(1)
            stack.push(2)

            assertThat(stack.size, `is`(capacity))
        }

        @Test
        @DisplayName("When popping from an empty stack, an empty optional is returned")
        internal fun whenPoppingFromEmptyStackThrow() {
            val stack = LruStack<Int>(10)

            val v = stack.pop()

            assertThat(v.isEmpty, `is`(true))
        }

        @Test
        @DisplayName("When popping from a stack, its size decreases")
        internal fun whenPoppingFromNotEmptyStackSizeDecreases() {
            val stack = LruStack<Int>(10)

            stack.push(0)
            stack.push(1)
            stack.push(2)

            stack.pop()
            stack.pop()

            assertThat(stack.size, `is`(1))
        }

        @Test
        @DisplayName("Test that the newly creates stack is empty")
        internal fun newStackIsEmpty() {
            val stack = LruStack<Int>(10)

            assertThat(stack.isEmpty(), `is`(true))
        }

        @Test
        @DisplayName("When adding something to stack, it is not empty")
        internal fun whenAddingToStackItIsNotEmpty() {
            val stack = LruStack<Int>(10)

            stack.push(1)

            assertThat(stack.isEmpty(), `is`(false))
        }

        @Test
        @DisplayName("When popping everything from a stack, it becomes not empty")
        internal fun whenPoppingAllElementsFromStackItBecomesEmpty() {
            val stack = LruStack<Int>(10)

            stack.push(0)
            stack.push(1)
            stack.push(2)

            stack.pop()
            stack.pop()
            stack.pop()

            assertThat(stack.isEmpty(), `is`(true))
        }
    }

    @Nested
    @DisplayName("Test the stack behaviours and functionalities")
    inner class TestStackFunctionalities {
        private val capacity = 3
        private lateinit var stack: LruStack<Int>

        @BeforeEach
        internal fun setUpStack() {
            stack = LruStack(capacity)
        }

        @Test
        @DisplayName("When pushing elements to stack, the latest pushed element comes first")
        internal fun whenPushingElementsIntoStackTheLatestPushedElementComesFirst() {
            stack.push("0 1 2")

            val actual = stack.print()
            val expected = "2 1 0"
            assertThat(actual, `is`(equalTo(expected)))
        }

        @Test
        @DisplayName("Test popping elements from the stack")
        internal fun testPop() {
            stack.push("0 1 2")

            val v1 = stack.pop()
            assertThat(v1.get(), `is`(2))
            assertThat(stack.print(), `is`(equalTo("1 0")))

            val v2 = stack.pop()
            assertThat(v2.get(), `is`(1))
            assertThat(stack.print(), `is`(equalTo("0")))

            val v3 = stack.pop()
            assertThat(v3.get(), `is`(0))
            assertThat(stack.print(), `is`(equalTo("")))
        }

        @Test
        @DisplayName("When the number of pushed element exceeds the capacity, the last element pushed out of the stack")
        internal fun whenCapacityIsExceededLastElementPushedOut() {
            stack.push("0 1 2")

            assertThat(stack.print(), `is`(equalTo("2 1 0")))

            stack.push(3)

            assertThat(stack.print(), `is`(equalTo("3 2 1")))
        }

        @Test
        @DisplayName("findBy(predicate) returns the first element satisfying the predicate")
        internal fun testFindBy() {
            stack.push("0 1 2")

            val valueOptional = stack.findBy { it < 2 }

            assertThat(valueOptional.isPresent(), `is`(true))
            assertThat(valueOptional.get(), `is`(1))
        }

        @Test
        @DisplayName("findBy(predicate) returns nothing if the predicate does not match any element")
        internal fun testFindByWhenPredicateDoesNotMatch() {
            stack.push("0 1 2")

            val valueOptional = stack.findBy { it > 2 }

            assertThat(valueOptional.isEmpty, `is`(true))
        }

        @Test
        @DisplayName("findBy(predicate) moves the found element to the top")
        internal fun testFindByMovesFoundElementFirst() {
            stack.push("0 1 2")

            stack.findBy { it < 2 }

            assertThat(stack.print(), `is`(equalTo("1 2 0")))
        }

        @Test
        @DisplayName("findAllBy(predicate) returns all elements satisfying the predicate")
        internal fun testFindAllBy() {
            stack.push("0 1 2")

            val elements = stack.findAllBy { it < 2 }

            assertThat(elements, `is`(equalTo(listOf(1, 0))))
        }

        @Test
        @DisplayName("findAllBy(predicate) returns empty list if predicate does not match any element")
        internal fun testFindByAllWhenPredicateDoesNotMatch() {
            stack.push("0 1 2")

            val elements = stack.findAllBy { it > 2 }

            assertThat(elements, `is`(equalTo(listOf())))
        }

        @Test
        @DisplayName("findAllBy(predicate) moves the found elements to the top")
        internal fun testFindByAllBringsElementsToHead() {
            stack.push("0 1 2")

            stack.findAllBy { it < 2 }

            assertThat(stack.print(), `is`(equalTo("1 0 2")))
        }

        @Test
        @DisplayName("peek() shows the head element, but does not remove it")
        internal fun testPeek() {
            stack.push("0 1 2")

            val element = stack.peek()

            assertThat(element, `is`(2))
            assertThat(stack.size, `is`(3))
            assertThat(stack.print(), `is`(equalTo("2 1 0")))
        }

        @Test
        @DisplayName("peek() on empty stack throws empty stack exception")
        internal fun testPeekOnEmptyStack() {
            assertThrows(EmptyStackException::class.java) { stack.peek() }
        }

        private fun LruStack<Int>.push(pattern: String) = pattern.split(" ")
                .map(String::toInt)
                .forEach(this::push)
        private fun LruStack<Int>.print() = asSequence().joinToString(" ")
    }
}