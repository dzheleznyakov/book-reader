package zh.bookreader.services.search.trie.encoders

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import zh.bookreader.services.search.SEARCH_PERSISTENCE_TRIE_PROFILE

@Component
@Profile(SEARCH_PERSISTENCE_TRIE_PROFILE)
class Encoders(encoders: Set<Encoder<*>>) {
    private val encoderRegister = HashMap<Class<*>, Encoder<*>>(encoders
        .map { it.encodedClass() to it }
        .toMap())

    @Suppress("UNCHECKED_CAST")
    fun <E> get(type: Class<E>): Encoder<E> = when {
        encoderRegister.containsKey(type) -> encoderRegister[type] as Encoder<E>
        Collection::class.java.isAssignableFrom(type) -> encoderRegister[Collection::class.java] as Encoder<E>
        else -> throw ClassNotRegistered(type)
    }

    class ClassNotRegistered(type: Class<*>) : RuntimeException("No encoder for class [${type}] found")
}