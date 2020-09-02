package zh.bookreader.model.document

import com.google.common.collect.ImmutableList

interface Document<CONTENT_TYPE> {
    DocumentType getDocumentType()
    CONTENT_TYPE getContent()
    List<DocumentFormatting> getFormatting()
    String getId()
    Map<String, String> getMetadata()

    static Document<Void> NULL = new Document<Void>() {
        @Override
        DocumentType getDocumentType() {
            return DocumentType.NULL
        }

        @Override
        Void getContent() {
        }

        @Override
        List<DocumentFormatting> getFormatting() {
            ImmutableList.of()
        }

        @Override
        String toString() {
            'Document.NULL'
        }

        @Override
        String getId() {
            null
        }

        @Override
        Map<String, String> getMetadata() {
            return [:]
        }
    }
}