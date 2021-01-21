package zh.bookreader.services.search.trie.encoders

import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.DataOutputStream

internal class EncodersTest {
    private val integerEncoder = IntEncoder()
    private val stringEncoder = StringEncoder()
    private val collectionEncoder = CollectionEncoder()
    private val mapEncoder = MapEncoder()

    private lateinit var encoders: Encoders

    @BeforeEach
    internal fun setUp() {
        encoders = Encoders(setOf(integerEncoder, stringEncoder, collectionEncoder, mapEncoder))
    }

    @Test
    @DisplayName("Registered encoders can be extracted by class")
    internal fun getEncoderByClass() {
        var encoder: Encoder<*> = encoders.get(Int::class.java)

        assertSame(encoder, integerEncoder)

        encoder = encoders.get(String::class.java)

        assertSame(encoder, stringEncoder)
    }

    @Test
    @DisplayName("It should return the int encoder for Integer by default")
    internal fun getEncoderForInteger() {
        val encoder: Encoder<*> = encoders.get(Integer::class.java)

        assertSame(encoder, integerEncoder)
    }

    @Test
    @DisplayName("Should return the general map encoder for a map by default")
    internal fun getEncoderForMap() {
        val encoder: Encoder<*> = encoders.get(LinkedHashMap::class.java)

        assertSame(encoder, mapEncoder)
    }

    @Test
    @DisplayName("Should return the general collection encoder for collections by default")
    internal fun getEncoderForCollection() {
        val encoder: Encoder<*> = encoders.get(CollectionClass::class.java)

        assertSame(encoder, collectionEncoder)
    }

    @Test
    @DisplayName("Should return a specific collection encoder if it registered before the general one")
    internal fun getSpecificEncoderForCollectionClass() {
        val collectionClassEncoder = CollectionClassEncoder()
        encoders = Encoders(setOf(collectionEncoder, collectionClassEncoder))

        val encoder: Encoder<*> = encoders.get(CollectionClass::class.java)

        assertSame(encoder, collectionClassEncoder)
    }

    @Test
    @DisplayName("It should throw if there is no registered encoder for a class")
    internal fun throwIfClassIsNotRegistered() {
        assertThrows<Encoders.ClassNotRegistered> { encoders.get(Short::class.java) }
    }

    private class CollectionClass<E>(set: HashSet<E>) : Collection<E> by set

    private class CollectionClassEncoder : Encoder<CollectionClass<*>> {
        override fun encode(out: DataOutputStream, value: CollectionClass<*>?, encoders: Encoders) {}

        override fun encodedClass() = CollectionClass::class.java
    }
}