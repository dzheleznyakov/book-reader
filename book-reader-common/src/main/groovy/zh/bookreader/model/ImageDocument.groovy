package zh.bookreader.model

class ImageDocument extends BaseDocument<Byte[]> {
    final Byte[] content

    private ImageDocument(DocumentBuilder<Byte[]> builder) {
        super(builder)
        content = builder.content
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        ImageDocument that = (ImageDocument) o

        if (!Arrays.equals(content, that.content)) return false
        if (formatting != that.formatting) return false
        if (id != that.id) return false
        if (metadata != that.metadata) return false

        return true
    }

    int hashCode() {
        int result
        result = (content != null ? Arrays.hashCode(content) : 0)
        result = 31 * result + (formatting != null ? formatting.hashCode() : 0)
        result = 31 * result + (id != null ? id.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }

    static DocumentBuilder<Byte[]> builder(DocumentType documentType) {
        new Builder(documentType)
    }

    static class Builder extends DocumentBuilder<Byte[]> {
        private Byte[] content = []

        Builder(DocumentType documentType) {
            super(documentType)
        }

        @Override
        DocumentBuilder<Byte[]> withContent(uri) {
            File file
            if (uri && (file = new File(uri)).exists())
                content = file.bytes
            this
        }

        @Override
        ImageDocument build() {
            new ImageDocument(this)
        }
    }
}
