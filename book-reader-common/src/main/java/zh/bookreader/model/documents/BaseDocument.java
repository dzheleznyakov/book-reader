package zh.bookreader.model.documents;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

abstract class BaseDocument<CONTENT_TYPE> implements Document<CONTENT_TYPE> {
    protected final DocumentType documentType;
    protected final String id;
    protected final Set<DocumentFormatting> formatting;
    protected final Map<String, String> metadata;

    protected BaseDocument(DocumentBuilder<CONTENT_TYPE> builder) {
        documentType = builder.documentType;
        id = builder.id;
        formatting = builder.formatting.build();
        metadata = builder.metadata.build();
    }

    @Nonnull
    @Override
    public DocumentType getDocumentType() {
        return documentType;
    }

    @Nonnull
    @Override
    public String getId() {
        return id;
    }

    @Nonnull
    @Override
    public Set<DocumentFormatting> getFormatting() {
        return formatting;
    }

    @Nonnull
    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }
}
