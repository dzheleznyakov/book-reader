package zh.bookreader.model;

import java.util.Iterator;
import java.util.LinkedList;

class EnclosingDocumentIterator implements Iterator<Document<?>> {
    private final LinkedList<Document<?>> stack = new LinkedList<>();

    EnclosingDocumentIterator(EnclosingDocument doc) {
        stack.push(doc);
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Document<?> next() {
        Document<?> doc = stack.pop();
        if (doc instanceof EnclosingDocument)
            stack.addAll(0, ((EnclosingDocument) doc).getContent());
        return doc;
    }
}
