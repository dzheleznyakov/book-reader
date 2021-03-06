package zh.bookreader.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import zh.bookreader.model.documents.Book;
import zh.bookreader.model.documents.Chapter;
import zh.bookreader.model.documents.DocumentFormatting;
import zh.bookreader.model.documents.DocumentType;
import zh.bookreader.model.documents.EnclosingDocument;
import zh.bookreader.model.documents.TextDocument;
import zh.bookreader.utils.ClassUtils;

public class ApiTestUtils {
    public static final String TEXT_1 = "Mock text 1.";
    public static final String TEXT_2 = "Mock text 2.";
    public static final String HEADER_1_TEXT = "Mock Header 1";
    public static final String PAR_1_ID = "mock-par-id";
    public static final String SEC_1_ID = "mock-sec-1-id";
    public static final String SEC_2_ID = "mock-sec-2-id";
    public static final String SEC_3_ID = "mock-sec-3-id";
    public static final ImmutableSet<DocumentFormatting> FORMATTING = ImmutableSet.of(DocumentFormatting.NOTE, DocumentFormatting.SIMPLE);

    public static final EnclosingDocument HEADER_1 = EnclosingDocument.builder(DocumentType.INLINED)
            .withContent(TextDocument.builder().withContent(HEADER_1_TEXT).build())
            .withFormatting(ImmutableList.of(DocumentFormatting.TITLE))
            .build();

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

    public static final EnclosingDocument SEC_3 = EnclosingDocument.builder(DocumentType.SECTION)
            .withContent(PAR)
            .withContent(HEADER_1)
            .withContent(TEXT_DOC_2)
            .withFormatting(FORMATTING)
            .withId(SEC_3_ID)
            .build();

    public static Book getBook() {
        Chapter chapter1 = new Chapter();
        chapter1.setId("Mock Chapter Name 1");

        Chapter chapter2 = new Chapter();
        chapter2.setId("Mock Chapter Name 2");

        Book book = new Book();
        book.setId("mock-book-id");
        book.setTitle("Mock Book Title");
        book.setReleaseDate("January 1970");
        book.setAuthors(ImmutableList.of("Author One", "Author Two"));
        book.setTopics(ImmutableList.of("Topic One", "Topic Two"));
        book.setDescription(ImmutableList.of(TEXT_DOC_2, SEC_1));
        book.setResources(ImmutableMap.of(
                "Resource 1", "Value 1",
                "Resource 2", "Value 2"));
        book.setImage(ClassUtils.cast(new byte[]{0, 1, 2, 3}));
        book.setChapters(ImmutableList.of(chapter1, chapter2));
        return book;
    }
}
