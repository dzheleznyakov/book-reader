package zh.bookreader.model;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class TextDocument extends BaseDocument<String> {
    private final String content;

    private TextDocument(DocumentBuilder<String> builder) {
        super(builder);
        content = builder.getContent();
    }

    @Nonnull
    @Override
    public String getContent() {
        return content;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;

        final TextDocument that = (TextDocument) obj;

        if (!Objects.equals(content, that.content)) return false;
        return Objects.equals(formatting, that.formatting);
    }

    public int hashCode() {
        int result;
        result = (content != null ? content.hashCode() : 0);
        result = 31 * result + (formatting != null ? formatting.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return content;
    }

    public static DocumentBuilder<String> builder(DocumentType documentType) {
        return new Builder(documentType);
    }

    public static class Builder extends DocumentBuilder<String> {
        private String content;

        Builder(DocumentType documentType) {
            super(documentType);
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public DocumentBuilder<String> withContent(Object content) {
            if (content instanceof String)
                this.content = (String) content;
            return this;
        }

        @Override
        public DocumentBuilder<String> withMetadata(Map<String, String> metadata) {
            return this;
        }

        @Override
        public DocumentBuilder<String> withId(String id) {
            return this;
        }

        @Override
        public TextDocument build() {
            return new TextDocument(this);
        }
    }
}
