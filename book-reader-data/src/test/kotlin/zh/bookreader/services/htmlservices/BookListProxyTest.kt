package zh.bookreader.services.htmlservices

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import zh.bookreader.model.Book
import zh.bookreader.testutils.hamcrest.ZhMatchers.isEqualTo
import java.nio.file.Paths

@DisplayName("Test BookListProxy")
internal class BookListProxyTest {
    private companion object Constants {
        private val LIBRARY_URI = javaClass.classLoader.getResource("library/").toURI()
        private val EMPTY_LIBRARY_URI = javaClass.classLoader.getResource("emptyLibrary/").toURI()
    }

    private var list: List<Book> = listOf()

    private val book1 = TestBookConstants.getBook1()
    private val book2 = TestBookConstants.getBook2()

    @BeforeEach
    internal fun setUp() {
        list = BookListProxy(LIBRARY_URI)
    }

    @Test
    @DisplayName("Test size")
    internal fun testSize() {
        assertThat(list, hasSize(2))
    }

    @Test
    @DisplayName("Test contains()")
    internal fun testContains() {
        assertThat(list.contains(book1), `is`(true))
    }

    @Test
    @DisplayName("Test containsAll(")
    internal fun testContainsAll() {
        assertThat(list.containsAll(setOf(book1, book2)), `is`(true))
    }

    @Test
    @DisplayName("Test get()")
    internal fun testGet() {
        assertThat(list[0], isEqualTo(book1))
    }

    @Test
    @DisplayName("Test indexOf()")
    internal fun testIndexOf() {
        assertThat(list.indexOf(book2), `is`(1))
    }

    @Test
    @DisplayName("Test lastIndexOf")
    internal fun testLastIndexOf() {
        assertThat(list.lastIndexOf(book1), `is`(0))
    }

    @Test
    @DisplayName("Test isEmpty()")
    internal fun testIsEmpty() {
        list = BookListProxy(EMPTY_LIBRARY_URI)

        assertThat(list.isEmpty(), `is`(true))
    }

    @Test
    @DisplayName("Test subList()")
    internal fun testSubList() {
        val subList = list.subList(0, 1)
        assertThat(subList, hasSize(1))
        assertThat(subList[0], isEqualTo(book1))
        assertThat(subList.indexOf(book2), `is`(-1))
    }

    @Test
    @DisplayName("Test iterator()")
    internal fun testIterator() {
        val iterator = list.iterator()

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.next(), isEqualTo(book1))

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.next(), isEqualTo(book2))

        assertThat(iterator.hasNext(), `is`(false))
    }

    @Test
    @DisplayName("Test listIterator()")
    internal fun testListIterator() {
        val iterator = list.listIterator()

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.hasPrevious(), `is`(false))
        assertThat(iterator.nextIndex(), `is`(0))
        assertThat(iterator.previousIndex(), `is`(-1))
        assertThat(iterator.next(), isEqualTo(book1))

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.hasPrevious(), `is`(true))
        assertThat(iterator.nextIndex(), `is`(1))
        assertThat(iterator.previousIndex(), `is`(0))
        assertThat(iterator.next(), isEqualTo(book2))

        assertThat(iterator.hasNext(), `is`(false))
        assertThat(iterator.hasPrevious(), `is`(true))
        assertThat(iterator.nextIndex(), `is`(2))
        assertThat(iterator.previousIndex(), `is`(1))
        assertThat(iterator.previous(), isEqualTo(book2))

        assertThat(iterator.previous(), isEqualTo(book1))
    }

    @Test
    @DisplayName("Test listIterator(index)")
    internal fun testListIteratorWithIndex() {
        val iterator = list.listIterator(1)

        assertThat(iterator.hasNext(), `is`(true))
        assertThat(iterator.hasPrevious(), `is`(true))
        assertThat(iterator.nextIndex(), `is`(1))
        assertThat(iterator.previousIndex(), `is`(0))

        assertThat(iterator.next(), isEqualTo(book2))
        assertThat(iterator.previous(), isEqualTo(book2))
        assertThat(iterator.previous(), isEqualTo(book1))
    }

    @Test
    @DisplayName("When creating a list with not resolving URI, it should be empty")
    internal fun testCreatingListWithNonResolvingURI() {
        list = BookListProxy(Paths.get("fakeLibrary/").toUri())

        assertThat(list.isEmpty(), `is`(true))
    }
}