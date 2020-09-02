package zh.bookreader.model

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet

import javax.annotation.Nonnull

interface Document<CONTENT_TYPE> {
    @Nonnull DocumentType getDocumentType()
    @Nonnull CONTENT_TYPE getContent()
    @Nonnull Set<DocumentFormatting> getFormatting()
    @Nonnull String getId()
    @Nonnull Map<String, String> getMetadata()

    static Document<Class<Void>> NULL = new Document<Class<Void>>() {
        @Nonnull
        @Override
        DocumentType getDocumentType() {
            DocumentType.NULL
        }

        @Nonnull
        @Override
        Class<Void> getContent() {
            Void.TYPE
        }

        @Nonnull
        @Override
        Set<DocumentFormatting> getFormatting() {
            ImmutableSet.of()
        }

        @Override
        String toString() {
            'Document.NULL'
        }

        @Nonnull
        @Override
        String getId() {
            ""
        }

        @Nonnull
        @Override
        Map<String, String> getMetadata() {
            ImmutableMap.of()
        }
    }
}