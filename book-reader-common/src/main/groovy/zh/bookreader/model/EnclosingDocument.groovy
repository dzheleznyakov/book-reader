package zh.bookreader.model

import com.google.common.collect.ImmutableList

class EnclosingDocument extends BaseDocument<List<Document>> {
    final List<Document> content

    private EnclosingDocument(Builder builder) {
        super(builder)
        content = builder.content.build()
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        EnclosingDocument that = (EnclosingDocument) o

        if (content != that.content) return false
        if (formatting != that.formatting) return false
        if (id != that.id) return false
        if (metadata != that.metadata) return false

        return true
    }

    int hashCode() {
        int result
        result = (content != null ? content.hashCode() : 0)
        result = 31 * result + (formatting != null ? formatting.hashCode() : 0)
        result = 31 * result + (id != null ? id.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }

    static BaseDocument.Builder<List<Document>> builder(DocumentType documentType) {
        new Builder(documentType)
    }

    static class Builder extends BaseDocument.Builder<List<Document>> {
        private final ImmutableList.Builder<Document> content = ImmutableList.builder()

        Builder(DocumentType documentType) {
            super(documentType)
        }

        @Override
        BaseDocument.Builder<List<Document>> withContent(content) {
            if (content)
                this.content.addAll content
            this
        }

        @Override
        EnclosingDocument build() {
            new EnclosingDocument(this)
        }
    }
}
