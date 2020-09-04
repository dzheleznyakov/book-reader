package zh.bookreader.model

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet

abstract class DocumentBuilder<CONTENT_TYPE> {
    final DocumentType documentType
    String id
    final ImmutableMap.Builder<String, String> metadata = ImmutableMap.builder()
    final ImmutableSet.Builder<DocumentFormatting> formatting = ImmutableSet.builder()

    protected DocumentBuilder(DocumentType documentType) {
        this.documentType = documentType
    }

    abstract DocumentBuilder<CONTENT_TYPE> withContent(def content)

    abstract <D extends Document<CONTENT_TYPE>> D build()

    DocumentBuilder<CONTENT_TYPE> withId(String id) {
        if (id)
            this.id = id
        this
    }

    DocumentBuilder<CONTENT_TYPE> withFormatting(Collection<DocumentFormatting> formatting) {
        if (formatting)
            this.formatting.addAll(formatting)
        this
    }

    DocumentBuilder<CONTENT_TYPE> withMetadata(Map<String, String> metadata) {
        if (metadata)
            this.metadata.putAll(metadata)
        this
    }
}
