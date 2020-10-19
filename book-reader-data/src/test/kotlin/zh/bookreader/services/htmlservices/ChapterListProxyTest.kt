package zh.bookreader.services.htmlservices

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.instanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.model.documents.Chapter
import java.io.File

internal class ChapterListProxyTest {
    @Suppress("JAVA_CLASS_ON_COMPANION")
    companion object {
        private val BOOK_FILE = javaClass.classLoader.getResource("library/book-one").toURI().run { File(this) }
        private val TOC = listOf("preface.html", "ch01.html", "ch02.html", "app.html");
    }

    private var list: List<Chapter> = listOf()

    private val ch01 = Chapter().apply { id = "ch01" }
    private val ch02 = Chapter().apply { id = "ch02" }
    private val ch03 = Chapter().apply { id = "ch03" }

    @BeforeEach
    internal fun setUp() {
        list = ChapterListProxy(BOOK_FILE, TOC)
    }

    @Test
    @DisplayName("Test size")
    internal fun testSize() {
        assertThat(list, hasSize(4))
    }

    @Test
    @DisplayName("Test contains()")
    internal fun testContains() {
        assertThat(list.contains(ch01), `is`(true))
    }

    @Test
    @DisplayName("Test containsAll(): expect true")
    internal fun testContainsAll_Positive() {
        val chapters = listOf(ch01, ch02)

        assertThat(list.containsAll(chapters), `is`(true))
    }

    @Test
    @DisplayName("Test containsAll(): expect false")
    internal fun testContainsAll_Negative() {
        val chapters = listOf(ch01, ch03)

        assertThat(list.containsAll(chapters), `is`(false))
    }

    @Test
    @DisplayName("Test get()")
    internal fun testGet() {
        val ch = list[1]

        assertThat(ch.id, `is`(equalTo("ch01")))
    }

    @Test
    @DisplayName("Test indexOf(): expect found")
    internal fun testIndexOf_Positive() {
        val ind = list.indexOf(ch01)

        assertThat(ind, `is`(1))
    }

    @Test
    @DisplayName("Test indexOf(): expect not found")
    internal fun testIndexOf_Negative() {
        val ind = list.indexOf(ch03)

        assertThat(ind, `is`(-1))
    }

    @Test
    @DisplayName("Test lastIndexOf()")
    internal fun testLastIndexOf() {
        val ind = list.lastIndexOf(ch02)

        assertThat(ind, `is`(2))
    }

    @Test
    @DisplayName("Test isEmpty(): should be not empty")
    internal fun testIsEmpty_Negative() {
        assertThat(list.isEmpty(), `is`(false))
    }

    @Test
    @DisplayName("Test isEmpty(): should be empty")
    internal fun testIsEmpty_Positive() {
        list = ChapterListProxy(BOOK_FILE, listOf())

        assertThat(list.isEmpty(), `is`(true))
    }

    @Test
    @DisplayName("Test subList()")
    internal fun testSubList() {
        val subList = list.subList(1, 3)

        assertThat(subList, instanceOf(ChapterListProxy::class.java))
        assertThat(subList.size, `is`(2))
        assertThat(subList.containsAll(listOf(ch01, ch02)), `is`(true))
    }

    @Test
    @DisplayName("Test iterator()")
    internal fun testIterator() {
        val iterator = list.iterator()

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.next().id, `is`(equalTo("preface")))

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.next().id, `is`(equalTo("ch01")))

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.next().id, `is`(equalTo("ch02")))

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.next().id, `is`(equalTo("app")))
    }

    @Test
    @DisplayName("Test listIterator()")
    internal fun testListIterator() {
        val iterator = list.listIterator();

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.hasPrevious(), `is`(false))
        assertThat(iterator.nextIndex(), `is`(0))
        assertThat(iterator.previousIndex(), `is`(-1))
        assertThat(iterator.next().id, `is`(equalTo("preface")))

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.hasPrevious(), `is`(true))
        assertThat(iterator.nextIndex(), `is`(1))
        assertThat(iterator.previousIndex(), `is`(0))
        assertThat(iterator.next().id, `is`(equalTo("ch01")))

        iterator.next()
        iterator.next()

        assertThat(iterator.hasNext(), `is`(false))
        assertThat(iterator.hasPrevious(), `is`(true))
        assertThat(iterator.nextIndex(), `is`(4))
        assertThat(iterator.previousIndex(), `is`(3))
        assertThat(iterator.previous().id, `is`(equalTo("app")))
    }

    @Test
    @DisplayName("Test listIterator(index)")
    internal fun testListIterator_withIndex() {
        val iterator = list.listIterator(2)

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.hasPrevious(), `is`(true))
        assertThat(iterator.nextIndex(), `is`(2))
        assertThat(iterator.previousIndex(), `is`(1))
        assertThat(iterator.next().id, `is`(equalTo("ch02")))

        assertThat(iterator.previous().id, `is`(equalTo("ch02")))
    }
}