package zh.bookreader.utils.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrieNode<T> {
    public static final char ROOT_LABEL = '-';

    private final char label;
    private final List<T> values = new ArrayList<>();
    private final TrieNode<T>[] children;

    TrieNode() {
        this(ROOT_LABEL);
    }

    private TrieNode(int chCode) {
        this((char) (chCode));
    }

    public TrieNode(char label) {
        this.label = label;
        this.children = (TrieNode<T>[]) Array.newInstance(TrieNode.class, 'z' - 'a' + 1);
    }

    public void setChild(char ch, TrieNode<T> child) {
        int index = ch - 'a';
        children[index] = child;
    }

    public boolean hasChild(char ch) {
        return children[ch - 'a'] != null;
    }

    public TrieNode<T> getChild(char ch) {
        return children[ch - 'a'];
    }

    public List<TrieNode<T>> getChildren() {
        List<TrieNode<T>> res = new ArrayList<>();
        Collections.addAll(res, children);
        return res;
    }

    public void add(T value) {
        values.add(value);
    }

    public char getLabel() {
        return label;
    }

    public List<T> getValues() {
        return values;
    }
}
