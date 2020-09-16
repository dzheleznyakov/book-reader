package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableSet;
import zh.bookreader.model.DocumentFormatting;
import zh.bookreader.model.DocumentType;
import zh.bookreader.model.EnclosingDocument;
import zh.bookreader.model.TextDocument;

public class Utils {
    public static final String TEXT_1 = "Mock text 1.";
    public static final String TEXT_2 = "Mock text 2.";
    public static final String PAR_1_ID = "mock-par-id";
    public static final String SEC_1_ID = "mock-sec-1-id";
    public static final String SEC_2_ID = "mock-sec-2-id";
    public static final ImmutableSet<DocumentFormatting> FORMATTING = ImmutableSet.of(DocumentFormatting.NOTE, DocumentFormatting.SIMPLE);

    public static final TextDocument TEXT_DOC_1 = TextDocument.builder(DocumentType.TEXT)
            .withContent(TEXT_1)
            .build();
    public static final TextDocument TEXT_DOC_2 = TextDocument.builder(DocumentType.TEXT)
            .withContent(TEXT_2)
            .build();
    public static final EnclosingDocument PAR = EnclosingDocument.builder(DocumentType.PARAGRAPH)
            .withContent(TEXT_DOC_1)
            .withFormatting(FORMATTING)
            .withId(PAR_1_ID)
            .build();
    public static final EnclosingDocument SEC_1 = EnclosingDocument.builder(DocumentType.SECTION)
            .withContent(PAR)
            .withFormatting(FORMATTING)
            .withId(SEC_1_ID)
            .build();
    public static final EnclosingDocument SEC_2 = EnclosingDocument.builder(DocumentType.SECTION)
            .withContent(PAR)
            .withContent(TEXT_DOC_2)
            .withFormatting(FORMATTING)
            .withId(SEC_2_ID)
            .build();
}
