package zh.bookreader.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import zh.bookreader.model.Book;
import zh.bookreader.model.Chapter;
import zh.bookreader.model.DocumentFormatting;
import zh.bookreader.model.DocumentType;
import zh.bookreader.model.EnclosingDocument;
import zh.bookreader.model.TextDocument;
import zh.bookreader.utils.ClassUtils;

public class ApiTestUtils {
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
