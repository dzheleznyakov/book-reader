package zh.bookreader.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class EnclosingDocument extends BaseDocument<List<Document<?>>> {
    private final List<Document<?>> content;

    private EnclosingDocument(DocumentBuilder<List<Document<?>>> builder) {
        super(builder);
        content = builder.getContent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnclosingDocument)) return false;

        EnclosingDocument that = (EnclosingDocument) o;

        if (!content.equals(that.content)) return false;
        if (documentType != that.documentType) return false;
        if (!id.equals(that.id)) return false;
        if (!formatting.equals(that.formatting)) return false;
        return metadata.equals(that.metadata);
    }

    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = 31 * result + documentType.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + formatting.hashCode();
        result = 31 * result + metadata.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("content", content)
                .add("documentType", documentType)
                .add("id", id)
                .add("formatting", formatting)
                .add("metadata", metadata)
                .toString();
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
