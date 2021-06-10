package zh.bookreader.utils.collections;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Trie<T> implements Iterable<TrieNode<T>> {
    private final TrieNode<T> root;

    public Trie() {
        root = new TrieNode<>();
    }

    public boolean contains(String path) {
        validatePath(path);
        return getDescendant(path).isPresent();
    }

    @Nonnull
    public List<T> get(String path) {
        validatePath(path);
        return getDescendant(path)
                .map(TrieNode::getValues)
                .map(ImmutableList::copyOf)
                .orElseGet(ImmutableList::of);
    }

    public void put(String path, @Nonnull T value) {
        validatePath(path);

        TrieNode<T> current = root;
        for (int i = 0; i < path.length(); ++i) {
            char ch = path.charAt(i);
            int index = ch - 'a';
            current = ensureChild(current, ch);
        }
        current.add(value);
    }

    @Nonnull
    private Optional<TrieNode<T>> getDescendant(@Nonnull String path) {
        TrieNode<T> current = root;
        for (int i = 0; i < path.length() && current != null; ++i) {
            char ch = path.charAt(i);
            int index = ch - 'a';
            current = current.getChildren().get(index);
        }
        return Optional.ofNullable(current);
    }

    @Nonnull
    private TrieNode<T> ensureChild(TrieNode<T> node, char ch) {
        if (!node.hasChild(ch))
            node.setChild(ch, new TrieNode<T>(ch, node.getLevel() + 1));
        return node.getChild(ch);
    }

    private void validatePath(String path) {
        path.chars()
                .filter(ch1 -> ch1 < 'a' || ch1 > 'z')
                .findAny()
                .ifPresent(ch -> {
                    throw new InvalidPath(path, ch);
                });
    }

    @Nonnull
    @Override
    public Iterator<TrieNode<T>> iterator() {
        return new PreorderIterator();
    }

    public Stream<TrieNode<T>> streamNodesInPreorder() {
        Spliterator<TrieNode<T>> spliterator = Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false);
    }

    public class PreorderIterator implements Iterator<TrieNode<T>> {
        private final LinkedList<TrieNode<T>> stack;

        public PreorderIterator() {
            stack = new LinkedList<>();
            stack.push(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public TrieNode<T> next() {
            TrieNode<T> result = stack.pop();
            for (char ch = 'z'; ch >= 'a'; --ch)
                if (result.hasChild(ch))
                    stack.push(result.getChild(ch));
            return result;
        }
    }

    public static class InvalidPath extends RuntimeException {
        public InvalidPath(String path, int ch) {
            super(String.format("" +
                    "Path [%s] contains illegal character [%s]. It should contain only characters from 'a' to 'z'",
                    path, (char) ch));
        }
    }
}
