package zh.bookreader.model.documents;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Map;

public abstract class DocumentBuilder<CONTENT_TYPE> {
    final DocumentType documentType;
    String id = "";
    final ImmutableMap.Builder<String, String> metadata = ImmutableMap.builder();
    final ImmutableSet.Builder<DocumentFormatting> formatting = ImmutableSet.builder();

    protected DocumentBuilder(DocumentType documentType) {
        this.documentType = documentType;
    }

    public abstract DocumentBuilder<CONTENT_TYPE> withContent(Object content);

    public abstract <D extends Document<CONTENT_TYPE>> D build();

    abstract CONTENT_TYPE getContent();

    public DocumentBuilder<CONTENT_TYPE> withId(String id) {
        if (id != null)
            this.id = id;
        return this;
    }

    public DocumentBuilder<CONTENT_TYPE> withFormatting(Collection<DocumentFormatting> formatting) {
        if (formatting != null)
            this.formatting.addAll(formatting);
        return this;
    }

    public DocumentBuilder<CONTENT_TYPE> withMetadata(Map<String, String> metadata) {
        if (metadata != null)
            this.metadata.putAll(metadata);
        return this;
    }
}
