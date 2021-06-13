package zh.bookreader.model.documents;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public interface Document<CONTENT_TYPE> {
    @Nonnull DocumentType getDocumentType();
    @Nonnull CONTENT_TYPE getContent();
    @Nonnull Set<DocumentFormatting> getFormatting();
    @Nonnull String getId();
    @Nonnull Map<String, String> getMetadata();

    Document<Class<Void>> NULL = new Document<>() {
        @Nonnull
        @Override
        public DocumentType getDocumentType() {
            return DocumentType.NULL;
        }

        @Nonnull
        @Override
        public Class<Void> getContent() {
            return Void.TYPE;
        }

        @Nonnull
        @Override
        public Set<DocumentFormatting> getFormatting() {
            return ImmutableSet.of();
        }

        @Override
        public String toString() {
            return "Document.NULL";
        }

        @Nonnull
        @Override
        public String getId() {
            return "";
        }

        @Nonnull
        @Override
        public Map<String, String> getMetadata() {
            return ImmutableMap.of();
        }
    };
}
