package zh.bookreader.model

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import groovy.transform.PackageScope

@PackageScope
abstract class BaseDocument<CONTENT_TYPE> implements Document<CONTENT_TYPE> {
    final DocumentType documentType
    final String id
    final Set<DocumentFormatting> formatting
    final Map<String, String> metadata

    protected BaseDocument(Builder<CONTENT_TYPE> builder) {
        documentType = builder.documentType
        id = builder.id
        formatting = builder.formatting.build()
        metadata = builder.metadata.build()
    }

    static abstract class Builder<CONTENT_TYPE> {
        final DocumentType documentType
        String id
        final ImmutableMap.Builder<String, String> metadata = ImmutableMap.builder()
        final ImmutableSet.Builder<DocumentFormatting> formatting = ImmutableSet.builder()

        protected Builder(DocumentType documentType) {
            this.documentType = documentType
        }

        abstract Builder<CONTENT_TYPE> withContent(def content)

        abstract <D extends Document<CONTENT_TYPE>> D build()

        Builder<CONTENT_TYPE> withId(String id) {
            if (id)
                this.id = id
            this
        }

        Builder<CONTENT_TYPE> withFormatting(Collection<DocumentFormatting> formatting) {
            if (formatting)
                this.formatting.addAll(formatting)
            this
        }

        Builder<CONTENT_TYPE> withMetadata(Map<String, String> metadata) {
            if (metadata)
                this.metadata.putAll(metadata)
            this
        }
    }
}
