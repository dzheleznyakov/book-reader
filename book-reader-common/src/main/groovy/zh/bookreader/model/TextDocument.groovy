package zh.bookreader.model

class TextDocument extends BaseDocument<String> {
    final String content

    private TextDocument(DocumentBuilder<String> builder) {
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

    @Override
    String toString() {
        content
    }

    static DocumentBuilder<String> builder(DocumentType documentType) {
        new Builder(documentType)
    }

    static class Builder extends DocumentBuilder<String> {
        private String content

        Builder(DocumentType documentType) {
            super(documentType)
        }

        @Override
        DocumentBuilder<String> withContent(def content) {
            this.content = content.replaceAll(/\s*\n\s*/, ' ')
            return this
        }

        @Override
        DocumentBuilder<String> withMetadata(Map<String, String> metadata) {
            this
        }

        @Override
        DocumentBuilder<String> withId(String id) {
            this
        }

        @Override
        TextDocument build() {
            new TextDocument(this)
        }
    }
}
