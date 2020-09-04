package zh.bookreader.model


import groovy.transform.PackageScope

@PackageScope
abstract class BaseDocument<CONTENT_TYPE> implements Document<CONTENT_TYPE> {
    final DocumentType documentType
    final String id
    final Set<DocumentFormatting> formatting
    final Map<String, String> metadata

    protected BaseDocument(DocumentBuilder<CONTENT_TYPE> builder) {
        documentType = builder.documentType
        id = builder.id
        formatting = builder.formatting.build()
        metadata = builder.metadata.build()
    }
}
