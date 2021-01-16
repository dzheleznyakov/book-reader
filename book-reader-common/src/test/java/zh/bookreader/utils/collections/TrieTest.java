package zh.bookreader.utils.collections;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrieTest {
    private Trie<Integer> trie;

    @BeforeEach
    void setUp() {
        trie = new Trie<>();
    }

    @Test
    void testContains_throwIfPathContainsNotOnlyLowerCaseLetters() {
        assertThrows(Trie.InvalidPath.class, () -> trie.contains("abC"));
    }

    @Test
    void emptyTrieDoesNotContainAnyPath() {
        assertThat(trie.contains("path"), is(false));
    }

    @Test
    void testGet_throwIfPathContainsNotOnlyLowerCaseLetters() {
        assertThrows(Trie.InvalidPath.class, () -> trie.get("abC"));
    }

    @Test
    void emptyTrieReturnEmptySetForAPath() {
        assertThat(trie.get("path"), is(empty()));
    }

    @Test
    void testPut_throwIfPathContainsNotOnlyLowerCaseLetters() {
        assertThrows(Trie.InvalidPath.class, () -> trie.put("abC", 1));
    }

    @Test
    void whenPuttingValueOnPath_ValueIsAvailable() {
        String path = "path";
        Integer value = 1;

        trie.put(path, value);

        assertThat(trie.contains(path), is(true));
        assertThat(trie.get(path), is(equalTo(ImmutableList.of(value))));
    }

    @Test
    void streamNodesInPreorder() {
        trie.put("ampersand", 1);
        trie.put("banana", 2);
        trie.put("apple", 3);

        String triePhrase = trie.streamNodesInPreorder()
                .filter(node -> node.getLabel() != TrieNode.ROOT_LABEL)
                .flatMap(node -> {
                    ImmutableList.Builder<String> builder = ImmutableList.builder();
                    builder.add(String.valueOf(node.getLabel()));
                    if (!node.getValues().isEmpty())
                        builder.add("->" + node.getValues());
                    return builder.build().stream();
                })
                .collect(Collectors.joining());

        assertThat(triePhrase, is(equalTo("ampersand->[1]pple->[3]banana->[2]")));
    }
}