package zh.bookreader.model.documents;

public class RawDocument extends TextDocument {
    RawDocument(DocumentBuilder<String> builder) {
        super(builder);
    }

    public static DocumentBuilder<String> builder() {
        return new Builder();
    }

    public static class Builder extends TextDocument.Builder {
        Builder() {
            super(DocumentType.RAW);
        }

        @Override
        public RawDocument build() {
            return new RawDocument(this);
        }
    }
}
