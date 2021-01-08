package zh.bookreader.utils.collections;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Trie<T> implements Iterable<Trie.TrieNode<T>> {
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
                .map(d -> d.values)
                .map(ImmutableList::copyOf)
                .orElseGet(ImmutableList::of);
    }

    public void put(String path, @Nonnull T value) {
        validatePath(path);

        TrieNode<T> current = root;
        for (int i = 0; i < path.length(); ++i) {
            char ch = path.charAt(i);
            int index = ch - 'a';
            current = ensureChild(current, index);
        }
        current.values.add(value);
    }

    @Nonnull
    private Optional<TrieNode<T>> getDescendant(@Nonnull String path) {
        TrieNode<T> current = root;
        for (int i = 0; i < path.length() && current != null; ++i) {
            char ch = path.charAt(i);
            int index = ch - 'a';
            current = current.children.get(index);
        }
        return Optional.ofNullable(current);
    }

    @Nonnull
    private TrieNode<T> ensureChild(TrieNode<T> node, int index) {
        List<TrieNode<T>> children = node.children;
        if (children.get(index) == null)
            children.set(index, new TrieNode<>(('a' + index)));
        return children.get(index);
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

    public static class TrieNode<T> {
        public static final char ROOT_LABEL = '-';

        private final char label;
        private final List<T> values = new ArrayList<>();
        private final List<TrieNode<T>> children;

        private TrieNode() {
            this(ROOT_LABEL);
        }

        private TrieNode(int chCode) {
            this((char) (chCode));
        }

        private TrieNode(char label) {
            this.label = label;

            this.children = new ArrayList<>();
            for (int i = 0; i < 'z' - 'a' + 1; ++i)
                children.add(null);
        }

        public char getLabel() {
            return label;
        }

        public List<T> getValues() {
            return values;
        }
    }

    public class PreorderIterator implements Iterator<TrieNode<T>> {
        private static final int CHILDREN_SIZE = 'z' - 'a' + 1;

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
            for (int i = result.children.size() - 1; i >= 0; --i)
                if (result.children.get(i) != null)
                    stack.push(result.children.get(i));
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
