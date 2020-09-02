package zh.bookreader.model.document

class TextDocument extends BaseDocument<String> {
    final String content

    private TextDocument(Builder builder) {
        super(builder)
        content = builder.content
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        TextDocument that = (TextDocument) o

        if (content != that.content) return false
        if (formatting != that.formatting) return false

        return true
    }

    int hashCode() {
        int result
        result = (content != null ? content.hashCode() : 0)
        result = 31 * result + (formatting != null ? formatting.hashCode() : 0)
        return result
    }

    static BaseDocument.Builder<String> builder(DocumentType documentType) {
        new Builder(documentType)
    }

    static class Builder extends BaseDocument.Builder<String> {
        private String content

        Builder(DocumentType documentType) {
            super(documentType)
        }

        @Override
        BaseDocument.Builder<String> withContent(def content) {
            this.content = content.replaceAll(/\s*\n\s*/, ' ')
            return this
        }

        @Override
        BaseDocument.Builder<String> withMetadata(Map<String, String> metadata) {
            this
        }

        @Override
        BaseDocument.Builder<String> withId(String id) {
            this
        }

        @Override
        TextDocument build() {
            new TextDocument(this)
        }
    }
}
