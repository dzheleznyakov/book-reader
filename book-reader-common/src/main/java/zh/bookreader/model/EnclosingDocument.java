package zh.bookreader.model;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class EnclosingDocument extends BaseDocument<List<Document<?>>> {
    private final List<Document<?>> content;

    private EnclosingDocument(DocumentBuilder<List<Document<?>>> builder) {
        super(builder);
        content = builder.getContent();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;

        final EnclosingDocument that = (EnclosingDocument) obj;

        if (!Objects.equals(content, that.content)) return false;
        if (!Objects.equals(formatting, that.formatting)) return false;
        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(metadata, that.metadata);
    }

    public int hashCode() {
        int result;
        result = (content != null ? content.hashCode() : 0);
        result = 31 * result + (formatting != null ? formatting.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        return result;
    }

    @Nonnull
    @Override
    public List<Document<?>> getContent() {
        return content;
    }

    public static DocumentBuilder<List<Document<?>>> builder(DocumentType documentType) {
        return new Builder(documentType);
    }

    public static class Builder extends DocumentBuilder<List<Document<?>>> {
        private final ImmutableList.Builder<Document<?>> content = ImmutableList.builder();

        Builder(DocumentType documentType) {
            super(documentType);
        }

        @Override
        public ImmutableList<Document<?>> getContent() {
            return content.build();
        }

        @Override
        public DocumentBuilder<List<Document<?>>> withContent(Object content) {
            if (content instanceof Collection)
                this.content.addAll((Collection<Document<?>>) content);
            if (content instanceof Document<?>)
                this.content.add((Document<?>) content);
            return this;
        }

        @Override
        public EnclosingDocument build() {
            return new EnclosingDocument(this);
        }
    }
}
